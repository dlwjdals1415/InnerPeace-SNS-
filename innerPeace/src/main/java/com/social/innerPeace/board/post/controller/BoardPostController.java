package com.social.innerPeace.board.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardPostController {
    @GetMapping("board/post/list")
    public String post(){
        return "postlist";
    }

}
