package com.social.innerPeace.user.account.controller;

import com.social.innerPeace.dto.SignupDTO;
import com.social.innerPeace.repository.HealerRepository;
import com.social.innerPeace.user.account.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class UserAccountController {
    @Autowired
    UserAccountService userAccountService;

    @GetMapping("/signup")
    public String signup(){
        return "redirect:/signup";
    }

    @PostMapping("/signup")
    public String register(SignupDTO dto, RedirectAttributes redirectAttributes){
        log.info("register dto:" + dto);
        if(dto.getEmail().equals(userAccountService.findByEmail(dto.getEmail()))) {

        }else {
            String email = userAccountService.register(dto);
            if (email != null && email.isEmpty() == false) {
                redirectAttributes.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
            }
        }
        return "redirect:/login";
    }
}
