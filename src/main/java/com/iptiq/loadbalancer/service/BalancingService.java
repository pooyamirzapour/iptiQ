package com.iptiq.loadbalancer.service;

import com.iptiq.loadbalancer.component.LoadBalancerTypeEnum;
import com.iptiq.provider.Provider;

public class BalancingService {
    private final BalancerStrategyService balancerStrategyService;

    public BalancingService(BalancerStrategyService balancerStrategyService) {
         this.balancerStrategyService = balancerStrategyService;
    }

    public Provider get(LoadBalancerTypeEnum loadBalancerTypeEnum) {
        return balancerStrategyService.getService(loadBalancerTypeEnum).getProvider();
    }

}
