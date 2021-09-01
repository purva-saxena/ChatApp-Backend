/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.backend.controllers;

import com.chatapp.backend.models.Message;
import com.chatapp.backend.services.MessageService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author hinas
 */
@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping(value = "/sendMessage")
    public HashMap<String, Object> sendMessage(@RequestParam Message message) {
        HashMap<String, Object> response = new HashMap<>();
        messageService.saveMessage(message);

        response.put("response", "success");
        response.put("message", "message_saved");

        return response;
    }

    public HashMap<String, Object> getMessage(@RequestBody Map<String, String> request) throws Exception {
        HashMap<String, Object> response = new HashMap<>();

        Long sender = Long.parseLong(request.get("sender"));

        Long receiver = Long.parseLong(request.get("receiver"));

        List<Message> messages = messageService.getMessages(sender, receiver);

        if (messages != null && messages.size() > 0) {
            response.put("response", "Success");
            response.put("messages", messages);
        } else {
            response.put("response", "Error");
            response.put("message", "No messages found");
        }

        return response;
    }
}
