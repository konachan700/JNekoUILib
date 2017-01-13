package com.jneko.jnekouilib.utils;

public interface MessageBusAction {
    public void OnMessage(String msg, Object messagePayload);
}
