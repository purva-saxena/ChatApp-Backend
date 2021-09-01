/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.backend.services;

import com.chatapp.backend.models.User;
import com.chatapp.backend.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hinas
 */
@Service
public class UserServices {

    @Autowired
    private UserRepository userRepo;

    public List<User> GetAllUser() {
        List<User> allUser = new ArrayList<>();
        allUser = userRepo.findAll();

        return allUser;
    }

    public User getUserById(long id) {

        return userRepo.getById(id);
    }

    /*
    public User getByName(String name){
        return userRepo.findByUsername(name);
    }*/

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public boolean UserExist(String email) {

        return userRepo.findByemail(email) != null;
    }

    public User getUserByEmail(String email) {
        return userRepo.findByemail(email);
    }

    public List<User> searchUserOfSimilarName(String name) {
        
        return userRepo.findByNameLike("%" + name + "%");
    
    }
}
