package com.example.workmanagement.utils.services.store;

import com.example.workmanagement.utils.dto.MessageDTO;

import java.util.ArrayList;
import java.util.List;

public class MessageStorage {

    private static MessageStorage messageStorage;

    private List<BoardMessages> boardMessages = new ArrayList<>();

    public static MessageStorage getInstance() {
        if (messageStorage == null)
            messageStorage = new MessageStorage();
        return messageStorage;
    }

    public List<BoardMessages> getBoardMessages() {
        return boardMessages;
    }

    public void setBoardMessages(List<BoardMessages> boardMessages) {
        this.boardMessages = boardMessages;
    }

    public void addMessage(MessageDTO message) {
        boardMessages.stream().filter(b -> b.getId() == message.getBoardId())
                .findFirst().get()
                .getMessages().add(message);
    }

    private MessageStorage() {}

}
