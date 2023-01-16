package com.iptiq.provider;

import java.util.UUID;

public class Provider {

    private String instance;
    private ProviderStatusEnum status;

    public Provider(String instance, ProviderStatusEnum status) {
        this.instance = instance;
        this.status = status;
    }

    public Provider() {
        this.instance = UUID.randomUUID().toString();
    }

    public Provider(String instance) {
        this.instance = instance;
    }

    public String get() {
        return this.instance;
    }

    //TODO: dummy logic
    public boolean healthCheck() {
        return true;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public ProviderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ProviderStatusEnum status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "instance='" + instance + '\'' +
                ", status=" + status +
                '}';
    }
}
