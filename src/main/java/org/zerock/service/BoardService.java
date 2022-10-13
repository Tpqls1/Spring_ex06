package org.zerock.service;

import java.util.List;

import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardService {
	// 게시글 목록 보기
//	public List<BoardVO> getList();
	public List<BoardVO> getList(Criteria cri);	// 페이징 처리
	public int getTotalCount(Criteria cri);
	// 게시글 추가
	// Business Layer 용어 : register
	// persistence layer 용어가 차이가 있다. : insert
	public void register(BoardVO board);
	// 게시글 상세 보기
	public BoardVO get(Integer bno);
	// 게시글 수정
	public boolean modify(BoardVO board);
	// 게시글 삭제
	public boolean remove(Integer bno);
	// 첨부파일목록을 가져오는 메소드 추가
	public List<BoardAttachVO> getAttachList(Integer bno);
}
