/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.backend.controllers;

import com.chatapp.backend.models.User;
import com.chatapp.backend.services.UserServices;
import com.chatapp.backend.utils.JWTUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author hinas
 */
@Controller
public class UserController {

    @Autowired
    UserServices UserServices;

    @Autowired
    JWTUtil JWTUtil;

    @GetMapping(value = "/get-users")
    public HashMap<String, Object> getUsers() throws Exception {
        HashMap<String, Object> response = new HashMap<>();

        List<User> users = UserServices.GetAllUser();

        if (users != null && users.size() != 0) {

            response.put("response", "success");
            response.put("user", users);

        } else {
            response.put("message", "No user exists");
            response.put("response", "error");

        }

        return response;
    }

    @PostMapping(value = "/search-user")
    public HashMap<String, Object> searchUser(@RequestBody Map<String, String> request) throws Exception {
        HashMap<String, Object> response = new HashMap<>();

        String query = request.get("query");

        List<User> users = UserServices.searchUserOfSimilarName(query);

        if (users != null && users.size() > 0) {
            response.put("response", "Success");
            response.put("users", users);
        } else {
            response.put("response", "Error");
            response.put("message", "No user found");
        }
        return response;
    }
}
