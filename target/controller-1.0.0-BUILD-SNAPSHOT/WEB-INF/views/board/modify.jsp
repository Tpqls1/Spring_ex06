<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../includes/header.jsp"%>
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Modify</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Modify</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
			<form action="/board/modify" method="post" role="form">
				<!-- pageNum, amount, 검색 기능 input 태그 추가 -->
				<input type="hidden" name="pageNum" value="${cri.pageNum}">
				<input type="hidden" name="amount" value="${cri.amount}">
				<input type="hidden" name="type" value="${cri.type}">
				<input type="hidden" name="keyword" value="${cri.keyword}">
				<div class="form-group">
					<label>Bno</label> <input class="form-control"
					 	name="bno" value='<c:out value="${board.bno}" />'
						readonly="readonly">
				</div>
				<div class="form-group">
					<label>Title</label> <input class="form-control"
					 	name="title" value='<c:out value="${board.title}" />'>
				</div>
				<div class="form-group">
					<label>Text area</label> <textarea class="form-control" rows="3"
					 	name="content"><c:out value="${board.content}" /></textarea>
				</div>
				<div class="form-group">
					<label>Writer</label> <input class="form-control"
					 	name="writer" value='<c:out value="${board.writer}" />'
						readonly="readonly">
				</div>
				<div class="form-group">
					<label>RegDate</label> <input class="form-control"
					 	name="regDate" value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.regDate}" />' 
						readonly="readonly">
				</div>
				<div class="form-group">
					<label>Update Date</label> <input class="form-control"
					 	name="updateDate" value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.updateDate}" />' 
						readonly="readonly">
				</div>
				<!-- data-oper : 버튼을 구분하기 위해 사용하는 속성
				버턴의 종류 : btn-default, btn-info, btn-danger
				 -->
				<button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
				<button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
				<button type="submit" data-oper='list' class="btn btn-info">List</button>
			</form>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<script>
$(document).ready(function() {	// HTML 태그가 모두 로딩이 된다음에 실행
	var formObj = $("form");
	
	// modify, remove, list
	$("button").on("click", function(e) {
		e.preventDefault();	// 디폴트 동작을 막는다.
		// $(this) : 이벤트가 발생한 요소 -> submit 버튼
		var operation = $(this).data("oper");	// data-oper 속성값을 읽는다.
		if(operation === 'remove') {		// Remove 버튼이 눌린 경우
			formObj.attr("action", "/board/remove");	// modify -> remove
		} else if(operation === 'list') {	// List 버튼이 눌린 경우
			// 페이징 관련된 파라미터를 가지고 다니도록 수정 필요
			formObj.attr("action", "/board/list");	// /board/modify -> /board/list
			formObj.attr("method", "get");
			// 필요한 파라미터(pageNum, amount)만 전송을 하기 위해 복사
			var pageNumTag = $("input[name='pageNum']").clone();
			var amountTag = $("input[name='amount']").clone();
			var typeTag = $("input[name='type']").clone();
			var keywordTag = $("input[name='keyword']").clone();
			formObj.empty();	// input 태그들을 모두 삭제
			formObj.append(pageNumTag);
			formObj.append(amountTag);
			formObj.append(typeTag);
			formObj.append(keywordTag);
		}
		formObj.submit();	// 서버로 요청
	});
});
</script>
<%@ include file="../includes/footer.jsp"%>