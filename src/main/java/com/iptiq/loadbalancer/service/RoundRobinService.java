package com.iptiq.loadbalancer.service;

import com.iptiq.loadbalancer.exception.IPTIQServiceException;
import com.iptiq.loadbalancer.queue.CircularQueue;
import com.iptiq.provider.Provider;
import com.iptiq.provider.ProviderStatusEnum;

import java.util.concurrent.locks.ReentrantLock;

import static com.iptiq.loadbalancer.exception.ErrorCode.PROVIDER_NOT_FOUND;

/**
 * Round-robin implementation algorithm.
 */
public class RoundRobinService implements LoadBalancingTypeService {

    private final CircularQueue circularQueue;

    ReentrantLock lock = new ReentrantLock();

    public RoundRobinService(CircularQueue circularQueue) {
        this.circularQueue = circularQueue;
    }

    /**
     * Peeks from circular queue, if the status is included, returns the provider otherwise go for the next.
     *
     * @return provider
     */
    @Override
    public Provider getProvider() {
        lock.lock();
        try {
            for (int i = 0; i < circularQueue.size(); i++) {
                Provider provider = circularQueue.peek();
                if (provider.getStatus().equals(ProviderStatusEnum.INCLUDED)) {
                    return provider;
                }
            }
            throw new IPTIQServiceException(PROVIDER_NOT_FOUND);
        } finally {
            lock.unlock();
        }
    }
}
