package com.social.innerPeace.user.userPage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HealerUserPageController {

    @GetMapping("user/userpage")
    public String userpage(){
        log.info("userpage call");
        return "userpage";
    }

}
