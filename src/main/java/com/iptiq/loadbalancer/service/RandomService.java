package com.iptiq.loadbalancer.service;

import com.iptiq.loadbalancer.queue.CircularQueue;
import com.iptiq.provider.Provider;
import com.iptiq.provider.ProviderStatusEnum;

import java.util.Queue;
import java.util.Random;

/**
 * Random implementation algorithm.
 */
public class RandomService implements LoadBalancingTypeService {

    private final CircularQueue circularQueue;

    public RandomService(CircularQueue circularProviderQueue) {
        this.circularQueue = circularProviderQueue;
    }

    @Override
    public Provider getProvider() {
        Queue<Provider> providers = circularQueue.getProviders(ProviderStatusEnum.INCLUDED);
        Random generator = new Random();
        int index = generator.nextInt(providers.size());
        return circularQueue.getProviderByIndex(index);
    }

}
