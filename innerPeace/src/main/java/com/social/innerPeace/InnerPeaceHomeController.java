package com.social.innerPeace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class InnerPeaceHomeController {

    @GetMapping({"/",""})
    public String index(){
        log.info("call main");
        return "redirect:board/post/list";
    }
}
