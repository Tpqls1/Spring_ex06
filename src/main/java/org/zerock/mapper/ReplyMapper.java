package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {
	public int insert(ReplyVO vo);		// 댓글 추가
	public ReplyVO read(Integer rno);	// 댓글 조회
	public int delete(Integer rno);		// 댓글 삭제
	public int update(ReplyVO vo);		// 댓글 수정 -> 수정 가능한 것 : reply
	// MyBatis 메소드의 매개변수로 파라미터를 한 개만 전달할 수 있다. (제약사항)
	// 해결방법 : 3가지
	// 1. Map을 이용하는 방법
	// 2. Class를 작성해서 필드를 여러 개 정의
	// 3. @Param를 사용하는 방법
	public List<ReplyVO> getListWithPaging(@Param("cri") Criteria cri, @Param("bno") Integer bno);
	// 댓글의 수를 가져오는 메소드
	public int getCountByBno(Integer bno);
}
