/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.backend.controllers;

/**
 *
 * @author hinas
 */
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class View {
	
	@RequestMapping("/resetPassword")
	String resetPassword(@RequestParam String username, @RequestParam String token, Map<String, Object> model) {		
		model.put("username", username);
		model.put("token", token);
		return "resetPassword";
	}
}
