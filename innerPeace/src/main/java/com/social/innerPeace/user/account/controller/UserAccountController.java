package com.social.innerPeace.user.account.controller;

import com.social.innerPeace.ip_enum.Role;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.user.account.service.UserAccountService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String register(HealerDTO dto, RedirectAttributes redirectAttributes){
        log.info("register dto:" + dto);
        String email = userAccountService.register(dto, Role.ROLE_USER);
        if(email.equals("duplicated")){
            redirectAttributes.addFlashAttribute("msg", "이미 가입한 회원입니다.");
            log.info("이미 가입한 회원입니다.");
            return "redirect:/user/account/signup";
        }else if (email != null && email.isEmpty() == false) {
            redirectAttributes.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
            return "redirect:/user/account/signin";
        }else{
            redirectAttributes.addFlashAttribute("msg", "이미 가입한 회원입니다.");
            log.info("회원가입을 하지못했습니다.");
            return "redirect:/user/account/signup";
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

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session){
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        log.info("profile nickname : {}", loginedHealer);
        HealerDTO dto = userAccountService.findHealerProfile(loginedHealer);
        model.addAttribute("dto", dto);
        return "profile";
    }

    @PostMapping("/profile/modify")
    public String profileModify(HealerDTO healerDTO, HttpSession session){
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        if (loginedHealer != null) {
            HealerDTO dto = userAccountService.modifyProfile(loginedHealer, healerDTO);
            session.setAttribute("loginedHealer", dto.getHealer_nickname());
        }
        return "redirect:/user/account/profile";
    }

    @PostMapping("/profile/img")
    public String profileImg(HealerDTO dto, HttpSession session){
        log.info("profile nickname : {}", dto.getHealer_nickname());
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        HealerDTO healerDTO = userAccountService.modifyProfileImage(loginedHealer, dto);
        return "redirect:/user/account/profile";
    }

    @GetMapping("/myinfo")
    public String myinfo(Model model, HttpSession session){
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        log.info("profile nickname : {}", loginedHealer);
        HealerDTO dto = userAccountService.findHealerInfo(loginedHealer);
        model.addAttribute("dto", dto);
        return "myinfo";
    }

    @PostMapping("/myinfo/modify")
    public String myinfo(HealerDTO healerDTO, HttpSession session){
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        if (loginedHealer != null) {
            HealerDTO dto = userAccountService.modifyMyinfo(loginedHealer, healerDTO);
            session.setAttribute("loginedHealer", dto.getHealer_nickname());
        }
        return "redirect:/user/account/myinfo";
    }

}
