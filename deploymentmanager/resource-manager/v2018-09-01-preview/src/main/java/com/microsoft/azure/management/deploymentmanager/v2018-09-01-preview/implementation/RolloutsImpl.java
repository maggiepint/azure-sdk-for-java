/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * def
 */

package com.microsoft.azure.management.deploymentmanager.v2018-09-01-preview.implementation;

import com.microsoft.azure.arm.resources.collection.implementation.GroupableResourcesCoreImpl;
import com.microsoft.azure.management.deploymentmanager.v2018-09-01-preview.Rollouts;
import com.microsoft.azure.management.deploymentmanager.v2018-09-01-preview.Rollout;
import rx.Observable;
import rx.Completable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import com.microsoft.azure.arm.resources.ResourceUtilsCore;
import com.microsoft.azure.arm.utils.RXMapper;
import rx.functions.Func1;
import com.microsoft.azure.arm.utils.PagedListConverter;

class RolloutsImpl extends GroupableResourcesCoreImpl<Rollout, RolloutImpl, RolloutInner, RolloutsInner, DeploymentManagerManager>  implements Rollouts {
    protected RolloutsImpl(DeploymentManagerManager manager) {
        super(manager.inner().rollouts(), manager);
    }

    @Override
    protected Observable<RolloutInner> getInnerAsync(String resourceGroupName, String name) {
        RolloutsInner client = this.inner();
        return client.getByResourceGroupAsync(resourceGroupName, name);
    }

    @Override
    protected Completable deleteInnerAsync(String resourceGroupName, String name) {
        RolloutsInner client = this.inner();
        return client.deleteAsync(resourceGroupName, name).toCompletable();
    }

    @Override
    public Observable<String> deleteByIdsAsync(Collection<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Observable.empty();
        }
        Collection<Observable<String>> observables = new ArrayList<>();
        for (String id : ids) {
            final String resourceGroupName = ResourceUtilsCore.groupFromResourceId(id);
            final String name = ResourceUtilsCore.nameFromResourceId(id);
            Observable<String> o = RXMapper.map(this.inner().deleteAsync(resourceGroupName, name), id);
            observables.add(o);
        }
        return Observable.mergeDelayError(observables);
    }

    @Override
    public Observable<String> deleteByIdsAsync(String...ids) {
        return this.deleteByIdsAsync(new ArrayList<String>(Arrays.asList(ids)));
    }

    @Override
    public void deleteByIds(Collection<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            this.deleteByIdsAsync(ids).toBlocking().last();
        }
    }

    @Override
    public void deleteByIds(String...ids) {
        this.deleteByIds(new ArrayList<String>(Arrays.asList(ids)));
    }

    @Override
    public RolloutImpl define(String name) {
        return wrapModel(name);
    }

    @Override
    public Observable<Rollout> cancelAsync(String resourceGroupName, String rolloutName) {
        RolloutsInner client = this.inner();
        return client.cancelAsync(resourceGroupName, rolloutName)
        .map(new Func1<RolloutInner, Rollout>() {
            @Override
            public Rollout call(RolloutInner inner) {
                return new RolloutImpl(inner.name(), inner, manager());
            }
        });
    }

    @Override
    public Observable<Rollout> restartAsync(String resourceGroupName, String rolloutName) {
        RolloutsInner client = this.inner();
        return client.restartAsync(resourceGroupName, rolloutName)
        .map(new Func1<RolloutInner, Rollout>() {
            @Override
            public Rollout call(RolloutInner inner) {
                return new RolloutImpl(inner.name(), inner, manager());
            }
        });
    }

    @Override
    protected RolloutImpl wrapModel(RolloutInner inner) {
        return  new RolloutImpl(inner.name(), inner, manager());
    }

    @Override
    protected RolloutImpl wrapModel(String name) {
        return new RolloutImpl(name, new RolloutInner(), this.manager());
    }

}
