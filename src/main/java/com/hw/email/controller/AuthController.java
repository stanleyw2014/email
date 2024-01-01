package com.hw.email.controller;


import com.hw.email.auth.JwtUtil;
import com.hw.email.dao.EmailRepository;
import com.hw.email.entity.User;
import com.hw.email.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final EmailRepository emailRepository;

    @Autowired
    public AuthController(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User userInStore = emailRepository.getUser(user.getUserId());
        if (null != userInStore) {
            if (user.getUserId().equals(userInStore.getUserId()) && user.getPassword().equals(userInStore.getPassword())) {
                String token = JwtUtil.createToken(user);
                return token;
            } else {
                throw new UnauthorizedException("Invalid user");
            }
        } else {
            throw new UnauthorizedException("Invalid user");
        }
    }
}
