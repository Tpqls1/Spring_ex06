package org.zerock.service;

import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;

public interface ReplyService {
	// CRUD(4개), 목록보기 -> 5개
	// 댓글 추가
	public int register(ReplyVO vo);
	// 댓글 읽기
	public ReplyVO get(Integer rno);
	// 댓글 수정
	public int modify(ReplyVO vo);
	// 댓글 삭제
	public int remove(Integer rno);
	// 댓글 목록
	public ReplyPageDTO getList(Criteria cri, Integer bno);
}
