package com.iptiq.provider;

import com.iptiq.loadbalancer.queue.CircularQueue;

public class ProviderService {

    private final CircularQueue circularQueue;

    public ProviderService(CircularQueue circularQueue) {
        this.circularQueue = circularQueue;
    }

    public void include(String instance) {
        circularQueue.add(new Provider(instance, ProviderStatusEnum.INCLUDED));
    }

    public void exclude(String instance) {
        circularQueue.exclude(new Provider(instance, ProviderStatusEnum.EXCLUDED));
    }

    public void healthCheck() {
        circularQueue.getQueue().forEach(provider -> {
            if (!provider.healthCheck() && provider.getStatus().equals(ProviderStatusEnum.INCLUDED)) {
                provider.setStatus(ProviderStatusEnum.EXCLUDED);
            } else if (provider.getStatus().equals(ProviderStatusEnum.EXCLUDED) && provider.healthCheck()
                    && provider.healthCheck()) {
                provider.setStatus(ProviderStatusEnum.INCLUDED);
            }
        });
    }

}
