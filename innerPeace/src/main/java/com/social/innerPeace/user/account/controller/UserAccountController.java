package com.social.innerPeace.user.account.controller;

import com.social.innerPeace.dto.SignupDTO;
import com.social.innerPeace.repository.HealerRepository;
import com.social.innerPeace.user.account.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/user/account")
public class UserAccountController {
    @Autowired
    UserAccountService userAccountService;

    @GetMapping("/signup")
    public String signup(){
        log.info("signup");
        return "signup";
    }

    @PostMapping("/signup")
    public String register(SignupDTO dto, RedirectAttributes redirectAttributes){
        log.info("register dto:" + dto);
        if(dto.getEmail().equals(userAccountService.findByEmail(dto.getEmail()))) {
            redirectAttributes.addFlashAttribute("msg", "이미 가입한 회원입니다.");
            return "redirect:/signup";
        }else {
            String email = userAccountService.register(dto);
            if (email != null && email.isEmpty() == false) {
                redirectAttributes.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
            }
        }
        return "redirect:/singin";
    }

    @GetMapping("/signin")
    public String login(){
        log.info("login");
        return "signin";
    }

    @PostMapping("/signin")
    public String doLogin(SignupDTO dto, RedirectAttributes redirectAttributes, HttpServletRequest request){
        log.info("doLogin dto:" + dto);
        SignupDTO memberDTO = userAccountService.login(dto);
        if(memberDTO==null){
            redirectAttributes.addFlashAttribute("msg","로그인을 하지 못했습니다.");
            return "redirect:/signin";
        }

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("member", memberDTO);
        return "redirect:/list";
    }
}
