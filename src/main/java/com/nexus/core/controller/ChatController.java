package com.nexus.core.controller;

import com.nexus.core.model.Message;
import com.nexus.core.service.ChatService;
import com.nexus.core.service.UserService;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    private String getCurrentTime() {
        return DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss").withZone(ZoneId.systemDefault()).format(Instant.now());
    }

    // Helper method to ask Spring Security who is holding the token
    private String getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Direct Messaging

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody Message msg) throws IOException {
        // Security check (ensure they aren't pretending to be someone else)
        if (!getAuthenticatedUser().equals(msg.getSender()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only send messages as yourself!");

        if (userService.findUser(msg.getRecipient()).isEmpty())
            return ResponseEntity.badRequest().body("Recipient not found");
        msg.setTimestamp(getCurrentTime());
        msg.setGroupMessage(false);
        chatService.saveMessage(msg);
        return ResponseEntity.ok("Message sent");
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam String requester, @RequestParam String otherUser) throws IOException {
        // Authorize Requester
        if (!getAuthenticatedUser().equals(requester))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only view your own history!");

        // Fetch History
        List<Message> history = chatService.getHistory(requester, otherUser, false);
        return ResponseEntity.ok(history);
    }

    // Group Messaging

    @PostMapping("/group/create")
    public ResponseEntity<String> createGroup(@RequestParam String groupName, @RequestParam String creator) throws IOException {
        if (!getAuthenticatedUser().equals(creator))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");

        boolean created = chatService.createGroup(groupName, creator);
        if (created)
            return ResponseEntity.ok("Group " + groupName + " created.");
        else
            return ResponseEntity.badRequest().body("Group already exists.");
    }

    @PostMapping("/group/join")
    public ResponseEntity<String> joinGroup(@RequestParam String groupName, @RequestParam String username) throws IOException {
        if (!getAuthenticatedUser().equals(username))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");

        boolean added = chatService.addMemberToGroup(groupName, username);
        if (added)
            return ResponseEntity.ok("Joined group " + groupName);
        else
            return ResponseEntity.badRequest().body("Failed to join. Group might not exist or user already in.");
    }

    @PostMapping("/group/send")
    public ResponseEntity<String> sendGroupMessage(@RequestBody Message msg) throws IOException {
        if (!getAuthenticatedUser().equals(msg.getSender()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        if (!chatService.isGroupMember(msg.getRecipient(), msg.getSender())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not a member of this group");
        }
        msg.setTimestamp(getCurrentTime());
        msg.setGroupMessage(true);
        chatService.saveMessage(msg);
        return ResponseEntity.ok("Group message sent");
    }

    @GetMapping("/group/history")
    public ResponseEntity<?> getGroupHistory(@RequestParam String groupName, @RequestParam String requester) throws IOException {
        if (!getAuthenticatedUser().equals(requester))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        if (!chatService.isGroupMember(groupName, requester))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a member of this group.");

        List<Message> history = chatService.getHistory(requester, groupName, true);
        return ResponseEntity.ok(history);
    }

    // Deletions

    @DeleteMapping("/clear-chat")
    public ResponseEntity<String> clearChat(@RequestParam String requester, @RequestParam String otherUser, @RequestParam(defaultValue = "false") boolean isGroup, UserDetails authenticatedPrincipal) throws IOException {
        if (!getAuthenticatedUser().equals(requester))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");

        // if clearing group chat then check whether user is part of group
        if (isGroup && !chatService.isGroupMember(otherUser, requester))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not a member of this group.");

        boolean done = chatService.clearChat(requester, otherUser, isGroup);
        if (done)
            return ResponseEntity.ok("Chat cleared for " + requester);
        else
            return ResponseEntity.badRequest().body("Chat not found");
    }

    @DeleteMapping("/delete-message")
    public ResponseEntity<String> deleteMessageForEveryone(@RequestParam String sender, @RequestParam String recipient, @RequestParam String timestamp, @RequestParam(defaultValue = "false") boolean isGroup) throws IOException {
        if (!getAuthenticatedUser().equals(sender))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");

        boolean deleted = chatService.deleteMessageForEveryone(sender, recipient, timestamp, isGroup);
        if (deleted)
            return ResponseEntity.ok("Message deleted for everyone");
        else
            return ResponseEntity.badRequest().body("Message not found or you are not the sender");
    }
}
