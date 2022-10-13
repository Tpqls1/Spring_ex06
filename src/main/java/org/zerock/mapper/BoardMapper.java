package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {
	// 게시글 목록을 가져오기 : SQL문을 메소드에 같이 추가하는 방법
	// 최신에 작성한 게시글을 먼저 출력이 되도록 : order by bno desc
	// 반환형 : List<BoardVO>
	// 매개변수 : 업음 -> 페이징, 검색기능 때문에 추가 필요
	// XML Mapper를 사용할 경우는 SQL문을 주석 처리해 준다. -> 두가지를 같이 적용할 수 없음
	// @Select("select * from tbl_board where bno > 0 order by bno desc")
	public List<BoardVO> getList();	// 모든 게시글 목록을 가져오는 메소드
	public List<BoardVO> getListWithPaging(Criteria cri);	// 해당 페이지의 게시글 목록 가져오기
	public int getTotalCount(Criteria cri);	// 검색 조건에 따른 게시글
	// 게시글 쓰기
	// 1. bno를 몰라도 되는 경우
	public void insert(BoardVO board);
	// 2. bno 값을 알아서 다음 동작으로 사용해야 되는 경우 -> 첨부 파일 정보를 저장하는 경우
	public void insertSelectKey(BoardVO board);
	// 게시글 상세 보기 : 설계 인터페이스 규격
	public BoardVO read(Integer bno);
	// 게시글 수정
	// 매개변수 : BoardVO (수정된 게시글 정보)
	// 반환형 : int (1: 성공, 0:실패)
	public int update(BoardVO board);
	// 게시글 삭제
	// 매개변수 : 삭제할 게시글 아이디
	// 반환형 : 삭제한 게시글의 수
	public int delete(Integer bno);
	// 댓글이 추가될 때는 amount=1, 댓글이 삭제될 때는 amount=-1
	public void updateReplyCnt(@Param("bno") Integer bno, @Param("amount") int amount);
}
