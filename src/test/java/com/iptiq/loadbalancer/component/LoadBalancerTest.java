package com.iptiq.loadbalancer.component;

import com.iptiq.loadbalancer.exception.IPTIQServiceException;
import com.iptiq.loadbalancer.queue.CircularQueue;
import com.iptiq.provider.Provider;
import com.iptiq.provider.ProviderStatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class LoadBalancerTest {

    private CircularQueue circularQueue;
    private LoadBalancer loadBalancer;

    @BeforeEach
    void setup() {
        circularQueue = new CircularQueue();
        loadBalancer = new LoadBalancer(circularQueue);
    }

    @Test
    void test_register_when_all_instances_are_unique() {
        List<Provider> providers = getProviders();
        loadBalancer.register(providers);
        Assertions.assertEquals(providers.size(), circularQueue.size());
    }

    @Test
    void test_register_when_all_instances_are_same() {
        List<Provider> providers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            providers.add(new Provider("Instance", ProviderStatusEnum.INCLUDED));
        }

        loadBalancer.register(providers);
        Assertions.assertEquals(1, circularQueue.size());
    }

    @Test
    void test_get_random_provider_is_working() {
        List<Provider> providers = getProviders();
        loadBalancer.register(providers);
        String randomProvider = loadBalancer.get(LoadBalancerTypeEnum.RANDOM);
        Assertions.assertNotNull(randomProvider);
    }

    @Test
    void test_get_roundRobin_provider_is_working() {
        List<Provider> providers = getProviders();
        loadBalancer.register(providers);
        String randomProvider = loadBalancer.get(LoadBalancerTypeEnum.ROUND_ROBIN);
        Assertions.assertEquals("Instance0", randomProvider);
    }

    @Test
    void test_include_is_working() {
        List<Provider> providers = new ArrayList<>();
        providers.add(new Provider("Instance0", ProviderStatusEnum.INCLUDED));
        loadBalancer.register(providers);
        loadBalancer.exclude("Instance0");
        loadBalancer.include("Instance0");
        String randomProvider = loadBalancer.get(LoadBalancerTypeEnum.ROUND_ROBIN);

        Assertions.assertEquals("Instance0", randomProvider);
    }

    @Test
    void test_exclude_is_working() {
        List<Provider> providers = new ArrayList<>();
        providers.add(new Provider("Instance0", ProviderStatusEnum.INCLUDED));
        loadBalancer.register(providers);
        loadBalancer.exclude("Instance0");

        Assertions.assertThrows(IPTIQServiceException.class, () -> loadBalancer.get(LoadBalancerTypeEnum.ROUND_ROBIN));
    }

    private List<Provider> getProviders() {
        List<Provider> providers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            providers.add(new Provider("Instance" + i, ProviderStatusEnum.INCLUDED));
        }
        return providers;
    }

}