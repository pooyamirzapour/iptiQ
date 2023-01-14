package com.iptiq.loadbalancer.service;

import com.iptiq.loadbalancer.component.LoadBalancerTypeEnum;
import com.iptiq.loadbalancer.queue.CircularQueue;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Chooses right algorithm implementation based on input.
 */
public class BalancerStrategyService {
    private final ConcurrentHashMap<LoadBalancerTypeEnum, LoadBalancingTypeService> map;

    public BalancerStrategyService(ConcurrentHashMap<LoadBalancerTypeEnum, LoadBalancingTypeService> map,
            CircularQueue circularQueue) {
        this.map = map;
        map.put(LoadBalancerTypeEnum.RANDOM, new RandomService(circularQueue));
        map.put(LoadBalancerTypeEnum.ROUND_ROBIN, new RoundRobinService(circularQueue));
    }

    public LoadBalancingTypeService getService(LoadBalancerTypeEnum loadBalancerTypeEnum) {
        return map.get(loadBalancerTypeEnum);
    }

}
