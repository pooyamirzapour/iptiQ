package com.iptiq.loadbalancer.task;

import com.iptiq.provider.ProviderService;

import java.util.TimerTask;

/**
 * A heart beat checker, runs with specified interval.
 */
public class HeartBeatChecker extends TimerTask {

    private final ProviderService providerService;

    public HeartBeatChecker(ProviderService providerService) {
        this.providerService = providerService;
    }

    @Override
    public void run() {
        providerService.healthCheck();
    }
}
