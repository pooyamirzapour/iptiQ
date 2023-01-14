package com.iptiq.loadbalancer.queue;

import com.iptiq.loadbalancer.exception.IPTIQServiceException;
import com.iptiq.provider.ProviderStatusEnum;
import com.iptiq.provider.Provider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CircularQueueTest {

    CircularQueue circularQueue;

    @BeforeEach
    void setup() {
        circularQueue = new CircularQueue();
    }

    @Test
    void test_queue_is_full() {
        for (int i = 0; i < 11; i++) {
            circularQueue.add(new Provider("Instance" + i, ProviderStatusEnum.INCLUDED));
        }

        Assertions.assertThrows(IPTIQServiceException.class,
                () -> circularQueue.add(new Provider("Extra Instance", ProviderStatusEnum.INCLUDED)));
    }

    @Test
    void test_queue_is_empty() {
        Assertions.assertThrows(IPTIQServiceException.class, circularQueue::peek);
    }

    @Test
    void test_queue_get_provider() {
        for (int i = 0; i < 10; i++) {
            circularQueue.add(new Provider("Instance" + i, ProviderStatusEnum.INCLUDED));
        }

        Assertions.assertEquals(circularQueue.size(), circularQueue.getProviders(ProviderStatusEnum.INCLUDED).size());
        Assertions.assertEquals(0, circularQueue.getProviders(ProviderStatusEnum.EXCLUDED).size());
    }

    @Test
    void test_peek_is_working() {
        for (int i = 0; i < 10; i++) {
            circularQueue.add(new Provider("Instance" + i, ProviderStatusEnum.INCLUDED));
        }
        Assertions.assertEquals("Instance0", circularQueue.peek().getInstance());
        Assertions.assertEquals("Instance1", circularQueue.peek().getInstance());
        Assertions.assertEquals("Instance2", circularQueue.peek().getInstance());
    }

    @Test
    void test_add_is_working() {
        circularQueue.add(new Provider("Instance1", ProviderStatusEnum.INCLUDED));
        circularQueue.add(new Provider("Instance1", ProviderStatusEnum.EXCLUDED));

        Assertions.assertEquals(ProviderStatusEnum.EXCLUDED, circularQueue.peek().getStatus());
    }
}