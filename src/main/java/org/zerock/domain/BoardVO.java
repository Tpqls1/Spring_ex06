package org.zerock.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BoardVO {
	private Integer bno;	// 객체
	private String title;
	private String content;
	private String writer;
	private Date regDate;		// 테이블의 컬럼이름과 동일하게
	private Date updateDate;
	
	private int replyCnt;		// 댓글의 수(댓글이 추가, 댓글이 삭제)
	
	// 첨부파일 목록을 추가
	private List<BoardAttachVO> attachList;
}
