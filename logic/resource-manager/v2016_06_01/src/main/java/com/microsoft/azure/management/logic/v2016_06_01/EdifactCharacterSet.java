/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.logic.v2016_06_01;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for EdifactCharacterSet.
 */
public enum EdifactCharacterSet {
    /** Enum value NotSpecified. */
    NOT_SPECIFIED("NotSpecified"),

    /** Enum value UNOB. */
    UNOB("UNOB"),

    /** Enum value UNOA. */
    UNOA("UNOA"),

    /** Enum value UNOC. */
    UNOC("UNOC"),

    /** Enum value UNOD. */
    UNOD("UNOD"),

    /** Enum value UNOE. */
    UNOE("UNOE"),

    /** Enum value UNOF. */
    UNOF("UNOF"),

    /** Enum value UNOG. */
    UNOG("UNOG"),

    /** Enum value UNOH. */
    UNOH("UNOH"),

    /** Enum value UNOI. */
    UNOI("UNOI"),

    /** Enum value UNOJ. */
    UNOJ("UNOJ"),

    /** Enum value UNOK. */
    UNOK("UNOK"),

    /** Enum value UNOX. */
    UNOX("UNOX"),

    /** Enum value UNOY. */
    UNOY("UNOY"),

    /** Enum value KECA. */
    KECA("KECA");

    /** The actual serialized value for a EdifactCharacterSet instance. */
    private String value;

    EdifactCharacterSet(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a EdifactCharacterSet instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed EdifactCharacterSet object, or null if unable to parse.
     */
    @JsonCreator
    public static EdifactCharacterSet fromString(String value) {
        EdifactCharacterSet[] items = EdifactCharacterSet.values();
        for (EdifactCharacterSet item : items) {
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
