/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.cognitiveservices.language.luis.authoring.models;


/**
 * The AddCustomPrebuiltDomainModelsOptionalParameter model.
 */
public class AddCustomPrebuiltDomainModelsOptionalParameter {
    /**
     * The domain name.
     */
    private String domainName;

    /**
     * Gets or sets the preferred language for the response.
     */
    private String thisclientacceptLanguage;

    /**
     * Get the domainName value.
     *
     * @return the domainName value
     */
    public String domainName() {
        return this.domainName;
    }

    /**
     * Set the domainName value.
     *
     * @param domainName the domainName value to set
     * @return the AddCustomPrebuiltDomainModelsOptionalParameter object itself.
     */
    public AddCustomPrebuiltDomainModelsOptionalParameter withDomainName(String domainName) {
        this.domainName = domainName;
        return this;
    }

    /**
     * Get the thisclientacceptLanguage value.
     *
     * @return the thisclientacceptLanguage value
     */
    public String thisclientacceptLanguage() {
        return this.thisclientacceptLanguage;
    }

    /**
     * Set the thisclientacceptLanguage value.
     *
     * @param thisclientacceptLanguage the thisclientacceptLanguage value to set
     * @return the AddCustomPrebuiltDomainModelsOptionalParameter object itself.
     */
    public AddCustomPrebuiltDomainModelsOptionalParameter withThisclientacceptLanguage(String thisclientacceptLanguage) {
        this.thisclientacceptLanguage = thisclientacceptLanguage;
        return this;
    }

}