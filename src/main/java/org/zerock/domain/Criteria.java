package org.zerock.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 페이지 처리와 검색 기능을 수행하기 위해 필요한 데이터 저장하는 클래스
@Getter
@Setter
@ToString
public class Criteria {
	// 필드
	private int pageNum;	// 현재 페이지 번호
	private int amount;		// 페이지 당 보여줄 게시글의 수 (보통 10개 : 가변)
	// 검색 기능
	private String type;	// 검색 유형
	private String keyword;	// 키워드
	
	// 디폴트 생성자 -> 기본적으로 목록 보기 -> 1페이지를 보여준다.
	public Criteria() {		
		this(1, 10);	// default : 1페이지, 10개
	}
	
	// 몇 페이지를 보여줄지? 몇 개를 가져올지? 설정하는 생성자
	public Criteria(int pageNum, int amount) {	// 보여줄 페이지에 대한 데이터
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	// 데이터베이스 검색 시 가져올 게시글의 시작 번호를 가져오는 Getter를 추가
	public int getPageStart() {	// 해당 페이지의 첫번째 게시글의 번호
		return (pageNum - 1) * amount;
	}
	
	// MyBatis에서 검색 유형에 대하여 판단하기 위해 type->배열 변환
	public String[] getTypeArr() {	// tc -> [0]:t, [1]:c
		return type == null ? new String[] { } : type.split("");
	}
	
	// Controller가 redirect 수행할 때 파라미터를 조금 더 편하게 조작할 수 있도록 메소드를 추가
	// 확장성을 위해서 : 다른 파라미터가 추가될 때, queryParam()를 추가하면 된다.
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.amount)
				.queryParam("type", this.type)
				.queryParam("keyword", this.keyword);
		return builder.toUriString();
	}
}
