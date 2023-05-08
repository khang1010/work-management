package com.example.workmanagement.utils.services.store;

import com.example.workmanagement.utils.dto.MessageDTO;

import java.util.ArrayList;
import java.util.List;

public class BoardMessages {

    private long id;

    private String name;

    private List<MessageDTO> messages;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public MessageDTO getLatestMessage() {
        return messages.get(messages.size() - 1);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BoardMessages(long id, String name) {
        this.id = id;
        this.name = name;
        this.messages = new ArrayList<>();
    }
}
