package org.zerock.domain;

import java.util.Date;

import lombok.Data;

// 댓글을 저장하는 빈

@Data
public class ReplyVO {
	private int rno;
	private int bno;
	private String reply;
	private String replyer;
	private Date replyDate;
	private Date updateDate;
}
