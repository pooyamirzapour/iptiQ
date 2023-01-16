package com.iptiq.loadbalancer.service;

import com.iptiq.provider.ProviderStatusEnum;
import com.iptiq.loadbalancer.queue.CircularQueue;
import com.iptiq.provider.Provider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class RoundRobinServiceTest {
    @Test
    void test_get_provider_random() {
        RoundRobinService randomService = new RoundRobinService(getCircularQueue());
        Provider provider = randomService.getProvider();
        Assertions.assertEquals("Instance0", provider.getInstance());
        provider = randomService.getProvider();
        Assertions.assertEquals("Instance1", provider.getInstance());
    }

    private CircularQueue getCircularQueue() {
        CircularQueue circularQueue = new CircularQueue();
        List<Provider> providers=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            providers.add(new Provider("Instance" + i, ProviderStatusEnum.INCLUDED));
        }
        circularQueue.addAll(providers);
        return circularQueue;
    }
}