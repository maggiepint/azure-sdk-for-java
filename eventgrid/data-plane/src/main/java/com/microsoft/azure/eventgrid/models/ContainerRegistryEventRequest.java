/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.eventgrid.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The request that generated the event.
 */
public class ContainerRegistryEventRequest {
    /**
     * The ID of the request that initiated the event.
     */
    @JsonProperty(value = "id")
    private String id;

    /**
     * The IP or hostname and possibly port of the client connection that
     * initiated the event. This is the RemoteAddr from the standard http
     * request.
     */
    @JsonProperty(value = "addr")
    private String addr;

    /**
     * The externally accessible hostname of the registry instance, as
     * specified by the http host header on incoming requests.
     */
    @JsonProperty(value = "host")
    private String host;

    /**
     * The request method that generated the event.
     */
    @JsonProperty(value = "method")
    private String method;

    /**
     * The user agent header of the request.
     */
    @JsonProperty(value = "useragent")
    private String useragent;

    /**
     * Get the ID of the request that initiated the event.
     *
     * @return the id value
     */
    public String id() {
        return this.id;
    }

    /**
     * Set the ID of the request that initiated the event.
     *
     * @param id the id value to set
     * @return the ContainerRegistryEventRequest object itself.
     */
    public ContainerRegistryEventRequest withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get the IP or hostname and possibly port of the client connection that initiated the event. This is the RemoteAddr from the standard http request.
     *
     * @return the addr value
     */
    public String addr() {
        return this.addr;
    }

    /**
     * Set the IP or hostname and possibly port of the client connection that initiated the event. This is the RemoteAddr from the standard http request.
     *
     * @param addr the addr value to set
     * @return the ContainerRegistryEventRequest object itself.
     */
    public ContainerRegistryEventRequest withAddr(String addr) {
        this.addr = addr;
        return this;
    }

    /**
     * Get the externally accessible hostname of the registry instance, as specified by the http host header on incoming requests.
     *
     * @return the host value
     */
    public String host() {
        return this.host;
    }

    /**
     * Set the externally accessible hostname of the registry instance, as specified by the http host header on incoming requests.
     *
     * @param host the host value to set
     * @return the ContainerRegistryEventRequest object itself.
     */
    public ContainerRegistryEventRequest withHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Get the request method that generated the event.
     *
     * @return the method value
     */
    public String method() {
        return this.method;
    }

    /**
     * Set the request method that generated the event.
     *
     * @param method the method value to set
     * @return the ContainerRegistryEventRequest object itself.
     */
    public ContainerRegistryEventRequest withMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * Get the user agent header of the request.
     *
     * @return the useragent value
     */
    public String useragent() {
        return this.useragent;
    }

    /**
     * Set the user agent header of the request.
     *
     * @param useragent the useragent value to set
     * @return the ContainerRegistryEventRequest object itself.
     */
    public ContainerRegistryEventRequest withUseragent(String useragent) {
        this.useragent = useragent;
        return this;
    }

}
