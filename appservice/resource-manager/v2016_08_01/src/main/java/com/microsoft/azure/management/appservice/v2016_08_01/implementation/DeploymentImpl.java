/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.appservice.v2016_08_01.implementation;

import com.microsoft.azure.management.appservice.v2016_08_01.Deployment;
import com.microsoft.azure.arm.model.implementation.CreatableUpdatableImpl;
import rx.Observable;
import org.joda.time.DateTime;

class DeploymentImpl extends CreatableUpdatableImpl<Deployment, DeploymentInner, DeploymentImpl> implements Deployment, Deployment.Definition, Deployment.Update {
    private final AppServiceManager manager;
    private String resourceGroupName;
    private String name;
    private String id;

    DeploymentImpl(String name, AppServiceManager manager) {
        super(name, new DeploymentInner());
        this.manager = manager;
        // Set resource name
        this.id = name;
        //
    }

    DeploymentImpl(DeploymentInner inner, AppServiceManager manager) {
        super(inner.name(), inner);
        this.manager = manager;
        // Set resource name
        this.id = inner.name();
        // resource ancestor names
        this.resourceGroupName = IdParsingUtils.getValueFromIdByName(inner.id(), "resourceGroups");
        this.name = IdParsingUtils.getValueFromIdByName(inner.id(), "sites");
        this.id = IdParsingUtils.getValueFromIdByName(inner.id(), "deployments");
        //
    }

    @Override
    public AppServiceManager manager() {
        return this.manager;
    }

    @Override
    public Observable<Deployment> createResourceAsync() {
        WebAppsInner client = this.manager().inner().webApps();
        return client.createDeploymentAsync(this.resourceGroupName, this.name, this.id, this.inner())
            .map(innerToFluentMap(this));
    }

    @Override
    public Observable<Deployment> updateResourceAsync() {
        WebAppsInner client = this.manager().inner().webApps();
        return client.createDeploymentAsync(this.resourceGroupName, this.name, this.id, this.inner())
            .map(innerToFluentMap(this));
    }

    @Override
    protected Observable<DeploymentInner> getInnerAsync() {
        WebAppsInner client = this.manager().inner().webApps();
        return client.getDeploymentAsync(this.resourceGroupName, this.name, this.id);
    }

    @Override
    public boolean isInCreateMode() {
        return this.inner().id() == null;
    }


    @Override
    public Boolean active() {
        return this.inner().active();
    }

    @Override
    public String author() {
        return this.inner().author();
    }

    @Override
    public String authorEmail() {
        return this.inner().authorEmail();
    }

    @Override
    public String deployer() {
        return this.inner().deployer();
    }

    @Override
    public String deploymentId() {
        return this.inner().deploymentId();
    }

    @Override
    public String details() {
        return this.inner().details();
    }

    @Override
    public DateTime endTime() {
        return this.inner().endTime();
    }

    @Override
    public String id() {
        return this.inner().id();
    }

    @Override
    public String kind() {
        return this.inner().kind();
    }

    @Override
    public String message() {
        return this.inner().message();
    }

    @Override
    public String name() {
        return this.inner().name();
    }

    @Override
    public DateTime startTime() {
        return this.inner().startTime();
    }

    @Override
    public Integer status() {
        return this.inner().status();
    }

    @Override
    public String type() {
        return this.inner().type();
    }

    @Override
    public DeploymentImpl withExistingSite(String resourceGroupName, String name) {
        this.resourceGroupName = resourceGroupName;
        this.name = name;
        return this;
    }

    @Override
    public DeploymentImpl withActive(Boolean active) {
        this.inner().withActive(active);
        return this;
    }

    @Override
    public DeploymentImpl withAuthor(String author) {
        this.inner().withAuthor(author);
        return this;
    }

    @Override
    public DeploymentImpl withAuthorEmail(String authorEmail) {
        this.inner().withAuthorEmail(authorEmail);
        return this;
    }

    @Override
    public DeploymentImpl withDeployer(String deployer) {
        this.inner().withDeployer(deployer);
        return this;
    }

    @Override
    public DeploymentImpl withDeploymentId(String deploymentId) {
        this.inner().withDeploymentId(deploymentId);
        return this;
    }

    @Override
    public DeploymentImpl withDetails(String details) {
        this.inner().withDetails(details);
        return this;
    }

    @Override
    public DeploymentImpl withEndTime(DateTime endTime) {
        this.inner().withEndTime(endTime);
        return this;
    }

    @Override
    public DeploymentImpl withKind(String kind) {
        this.inner().withKind(kind);
        return this;
    }

    @Override
    public DeploymentImpl withMessage(String message) {
        this.inner().withMessage(message);
        return this;
    }

    @Override
    public DeploymentImpl withStartTime(DateTime startTime) {
        this.inner().withStartTime(startTime);
        return this;
    }

    @Override
    public DeploymentImpl withStatus(Integer status) {
        this.inner().withStatus(status);
        return this;
    }

}
