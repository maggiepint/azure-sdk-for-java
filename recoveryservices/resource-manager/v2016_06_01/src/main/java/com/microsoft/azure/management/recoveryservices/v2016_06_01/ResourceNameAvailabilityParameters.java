/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.recoveryservices.v2016_06_01;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resource Name availability input parameters - Resource type and resource
 * name.
 */
public class ResourceNameAvailabilityParameters {
    /**
     * Describes the Resource type: Microsoft.RecoveryServices/Vaults.
     */
    @JsonProperty(value = "type")
    private String type;

    /**
     * Resource name for which availability needs to be checked.
     */
    @JsonProperty(value = "name")
    private String name;

    /**
     * Get describes the Resource type: Microsoft.RecoveryServices/Vaults.
     *
     * @return the type value
     */
    public String type() {
        return this.type;
    }

    /**
     * Set describes the Resource type: Microsoft.RecoveryServices/Vaults.
     *
     * @param type the type value to set
     * @return the ResourceNameAvailabilityParameters object itself.
     */
    public ResourceNameAvailabilityParameters withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get resource name for which availability needs to be checked.
     *
     * @return the name value
     */
    public String name() {
        return this.name;
    }

    /**
     * Set resource name for which availability needs to be checked.
     *
     * @param name the name value to set
     * @return the ResourceNameAvailabilityParameters object itself.
     */
    public ResourceNameAvailabilityParameters withName(String name) {
        this.name = name;
        return this;
    }

}
