package com.iptiq.loadbalancer.queue;

import com.iptiq.loadbalancer.exception.ErrorCode;
import com.iptiq.loadbalancer.exception.IPTIQServiceException;
import com.iptiq.provider.Provider;
import com.iptiq.provider.ProviderStatusEnum;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * A custom circular queue. It creates a LinkedList and implements basic methods.
 */
public class CircularQueue {

    private final LinkedList<Provider> queue = new LinkedList<>();

    public CircularQueue() {
    }

    /**
     * Add a provider to circular queue.
     * @param provider Provider
     */
    public void add(Provider provider) {
        if (isReplaced(provider)) {
            return;
        }
        int size = 10;
        if (queue.size() > size) {
            throw new IPTIQServiceException(ErrorCode.QUEUE_IS_FULL);
        }
        queue.add(provider);
    }

    private boolean isReplaced(Provider provider) {
        if (queue.stream().anyMatch(f -> f.getInstance().equals(provider.getInstance()))) {
            queue.stream()
                    .filter(f -> f.getInstance().equals(provider.getInstance()))
                    .findFirst()
                    .ifPresent(f -> f.setStatus(provider.getStatus()));
            return true;
        }
        return false;
    }

    /**
     * Peeks a provider from circular queue.
     * @return provider
     */
    public Provider peek() {
        Provider provider;
        if (queue.size() == 0) {
            throw new IPTIQServiceException(ErrorCode.QUEUE_IS_EMPTY);
        }
        provider = queue.poll();
        add(provider);
        return provider;
    }

    public Queue<Provider> getQueue() {
        return queue;
    }

    /**
     * Get providers based on the status.
     * @param providerStatusEnum Status
     * @return a queue.
     */
    public Queue<Provider> getProviders(ProviderStatusEnum providerStatusEnum) {
        return queue.stream()
                .filter(f -> f.getStatus().equals(providerStatusEnum))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Provider getProviderByIndex(int index) {
        return queue.get(index);
    }

    public int size() {
        return queue.size();
    }

    public void addAll(List<Provider> providers) {
        for (Provider provider : providers) {
            add(provider);
        }
    }

    public void remove(Provider provider) {
        queue.remove(provider);
    }

    public void exclude(Provider provider) {
        if (queue.stream().anyMatch(f -> f.getInstance().equals(provider.getInstance()))) {
            queue.stream()
                    .filter(f -> f.getInstance().equals(provider.getInstance()))
                    .findFirst()
                    .ifPresent(f -> f.setStatus(ProviderStatusEnum.EXCLUDED));
        }
    }
}