package com.social.innerPeace.board.comment.controller;

import com.social.innerPeace.board.comment.service.BoardCommentService;
import com.social.innerPeace.dto.CommentDTO;
import com.social.innerPeace.dto.CommentListDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/comment")
public class BoardCommentController {
    private final BoardCommentService boardCommentService;

    private String getLoginedHealer(HttpSession session) {
        return (String) session.getAttribute("loginedHealer");
    }

    private String generateHtmlContent(CommentDTO comment, String healer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String htmlContent = "<section class=\"w100 pad10 row\">"
                + "<article class=\"w5\">"
                + "<img class=\"w30p hauto\" src=\"" + comment.getHealer_profile_image() +"\" alt=\"프로필\">"
                + "</article>"
                + "<article class=\"w80 hauto ml20 mr20 col\">"
                + "<span>"
                + "<a href=\"/user/userpage/" + comment.getNickName() + "\">"
                + "<span class=\"text_hover\">" + comment.getNickName() + "</span>"
                + "</a>"
                + "<span>"
                + "  " + comment.getComment_content()
                + "</span>"
                + "</span>"
                + "<article class=\"mt10 font_s15 row_cb\">"
                + "<span class=\"regday\">" + comment.getComment_regday().format(formatter) + "</span>"
                + "</article>"
                + "</article>"
                + "<article class=\"w10 hauto\">";
        if (healer != null) {
            htmlContent += "<input class=\"bor_none bg_e0 text_hover font_s20\" type=\"button\" value=\"···\" "
                    + "onclick=\"comment_menu('" + comment.getComment_no() + "','" + comment.getComment_content().replace("'", "\\'") + "','" + comment.getNickName() + "')\">";
        }

        htmlContent += "</article>" + "</section>";
        return htmlContent;
    }

    @PostMapping("/write")
    public ResponseEntity<Object> save(@RequestBody CommentDTO commentDTO, HttpSession session) {
        String healer = getLoginedHealer(session);
        if (healer == null) {
            return null;
        }
        commentDTO.setNickName(healer);
        Long saveResult = boardCommentService.write(commentDTO);
        if (saveResult != null) {
            return ResponseEntity.ok(saveResult);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/modify")
    public ResponseEntity<Object> modify(@RequestBody CommentDTO commentDTO, HttpSession session) {
        String healer = getLoginedHealer(session);
        if (healer == null) {
            return new ResponseEntity<>("로그인한 사용자가 아닙니다", HttpStatus.BAD_REQUEST);
        }
        commentDTO.setNickName(healer);
        Long saveResult = boardCommentService.modify(commentDTO);
        if (saveResult == 1L) {
            return ResponseEntity.ok(saveResult);
        } else if (saveResult == 0L) {
            return new ResponseEntity<>("작성자가 일치하지않습니다.", HttpStatus.BAD_GATEWAY);
        } else {
            return new ResponseEntity<>("해당 댓글을 찾을수 없습니다", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Object> delete(@RequestBody CommentDTO commentDTO, HttpSession session) {
        String healer = getLoginedHealer(session);
        if (healer == null) {
            return new ResponseEntity<>("로그인한 사용자가 아닙니다", HttpStatus.BAD_REQUEST);
        }
        commentDTO.setNickName(healer);
        Long saveResult = boardCommentService.delete(commentDTO);
        if (saveResult == 1L) {
            return ResponseEntity.ok(saveResult);
        } else if (saveResult == 0L) {
            return new ResponseEntity<>("작성자가 일치하지않습니다.", HttpStatus.BAD_GATEWAY);
        } else {
            return new ResponseEntity<>("해당 댓글을 찾을수 없습니다", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/scroll")
    public ResponseEntity<Object> scroll(@RequestParam("post_no") Long post_no, @RequestParam("comment_no") Long comment_no, HttpSession session) {
        String healer = getLoginedHealer(session);
        List<CommentDTO> commentList = boardCommentService.scroll(post_no, comment_no);
        List<CommentListDTO> dtoList = new ArrayList<>();
        for (CommentDTO comment: commentList) {
            String htmlContent = generateHtmlContent(comment, healer);
            long comment_n = comment.getComment_no();
            CommentListDTO commentListDTO = CommentListDTO.builder()
                    .comment_no(comment_n)
                    .commentHtml(htmlContent)
                    .build();
            dtoList.add(commentListDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }
}

