package com.social.innerPeace.user.find.controller;

import com.social.innerPeace.dto.MemberDTO;
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

@Slf4j
@Controller
@RequestMapping("/user")
public class UserFIndController {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @GetMapping("/find")
    public String find(){
        log.info("find");
        return "find";
    }

    @PostMapping("/find/pwForm")
    @ResponseBody
    public ResponseEntity<Object> findPwForm(MemberDTO memberDTO){
        log.info("findPwForm");
        String email = userAccountService.compareByEmail(memberDTO.getEmail()).getEmail();
        if (email == null){
            return ResponseEntity.status(400).body("가입된 이메일이 아닙니다.");
        }else {
            String token = confirmationTokenService.createEmailModifyPasswordToken(email);
            if(token != null && !token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("ok");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 찾기 작업에 실패하였습니다.");
            }
        }
    }

}

