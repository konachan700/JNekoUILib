package com.jneko.jnekouilib.utils;

public interface MessageBusAction {
    public void OnMessage(MessageBusActions messageID, Object ... messagePayload);
}
