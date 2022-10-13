<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../includes/header.jsp"%>
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Read</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Read Page</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="form-group">
					<label>Bno</label> <input class="form-control"
					 	name="bno" value='<c:out value="${board.bno}" />'
						readonly="readonly">
				</div>
				<div class="form-group">
					<label>Title</label> <input class="form-control"
					 	name="title" value='<c:out value="${board.title}" />'
						readonly="readonly">
				</div>
				<div class="form-group">
					<label>Text area</label> <textarea class="form-control" rows="3"
					 	name="content" readonly="readonly"><c:out value="${board.content}" /></textarea>
				</div>
				<div class="form-group">
					<label>Writer</label> <input class="form-control"
					 	name="writer" value='<c:out value="${board.writer}" />'
						readonly="readonly">
				</div>
				<!-- data-oper : 버튼을 구분하기 위해 사용하는 속성
				버턴의 종류 : btn-default, btn-info, btn-major
				 -->
				<%-- <button data-oper='modify' class="btn btn-default"
					onclick="location.href='/board/modify?bno=<c:out value="${board.bno}" />'">Modify</button>
				<button data-oper='list' class="btn btn-info"
					onclick="location.href='/board/list'">List</button> --%>
				<button data-oper='modify' class="btn btn-default">Modify</button>
				<button data-oper='list' class="btn btn-info">List</button>
			</div>
			<!-- /.panel-body -->
			<!-- form을 추가 : 확장성, 페이징 처리, 검색 기능
			사용자에게 보이지 않토록 input 태그의 type을 hidden으로 한다.
			 -->
			<form id="operForm" action="/board/modify" method="get">
				<input type="hidden" name="bno" value="${board.bno}">
				<input type="hidden" name="pageNum" value="${cri.pageNum}">
				<input type="hidden" name="amount" value="${cri.amount}">
				<!-- 추가로 필요하면 더 추가한다. -> 검색기능 -->
				<input type="hidden" name="type" value="${cri.type}">
				<input type="hidden" name="keyword" value="${cri.keyword}">
			</form>
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<!-- 댓글 처리하는 부분 -->
<div class="row">
  <div class="col-lg-12">
  <!-- panel -->
  <div class="panel panel-default">
    <div class="panel-heading">
      <i class="fa fa-comments fa-fw"></i> Reply
      <button id='addReplyBtn' class="btn btn-primary btn-xs pull-right">New Reply</button>
    </div>
    <div class="panel-body">
      <ul class="chat">
        <!-- default로 댓글 하나를 임의로 추가 -> 형식을 알수 있음 -->
        <li class="left clearfix" data-rno='12'>
          <div>
            <div class="header">
              <strong class="primary-font">user00</strong>
              <small class="pull-right text-muted">2018-01-01 13:13</small>
            </div>
            <p>Good job!</p>
          </div>
        </li>
      </ul>
    </div>
	<!-- 페이징 처리하는 부분을 추가 -->
	<div class="panel-footer"></div> 
  </div>
  
  </div>
  <!-- ./ end row -->
</div>
<!-- Modal : default로 제공하는 모달창 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
  aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
          aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">REPLY MODAL</h4>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>Reply</label> 
          <input class="form-control" name='reply' value='New Reply!!!!'>
        </div>      
        <div class="form-group">
          <label>Replyer</label> 
          <input class="form-control" name='replyer' value='replyer'>
        </div>
        <div class="form-group">
          <label>Reply Date</label> 
          <input class="form-control" name='replyDate' value='2018-01-01 13:13'>
        </div>
      </div>
      <div class="modal-footer">
        <button id='modalModBtn' type="button" class="btn btn-warning">Modify</button>
        <button id='modalRemoveBtn' type="button" class="btn btn-danger">Remove</button>
        <button id='modalRegisterBtn' type="button" class="btn btn-primary">Register</button>
        <button id='modalCloseBtn' type="button" class="btn btn-default">Close</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<!-- 서버와 통신하는 JS 파일(reply.js)을 import -->
<script type="text/javascript" src="/resources/js/reply.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var bnoValue = '<c:out value="${board.bno}" />';	// 게시글 번호 저장
	var replyUL = $(".chat");
	
	showList(1);	// 댓글 목록 가져와서 보여주기 : 1페이지
	function showList(page) {	// 댓글 페이지를 보여주는 함수 : -1->마지막 페이지를 보여달라
		console.log("show list " + page);

		// reply.js 모듈을 호출해서 
		// getList : 댓글 목록을 가져오는 함수
		// 입력 파라미터 : bno(게시글 번호), page(페이지 번호)
		// function() : callback 함수 -> 서버로부터 댓글목록을 수신하면 그 결과를 반환
		replyService.getList({bno:bnoValue, page: page || 1}, function(replyCnt, list) {
			console.log("list: " + list);	// 댓글 목록을 출력
			console.log("replyCnt: " + replyCnt);
			
			if(page == -1) {	// 마지막 페이지를 요청할 경우
				pageNum = Math.ceil(replyCnt / 10.0);	// 마지막 페이지
				showList(pageNum);	// 마지막 페이지 보여준다.
				return;
			}
			
			var str = "";	// 댓글에 목록을 나타내는 문자열 데이터
			if(list == null || list.length == 0) {	// 댓글이 존재하지 않으면
				replyUL.html("");	// 댓글 목록을 clear (default로 하나를 보여주는 것을 삭제)
				return;
			}
			// 댓글이 존재하면
			for(var i = 0, len = list.length || 0;i < len;i++) {	// 댓글에 대하여
				str += "<li class='left clearfix' data-rno='" + list[i].rno + "'>";
				str += " <div><div class='header'><strong class='primary-font'>["
					+ list[i].rno + "]" + list[i].replyer + "</strong>";
				str += "  <small class='pull-right text-muted'>"
					+ replyService.displayTime(list[i].replyDate)
					+ "</small></div>";
				str += "  <p>" + list[i].reply + "</p></div></li>";
			}
			replyUL.html(str);	// 댓글 목록(<ul> 밑에) <li> 들을 추가
			// 페이징 처리 부분을 추가
			showReplyPage(replyCnt);
		});
	}
	
	// 모달창에 대하여 조작
	var modal = $(".modal");
	var modalInputReply = modal.find("input[name='reply']");		// 내용을 입력창
	var modalInputReplyer = modal.find("input[name='replyer']");	// 작성자
	var modalInputReplyDate = modal.find("input[name='replyDate']");// 작성일
	
	var modalModBtn = $("#modalModBtn");			// 수정 버튼
	var modalRemoveBtn = $("#modalRemoveBtn");		// 삭제 버튼
	var modalRegisterBtn = $("#modalRegisterBtn");	// 등록 버튼
	var modalCloseBtn = $("#modalCloseBtn");		// 닫기 버튼
	
	$("#addReplyBtn").on("click", function(e) {		// 댓글 추가 버튼 처리
		modal.find("input").val("");	// 모든 입력 창을 clear
		modalInputReplyDate.closest("div").hide();	// 작성일 감추기
		modal.find("button[id != 'modalCloseBtn']").hide();	// 수정, 삭제, 등록버튼을 감추기
		modalRegisterBtn.show();	// 등록버튼을 보여주기
		$(".modal").modal("show");	// show : 모달창을 보여주기, hide : 감추기 
	});
	
	// 모달창의 등록 버튼을 누르면 서버로 댓글 추가를 전송
	modalRegisterBtn.on("click", function(e) {
		var reply = {	// ReplyVO 객체를 추가
			reply: modalInputReply.val(),
			replyer: modalInputReplyer.val(),
			bno: bnoValue
		};
		
		// 서버로 댓글 저장
		replyService.add(reply, function(result) {
			alert(result);	// success
			modal.find("input").val("");	// 모달창의 입력 값을 clear
			modal.modal("hide");			// 모달창을 감추기
			
			showList(-1);	// 댓글 목록을 갱신 -> 마지막 페이지를 보여준다.
			// 추가된 댓글이 마지막 페이지에 있으므로 추가된 댓글이 있는지 알수 있다.
		});
	});
	
	// 댓글 조회
	$(".chat").on("click", "li", function(e){	// li 태그는 댓글이 추가되면 생기는 것
	    var rno = $(this).data("rno");
	    console.log(rno);

	    replyService.get(rno, function(reply){
	    	modalInputReply.val(reply.reply);
	        modalInputReplyer.val(reply.replyer);
	        modalInputReplyDate.val(replyService.displayTime( reply.replyDate))
	        	.attr("readonly","readonly");
	        modal.data("rno", reply.rno);
	        
	        modal.find("button[id !='modalCloseBtn']").hide();
	        modalModBtn.show();
	        modalRemoveBtn.show();
	        
	        $(".modal").modal("show");        
	    });
	});
	
	// 수정 버튼이 눌리면 동작
	modalModBtn.on("click", function(e){
		var reply = {rno:modal.data("rno"), reply: modalInputReply.val(),
			replyer: modalInputReplyer.val()};
      
		replyService.update(reply, function(result){
        			alert(result);
        			modal.modal("hide");
        			showList(pageNum);	// 현재 페이지를 유지
		});
	});

	// 삭제 버튼 처리
	modalRemoveBtn.on("click", function (e){
		var rno = modal.data("rno");
	  	  
		replyService.remove(rno, function(result){
	  	    alert(result);
	  	    modal.modal("hide");
	  	    showList(pageNum);	// 현재 페이지를 유지
		});
	});
	
	// 닫기 버튼 처리 -> 모달창을 닫아 준다.
	modalCloseBtn.on("click", function(e) {
		modal.find("input").val("");	// 모달의 입력창을 clear
		modal.modal("hide");	// 모달창을 닫기
	});

	var pageNum = 1;	// 초기 페이지 1 페이지
	var replyPageFooter = $(".panel-footer");	// 페이지 처리하는 부분
	
	// 페이지 처리하는 부분을 만들어주는 함수를 정의
	function showReplyPage(replyCnt) {
		var endNum = Math.ceil(pageNum / 10.0) * 10;  
	    var startNum = endNum - 9;       
	    var prev = startNum != 1;
	    var next = false;
	      
	    if(endNum * 10 >= replyCnt){
	        endNum = Math.ceil(replyCnt/10.0);
	    }
	    if(endNum * 10 < replyCnt){
	        next = true;
	    }
	    var str = "<ul class='pagination pull-right'>";
	    if(prev){
	        str+= "<li class='page-item'><a class='page-link' href='"+(startNum -1)+"'>Previous</a></li>";
	    }
	    for(var i = startNum ; i <= endNum; i++){
	        var active = pageNum == i? "active":"";
	        str+= "<li class='page-item "+active+" '><a class='page-link' href='"+i+"'>"+i+"</a></li>";
	    }
	    if(next){
	        str+= "<li class='page-item'><a class='page-link' href='"+(endNum + 1)+"'>Next</a></li>";
	    }
	    str += "</ul></div>";
	    console.log(str);
	    replyPageFooter.html(str);
	}
	// 댓글의 페이징 이동
	replyPageFooter.on("click", "li a", function(e){
		e.preventDefault();
		console.log("page click");
		var targetPageNum = $(this).attr("href");
		console.log("targetPageNum: " + targetPageNum);
		pageNum = targetPageNum;
		showList(pageNum);
	});
});
</script>
<script>
$(document).ready(function() {	// HTML 코드가 로딩된 다음에 실행되도록
	var operForm = $("#operForm");
	
	// 버튼이 눌리면 실행되도록 이벤트를 등록
	$("button").on("click", function(e) {
		e.preventDefault();	// 디폴트 동작을 막고
		// 어느 버튼이 눌렸는지 확인
		// $(this) : 눌려진 버튼
		var operation = $(this).data("oper");
		if(operation === 'modify') {	// modify 화면으로 이동
			operForm.submit();
		} else if(operation === 'list') {	// 목록보기 화면으로 이동
			operForm.find("#bno").remove();	// input bno를 삭제
			operForm.attr("action", "/board/list");
			operForm.submit();
		}
	});
});
</script>
<%@ include file="../includes/footer.jsp"%>