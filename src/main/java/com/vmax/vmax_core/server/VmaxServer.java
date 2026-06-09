package com.vmax.vmax_core.server;

import com.vmax.vmax_core.api_helper.ApiHelper;

/**
 * Abstract class for a Vmax server.
 * Implementing classes should override the {@link #activate() <code>activate()</code>} and {@link #deactivate() <code>deactivate()</code>} methods.
 */
public abstract class VmaxServer {
    
    protected final ApiHelper apiHelper;

    public VmaxServer(ApiHelper apiHelper) {
        this.apiHelper = apiHelper;
    }

    /**
     * Starts the server.
     * Override this method in implementing classes. 
     * @throws Exception if the server could not be started
     */
    public abstract void activate() throws Exception;
    
    /**
     * Stops the server.
     * Override this method in implementing classes. 
     */
    public abstract void deactivate();

}
