/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapp.backend.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.chatapp.backend.javaMail.SendMail;
import com.chatapp.backend.models.User;
import com.chatapp.backend.services.UserServices;
import com.chatapp.backend.utils.JWTUtil;
import java.io.IOException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hinas
 */
@RestController
public class AuthController {

    @Autowired
    UserServices userService;

    @Autowired
    JWTUtil JWTUtil;

    @Autowired
    SendMail sendMail;

    @PostMapping(value = "/login")
    public HashMap<String, Object> loginUser(@RequestBody User user) throws Exception {

        HashMap<String, Object> response = new HashMap<>();

        String email = user.getEmail();
        String pass = user.getPass();

        User tempUser = userService.getUserByEmail(email);
        if (tempUser != null) {

            // check password
            BCrypt.Result result = BCrypt.verifyer().verify(pass.toCharArray(), tempUser.getPass());

            if (result.verified) {
                //Generating JWT
                String jwt = JWTUtil.generateToken(email);

                response.put("jwt", jwt);

                response.put("message", "Login Successfully");
                response.put("response", "Success");

            } else {
                System.out.println("Incorrect pass");
                response.put("message", "Incorrect password");
                response.put("response", "Error");
            }
        } else {
            System.out.println("User not exists");
            response.put("message", "User not exists");
            response.put("response", "Error");
        }
        return response;
    }

    @PostMapping(value = "/register")
    public HashMap<String, Object> registerUser(@RequestBody User user) throws Exception {

        HashMap response = new HashMap<>();
        String email = user.getEmail();
        String password = user.getPass();
        String name = user.getName();
        
        System.out.println("*********"+user.getForgotPasswordToken());

        if (!userService.UserExist(user.getEmail())) {

            String hashedPass = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            user.setPass(hashedPass);

            String token = getAlphaNumericString();
            user.setEmailVerificationToken(token);

            userService.saveUser(user);

            String messageBody = "Please click this link to verify email :  http://localhost:9000/verifyEmail?username=" + email + "&token=" + token;

            boolean mailSentStatus = sendMail.sendEmail(email, "Email Verification", messageBody);

            if (mailSentStatus) {
                response.put("response", "success");
                response.put("message", "Mail verification link sent to your email.");

            } else {
                response.put("response", "Error");
                response.put("message", "Error in sending mail");

            }

        } else {
            response.put("reponse", "Error");
            response.put("message", "User already exist !! Try with some other email! ");
        }

        return response;
    }

    @PostMapping(value = "/forgotPass")
    public HashMap<String, Object> forgotPass(@RequestBody User user) {
        HashMap<String, Object> response = new HashMap<>();
        String name = user.getName();
        String email = user.getEmail();
        System.out.println(name);

        User tempUser = userService.getUserByEmail(email);
        if (tempUser != null) {

            String token = getAlphaNumericString();
            tempUser.setForgotPasswordToken(token);
            userService.saveUser(tempUser);

            String messageBody = "Please click this link to verify email :  http://localhost:9000/resetPass?username=" + email + "&token=" + token;

            boolean mailSentStatus = sendMail.sendEmail(email, "Reset Password", messageBody);

            if (mailSentStatus) {
                response.put("response", "success");
                response.put("message", "Check Your mail to reset your password.");

            } else {
                response.put("response", "Error");
                response.put("message", "Error in sending mail");

            }

        } else {
            response.put("response", "Error");
            response.put("message", "Email Id not registered");
        }

        return response;
    }

    @PostMapping("/resetPass")
    public HashMap<String, Object> resetPass(@RequestParam String username, @RequestParam String token, @RequestParam String password) throws IOException {

        HashMap<String, Object> response = new HashMap<>();
        User tempUser = userService.getUserByEmail(username);
        if (tempUser != null) {

            if (tempUser.getForgotPasswordToken().equals(token)) {
                // hashing password before saving it to database
                String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                tempUser.setPass(password);
                // saving user
                userService.saveUser(tempUser);
                response.put("status", "changed");
                response.put("message", "Successfully changed password");

            } else {
                response.put("status", "Error");
                response.put("message", "Something went wrong");

            }

        } else {
            response.put("response", "Error");
            response.put("message", "Email Id not registered");
        }

        return response;
    }

// function to generate a random string of length 10 
    private String getAlphaNumericString() {

        int n = 10;
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb 
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    @GetMapping(value = "/verifyEmail")
    private String verifyEmail(@RequestParam String username, @RequestParam String token) throws Exception {

        User tempUser = userService.getUserByEmail(username);

        if (tempUser != null) {
            if (tempUser.getEmailVerificationToken().equals(token)) {
                tempUser.setEmailVerified(true);
                userService.saveUser(tempUser);
                return "<h2>Email verified successfully</h2>";
            } else {
                return "<h2>Email not verified</h2>";
            }
        } else {
            return "<h2>User not exists !!</h2>";
        }
    }

}
