package com.nexus.core.service;

import com.nexus.core.model.Message;
import com.nexus.core.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    // Main features
    public void saveMessage(Message msg) {
        chatRepository.saveMessage(msg);
    }

    public List<Message> getHistory(String requester, String other, boolean isGroup) {
        if (isGroup)
            return chatRepository.getGroupHistory(other, requester);
        else
            return chatRepository.getDirectHistory(requester, other);
    }

    // Deletions

    // Clear chat
    public boolean clearChat(String requester, String target, boolean isGroup) {
        if (isGroup)
            chatRepository.clearGroupChat(requester, target);
        else
            chatRepository.clearDirectChat(requester, target);
        return true;
    }

    // Delete for everyone
    public boolean deleteMessageForEveryone(String sender, String otherUser, String timestamp, boolean isGroup) {
        return chatRepository.deleteMessageForEveryone(sender, otherUser, timestamp, isGroup);
    }

    // Group chat features

    public boolean createGroup(String groupName, String creator) {
        return chatRepository.createGroup(groupName, creator);
    }

    public boolean addMemberToGroup(String groupName, String newMember) {
        return chatRepository.addMemberToGroup(groupName, newMember);
    }

    public boolean isGroupMember(String groupName, String user) {
        return chatRepository.isGroupMember(groupName, user);
    }
}