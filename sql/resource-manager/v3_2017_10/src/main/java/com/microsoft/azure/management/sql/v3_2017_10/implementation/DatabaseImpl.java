/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.sql.v3_2017_10.implementation;

import com.microsoft.azure.management.sql.v3_2017_10.Database;
import com.microsoft.azure.arm.model.implementation.CreatableUpdatableImpl;
import rx.Observable;
import com.microsoft.azure.management.sql.v3_2017_10.DatabaseUpdate;
import java.util.UUID;
import org.joda.time.DateTime;
import java.util.Map;
import com.microsoft.azure.management.sql.v3_2017_10.Sku;
import com.microsoft.azure.management.sql.v3_2017_10.CreateMode;
import com.microsoft.azure.management.sql.v3_2017_10.SampleName;
import com.microsoft.azure.management.sql.v3_2017_10.DatabaseStatus;
import com.microsoft.azure.management.sql.v3_2017_10.CatalogCollationType;
import com.microsoft.azure.management.sql.v3_2017_10.DatabaseLicenseType;
import com.microsoft.azure.management.sql.v3_2017_10.DatabaseReadScale;
import rx.functions.Func1;

class DatabaseImpl extends CreatableUpdatableImpl<Database, DatabaseInner, DatabaseImpl> implements Database, Database.Definition, Database.Update {
    private final SqlManager manager;
    private String resourceGroupName;
    private String serverName;
    private String databaseName;
    private DatabaseUpdate updateParameter;

    DatabaseImpl(String name, SqlManager manager) {
        super(name, new DatabaseInner());
        this.manager = manager;
        // Set resource name
        this.databaseName = name;
        //
        this.updateParameter = new DatabaseUpdate();
    }

    DatabaseImpl(DatabaseInner inner, SqlManager manager) {
        super(inner.name(), inner);
        this.manager = manager;
        // Set resource name
        this.databaseName = inner.name();
        // resource ancestor names
        this.resourceGroupName = IdParsingUtils.getValueFromIdByName(inner.id(), "resourceGroups");
        this.serverName = IdParsingUtils.getValueFromIdByName(inner.id(), "servers");
        this.databaseName = IdParsingUtils.getValueFromIdByName(inner.id(), "databases");
        //
        this.updateParameter = new DatabaseUpdate();
    }

    @Override
    public SqlManager manager() {
        return this.manager;
    }

    @Override
    public Observable<Database> createResourceAsync() {
        DatabasesInner client = this.manager().inner().databases();
        return client.createOrUpdateAsync(this.resourceGroupName, this.serverName, this.databaseName, this.inner())
            .map(new Func1<DatabaseInner, DatabaseInner>() {
               @Override
               public DatabaseInner call(DatabaseInner resource) {
                   resetCreateUpdateParameters();
                   return resource;
               }
            })
            .map(innerToFluentMap(this));
    }

    @Override
    public Observable<Database> updateResourceAsync() {
        DatabasesInner client = this.manager().inner().databases();
        return client.updateAsync(this.resourceGroupName, this.serverName, this.databaseName, this.updateParameter)
            .map(new Func1<DatabaseInner, DatabaseInner>() {
               @Override
               public DatabaseInner call(DatabaseInner resource) {
                   resetCreateUpdateParameters();
                   return resource;
               }
            })
            .map(innerToFluentMap(this));
    }

    @Override
    protected Observable<DatabaseInner> getInnerAsync() {
        DatabasesInner client = this.manager().inner().databases();
        return client.getAsync(this.resourceGroupName, this.serverName, this.databaseName);
    }

    @Override
    public boolean isInCreateMode() {
        return this.inner().id() == null;
    }

    private void resetCreateUpdateParameters() {
        this.updateParameter = new DatabaseUpdate();
    }

    @Override
    public CatalogCollationType catalogCollation() {
        return this.inner().catalogCollation();
    }

    @Override
    public String collation() {
        return this.inner().collation();
    }

    @Override
    public CreateMode createMode() {
        return this.inner().createMode();
    }

    @Override
    public DateTime creationDate() {
        return this.inner().creationDate();
    }

    @Override
    public String currentServiceObjectiveName() {
        return this.inner().currentServiceObjectiveName();
    }

    @Override
    public Sku currentSku() {
        return this.inner().currentSku();
    }

    @Override
    public UUID databaseId() {
        return this.inner().databaseId();
    }

    @Override
    public String defaultSecondaryLocation() {
        return this.inner().defaultSecondaryLocation();
    }

    @Override
    public DateTime earliestRestoreDate() {
        return this.inner().earliestRestoreDate();
    }

    @Override
    public String elasticPoolId() {
        return this.inner().elasticPoolId();
    }

    @Override
    public String failoverGroupId() {
        return this.inner().failoverGroupId();
    }

    @Override
    public String id() {
        return this.inner().id();
    }

    @Override
    public String kind() {
        return this.inner().kind();
    }

    @Override
    public DatabaseLicenseType licenseType() {
        return this.inner().licenseType();
    }

    @Override
    public String location() {
        return this.inner().location();
    }

    @Override
    public String longTermRetentionBackupResourceId() {
        return this.inner().longTermRetentionBackupResourceId();
    }

    @Override
    public String managedBy() {
        return this.inner().managedBy();
    }

    @Override
    public Long maxLogSizeBytes() {
        return this.inner().maxLogSizeBytes();
    }

    @Override
    public Long maxSizeBytes() {
        return this.inner().maxSizeBytes();
    }

    @Override
    public String name() {
        return this.inner().name();
    }

    @Override
    public DatabaseReadScale readScale() {
        return this.inner().readScale();
    }

    @Override
    public String recoverableDatabaseId() {
        return this.inner().recoverableDatabaseId();
    }

    @Override
    public String recoveryServicesRecoveryPointId() {
        return this.inner().recoveryServicesRecoveryPointId();
    }

    @Override
    public String requestedServiceObjectiveName() {
        return this.inner().requestedServiceObjectiveName();
    }

    @Override
    public String restorableDroppedDatabaseId() {
        return this.inner().restorableDroppedDatabaseId();
    }

    @Override
    public DateTime restorePointInTime() {
        return this.inner().restorePointInTime();
    }

    @Override
    public SampleName sampleName() {
        return this.inner().sampleName();
    }

    @Override
    public Sku sku() {
        return this.inner().sku();
    }

    @Override
    public DateTime sourceDatabaseDeletionDate() {
        return this.inner().sourceDatabaseDeletionDate();
    }

    @Override
    public String sourceDatabaseId() {
        return this.inner().sourceDatabaseId();
    }

    @Override
    public DatabaseStatus status() {
        return this.inner().status();
    }

    @Override
    public Map<String, String> tags() {
        return this.inner().getTags();
    }

    @Override
    public String type() {
        return this.inner().type();
    }

    @Override
    public Boolean zoneRedundant() {
        return this.inner().zoneRedundant();
    }

    @Override
    public DatabaseImpl withExistingServer(String resourceGroupName, String serverName) {
        this.resourceGroupName = resourceGroupName;
        this.serverName = serverName;
        return this;
    }

    @Override
    public DatabaseImpl withLocation(String location) {
        this.inner().withLocation(location);
        return this;
    }

    @Override
    public DatabaseImpl withCatalogCollation(CatalogCollationType catalogCollation) {
        if (isInCreateMode()) {
            this.inner().withCatalogCollation(catalogCollation);
        } else {
            this.updateParameter.withCatalogCollation(catalogCollation);
        }
        return this;
    }

    @Override
    public DatabaseImpl withCollation(String collation) {
        if (isInCreateMode()) {
            this.inner().withCollation(collation);
        } else {
            this.updateParameter.withCollation(collation);
        }
        return this;
    }

    @Override
    public DatabaseImpl withCreateMode(CreateMode createMode) {
        if (isInCreateMode()) {
            this.inner().withCreateMode(createMode);
        } else {
            this.updateParameter.withCreateMode(createMode);
        }
        return this;
    }

    @Override
    public DatabaseImpl withElasticPoolId(String elasticPoolId) {
        if (isInCreateMode()) {
            this.inner().withElasticPoolId(elasticPoolId);
        } else {
            this.updateParameter.withElasticPoolId(elasticPoolId);
        }
        return this;
    }

    @Override
    public DatabaseImpl withLicenseType(DatabaseLicenseType licenseType) {
        if (isInCreateMode()) {
            this.inner().withLicenseType(licenseType);
        } else {
            this.updateParameter.withLicenseType(licenseType);
        }
        return this;
    }

    @Override
    public DatabaseImpl withLongTermRetentionBackupResourceId(String longTermRetentionBackupResourceId) {
        if (isInCreateMode()) {
            this.inner().withLongTermRetentionBackupResourceId(longTermRetentionBackupResourceId);
        } else {
            this.updateParameter.withLongTermRetentionBackupResourceId(longTermRetentionBackupResourceId);
        }
        return this;
    }

    @Override
    public DatabaseImpl withMaxSizeBytes(Long maxSizeBytes) {
        if (isInCreateMode()) {
            this.inner().withMaxSizeBytes(maxSizeBytes);
        } else {
            this.updateParameter.withMaxSizeBytes(maxSizeBytes);
        }
        return this;
    }

    @Override
    public DatabaseImpl withReadScale(DatabaseReadScale readScale) {
        if (isInCreateMode()) {
            this.inner().withReadScale(readScale);
        } else {
            this.updateParameter.withReadScale(readScale);
        }
        return this;
    }

    @Override
    public DatabaseImpl withRecoverableDatabaseId(String recoverableDatabaseId) {
        if (isInCreateMode()) {
            this.inner().withRecoverableDatabaseId(recoverableDatabaseId);
        } else {
            this.updateParameter.withRecoverableDatabaseId(recoverableDatabaseId);
        }
        return this;
    }

    @Override
    public DatabaseImpl withRecoveryServicesRecoveryPointId(String recoveryServicesRecoveryPointId) {
        if (isInCreateMode()) {
            this.inner().withRecoveryServicesRecoveryPointId(recoveryServicesRecoveryPointId);
        } else {
            this.updateParameter.withRecoveryServicesRecoveryPointId(recoveryServicesRecoveryPointId);
        }
        return this;
    }

    @Override
    public DatabaseImpl withRestorableDroppedDatabaseId(String restorableDroppedDatabaseId) {
        if (isInCreateMode()) {
            this.inner().withRestorableDroppedDatabaseId(restorableDroppedDatabaseId);
        } else {
            this.updateParameter.withRestorableDroppedDatabaseId(restorableDroppedDatabaseId);
        }
        return this;
    }

    @Override
    public DatabaseImpl withRestorePointInTime(DateTime restorePointInTime) {
        if (isInCreateMode()) {
            this.inner().withRestorePointInTime(restorePointInTime);
        } else {
            this.updateParameter.withRestorePointInTime(restorePointInTime);
        }
        return this;
    }

    @Override
    public DatabaseImpl withSampleName(SampleName sampleName) {
        if (isInCreateMode()) {
            this.inner().withSampleName(sampleName);
        } else {
            this.updateParameter.withSampleName(sampleName);
        }
        return this;
    }

    @Override
    public DatabaseImpl withSku(Sku sku) {
        if (isInCreateMode()) {
            this.inner().withSku(sku);
        } else {
            this.updateParameter.withSku(sku);
        }
        return this;
    }

    @Override
    public DatabaseImpl withSourceDatabaseDeletionDate(DateTime sourceDatabaseDeletionDate) {
        if (isInCreateMode()) {
            this.inner().withSourceDatabaseDeletionDate(sourceDatabaseDeletionDate);
        } else {
            this.updateParameter.withSourceDatabaseDeletionDate(sourceDatabaseDeletionDate);
        }
        return this;
    }

    @Override
    public DatabaseImpl withSourceDatabaseId(String sourceDatabaseId) {
        if (isInCreateMode()) {
            this.inner().withSourceDatabaseId(sourceDatabaseId);
        } else {
            this.updateParameter.withSourceDatabaseId(sourceDatabaseId);
        }
        return this;
    }

    @Override
    public DatabaseImpl withTags(Map<String, String> tags) {
        if (isInCreateMode()) {
            this.inner().withTags(tags);
        } else {
            this.updateParameter.withTags(tags);
        }
        return this;
    }

    @Override
    public DatabaseImpl withZoneRedundant(Boolean zoneRedundant) {
        if (isInCreateMode()) {
            this.inner().withZoneRedundant(zoneRedundant);
        } else {
            this.updateParameter.withZoneRedundant(zoneRedundant);
        }
        return this;
    }

}