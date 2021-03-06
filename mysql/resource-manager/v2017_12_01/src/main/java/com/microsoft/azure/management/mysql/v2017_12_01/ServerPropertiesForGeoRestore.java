/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.mysql.v2017_12_01;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The properties used to create a new server by restoring to a different
 * region from a geo replicated backup.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "createMode")
@JsonTypeName("GeoRestore")
public class ServerPropertiesForGeoRestore extends ServerPropertiesForCreate {
    /**
     * The source server id to restore from.
     */
    @JsonProperty(value = "sourceServerId", required = true)
    private String sourceServerId;

    /**
     * Get the sourceServerId value.
     *
     * @return the sourceServerId value
     */
    public String sourceServerId() {
        return this.sourceServerId;
    }

    /**
     * Set the sourceServerId value.
     *
     * @param sourceServerId the sourceServerId value to set
     * @return the ServerPropertiesForGeoRestore object itself.
     */
    public ServerPropertiesForGeoRestore withSourceServerId(String sourceServerId) {
        this.sourceServerId = sourceServerId;
        return this;
    }

}
