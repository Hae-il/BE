package com.haeil.full.chatbot.context;

public class ClientContextHolder {

    private static final ThreadLocal<Long> CLIENT_ID_HOLDER = new ThreadLocal<>();

    private ClientContextHolder() {}

    public static void setClientId(Long clientId) {
        CLIENT_ID_HOLDER.set(clientId);
    }

    public static Long getClientId() {
        return CLIENT_ID_HOLDER.get();
    }

    public static void clear() {
        CLIENT_ID_HOLDER.remove();
    }
}
