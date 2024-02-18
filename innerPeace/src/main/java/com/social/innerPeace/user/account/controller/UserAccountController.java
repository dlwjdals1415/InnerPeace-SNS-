package com.social.innerPeace.user.account.controller;

import com.social.innerPeace.entity.ConfirmationToken;
import com.social.innerPeace.ip_enum.Role;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.rest.service.ConfirmationTokenService;
import com.social.innerPeace.user.account.service.UserAccountService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.NamedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
@RequestMapping("/user/account")
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @GetMapping("/signup")
    public String signup() {
        log.info("signup");
        return "signup";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<Object> register(HealerDTO dto) {
        log.info("register dto:" + dto);
        String emailRegex = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(dto.getHealer_birth(), formatter);
        LocalDate currentDateMinus14Years = LocalDate.now().minusYears(14);
        if(!dto.getHealer_email().matches(emailRegex)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 형식이 올바르지 않습니다.");
        }
        if (dto.getHealer_pw().length() <= 12 || !dto.getHealer_pw().matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 형식이 올바르지 않습니다. 비밀번호는 12자 이상, 영문, 숫자, 특수문자를 모두 포함해야 합니다.");
        }
        if (!dto.getHealer_phone().matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("휴대폰번호 형식이 올바르지 않습니다.");
        }
        if(birthDate.isAfter(currentDateMinus14Years)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("만 14세 이상만 가입이 가능합니다.");
        }
        String email = userAccountService.register(dto, Role.ROLE_USER);
        if (email.equals("duplicated")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입한 이력이있는 이메일입니다.");
        } else if (email != null && email.isEmpty() == false) {
            confirmationTokenService.createEmailConfirmationToken(email);
            return ResponseEntity.status(HttpStatus.OK).body(email);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입을 하지못했습니다.");
        }
    }

    @GetMapping("/signin")
    public String login() {
        log.info("login");
        return "signin";
    }

    @GetMapping("/loginError")
    public String loginError(@RequestParam(name = "msg") String msg, RedirectAttributes attributes) {
        log.info("loginError : {}", msg);
        attributes.addFlashAttribute("msg", msg);
        return "redirect:/user/account/signin";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        log.info("profile nickname : {}", loginedHealer);
        HealerDTO dto = userAccountService.findHealerProfile(loginedHealer);
        model.addAttribute("dto", dto);
        return "profile";
    }

    @PostMapping("/profile/modify")
    public String profileModify(HealerDTO healerDTO, HttpSession session) {
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        if (loginedHealer != null) {
            HealerDTO dto = userAccountService.modifyProfile(loginedHealer, healerDTO);
            session.setAttribute("loginedHealer", dto.getHealer_nickname());
        }
        return "redirect:/user/account/profile";
    }

    @PostMapping("/myinfo/modify")
    public String myinfo(HealerDTO dto) {
        log.info("profile nickname : {}", dto.getHealer_nickname());

        return "myinfo";
    }

    @PostMapping("/profile/img")
    public String profileImg(HealerDTO dto, HttpSession session) {
        log.info("profile nickname : {}", dto.getHealer_nickname());
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        HealerDTO healerDTO = userAccountService.modifyProfileImage(loginedHealer, dto);
        return "redirect:/user/account/profile";
    }

    @GetMapping("/myinfo")
    public String myinfo() {
        return "myinfo";
    }

    @PostMapping("/pwmodify")
    @ResponseBody
    public ResponseEntity<Object> pwModify(HttpSession session){
        log.info("pwModify");
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        if (loginedHealer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인이 필요합니다.");
        }
        String email = userAccountService.findEmail(loginedHealer);
        String token = confirmationTokenService.createEmailModifyPasswordToken(email);
        if(token != null && !token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 작업을 수행하지못하였습니다.");
        }
    }

    @PostMapping("/password-modify")
    @ResponseBody
    public ResponseEntity<Object> passwordmodify(HealerDTO dto,@RequestParam(name = "token") String token) throws BadRequestException {
        log.info("passwordmodify");
        if (dto.getHealer_pw().length() <= 12 || !dto.getHealer_pw().matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 형식이 올바르지 않습니다. 비밀번호는 12자 이상, 영문, 숫자, 특수문자를 모두 포함해야 합니다.");
        }
        ConfirmationToken tokenEntity = confirmationTokenService.findByIdAndExpirationDateAfterAndExpired(token);
        if (tokenEntity != null && !tokenEntity.isExpired()){
            String email = tokenEntity.getEmail();
            if (email != null && !email.isEmpty()) {
                if (userAccountService.modifyPassword(token,email, dto.getHealer_pw())) {
                    return ResponseEntity.status(HttpStatus.OK).body("ok");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경 작업을 수행하지못하였습니다.");

    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Object> delete(HttpSession session,HealerDTO dto){
        log.info("delete");
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        if (loginedHealer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인이 필요합니다.");
        }
        String email = userAccountService.delete(loginedHealer,dto);
        if (email != null && !email.isEmpty()) {
            if (email.equals("비밀번호")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
            }
            String token = confirmationTokenService.createEmailDeleteAccountToken(email);
            if(token != null && !token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("ok");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원탈퇴작업을 실패하였습니다.");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원탈퇴작업을 실패하였습니다.");
    }

}
