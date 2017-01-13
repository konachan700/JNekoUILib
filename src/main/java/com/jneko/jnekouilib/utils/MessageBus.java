package com.jneko.jnekouilib.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MessageBus {
    private static final Map<String, Set<MessageBusAction>>
            messages = new HashMap<>();
    
    private static String getMessage(String a, String b) {
        return a + "_" + b;
    } 
    
    public static final void registerMessageReceiver(String messageReceiverName, String messageID, MessageBusAction action) {
        final String msg = getMessage(messageReceiverName, messageID);
        if (!isMessageReceiverExist(msg))
            messages.put(msg, new HashSet<>());
        
        messages.get(msg).add(action);
    }
    
    public static final void unregisterMessageReceiver(String messageReceiverName, String messageID, MessageBusAction action) {
        final String msg = getMessage(messageReceiverName, messageID);
        if (!isMessageReceiverExist(msg))
            return;
        messages.get(msg).remove(action);
    }
    
    public static final void unregisterAllMessageReceivers(String messageReceiverName, String messageID) {
        final String msg = getMessage(messageReceiverName, messageID);
        if (!isMessageReceiverExist(msg))
            return;
        messages.remove(msg);
    }
    
    private static boolean isMessageReceiverExist(String msg) {
        return messages.containsKey(msg);
    }   
    
    public static final void sendMessage(String messageReceiverName, String messageID, Object messagePayload) {
        final String msg = getMessage(messageReceiverName, messageID);
        if (!isMessageReceiverExist(msg))
            return;
        messages.get(msg).forEach(msgX -> {
            msgX.OnMessage(msg, messagePayload); 
        });
    }
    
    public static final void sendMessage(String messageReceiverName, String messageID) {
        sendMessage(messageReceiverName, messageID, null);
    }
}
