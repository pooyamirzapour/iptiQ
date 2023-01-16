package com.iptiq.loadbalancer.queue;

import com.iptiq.loadbalancer.component.LoadBalancerTypeEnum;
import com.iptiq.loadbalancer.exception.IPTIQServiceException;
import com.iptiq.provider.ProviderStatusEnum;
import com.iptiq.provider.Provider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CircularQueueTest {

    CircularQueue circularQueue;

    @BeforeEach
    void setup() {
        circularQueue = new CircularQueue();
    }

    @Test
    void test_queue_is_full() {
        List<Provider> providers=new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            providers.add(new Provider("Instance" + i, ProviderStatusEnum.INCLUDED));
        }
        Assertions.assertThrows(IPTIQServiceException.class,
                () -> circularQueue.addAll(providers));
    }

    @Test
    void test_queue_is_empty() {
        Assertions.assertThrows(IPTIQServiceException.class, circularQueue::peek);
    }

    @Test
    void test_queue_get_provider() {
        List<Provider> providers=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            providers.add(new Provider("Instance" + i, ProviderStatusEnum.INCLUDED));
        }
        circularQueue.addAll(providers);
        Assertions.assertEquals(circularQueue.size(), circularQueue.getProviders(ProviderStatusEnum.INCLUDED).size());
        Assertions.assertEquals(0, circularQueue.getProviders(ProviderStatusEnum.EXCLUDED).size());
    }

    @Test
    void test_peek_is_working() {
        List<Provider> providers=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            providers.add(new Provider("Instance" + i, ProviderStatusEnum.INCLUDED));
        }
        circularQueue.addAll(providers);
        Assertions.assertEquals("Instance0", circularQueue.peek().getInstance());
        Assertions.assertEquals("Instance1", circularQueue.peek().getInstance());
        Assertions.assertEquals("Instance2", circularQueue.peek().getInstance());
    }


    @Test
    void test_validate_provider_for_initialize()
    {
        List<Provider> providers = new ArrayList<>();
        providers.add(new Provider("Instance0", ProviderStatusEnum.EXCLUDED));
        providers.add(new Provider("Instance1", ProviderStatusEnum.EXCLUDED));
       Assertions.assertThrows(IPTIQServiceException.class,()-> circularQueue.validate(providers));
    }


}