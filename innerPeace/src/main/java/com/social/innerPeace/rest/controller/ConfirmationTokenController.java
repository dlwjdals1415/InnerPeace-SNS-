package com.social.innerPeace.rest.controller;

import com.social.innerPeace.rest.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ConfirmationTokenController {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @GetMapping("confirm-email")
    public String viewConfirmEmail(@RequestParam(name = "token") String token){
        confirmationTokenService.confirmEmail(token);
        return "redirect:/user/account/signin";
    }
}
