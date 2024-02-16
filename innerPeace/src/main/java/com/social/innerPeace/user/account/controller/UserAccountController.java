package com.social.innerPeace.user.account.controller;

import com.social.innerPeace.ip_enum.Role;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.rest.service.ConfirmationTokenService;
import com.social.innerPeace.user.account.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/user/account")
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @GetMapping("/signup")
    public String signup(){
        log.info("signup");
        return "signup";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<Object> register(HealerDTO dto){
        log.info("register dto:" + dto);
        String email = userAccountService.register(dto, Role.ROLE_USER);
        if(email.equals("duplicated")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입한 이력이있는 이메일입니다.");
        }else if (email != null && email.isEmpty() == false) {
            confirmationTokenService.createEmailConfirmationToken(email);
            return ResponseEntity.status(HttpStatus.OK).body(email);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입을 하지못했습니다.");
        }
    }

    @GetMapping("/signin")
    public String login(){
        log.info("login");
        return "signin";
    }
    @GetMapping("/loginError")
    public String loginError(String msg, RedirectAttributes attributes){
        log.info("loginError : {}", msg);
        attributes.addFlashAttribute("msg", msg);
        return "redirect:/login";
    }

    @PostMapping("/profile")
    public String profile(HealerDTO dto){
        log.info("profile nickname : {}", dto.getHealer_nickname());
        return "profile";
    }

    @PostMapping("/myinfo")
    public String myinfo(HealerDTO dto){
        log.info("profile nickname : {}", dto.getHealer_nickname());
        return "myinfo";
    }

    @PostMapping("/profile/img")
    public String profileImg(HealerDTO dto){
        log.info("profile nickname : {}", dto.getHealer_nickname());
        return "profile";
    }

}
