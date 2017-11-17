/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.cognitiveservices.vision.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Result of the verify operation.
 */
public class VerifyResult {
    /**
     * True if the two faces belong to the same person or the face belongs to
     * the person, otherwise false.
     */
    @JsonProperty(value = "isIdentical", required = true)
    private boolean isIdentical;

    /**
     * "A number indicates the similarity confidence of whether two faces
     * belong to the same person, or whether the face belongs to the person. By
     * default, isIdentical is set to True if similarity confidence is greater
     * than or equal to 0.5. This is useful for advanced users to override
     * "isIdentical" and fine-tune the result on their own data".
     */
    @JsonProperty(value = "confidence")
    private Double confidence;

    /**
     * Get the isIdentical value.
     *
     * @return the isIdentical value
     */
    public boolean isIdentical() {
        return this.isIdentical;
    }

    /**
     * Set the isIdentical value.
     *
     * @param isIdentical the isIdentical value to set
     * @return the VerifyResult object itself.
     */
    public VerifyResult withIsIdentical(boolean isIdentical) {
        this.isIdentical = isIdentical;
        return this;
    }

    /**
     * Get the confidence value.
     *
     * @return the confidence value
     */
    public Double confidence() {
        return this.confidence;
    }

    /**
     * Set the confidence value.
     *
     * @param confidence the confidence value to set
     * @return the VerifyResult object itself.
     */
    public VerifyResult withConfidence(Double confidence) {
        this.confidence = confidence;
        return this;
    }

}
