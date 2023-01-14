package com.iptiq.loadbalancer.component;

import com.iptiq.loadbalancer.exception.IPTIQServiceException;
import com.iptiq.loadbalancer.queue.CircularQueue;
import com.iptiq.loadbalancer.service.BalancerStrategyService;
import com.iptiq.loadbalancer.service.BalancingService;
import com.iptiq.loadbalancer.service.LoadBalancingTypeService;
import com.iptiq.loadbalancer.service.RandomService;
import com.iptiq.loadbalancer.service.RoundRobinService;
import com.iptiq.loadbalancer.task.HeartBeatChecker;
import com.iptiq.provider.Provider;
import com.iptiq.provider.ProviderService;
import com.iptiq.provider.ProviderStatusEnum;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import static com.iptiq.loadbalancer.exception.ErrorCode.LOAD_BALANCER_IS_OUT_OF_CAPACITY;

/**
 * Load Balancer class is a component responsible for request's distribution among providers.
 */
public class LoadBalancer {

    public static final int TASK_INTERVAL = 10000;
    public static final int TASK_DELAY = 0;
    private final CircularQueue circularQueue;
    private final BalancingService  balancingService;
    private final ProviderService providerService;

    private Semaphore semaphore;

    private static final Integer REQUEST_CAPACITY = 10;

    public LoadBalancer(CircularQueue circularQueue) {
        this.circularQueue = circularQueue;

        ConcurrentHashMap<LoadBalancerTypeEnum, LoadBalancingTypeService>
                concurrentHashMap = new ConcurrentHashMap<>();

        BalancerStrategyService balancerStrategyService =
                new BalancerStrategyService(concurrentHashMap, circularQueue);

        balancingService = new BalancingService(balancerStrategyService);

        this.providerService = new ProviderService(circularQueue);


    }

    private void restrictInputRequest(CircularQueue circularQueue) {
        semaphore = new Semaphore((int) circularQueue.getQueue()
                .stream()
                .filter(f -> f.getStatus().equals(ProviderStatusEnum.INCLUDED))
                .count() * REQUEST_CAPACITY);
    }

    /**
     * It takes a list of providers and register them.
     *
     * @param instances - Instances
     */
    public void register(List<Provider> instances) {
        circularQueue.addAll(instances);
        scheduleTask();
        restrictInputRequest(circularQueue);
    }

    private void scheduleTask() {
        Timer scheduler = new Timer();
        scheduler.scheduleAtFixedRate(new HeartBeatChecker(providerService), TASK_DELAY, TASK_INTERVAL);
    }

    /**
     * Based on input type of service goes for picking a provider.
     *
     * @param loadBalancerTypeEnum - Type
     * @return String instanceId
     */
    public String get(LoadBalancerTypeEnum loadBalancerTypeEnum) {
        try {
            System.out.println(semaphore.availablePermits());
            semaphore.acquire();
            return balancingService.get(loadBalancerTypeEnum).get();
        } catch (InterruptedException ex) {
            throw new IPTIQServiceException(LOAD_BALANCER_IS_OUT_OF_CAPACITY);
        } finally {
            semaphore.release();
        }
    }

    /**
     * include a provider into load balancer.
     *
     * @param providerId - Provider ID
     */
    public void include(String providerId) {
        providerService.include(providerId);
    }

    public void exclude(String providerId) {
        providerService.exclude(providerId);
    }

}
