package com.social.innerPeace.user.follow.controller;

import com.social.innerPeace.dto.FollowDTO;
import com.social.innerPeace.user.follow.service.UserFollowService;
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

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserFollowController {
    @Autowired
    UserFollowService userFollowService;

    @GetMapping("/{nickName}/following")
    public String following(Model model, @PathVariable(name = "nickName") String nickName, HttpSession session) {
        log.info("follow call");
        String loginedMember = (String) session.getAttribute("loginedMember");
        List<FollowDTO> dtoList = userFollowService.findFollowing(nickName,loginedMember);

        model.addAttribute("dtoList", dtoList);
        return "following";
    }

    @GetMapping("/{nickName}/follower")
    public String follower(Model model, @PathVariable(name = "nickName") String nickName, HttpSession session) {
        log.info("follower call");
        String loginedMember = (String) session.getAttribute("loginedMember");
        List<FollowDTO> dtoList = userFollowService.findFollower(nickName,loginedMember);
        model.addAttribute("dtoList", dtoList);
        return "follower";
    }

    @PostMapping("/{nickName}/following/scroll")
    @ResponseBody
    public ResponseEntity<Object> followingScroll(@PathVariable(name = "nickName") String nickName,@ModelAttribute("follow_no") long follow_no, HttpSession session) {
        log.info("follower scroll call follow_no : {}", follow_no);
        String loginedMember = (String) session.getAttribute("loginedMember");
        List<FollowDTO> dtoList = userFollowService.findFollowingScroll(nickName,loginedMember,follow_no);

        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @PostMapping("/{nickName}/follower/scroll")
    @ResponseBody
    public ResponseEntity<Object> followerScroll(@PathVariable(name = "nickName") String nickName,@ModelAttribute("follow_no") long follow_no, HttpSession session) {
        log.info("follower scroll call follow_no : {}", follow_no);
        String loginedMember = (String) session.getAttribute("loginedMember");
        List<FollowDTO> dtoList = userFollowService.findFollowerScroll(nickName,loginedMember,follow_no);

        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @PostMapping("/follow")
    @ResponseBody
    public ResponseEntity<Object> follow(@RequestParam(name = "nickName") String follower, HttpSession session) {
        log.info("follow call follow_no : {}", follower);
        String nickName = (String) session.getAttribute("loginedMember");
        String follow = userFollowService.follow(follower, nickName);
        if(follow != null){
            return ResponseEntity.status(HttpStatus.OK).body(follow);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("팔로우 실패");
        }
    }
}
