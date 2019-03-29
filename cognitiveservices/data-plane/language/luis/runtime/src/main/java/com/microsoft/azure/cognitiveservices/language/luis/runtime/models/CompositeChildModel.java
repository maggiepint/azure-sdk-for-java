/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.cognitiveservices.language.luis.runtime.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Child entity in a LUIS Composite Entity.
 */
public class CompositeChildModel {
    /**
     * Type of child entity.
     */
    @JsonProperty(value = "type", required = true)
    private String type;

    /**
     * Value extracted by LUIS.
     */
    @JsonProperty(value = "value", required = true)
    private String value;

    /**
     * Get the type value.
     *
     * @return the type value
     */
    public String type() {
        return this.type;
    }

    /**
     * Set the type value.
     *
     * @param type the type value to set
     * @return the CompositeChildModel object itself.
     */
    public CompositeChildModel withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get the value value.
     *
     * @return the value value
     */
    public String value() {
        return this.value;
    }

    /**
     * Set the value value.
     *
     * @param value the value value to set
     * @return the CompositeChildModel object itself.
     */
    public CompositeChildModel withValue(String value) {
        this.value = value;
        return this;
    }

}
