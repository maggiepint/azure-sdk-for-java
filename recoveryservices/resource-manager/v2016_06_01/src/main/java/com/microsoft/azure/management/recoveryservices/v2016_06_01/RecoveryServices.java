/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.recoveryservices.v2016_06_01;

import rx.Observable;
import com.microsoft.azure.management.recoveryservices.v2016_06_01.implementation.RecoveryServicesInner;
import com.microsoft.azure.arm.model.HasInner;

/**
 * Type representing RecoveryServices.
 */
public interface RecoveryServices extends HasInner<RecoveryServicesInner> {
    /**
     * API to check for resource name availability.
    A name is available if no other resource exists that has the same SubscriptionId, Resource Name and Type
    or if one or more such resources exist, each of these must be GCed and their time of deletion be more than 24 Hours Ago.
     *
     * @param resourceGroupName The name of the resource group where the recovery services vault is present.
     * @param location Location of the resource
     * @param input Contains information about Resource type and Resource name
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    Observable<ResourceNameAvailabilityResponseResource> checkNameAvailabilityAsync(String resourceGroupName, String location, ResourceNameAvailabilityParameters input);

}
