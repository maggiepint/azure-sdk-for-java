/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.azure.common.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Annotation for parameterized host name targeting a REST service.
 *
 * <p>This is the 'host' field or 'x-ms-parameterized-host.hostTemplate' field in a Swagger document. parameters are
 * enclosed in {}s, e.g. {accountName}. An HTTP client must accept the parameterized host as the base URL for the request,
 * replacing the parameters during runtime with the actual values users provide.</p>
 *
 * <p>For parameterized hosts, parameters annotated with {@link HostParam} must be provided. See Java docs in
 * {@link HostParam} for directions for host parameters.</p>
 *
 * <p>The host's value must contain the scheme/protocol and the host. The host's value may contain the
 * port number.</p>
 *
 * <p><strong>Example 1: Static annotation</strong></p>
 *
 * <pre>
 * {@literal @}Host("https://management.azure.com")
 *  interface VirtualMachinesService {
 *   {@literal @}GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.Compute/virtualMachines/{vmName}")
 *    VirtualMachine getByResourceGroup(@PathParam("resourceGroupName") String rgName, @PathParam("vmName") String vmName, @PathParam("subscriptionId") String subscriptionId);
 *  }</pre>
 *
 * <p><strong>Example 2: Dynamic annotation</strong></p>
 *
 * <pre>
 * {@literal @}Host("https://{vaultName}.vault.azure.net:443")
 *  interface KeyVaultService {
 *    {@literal @}GET("secrets/{secretName}")
 *     Secret get(@HostParam("vaultName") String vaultName, @PathParam("secretName") String secretName);
 *  }</pre>
 */
@Target(value = {TYPE})
@Retention(RetentionPolicy.RUNTIME)        // Record this annotation in the class file and make it available during runtime.
public @interface Host {
    /**
     * Get the protocol/scheme, host, and optional port number in a single string.
     * @return The protocol/scheme, host, and optional port number in a single string.
     */
    String value() default "";
}
