<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="../includes/header.jsp"%>
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Tables</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board List Page
				<!-- pull-right : 오른쪽 정렬(에 배치)
				btn-xs : 버튼의 크기
				 -->
				<Button id="regBtn" type="button" class="btn btn-xs pull-right">Register New Board</Button>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<table class="table table-striped table-bordered table-hover"
					id="dataTables-example">
					<thead>
						<tr>
							<th>번호</th>
							<th>제목</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>수정일</th>
						</tr>
					</thead>
					<c:forEach items="${list}" var="board">
						<tr>
							<td><c:out value="${board.bno}" /></td>
							<!-- 링크 파라미터 추가
							방법 1 : 파라미터를 추가 -> 검색엔진에 노출이 필요한 경우
							방법 2 : 확장성 때문에 자바스크립트를 이용(이벤트 처리)해서 처리
							 -->
							<td><a class="move" href='<c:out value="${board.bno}" />'>
									<c:out value="${board.title}" />
									<b>[ <c:out value="${board.replyCnt}" /> ]</b></a>
							</td>
							<td><c:out value="${board.writer}" /></td>
							<td><fmt:formatDate value="${board.regDate}"
									pattern="yyyy-MM-dd" /></td>
							<td><fmt:formatDate value="${board.updateDate}"
									pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
				</table>
				<!-- 검색유형과 검색어, 검색버튼을 추가 -->
				<div class="row">
				  <div class="col-lg-12">
				    <form action="/board/list" id="searchForm" method="get">
				      <select name="type">
				        <option value="" <c:out value="${pageMaker.cri.type == null? 'selected':''}"/>>--</option>
				        <option value="T" <c:out value="${pageMaker.cri.type eq 'T'? 'selected':''}"/>>제목</option>
				        <option value="C" <c:out value="${pageMaker.cri.type eq 'C'? 'selected':''}"/>>내용</option>
				        <option value="W" <c:out value="${pageMaker.cri.type eq 'W'? 'selected':''}"/>>작성자</option>
				        <option value="TC" <c:out value="${pageMaker.cri.type eq 'TC'? 'selected':''}"/>>제목 or 내용</option>
				        <option value="TW" <c:out value="${pageMaker.cri.type eq 'TW'? 'selected':''}"/>>제목 or 작성자</option>
				        <option value="TCW" <c:out value="${pageMaker.cri.type eq 'TCW'? 'selected':''}"/>>제목 or 내용 or 작성자</option>
				      </select>
				      <input type="text" name="keyword" value="${pageMaker.cri.keyword}">
				      <input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}">
				      <input type="hidden" name="amount" value="${pageMaker.cri.amount}">
				      <button class="btn btn-default">Search</button>
				    </form>
				  </div>
				</div>
				<!-- 페이징 처리를 위한 부분 추가 -->
				<div class="pull-right"><!-- 오른쪽으로 정렬 -->
					<ul class="pagination">
					<!-- 이전 페이지 -->
					<c:if test="${pageMaker.prev}">
						<li class="pagenate_button previous"><a href="${pageMaker.startPage - 1}">Previous</a></li>	
					</c:if>
					<!-- startPage .... endPage -->
					<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
						<li class="pagenate_button ${pageMaker.cri.pageNum == num ? 'active' : ''}"><a href="${num}">${num}</a></li>
					</c:forEach>
					<!-- 다음 페이지 -->
					<c:if test="${pageMaker.next}">
						<li class="pagenate_button next"><a href="${pageMaker.endPage + 1}">Next</a></li>
					</c:if>
					</ul>
				</div>
				<!-- 페이징 처리를 위한 폼을 추가 : 확장성을 위해서 -> 앞으로 다른 파라미터를 추가되더라도
				같은 형태로 적용하려고 -->
				<form id="actionForm" action="/board/list" method="get">
					<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}">
					<input type="hidden" name="amount" value="${pageMaker.cri.amount}">
					<!-- 추가 된다. -> 검색 기능 -->
					<input type="hidden" name="type" value="${pageMaker.cri.type}">
					<input type="hidden" name="keyword" value="${pageMaker.cri.keyword}">
				</form>
				<!-- Modal -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title" id="myModalLabel">Modal title</h4>
							</div>
							<div class="modal-body">처리가 완료되었습니다.</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Close</button>
								<button type="button" class="btn btn-primary">Save
									changes</button>
							</div>
						</div>
						<!-- /.modal-content -->
					</div>
					<!-- /.modal-dialog -->
				</div>
				<!-- /.modal -->
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<script type="text/javascript">
	$(document).ready(function() { // HTML 태그가 모두 렌더링이 끝난다음에 실행
		var result = '<c:out value="${result}" />'; // controller로부터 수신된 결과를 저장
		console.log(result);
		// 게시글 쓰기 : 게시글 번호, 게시글 수정/게시글 삭제 : Success가 저장된다.
		// 모달창을 띄워 준다.
		checkModal(result);
		history.replaceState({}, null, null);
		function checkModal(result) {
			// history.state : 모달창을 띄우지 않토록 마크가 되어 있는 경우
			if(result === '' || history.state) {	// back button이나 forward button에 의하여 이 페이지로 이동
				return;
			}
			// 게시글 쓰기, 게시글 수정, 게시글 삭제
			if(parseInt(result) > 0) {	// 새로운 게시글이 추가된 경우
				// 모달창의 본문을 변경
				$(".modal-body").html("게시글" + parseInt(result) + " 번이 등록되었습니다.");
			}
			$("#myModal").modal("show");	// 모달창을 띄워준다.
		}
		// 게시글 쓰기 버튼을 눌리면 게시글 쓰기(/board/register) 페이지로 이동
		$("#regBtn").on("click", function(e) {
			self.location = "/board/register";	// 게시글 쓰기 페이지로 이동
		});
		
		// form 객체를 선언
		var actionForm = $("#actionForm");
		// 페이징 링크가 눌리면 페이지 이동 처리
		$(".pagenate_button a").on("click", function(e) {
			// $(this) : 눌린 링크(a)
			e.preventDefault();		// 우선 디폴트 동작을 막고(서버로 전송을 막는다)
			console.log("click 눌림");
			// input 태그 pageNum에 눌린 페이지 번호를 넣어준다.
			actionForm.find("input[name='pageNum']").val($(this).attr("href"));
			// actionForm.attr("action", "/board/list");
			actionForm.submit();	// 서버로 전송이 된다. submit 버튼을 누른것과 같은 동작
		});
		
		// 상세보기 링크가 눌렸을 때 동작
		$(".move").on("click", function(e) {
			e.preventDefault();	// 디폴트 동작을 막고
			// form을 이용 : pageNum, amount 데이터가 있으므로
			actionForm.append("<input type='hidden' name='bno' value='"
				+ $(this).attr('href') + "'>");	// input 태그 bno를 추가
			actionForm.attr("action", "/board/get");	// /board/list -> /board/get
			actionForm.submit();	// 상세보기로 이동
		});
		
		// 검색부분에 대한 폼을 변수에 저장
		var searchForm = $("#searchForm");
		// 검색버튼에 click 이벤트를 추가
		$("#searchForm button").on("click", function(e) {
			e.preventDefault();
			// 유효성 검사
			if(!searchForm.find("option:selected").val()) {	// 검색 유형을 선택하지 않은 경우
				alert("검색 종류를 선택하세요.");
				return false;	// 서버로 전송이 되지 않는다.
			}
			if(!searchForm.find("input[name='keyword']").val()) {	// 키워드를 입력하지 않은 경우
				alert("키워드를 입력하세요.");
				return false;	// 서버로 전송이 되지 않는다.
			}
			// 검색을 요청했으면 pageNum = 1로 변경한다.
			searchForm.find("input[name='pageNum']").val("1");
			searchForm.submit();	// 검색 요청을 한다.
		});
	});
</script>
<%@ include file="../includes/footer.jsp"%>