package org.zerock.domain;

import lombok.Getter;
import lombok.ToString;

// 페이징 처리를 위한 화면을 보여주기 위한 데이터를 가지고 있는 빈
@Getter
@ToString
public class PageDTO {
	// 페이징 처리를 위해서 필요한 데이터
	private int startPage;	// 화면에 보여줄 처음 페이지 번호
	private int endPage;	// 마지막 페이지 번호
	private boolean prev, next;	// 이전/다음 버튼이 필요한지 여부
	private int total;		// 전체 데이터의 수
	private Criteria cri;	// 페이징 처리를 위한 데이터
	
	// 페이징 처리를 위한 객체를 생성자
	// input : 페이징 처리를 위한 기본 데이터 (Criteria : 현재 페이지, 보여줄 데이터의 수)
	// total : 전체 데이터의 수 -> 페이징 처리를 위해 필요한 데이터를 만들어 준다.
	public PageDTO(Criteria cri, int total) {
		this.cri = cri;
		this.total = total;
		// 필요한 데이터를 만들어 주어야 한다. : 공식
		this.endPage = (int)(Math.ceil(cri.getPageNum() / 10.0)) * 10;
		this.startPage = this.endPage - 9;	// 10페이지씩 보여준다고 가정한 상태
		// 전체 데이터의 갯수를 보여주기 위한 페이지 번호
		int realEnd = (int)(Math.ceil((total * 1.0) / cri.getAmount()));
		if(realEnd <= this.endPage) {
			this.endPage = realEnd;
		}
		this.prev = this.startPage > 1;	// 이전 페이지가 있다면 true
		this.next = this.endPage < realEnd;	// 다음 페이지가 있다면 true
	}
}
