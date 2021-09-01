/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.backend.repositories;

import com.chatapp.backend.models.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author hinas
 */

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
    public List<Message> findBySenderEqualsAndRecipientEqualsOrRecipientEqualsAndSenderEqualsOrderByCreatedAt(Long sender, Long recipient, Long sender1, Long recipient1);
	
}
