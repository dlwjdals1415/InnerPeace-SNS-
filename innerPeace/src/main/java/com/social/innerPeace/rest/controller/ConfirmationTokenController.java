package com.social.innerPeace.rest.controller;

import com.social.innerPeace.rest.service.ConfirmationTokenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ConfirmationTokenController {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @GetMapping("confirm-email")
    public String viewConfirmEmail(@RequestParam(name = "token") String token, RedirectAttributes attributes){
        confirmationTokenService.confirmEmail(token);
        attributes.addFlashAttribute("msg", "이메일 인증이 완료되었습니다.");
        return "redirect:/user/account/signin";
    }

    @GetMapping("modify-password")
    public String viewModifyPassword(@RequestParam(name = "token") String token, Model model){
        model.addAttribute("token", token);
        return "modifypassword";
    }

    @GetMapping("delete-account")
    public String deleteAccount(@RequestParam(name = "token") String token, HttpSession  session){
        confirmationTokenService.deleteAccount(token);
        session.invalidate();;
        return "deleteaccount";
    }

}
