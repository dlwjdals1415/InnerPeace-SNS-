package com.social.innerPeace;

import com.social.innerPeace.board.comment.service.BoardCommentService;
import com.social.innerPeace.dto.CommentDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class InnerPeaceApplicationTests {
	@Autowired
	private BoardCommentService boardCommentService;
	@Test
	void contextLoads() {

	}

	@Test
	void commentInsertTest(){
		CommentDTO commentDTO  = CommentDTO.builder().post_no(1L).healerEmail("a@a")
						.comment_content("댓글 테스트")
								.build();

		boardCommentService.save(commentDTO);
		List<CommentDTO> commentDTOList = boardCommentService.findAll(commentDTO.getPost_no());
log.info(commentDTOList.toString());
	}

}
