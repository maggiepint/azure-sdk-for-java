// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.applicationconfig;

import com.azure.applicationconfig.models.ConfigurationSetting;
import com.azure.applicationconfig.models.SettingFields;
import com.azure.applicationconfig.models.RequestOptions;
import com.azure.applicationconfig.models.RevisionOptions;
import com.azure.applicationconfig.models.RevisionRange;
import com.azure.common.http.rest.RestException;
import com.microsoft.azure.core.InterceptorManager;
import com.microsoft.azure.core.TestMode;
import com.microsoft.azure.utils.SdkContext;
import com.azure.common.http.HttpClient;
import com.azure.common.http.policy.HttpLogDetailLevel;
import com.azure.common.http.rest.RestResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Theories.class)
public class ConfigurationClientTest {
    private static final String PLAYBACK_URI_BASE = "http://localhost:";

    private final Logger logger = LoggerFactory.getLogger(ConfigurationClientTest.class);

    private InterceptorManager interceptorManager;
    private ConfigurationClient client;
    private String keyPrefix;
    private String labelPrefix;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void beforeTest() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        final TestMode testMode = getTestMode();

        interceptorManager = InterceptorManager.create(testName.getMethodName(), testMode);

        if (interceptorManager.isPlaybackMode()) {
            logger.info("PLAYBACK MODE");

            final String playbackUri = getPlaybackUri(testMode);
            final String connectionString = "endpoint=" + playbackUri + ";Id=0000000000000;Secret=MDAwMDAw";

            client = ConfigurationClient.builder()
                    .credentials(new ConfigurationClientCredentials(connectionString))
                    .httpClient(interceptorManager.getPlaybackClient())
                    .httpLogDetailLevel(HttpLogDetailLevel.BODY_AND_HEADERS)
                    .build();
        } else {
            logger.info("RECORD MODE");

            final String connectionString = System.getenv("AZCONFIG_CONNECTION_STRING");
            Objects.requireNonNull(connectionString, "AZCONFIG_CONNECTION_STRING expected to be set.");

            client = ConfigurationClient.builder()
                    .credentials(new ConfigurationClientCredentials(connectionString))
                    .httpClient(HttpClient.createDefault().wiretap(true))
                    .httpLogDetailLevel(HttpLogDetailLevel.BODY_AND_HEADERS)
                    .addPolicy(interceptorManager.getRecordPolicy())
                    .build();
        }

        keyPrefix = SdkContext.randomResourceName("key", 8);
        labelPrefix = SdkContext.randomResourceName("label", 8);
    }

    private static String getPlaybackUri(TestMode testMode) throws IOException {
        if (testMode == TestMode.RECORD) {
            Properties mavenProps = new Properties();

            try (InputStream in = ConfigurationClientTest.class.getResourceAsStream("/maven.properties")) {
                if (in == null) {
                    throw new IOException(
                            "The file \"maven.properties\" has not been generated yet. Please execute \"mvn compile\" to generate the file.");
                }
                mavenProps.load(in);
            }

            String port = mavenProps.getProperty("playbackServerPort");
            // 11080 and 11081 needs to be in sync with values in jetty.xml file
            return PLAYBACK_URI_BASE + port;
        } else {
            return PLAYBACK_URI_BASE + "1234";
        }
    }

    private TestMode getTestMode() throws IllegalArgumentException {
        final String azureTestMode = System.getenv("AZURE_TEST_MODE");

        if (azureTestMode != null) {
            try {
                return TestMode.valueOf(azureTestMode.toUpperCase(Locale.US));
            } catch (IllegalArgumentException e) {
                logger.error("Could not parse '{}' into TestEnum.", azureTestMode);
                throw e;
            }
        } else {
            logger.info("Environment variable 'AZURE_TEST_MODE' has not been set yet. Using 'Playback' mode.");
            return TestMode.PLAYBACK;
        }
    }

    @After
    public void afterTest() {
        cleanUpResources();
        interceptorManager.close();
    }

    private void cleanUpResources() {
        logger.info("Cleaning up created key values.");
        client.listKeyValues(new RevisionOptions().key(keyPrefix + "*"))
                .flatMap(configurationSetting -> {
                    logger.info("Deleting key:label [{}:{}]. isLocked? {}", configurationSetting.key(), configurationSetting.label(), configurationSetting.isLocked());

                    if (configurationSetting.isLocked()) {
                        return client.unlock(configurationSetting.key(), configurationSetting.label()).flatMap(response -> {
                            ConfigurationSetting kv = response.body();
                            return client.delete(kv.key(), kv.label(), null)
                                    .retryBackoff(3, Duration.ofSeconds(10));
                        });
                    } else {
                        return client.delete(configurationSetting.key(), configurationSetting.label(), null)
                                .retryBackoff(3, Duration.ofSeconds(10));
                    }
                })
                .blockLast();

        logger.info("Finished cleaning up values.");
    }

    /**
     * Tests that a configuration is able to be added, these are differentiate from each other using a key or key-label identifier.
     */
    @Test
    public void addSetting() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting newConfiguration = new ConfigurationSetting().withKey(key).withValue("myNewValue");

        final Consumer<ConfigurationSetting> testRunner = (expected) -> {
            StepVerifier.create(client.add(expected))
                    .assertNext(response -> assertConfigurationEquals(expected, response))
                    .verifyComplete();
        };

        testRunner.accept(newConfiguration);
        testRunner.accept(newConfiguration.withLabel(label));
    }

    /**
     * Tests that a configuration cannot be added twice with the same key. THis should return a 412 error.
     */
    @Test
    public void addExistingSetting() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting newConfiguration = new ConfigurationSetting().withKey(key).withValue("myNewValue");

        final Consumer<ConfigurationSetting> testRunner = (expected) -> {
            StepVerifier.create(client.add(expected).then(client.add(expected)))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.PRECONDITION_FAILED.code()));
        };

        testRunner.accept(newConfiguration);
        testRunner.accept(newConfiguration.withLabel(label));
    }

    /**
     * Tests that we can lock and unlock a configuration.
     */
    @Test
    public void lockUnlockSetting() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting lockableConfiguration = new ConfigurationSetting().withKey(key).withValue("myLockUnlockValue");

        final Consumer<ConfigurationSetting> testRunner = (expected) -> {
            StepVerifier.create(client.add(expected))
                    .assertNext(response -> assertConfigurationEquals(expected, response))
                    .verifyComplete();

            StepVerifier.create(client.lock(expected.key(), expected.label()))
                    .assertNext(response -> {
                        assertConfigurationEquals(expected, response);
                        assertTrue(response.body().isLocked());
                    })
                    .verifyComplete();

            StepVerifier.create(client.unlock(expected.key(), expected.label()))
                    .assertNext(response -> {
                        assertConfigurationEquals(expected, response);
                        assertFalse(response.body().isLocked());
                    })
                    .verifyComplete();
        };

        testRunner.accept(lockableConfiguration);
        testRunner.accept(lockableConfiguration.withLabel(label));
    }

    /**
     * Tests that attempt to lock or unlock a non-existent configuration fails, this results in a 404.
     */
    @Test
    public void lockUnlockSettingNotFound() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting notFindableConfiguration = new ConfigurationSetting().withKey(key).withValue("myExpectedNotFound");

        final Consumer<ConfigurationSetting> testRunner = (ConfigurationSetting expected) -> {
            StepVerifier.create(client.lock(expected.key(), expected.label()))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.NOT_FOUND.code()));

            StepVerifier.create(client.unlock(expected.key(), expected.label()))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.NOT_FOUND.code()));
        };

        testRunner.accept(notFindableConfiguration);
        testRunner.accept(notFindableConfiguration.withLabel(label));
    }

    /**
     * Tests that a configuration is able to be added or updated with set.
     * When the configuration is locked updates cannot happen, this will result in a 409.
     */
    @Test
    public void setSetting() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting setConfiguration = new ConfigurationSetting().withKey(key).withValue("myNewValue");
        final ConfigurationSetting updateConfiguration = new ConfigurationSetting().withKey(key).withValue("myUpdatedValue");

        final BiConsumer<ConfigurationSetting, ConfigurationSetting> testRunner = (expected, update) -> {
            StepVerifier.create(client.set(expected))
                    .assertNext(response -> assertConfigurationEquals(expected, response))
                    .verifyComplete();

            StepVerifier.create(client.lock(expected.key(), expected.label()).then(client.set(update)))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.CONFLICT.code()));

            StepVerifier.create(client.unlock(expected.key(), expected.label()).then(client.set(update)))
                    .assertNext(response -> assertConfigurationEquals(update, response))
                    .verifyComplete();
        };

        testRunner.accept(setConfiguration, updateConfiguration);
        testRunner.accept(setConfiguration.withLabel(label), updateConfiguration.withLabel(label));
    }

    /**
     * Tests that when an etag is passed to set it will only set if the current representation of the setting has the etag.
     * If the set etag doesn't match anything the update won't happen, this will result in a 412. This will prevent set from doing an add as well.
     */
    @Test
    public void setSettingIfEtag() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting newConfiguration = new ConfigurationSetting().withKey(key).withValue("myNewValue");
        final ConfigurationSetting updateConfiguration = new ConfigurationSetting().withKey(key).withValue("myUpdateValue");

        final BiConsumer<ConfigurationSetting, ConfigurationSetting> testRunner = (initial, update) -> {
            StepVerifier.create(client.set(initial.withEtag("badEtag")))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.PRECONDITION_FAILED.code()));

            final String etag = client.add(initial).block().body().etag();

            StepVerifier.create(client.set(update.withEtag(etag)))
                    .assertNext(response -> assertConfigurationEquals(update, response))
                    .verifyComplete();

            StepVerifier.create(client.set(initial))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.PRECONDITION_FAILED.code()));

            StepVerifier.create(client.get(update.key(), update.label()))
                    .assertNext(response -> assertConfigurationEquals(update, response))
                    .verifyComplete();
        };

        testRunner.accept(newConfiguration, updateConfiguration);
        testRunner.accept(newConfiguration.withLabel(label), updateConfiguration.withLabel(label));
    }

    /**
     * Tests that update cannot be done to a non-existent configuration, this will result in a 412.
     * Unlike set update isn't able to create the configuration.
     */
    @Test
    public void updateNoExistingSetting() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting expectedFail = new ConfigurationSetting().withKey(key).withValue("myFailingUpdate");

        final Consumer<ConfigurationSetting> testRunner = (expected) -> {
            StepVerifier.create(client.update(expected))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.PRECONDITION_FAILED.code()));
        };

        testRunner.accept(expectedFail);
        testRunner.accept(expectedFail.withLabel(label));
    }

    /**
     * Tests that a configuration is able to be updated when it exists.
     * When the configuration is locked updates cannot happen, this will result in a 409.
     */
    @Test
    public void updateSetting() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting newConfiguration = new ConfigurationSetting().withKey(key).withValue("myNewValue");
        final ConfigurationSetting updateConfiguration = new ConfigurationSetting().withKey(key).withValue("myUpdatedValue");

        final BiConsumer<ConfigurationSetting, ConfigurationSetting> testRunner = (initial, update) -> {
            StepVerifier.create(client.add(initial))
                    .assertNext(response -> assertConfigurationEquals(initial, response))
                    .verifyComplete();

            StepVerifier.create(client.lock(initial.key(), initial.label()).then(client.update(update)))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.CONFLICT.code()));

            StepVerifier.create(client.unlock(initial.key(), initial.label()).then(client.update(update)))
                    .assertNext(response -> assertConfigurationEquals(update, response))
                    .verifyComplete();
        };

        testRunner.accept(newConfiguration, updateConfiguration);
        testRunner.accept(newConfiguration.withLabel(label), updateConfiguration.withLabel(label));
    }

    /**
     * Tests that when an etag is passed to update it will only update if the current representation of the setting has the etag.
     * If the update etag doesn't match anything the update won't happen, this will result in a 412.
     */
    @Test
    public void updateSettingIfEtag() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting newConfiguration = new ConfigurationSetting().withKey(key).withValue("myNewValue");
        final ConfigurationSetting updateConfiguration = new ConfigurationSetting().withKey(key).withValue("myUpdateValue");
        final ConfigurationSetting finalConfiguration = new ConfigurationSetting().withKey(key).withValue("myFinalValue");

        updateSettingIfEtagHelper(newConfiguration, updateConfiguration, finalConfiguration);
        updateSettingIfEtagHelper(newConfiguration.withLabel(label), updateConfiguration.withLabel(label), finalConfiguration.withLabel(label));
    }

    private void updateSettingIfEtagHelper(ConfigurationSetting initial, ConfigurationSetting update, ConfigurationSetting last) {
        final String initialEtag = client.add(initial).block().body().etag();
        final String updateEtag = client.update(update).block().body().etag();

        StepVerifier.create(client.update(last.withEtag(initialEtag)))
                .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.PRECONDITION_FAILED.code()));

        StepVerifier.create(client.get(update.key(), update.label()))
                .assertNext(response -> assertConfigurationEquals(update, response))
                .verifyComplete();

        StepVerifier.create(client.update(last.withEtag(updateEtag)))
                .assertNext(response -> assertConfigurationEquals(last, response))
                .verifyComplete();

        StepVerifier.create(client.get(last.key(), last.label()))
                .assertNext(response -> assertConfigurationEquals(last, response))
                .verifyComplete();
    }

    /**
     * Tests that a configuration is able to be retrieved when it exists, whether or not it is locked.
     */
    @Test
    public void getSetting() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final ConfigurationSetting newConfiguration = new ConfigurationSetting().withKey(key).withValue("myNewValue");

        final Consumer<ConfigurationSetting> testRunner = (expected) -> {
            StepVerifier.create(client.add(expected).then(client.get(expected.key(), expected.label())))
                    .assertNext(response -> assertConfigurationEquals(expected, response))
                    .verifyComplete();

            StepVerifier.create(client.lock(expected.key(), expected.label()).then(client.get(expected.key(), expected.label())))
                    .assertNext(response -> assertConfigurationEquals(expected, response))
                    .verifyComplete();
        };

        testRunner.accept(newConfiguration);
        testRunner.accept(newConfiguration.withLabel("myLabel"));
    }

    /**
     * Tests that attempting to retrieve a non-existent configuration doesn't work, this will result in a 404.
     */
    @Test
    public void getSettingNotFound() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final ConfigurationSetting neverRetrievedConfiguration = new ConfigurationSetting().withKey(key).withValue("myNeverRetreivedValue");

        StepVerifier.create(client.add(neverRetrievedConfiguration))
                .assertNext(response -> assertConfigurationEquals(neverRetrievedConfiguration, response))
                .verifyComplete();

        StepVerifier.create(client.get("myNonExistentKey"))
                .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.NOT_FOUND.code()));

        StepVerifier.create(client.get(key, "myNonExistentLabel"))
                .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.NOT_FOUND.code()));
    }

    /**
     * Tests that configurations are able to be deleted when they exist.
     * When the configuration is locked deletes cannot happen, this will result in a 409.
     * After the configuration has been deleted attempting to get it will result in a 404, the same as if the configuration never existed.
     */
    @Test
    public void deleteSetting() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting deletableConfiguration = new ConfigurationSetting().withKey(key).withValue("myValue");

        final Consumer<ConfigurationSetting> testRunner = (expected) -> {
            StepVerifier.create(client.add(expected).then(client.get(expected.key(), expected.label())))
                    .assertNext(response -> assertConfigurationEquals(expected, response))
                    .verifyComplete();

            StepVerifier.create(client.lock(expected.key(), expected.label()).then(client.delete(expected.key(), expected.label(), null)))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.CONFLICT.code()));

            StepVerifier.create(client.unlock(expected.key(), expected.label()).then(client.delete(expected.key(), expected.label(), null)))
                    .assertNext(response -> assertConfigurationEquals(expected, response))
                    .verifyComplete();

            StepVerifier.create(client.get(expected.key(), expected.label()))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.NOT_FOUND.code()));
        };

        testRunner.accept(deletableConfiguration);
        testRunner.accept(deletableConfiguration.withLabel(label));
    }

    /**
     * Tests that attempting to delete a non-existent configuration will return a 204.
     */
    @Test
    public void deleteSettingNotFound() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final ConfigurationSetting neverDeletedConfiguation = new ConfigurationSetting().withKey(key).withValue("myNeverDeletedValue");

        StepVerifier.create(client.add(neverDeletedConfiguation))
                .assertNext(response -> assertConfigurationEquals(neverDeletedConfiguation, response))
                .verifyComplete();

        StepVerifier.create(client.delete("myNonExistentKey"))
                .assertNext(response -> assertConfigurationEquals(null, response, HttpResponseStatus.NO_CONTENT.code()))
                .verifyComplete();

        StepVerifier.create(client.delete(neverDeletedConfiguation.key(), "myNonExistentLabel", null))
                .assertNext(response -> assertConfigurationEquals(null, response, HttpResponseStatus.NO_CONTENT.code()))
                .verifyComplete();

        StepVerifier.create(client.get(neverDeletedConfiguation.key()))
                .assertNext(response -> assertConfigurationEquals(neverDeletedConfiguation, response))
                .verifyComplete();
    }

    /**
     * Tests that when an etag is passed to delete it will only delete if the current representation of the setting has the etag.
     * If the delete etag doesn't match anything the delete won't happen, this will result in a 412.
     */
    @Test
    public void deleteSettingWithETag() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName(labelPrefix, 16);
        final ConfigurationSetting newConfiguration = new ConfigurationSetting().withKey(key).withValue("myNewValue");
        final ConfigurationSetting updateConfiguration = new ConfigurationSetting().withKey(key).withValue("myUpdateValue");

        final BiConsumer<ConfigurationSetting, ConfigurationSetting> testRunner = (initial, update) -> {
            final String initialEtag = client.add(initial).block().body().etag();
            final String updateEtag = client.update(update).block().body().etag();

            StepVerifier.create(client.get(initial.key(), initial.label()))
                    .assertNext(response -> assertConfigurationEquals(update, response))
                    .verifyComplete();

            StepVerifier.create(client.delete(initial.key(), initial.label(), initialEtag))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.PRECONDITION_FAILED.code()));

            StepVerifier.create(client.delete(initial.key(), initial.label(), updateEtag))
                    .assertNext(response -> assertConfigurationEquals(update, response))
                    .verifyComplete();

            StepVerifier.create(client.get(initial.key(), initial.label()))
                    .verifyErrorSatisfies(ex -> assertRestException(ex, HttpResponseStatus.NOT_FOUND.code()));
        };

        testRunner.accept(newConfiguration, updateConfiguration);
        testRunner.accept(newConfiguration.withLabel(label), updateConfiguration.withLabel(label));
    }

    /**
     * Verifies that a ConfigurationSetting can be added with a label, and that we can fetch that ConfigurationSetting
     * from the service when filtering by either its label or just its key.
     */
    @Test
    public void listWithKeyAndLabel() {
        final String value = "myValue";
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final String label = SdkContext.randomResourceName("lbl", 8);
        final ConfigurationSetting expected = new ConfigurationSetting().withKey(key).withValue(value).withLabel(label);

        StepVerifier.create(client.set(expected))
                .assertNext(response -> assertConfigurationEquals(expected, response))
                .verifyComplete();

        StepVerifier.create(client.listKeyValues(new RevisionOptions().key(key).label(label)))
                .assertNext(configurationSetting -> assertConfigurationEquals(expected, configurationSetting))
                .verifyComplete();

        StepVerifier.create(client.listKeyValues(new RevisionOptions().key(key)))
                .assertNext(configurationSetting -> assertConfigurationEquals(expected, configurationSetting))
                .verifyComplete();
    }

    /**
     * Verifies that we can select filter results by key, label, and select fields using RequestOptions.
     */
    @Test
    public void listSettingsSelectFields() {
        final String label = "my-first-mylabel";
        final String label2 = "my-second-mylabel";
        final int numberToCreate = 8;
        final Map<String, String> tags = new HashMap<>();
        tags.put("tag1", "value1");
        tags.put("tag2", "value2");

        final EnumSet<SettingFields> fields = EnumSet.of(SettingFields.KEY, SettingFields.ETAG, SettingFields.CONTENT_TYPE, SettingFields.TAGS);
        final RequestOptions secondLabelOptions = new RequestOptions()
            .label("*-second*")
            .key(keyPrefix + "-fetch-*")
            .fields(fields);
        final List<ConfigurationSetting> settings = IntStream.range(0, numberToCreate)
            .mapToObj(value -> {
                String key = value % 2 == 0  ? keyPrefix + "-" + value : keyPrefix + "-fetch-" + value;
                String lbl = value / 4 == 0 ? label : label2;
                return new ConfigurationSetting().withKey(key).withValue("myValue2").withLabel(lbl).withTags(tags);
            })
            .collect(Collectors.toList());

        final List<Mono<RestResponse<ConfigurationSetting>>> results = new ArrayList<>();
        for (ConfigurationSetting setting : settings) {
            results.add(client.set(setting));
        }

        // Waiting for all the settings to be added.
        Flux.merge(results).blockLast();

        StepVerifier.create(client.listKeyValues(secondLabelOptions))
            .assertNext(setting -> {
                // These are the fields we chose in our filter.
                assertNotNull(setting.etag());
                assertNotNull(setting.key());
                assertTrue(setting.key().contains(keyPrefix));
                assertNotNull(setting.tags());
                assertEquals(tags.size(), setting.tags().size());

                assertNull(setting.lastModified());
                assertNull(setting.contentType());
                assertNull(setting.label());
            })
            .assertNext(setting -> {
                // These are the fields we chose in our filter.
                assertNotNull(setting.etag());
                assertNotNull(setting.key());
                assertTrue(setting.key().contains(keyPrefix));
                assertNotNull(setting.tags());
                assertEquals(tags.size(), setting.tags().size());

                assertNull(setting.lastModified());
                assertNull(setting.contentType());
                assertNull(setting.label());
            })
            .verifyComplete();
    }

    /**
     * Verifies that we can get a ConfigurationSetting at the provided accept datetime
     */
    @Test
    public void listSettingsAcceptDateTime() {
        final String keyName = SdkContext.randomResourceName(keyPrefix, 16);
        final ConfigurationSetting original = new ConfigurationSetting().withKey(keyName).withValue("myValue");
        final ConfigurationSetting updated = new ConfigurationSetting().withKey(keyName).withValue("anotherValue");
        final ConfigurationSetting updated2 = new ConfigurationSetting().withKey(keyName).withValue("anotherValue2");

        // Create 3 revisions of the same key.
        StepVerifier.create(client.set(original).delayElement(Duration.ofSeconds(2)))
            .assertNext(response -> assertConfigurationEquals(original, response))
            .verifyComplete();
        StepVerifier.create(client.set(updated).delayElement(Duration.ofSeconds(2)))
            .assertNext(response -> assertConfigurationEquals(updated, response))
            .verifyComplete();
        StepVerifier.create(client.set(updated2))
            .assertNext(response -> assertConfigurationEquals(updated2, response))
            .verifyComplete();

        // Gets all versions of this value so we can get the one we want at that particular date.
        List<ConfigurationSetting> revisions = client.listKeyValueRevisions(new RevisionOptions().key(keyName)).collectList().block();

        assertNotNull(revisions);
        assertEquals(3, revisions.size());

        // We want to fetch the configuration setting when we first updated its value.
        RequestOptions options = new RequestOptions().key(keyName).acceptDatetime(revisions.get(1).lastModified());
        StepVerifier.create(client.listKeyValues(options))
            .assertNext(response -> assertConfigurationEquals(updated, response))
            .verifyComplete();
    }

    /**
     * Verifies that we can get all of the revisions for this ConfigurationSetting.
     */
    @Test
    public void listRevisions() {
        final String keyName = SdkContext.randomResourceName(keyPrefix, 16);
        final ConfigurationSetting original = new ConfigurationSetting().withKey(keyName).withValue("myValue");
        final ConfigurationSetting updated = new ConfigurationSetting().withKey(keyName).withValue("anotherValue");
        final ConfigurationSetting updated2 = new ConfigurationSetting().withKey(keyName).withValue("anotherValue2");

        // Create 3 revisions of the same key.
        StepVerifier.create(client.set(original))
                .assertNext(response -> assertConfigurationEquals(original, response))
                .verifyComplete();
        StepVerifier.create(client.set(updated))
                .assertNext(response -> assertConfigurationEquals(updated, response))
                .verifyComplete();
        StepVerifier.create(client.set(updated2))
                .assertNext(response -> assertConfigurationEquals(updated2, response))
                .verifyComplete();

        // Get all revisions for a key, they are listed in descending order.
        StepVerifier.create(client.listKeyValueRevisions(new RevisionOptions().key(keyName)))
                .assertNext(response -> assertConfigurationEquals(updated2, response))
                .assertNext(response -> assertConfigurationEquals(updated, response))
                .assertNext(response -> assertConfigurationEquals(original, response))
                .verifyComplete();
    }

    /**
     * Verifies that we can get a subset of the revisions using "Range" header
     */
    @Test
    public void listRevisionsRange() {
        final String keyName = SdkContext.randomResourceName(keyPrefix, 16);
        final ConfigurationSetting original = new ConfigurationSetting().withKey(keyName).withValue("myValue");
        final ConfigurationSetting updated = new ConfigurationSetting().withKey(keyName).withValue("anotherValue");
        final ConfigurationSetting updated2 = new ConfigurationSetting().withKey(keyName).withValue("anotherValueIUpdated");

        StepVerifier.create(client.set(original))
            .assertNext(response -> assertConfigurationEquals(original, response))
            .verifyComplete();
        StepVerifier.create(client.set(updated))
            .assertNext(response -> assertConfigurationEquals(updated, response))
            .verifyComplete();
        StepVerifier.create(client.set(updated2))
            .assertNext(response -> assertConfigurationEquals(updated2, response))
            .verifyComplete();

        // Get a subset of revisions, the first revision and the original value.
        final RevisionOptions revisions = new RevisionOptions().key(keyName).range(new RevisionRange(1));
        StepVerifier.create(client.listKeyValueRevisions(revisions))
            .assertNext(response -> {
                assertEquals(keyName, response.key());
                assertEquals(updated.value(), response.value());
            })
            .assertNext(response -> {
                assertEquals(keyName, response.key());
                assertEquals(original.value(), response.value());
            })
            .verifyComplete();

        // Get a subset of revisions, the current value and the first revision.
        StepVerifier.create(client.listKeyValueRevisions(new RevisionOptions().key(keyName).range(new RevisionRange(0, 1))))
            .assertNext(response -> {
                assertEquals(keyName, response.key());
                assertEquals(updated2.value(), response.value());
            })
            .assertNext(response -> {
                assertEquals(keyName, response.key());
                assertEquals(updated.value(), response.value());
            })
            .verifyComplete();

        // Gets an error because there is no 3rd revision.
        final RevisionOptions revisions2 = new RevisionOptions().key(keyName).range(new RevisionRange(2, 3));
        StepVerifier.create(client.listKeyValueRevisions(revisions2))
            .expectErrorSatisfies(error -> {
                assertTrue(error instanceof RestException);
                assertTrue(error.getMessage().contains("416"));
            }).verify();
    }

    /**
     * Verifies that we can get a subset of revisions based on the "acceptDateTime"
     */
    @Test
    public void listRevisionsAcceptDateTime() {
        final String keyName = SdkContext.randomResourceName(keyPrefix, 16);
        final ConfigurationSetting original = new ConfigurationSetting().withKey(keyName).withValue("myValue");
        final ConfigurationSetting updated = new ConfigurationSetting().withKey(keyName).withValue("anotherValue");
        final ConfigurationSetting updated2 = new ConfigurationSetting().withKey(keyName).withValue("anotherValue2");

        // Create 3 revisions of the same key.
        StepVerifier.create(client.set(original).delayElement(Duration.ofSeconds(2)))
            .assertNext(response -> assertConfigurationEquals(original, response))
            .verifyComplete();
        StepVerifier.create(client.set(updated).delayElement(Duration.ofSeconds(2)))
            .assertNext(response -> assertConfigurationEquals(updated, response))
            .verifyComplete();
        StepVerifier.create(client.set(updated2))
            .assertNext(response -> assertConfigurationEquals(updated2, response))
            .verifyComplete();

        // Gets all versions of this value.
        List<ConfigurationSetting> revisions = client.listKeyValueRevisions(new RevisionOptions().key(keyName)).collectList().block();

        assertNotNull(revisions);
        assertEquals(3, revisions.size());

        // We want to fetch all the revisions that existed up and including when the first revision was created.
        // Revisions are returned in descending order from creation date.
        RevisionOptions options = new RevisionOptions().key(keyName).acceptDatetime(revisions.get(1).lastModified());
        StepVerifier.create(client.listKeyValueRevisions(options))
            .assertNext(response -> assertConfigurationEquals(updated, response))
            .assertNext(response -> assertConfigurationEquals(original, response))
            .verifyComplete();
    }

    /**
     * Verifies that, given a ton of existing settings, we can list the ConfigurationSettings using pagination
     * (ie. where 'nextLink' has a URL pointing to the next page of results.)
     *
     * TODO (conniey): Remove the manual retry when issue is fixed: https://github.com/azure/azure-sdk-for-java/issues/3183
     */
    @Test
    public void listSettingsWithPagination() {
        final String label = "listed-label";
        final int numberExpected = 50;
        List<ConfigurationSetting> settings = IntStream.range(0, numberExpected)
                .mapToObj(value -> new ConfigurationSetting()
                        .withKey(keyPrefix + "-" + value)
                        .withValue("myValue")
                        .withLabel(label))
                .collect(Collectors.toList());

        List<Mono<RestResponse<ConfigurationSetting>>> results = new ArrayList<>();
        for (ConfigurationSetting setting : settings) {
            results.add(client.set(setting).retryBackoff(2, Duration.ofSeconds(30)));
        }

        RequestOptions filter = new RequestOptions().label(label);

        Flux.merge(results).blockLast();
        StepVerifier.create(client.listKeyValues(filter))
                .expectNextCount(numberExpected)
                .expectComplete()
                .verify();
    }

    /**
     * Verifies the conditional "GET" scenario where the setting has yet to be updated, resulting in a 304. This GET
     * scenario will return a setting when the etag provided does not match the one of the current setting.
     */
    @Ignore("Getting a configuration setting only when the value has changed is not a common scenario.")
    @Test
    public void getSettingWhenValueNotUpdated() {
        final String key = SdkContext.randomResourceName(keyPrefix, 16);
        final ConfigurationSetting expected = new ConfigurationSetting().withKey(key).withValue("myValue");
        final ConfigurationSetting newExpected = new ConfigurationSetting().withKey(key).withValue("myNewValue");
        final RestResponse<ConfigurationSetting> block = client.add(expected).single().block();

        assertNotNull(block);
        assertConfigurationEquals(expected, block);

//        String etag = block.body().etag();
//        StepVerifier.create(client.get(key, null, etag))
//                .expectErrorSatisfies(ex -> {
//                    Assert.assertTrue(ex instanceof RestException);
//                    // etag has not changed, so getting 304 NotModified code according to service spec
//                    Assert.assertTrue(ex.getMessage().contains("304"));
//                })
//                .verify();

        StepVerifier.create(client.set(newExpected))
                .assertNext(response -> assertConfigurationEquals(newExpected, response))
                .expectComplete()
                .verify();
    }

    @Ignore("This test exists to clean up resources missed due to 429s.")
    @Test
    public void deleteAllSettings() {
        client.listKeyValues(new RequestOptions().key("key*"))
                .flatMap(configurationSetting -> {
                    logger.info("Deleting key:label [{}:{}]. isLocked? {}", configurationSetting.key(), configurationSetting.label(), configurationSetting.isLocked());

                    if (configurationSetting.isLocked()) {
                        return client.unlock(configurationSetting.key(), configurationSetting.label()).flatMap(response -> {
                            ConfigurationSetting kv = response.body();
                            return client.delete(kv.key(), kv.label(), null);
                        });
                    } else {
                        return client.delete(configurationSetting.key(), configurationSetting.label(), null);
                    }
                }).blockLast();
    }

    @DataPoints("invalidKeys")
    public static String[] invalidKeys = new String[] { "", null };

    /**
     * Test the API will not make a get call without having a key passed, an IllegalArgumentException should be thrown.
     */
    @Theory
    public void getSettingRequiresKey(@FromDataPoints("invalidKeys") String key) {
        assertRunnableThrowsArgumentException(() -> client.get(key));
    }

    /**
     * Test the API will not make a delete call without having a key passed, an IllegalArgumentException should be thrown.
     */
    @Theory
    public void deleteSettingRequiresKey(@FromDataPoints("invalidKeys") String key, String label, String etag) {
        assertRunnableThrowsArgumentException(() -> client.delete(key));
        assertRunnableThrowsArgumentException(() -> client.delete(key, label, etag));
    }

    /**
     * Test the API will not make lock or unlock calls without having a key passed, an IllegalArgumentException should be thrown.
     */
    @Theory
    public void lockAndUnlockRequiresKey(@FromDataPoints("invalidKeys") String key, String label) {
        assertRunnableThrowsArgumentException(() -> client.lock(key));
        assertRunnableThrowsArgumentException(() -> client.lock(key, label));

        assertRunnableThrowsArgumentException(() -> client.unlock(key));
        assertRunnableThrowsArgumentException(() -> client.unlock(key, label));
    }

    /**
     * Helper method to verify that the RestResponse matches what was expected. This method assumes a response status of 200.
     * @param expected ConfigurationSetting expected to be returned by the service
     * @param response RestResponse returned by the service, the body should contain a ConfigurationSetting
     */
    private static void assertConfigurationEquals(ConfigurationSetting expected, RestResponse<ConfigurationSetting> response) {
        assertConfigurationEquals(expected, response, 200);
    }

    /**
     * Helper method to verify that the RestResponse matches what was expected.
     * @param expected ConfigurationSetting expected to be returned by the service
     * @param response RestResponse returned from the service, the body should contain a ConfigurationSetting
     * @param expectedStatusCode Expected HTTP status code returned by the service
     */
    private static void assertConfigurationEquals(ConfigurationSetting expected, RestResponse<ConfigurationSetting> response, final int expectedStatusCode) {
        assertNotNull(response);
        assertEquals(expectedStatusCode, response.statusCode());

        assertConfigurationEquals(expected, response.body());
    }

    /**
     * Helper method to verify that the returned ConfigurationSetting matches what was expected.
     * @param expected ConfigurationSetting expected to be returned by the service
     * @param actual ConfigurationSetting contained in the RestResponse body
     */
    private static void assertConfigurationEquals(ConfigurationSetting expected, ConfigurationSetting actual) {
        if (expected == null) {
            assertNull(actual);
            return;
        }

        assertNotNull(actual);
        assertEquals(expected.key(), actual.key());

        // This is because we have the "null" label which is deciphered in the service as "\0".
        if (ConfigurationSetting.NULL_LABEL.equals(expected.label())) {
            assertNull(actual.label());
        } else {
            assertEquals(expected.label(), actual.label());
        }

        assertEquals(expected.value(), actual.value());
        assertEquals(expected.contentType(), actual.contentType());

        if (expected.tags() != null) {
            assertEquals(expected.tags().size(), actual.tags().size());

            expected.tags().forEach((key, value) -> {
                assertTrue(actual.tags().containsKey(key));
                assertEquals(value, actual.tags().get(key));
            });
        }
    }

    /**
     * Helper method to verify the error was a RestException and it has a specific HTTP response code.
     * @param ex Expected error thrown during the test
     * @param expectedStatusCode Expected HTTP status code contained in the error response
     */
    private static void assertRestException(Throwable ex, int expectedStatusCode) {
        assertTrue(ex instanceof RestException);
        assertEquals(expectedStatusCode, ((RestException) ex).response().statusCode());
    }

    /**
     * Helper method to verify that a command throws an IllegalArgumentException.
     * @param exceptionThrower Command that should throw the exception
     */
    private static void assertRunnableThrowsArgumentException(Runnable exceptionThrower) {
        try {
            exceptionThrower.run();
            fail();
        } catch (IllegalArgumentException ex) {

        }
    }
}
