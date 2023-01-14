package com.iptiq.loadbalancer.service;

import com.iptiq.provider.ProviderStatusEnum;
import com.iptiq.loadbalancer.queue.CircularQueue;
import com.iptiq.provider.Provider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RandomServiceTest {

    @Test
    void test_get_provider_random() {
        RandomService randomService = new RandomService(getCircularQueue());
        Provider provider = randomService.getProvider();
        Assertions.assertNotNull(provider);
    }

    private CircularQueue getCircularQueue() {
        CircularQueue circularQueue = new CircularQueue();
        for (int i = 0; i < 10; i++) {
            circularQueue.add(new Provider("Instance" + i, ProviderStatusEnum.INCLUDED));
        }
        return circularQueue;
    }

}