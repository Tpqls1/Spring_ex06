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
<div class='bigPictureWrapper'>
  <div class='bigPicture'>
  </div>
</div>

<style>
.uploadResult {
  width:100%;
  background-color: gray;
}
.uploadResult ul{
  display:flex;
  flex-flow: row;
  justify-content: center;
  align-items: center;
}
.uploadResult ul li {
  list-style: none;
  padding: 10px;
  align-content: center;
  text-align: center;
}
.uploadResult ul li img{
  width: 100px;
}
.uploadResult ul li span {
  color:white;
}
.bigPictureWrapper {
  position: absolute;
  display: none;
  justify-content: center;
  align-items: center;
  top:0%;
  width:100%;
  height:100%;
  background-color: gray; 
  z-index: 100;
  background:rgba(255,255,255,0.5);
}
.bigPicture {
  position: relative;
  display:flex;
  justify-content: center;
  align-items: center;
}
.bigPicture img {
  width:600px;
}
</style>
<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-default">
      <div class="panel-heading">Files</div><!-- /.panel-heading -->
      <div class="panel-body">
        <div class="form-group uploadDiv">
            <input type="file" name='uploadFile' multiple="multiple">
        </div>
        <div class='uploadResult'> 
          <ul>
          </ul>
        </div>
      </div><!--  end panel-body -->
    </div><!--  end panel-body -->
  </div><!-- end panel -->
</div><!-- /.row -->
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
		} else if(operation === 'modify') {
		      console.log("submit clicked");
		      var str = "";
		      $(".uploadResult ul li").each(function(i, obj){
		        var jobj = $(obj);
		        console.dir(jobj);
		        str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
		        str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
		        str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
		        str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+ jobj.data("type")+"'>";
		      });
		      formObj.append(str).submit();
		  }
		  formObj.submit();
	});
	
	(function(){
		var bno = '<c:out value="${board.bno}"/>';
		$.getJSON("/board/getAttachList", {bno: bno}, function(arr){
		  console.log(arr);
		  var str = "";
		  $(arr).each(function(i, attach){
		    //image type
		    if(attach.fileType){
		      var fileCallPath =  encodeURIComponent( attach.uploadPath+ "/s_"+attach.uuid +"_"+attach.fileName);
		      str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' ";
		      str +=" data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
		      str += "<span> "+ attach.fileName+"</span>";
		      str += "<button type='button' data-file=/'"+fileCallPath+"/' data-type='image' ";
		      str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
		      str += "<img src='/display?fileName="+fileCallPath+"'>";
		      str += "</div>";
		      str +"</li>";
		    }else{  
		      str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' ";
		      str += "data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
		      str += "<span> "+ attach.fileName+"</span><br/>";
		      str += "<button type='button' data-file=/'"+fileCallPath+"/' data-type='file' ";
		      str += " class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
		      str += "<img src='/resources/img/attach.png'></a>";
		      str += "</div>";
		      str +"</li>";
		    }
		  });
		  $(".uploadResult ul").html(str);
		});//end getjson
	})();//end function
	  // 첨부파일 삭제 버튼 처리 
	  // 파일은 삭제하지 않고, li태그만 삭제 -> form 전송할 때 데이터베이스 반영됨 
	  // 그 때 첨부파일을 삭제한다.
	  // 첨부파일을 먼저 삭제하면, submit을 하지 않고 빠져나가는 경우 첨부파일만 삭제되는 현상이 발생
	  $(".uploadResult").on("click", "button", function(e){   
			console.log("delete file");
		if(confirm("Remove this file? ")){
	  		var targetLi = $(this).closest("li");
	  		targetLi.remove();
			}
		});
	  
	// 첨부 파일 추가
	var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$"); 
	var maxSize = 5242880; //5MB
	function checkExtension(fileName, fileSize){ 
		if(fileSize >= maxSize){
			alert("파일 사이즈 초과");
			return false;
		}
		if(regex.test(fileName)){
			alert("해당 종류의 파일은 업로드할 수 없습니다."); 
			return false;
		}
			return true; 
		}
		var cloneObj = $(".uploadDiv").clone(); 
		$("input[type='file']").change(function(e){
		var formData = new FormData();
		var inputFile = $("input[name='uploadFile']"); 
		var files = inputFile[0].files;
	
		for(var i = 0; i < files.length; i++){
	if(!checkExtension(files[i].name, files[i].size) ){ 
		return false;
	}
	 formData.append("uploadFile", files[i]); 
	}
	$.ajax({
	url: '/uploadAjaxAction', 
	processData: false, 
	contentType: false,data: formData,
	type: 'POST', 
	dataType:'json',
	success: function(result){
		console.log(result);
	showUploadResult(result); //업로드 결과 처리 함수 
	$(".uploadDiv").html(cloneObj.html());
	}
		}); //$.ajax
	});
		
	function showUploadResult(uploadResultArr){ 
		if(!uploadResultArr || uploadResultArr.length == 0){ 
			return; 
		} 
		var uploadUL = $(".uploadResult ul");
		var str ="";
	$(uploadResultArr).each(function(i, obj){
		if(obj.image){
			var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid +"_"+obj.fileName);
		    str += "<li data-path='"+obj.uploadPath+"'";
		    str += " data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'";
		    str += "><div>";
		    str += "<span> "+ obj.fileName+"</span>";
		    str += "<button type='button' data-file='" + fileCallPath + "' data-type='image' " +
		    	"class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
		    str += "<img src='/display?fileName="+fileCallPath+"'>";
		    str += "</div>";
		    str +"</li>";
		}else{
		    var fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName);            
		    var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
		    str += "<li "
			str += "data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"' ><div>";
		    str += "<span> "+ obj.fileName+"</span>";
		    str += "<button type='button' data-file='" + fileCallPath + "' data-type='image'" + 
		    	" class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
		    str += "<img src='/resources/img/attach.png'></a>";
		    str += "</div>";
		    str +"</li>";
		}
	});
	 	uploadUL.append(str);
	  }

	
	var str = "";
	$(".uploadResult ul li").each(function(i, obj){
		var jobj = $(obj);
		console.dir(jobj);
		str += "<input type='hidden' name='attachList["+i+"].fileName'value='"+jobj.data("filename")+"'>";
		str += "<input type='hidden' name='attachList["+i+"].uuid'value='"+jobj.data("uuid")+"'>";
		str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
		str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("type")+"'>"; 
	});
});
</script>
<%@ include file="../includes/footer.jsp"%>