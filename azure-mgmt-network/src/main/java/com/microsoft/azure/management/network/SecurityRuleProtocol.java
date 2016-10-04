/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines values for SecurityRuleProtocol.
 */
public final class SecurityRuleProtocol {
    /** Static value Tcp for SecurityRuleProtocol. */
    public static final SecurityRuleProtocol TCP = new SecurityRuleProtocol("Tcp");

    /** Static value Udp for SecurityRuleProtocol. */
    public static final SecurityRuleProtocol UDP = new SecurityRuleProtocol("Udp");

    /** Static value * for SecurityRuleProtocol. */
    public static final SecurityRuleProtocol ASTERISK = new SecurityRuleProtocol("*");

    private String value;

    /**
     * Creates a custom value for SecurityRuleProtocol.
     * @param value the custom value
     */
    public SecurityRuleProtocol(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SecurityRuleProtocol)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        SecurityRuleProtocol rhs = (SecurityRuleProtocol) obj;
        if (value == null) {
            return rhs.value == null;
        } else {
            return value.equals(rhs.value);
        }
    }
}