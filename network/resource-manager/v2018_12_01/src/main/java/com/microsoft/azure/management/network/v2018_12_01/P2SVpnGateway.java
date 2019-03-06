/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.network.v2018_12_01;

import com.microsoft.azure.arm.model.HasInner;
import com.microsoft.azure.arm.resources.models.Resource;
import com.microsoft.azure.arm.resources.models.GroupableResourceCore;
import com.microsoft.azure.arm.resources.models.HasResourceGroup;
import com.microsoft.azure.arm.model.Refreshable;
import com.microsoft.azure.arm.model.Updatable;
import com.microsoft.azure.arm.model.Appliable;
import com.microsoft.azure.arm.model.Creatable;
import com.microsoft.azure.arm.resources.models.HasManager;
import com.microsoft.azure.management.network.v2018_12_01.implementation.NetworkManager;
import com.microsoft.azure.SubResource;
import com.microsoft.azure.management.network.v2018_12_01.implementation.P2SVpnGatewayInner;

/**
 * Type representing P2SVpnGateway.
 */
public interface P2SVpnGateway extends HasInner<P2SVpnGatewayInner>, Resource, GroupableResourceCore<NetworkManager, P2SVpnGatewayInner>, HasResourceGroup, Refreshable<P2SVpnGateway>, Updatable<P2SVpnGateway.Update>, HasManager<NetworkManager> {
    /**
     * @return the etag value.
     */
    String etag();

    /**
     * @return the p2SVpnServerConfiguration value.
     */
    SubResource p2SVpnServerConfiguration();

    /**
     * @return the provisioningState value.
     */
    ProvisioningState provisioningState();

    /**
     * @return the virtualHub value.
     */
    SubResource virtualHub();

    /**
     * @return the vpnClientAddressPool value.
     */
    AddressSpace vpnClientAddressPool();

    /**
     * @return the vpnClientConnectionHealth value.
     */
    VpnClientConnectionHealth vpnClientConnectionHealth();

    /**
     * @return the vpnGatewayScaleUnit value.
     */
    Integer vpnGatewayScaleUnit();

    /**
     * The entirety of the P2SVpnGateway definition.
     */
    interface Definition extends DefinitionStages.Blank, DefinitionStages.WithGroup, DefinitionStages.WithCreate {
    }

    /**
     * Grouping of P2SVpnGateway definition stages.
     */
    interface DefinitionStages {
        /**
         * The first stage of a P2SVpnGateway definition.
         */
        interface Blank extends GroupableResourceCore.DefinitionWithRegion<WithGroup> {
        }

        /**
         * The stage of the P2SVpnGateway definition allowing to specify the resource group.
         */
        interface WithGroup extends GroupableResourceCore.DefinitionStages.WithGroup<WithCreate> {
        }

        /**
         * The stage of the p2svpngateway definition allowing to specify P2SVpnServerConfiguration.
         */
        interface WithP2SVpnServerConfiguration {
            /**
             * Specifies p2SVpnServerConfiguration.
             * @param p2SVpnServerConfiguration The P2SVpnServerConfiguration to which the p2sVpnGateway is attached to
             * @return the next definition stage
             */
            WithCreate withP2SVpnServerConfiguration(SubResource p2SVpnServerConfiguration);
        }

        /**
         * The stage of the p2svpngateway definition allowing to specify ProvisioningState.
         */
        interface WithProvisioningState {
            /**
             * Specifies provisioningState.
             * @param provisioningState The provisioning state of the resource. Possible values include: 'Succeeded', 'Updating', 'Deleting', 'Failed'
             * @return the next definition stage
             */
            WithCreate withProvisioningState(ProvisioningState provisioningState);
        }

        /**
         * The stage of the p2svpngateway definition allowing to specify VirtualHub.
         */
        interface WithVirtualHub {
            /**
             * Specifies virtualHub.
             * @param virtualHub The VirtualHub to which the gateway belongs
             * @return the next definition stage
             */
            WithCreate withVirtualHub(SubResource virtualHub);
        }

        /**
         * The stage of the p2svpngateway definition allowing to specify VpnClientAddressPool.
         */
        interface WithVpnClientAddressPool {
            /**
             * Specifies vpnClientAddressPool.
             * @param vpnClientAddressPool The reference of the address space resource which represents Address space for P2S VpnClient
             * @return the next definition stage
             */
            WithCreate withVpnClientAddressPool(AddressSpace vpnClientAddressPool);
        }

        /**
         * The stage of the p2svpngateway definition allowing to specify VpnGatewayScaleUnit.
         */
        interface WithVpnGatewayScaleUnit {
            /**
             * Specifies vpnGatewayScaleUnit.
             * @param vpnGatewayScaleUnit The scale unit for this p2s vpn gateway
             * @return the next definition stage
             */
            WithCreate withVpnGatewayScaleUnit(Integer vpnGatewayScaleUnit);
        }

        /**
         * The stage of the definition which contains all the minimum required inputs for
         * the resource to be created (via {@link WithCreate#create()}), but also allows
         * for any other optional settings to be specified.
         */
        interface WithCreate extends Creatable<P2SVpnGateway>, Resource.DefinitionWithTags<WithCreate>, DefinitionStages.WithP2SVpnServerConfiguration, DefinitionStages.WithProvisioningState, DefinitionStages.WithVirtualHub, DefinitionStages.WithVpnClientAddressPool, DefinitionStages.WithVpnGatewayScaleUnit {
        }
    }
    /**
     * The template for a P2SVpnGateway update operation, containing all the settings that can be modified.
     */
    interface Update extends Appliable<P2SVpnGateway>, Resource.UpdateWithTags<Update>, UpdateStages.WithP2SVpnServerConfiguration, UpdateStages.WithProvisioningState, UpdateStages.WithVirtualHub, UpdateStages.WithVpnClientAddressPool, UpdateStages.WithVpnGatewayScaleUnit {
    }

    /**
     * Grouping of P2SVpnGateway update stages.
     */
    interface UpdateStages {
        /**
         * The stage of the p2svpngateway update allowing to specify P2SVpnServerConfiguration.
         */
        interface WithP2SVpnServerConfiguration {
            /**
             * Specifies p2SVpnServerConfiguration.
             * @param p2SVpnServerConfiguration The P2SVpnServerConfiguration to which the p2sVpnGateway is attached to
             * @return the next update stage
             */
            Update withP2SVpnServerConfiguration(SubResource p2SVpnServerConfiguration);
        }

        /**
         * The stage of the p2svpngateway update allowing to specify ProvisioningState.
         */
        interface WithProvisioningState {
            /**
             * Specifies provisioningState.
             * @param provisioningState The provisioning state of the resource. Possible values include: 'Succeeded', 'Updating', 'Deleting', 'Failed'
             * @return the next update stage
             */
            Update withProvisioningState(ProvisioningState provisioningState);
        }

        /**
         * The stage of the p2svpngateway update allowing to specify VirtualHub.
         */
        interface WithVirtualHub {
            /**
             * Specifies virtualHub.
             * @param virtualHub The VirtualHub to which the gateway belongs
             * @return the next update stage
             */
            Update withVirtualHub(SubResource virtualHub);
        }

        /**
         * The stage of the p2svpngateway update allowing to specify VpnClientAddressPool.
         */
        interface WithVpnClientAddressPool {
            /**
             * Specifies vpnClientAddressPool.
             * @param vpnClientAddressPool The reference of the address space resource which represents Address space for P2S VpnClient
             * @return the next update stage
             */
            Update withVpnClientAddressPool(AddressSpace vpnClientAddressPool);
        }

        /**
         * The stage of the p2svpngateway update allowing to specify VpnGatewayScaleUnit.
         */
        interface WithVpnGatewayScaleUnit {
            /**
             * Specifies vpnGatewayScaleUnit.
             * @param vpnGatewayScaleUnit The scale unit for this p2s vpn gateway
             * @return the next update stage
             */
            Update withVpnGatewayScaleUnit(Integer vpnGatewayScaleUnit);
        }

    }
}
