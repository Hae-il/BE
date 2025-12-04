package com.haeil.be.chatbot.dto.request;

public class ClientContext {

    private final Long clientId;

    public ClientContext(Long clientId) {
        this.clientId = clientId;
    }

    public Long getClientId() {
        return clientId;
    }
}
