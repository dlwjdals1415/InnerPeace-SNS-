package com.social.innerPeace.user.follow.controller;

import com.social.innerPeace.dto.CommentDTO;
import com.social.innerPeace.dto.FollowDTO;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.repository.FollowRepository;
import com.social.innerPeace.user.follow.service.HealerFollowService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class HealerFollowController {
    @Autowired
    HealerFollowService healerFollowService;

    @GetMapping("/{healer_nickname}/following")
    public String following(Model model, @PathVariable(name = "healer_nickname") String healer_nickname, HttpSession session) {
        log.info("follow call");
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        List<FollowDTO> dtoList = healerFollowService.findFollowing(healer_nickname,loginedHealer);

        model.addAttribute("dtoList", dtoList);
        return "following";
    }

    @GetMapping("/{healer_nickname}/follower")
    public String follower(Model model, @PathVariable(name = "healer_nickname") String healer_nickname, HttpSession session) {
        log.info("follower call");
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        List<FollowDTO> dtoList = healerFollowService.findFollower(healer_nickname,loginedHealer);
        model.addAttribute("dtoList", dtoList);
        return "follower";
    }

    @PostMapping("/{healer_nickname}/following/scroll")
    @ResponseBody
    public ResponseEntity<Object> followingScroll(@PathVariable(name = "healer_nickname") String healer_nickname,@ModelAttribute("follow_no") long follow_no, HttpSession session) {
        log.info("follower scroll call follow_no : {}", follow_no);
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        List<FollowDTO> dtoList = healerFollowService.findFollowingScroll(healer_nickname,loginedHealer,follow_no);

        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @PostMapping("/{healer_nickname}/follower/scroll")
    @ResponseBody
    public ResponseEntity<Object> followerScroll(@PathVariable(name = "healer_nickname") String healer_nickname,@ModelAttribute("follow_no") long follow_no, HttpSession session) {
        log.info("follower scroll call follow_no : {}", follow_no);
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        List<FollowDTO> dtoList = healerFollowService.findFollowerScroll(healer_nickname,loginedHealer,follow_no);

        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @PostMapping("/follow")
    @ResponseBody
    public ResponseEntity<Object> follow(@RequestParam(name = "healer_nickname") String follower, HttpSession session) {
        log.info("follow call follow_no : {}", follower);
        String healer_nickname = (String) session.getAttribute("loginedHealer");
        String follow = healerFollowService.follow(follower, healer_nickname);
        if(follow != null){
            return ResponseEntity.status(HttpStatus.OK).body(follow);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("팔로우 실패");
        }
    }
}
