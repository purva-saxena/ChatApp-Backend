/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.backend.services;

import com.chatapp.backend.models.Message;
import com.chatapp.backend.repositories.MessageRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hinas
 */
@Service
public class MessageService {
    
    @Autowired
    MessageRepository messageRepository;
    
    
    public void saveMessage(Message message){
        messageRepository.save(message);
    }
    
    public List<Message> getMessages(Long sender, Long recipient){
        return messageRepository.findBySenderEqualsAndRecipientEqualsOrRecipientEqualsAndSenderEqualsOrderByCreatedAt(sender, recipient, sender, recipient);
    }
    
}
