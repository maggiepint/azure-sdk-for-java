/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.cognitiveservices.vision.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error information returned by the API.
 */
public class APIError {
    /**
     * The error property.
     */
    @JsonProperty(value = "error")
    private Error error;

    /**
     * Get the error value.
     *
     * @return the error value
     */
    public Error error() {
        return this.error;
    }

    /**
     * Set the error value.
     *
     * @param error the error value to set
     * @return the APIError object itself.
     */
    public APIError withError(Error error) {
        this.error = error;
        return this;
    }

}
