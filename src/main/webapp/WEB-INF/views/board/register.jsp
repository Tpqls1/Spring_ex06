<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../includes/header.jsp"%>
<style>
.uploadResult {
	width: 100%;
	background-color: gray;
}
.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}
.uploadResult ul li {
	list-style: none;
	padding: 10px;
}
.uploadResult ul li img {
	width: 20px;
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
}
.bigPicture {
  position: relative;
  display:flex;
  justify-content: center;
  align-items: center;
}
.bigPicture img {
  width: 600px;
}
</style>
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Register</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Register</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form action="/board/register" method="post" role="form">
					<div class="form-group">
						<label>Title</label> <input class="form-control" name="title">
					</div>
					<div class="form-group">
						<label>Text area</label>
						<textarea rows="3" class="form-control" name="content"></textarea>
					</div>
					<div class="form-group">
						<label>Writer</label> <input class="form-control" name="writer">
					</div>
					<button type="submit" class="btn btn-default">Submit Button</button>
					<button type="reset" class="btn btn-default">Reset Button</button>
				</form>
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-default">
      <div class="panel-heading">File Attach</div><!-- /.panel-heading -->
      <div class="panel-body">
        <div class="form-group uploadDiv">
            <input type="file" name='uploadFile' multiple>
        </div>
        <div class='uploadResult'> 
          <ul>
          </ul>
        </div>
      </div><!--  end panel-body -->
    </div><!--  end panel-body -->
  </div><!-- end panel -->
</div><!-- /.row -->
<script type="text/javascript">
$(document).ready(function() {	// HTML 코드가 로딩된 다음에 실행된다.
	// 폼의 submit 버튼을 누르면 제어
	var formObj = $("form[role='form']");
	$("button[type='submit']").on("click", function(e){    // 등록 버튼의 default 동작을 막는다.
	    e.preventDefault();	// 서버로 전송이 되지 않토록 함
	    console.log("submit clicked");
	    // 첨부파일에 대한 정보를 input 태그 type="hidden"에 추가
	    var str = "";
	    $(".uploadResult ul li").each(function(i, obj){
	      var jobj = $(obj);
	      console.dir(jobj);
	      console.log("-------------------------");
	      console.log(jobj.data("filename"));
	      str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
	      str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
	      str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
	      str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+ jobj.data("type")+"'>";
	    });
	    console.log(str);
	    formObj.append(str).submit();
	});

	var regex = new RegExp("(.*?\.(exe|sh|zip|alz)$)");	// 확장자 제한
	var maxSize = 5242880;	// 파일 크기를 5MB 크기로 제한
	
	function checkExtension(filename, filesize) {
		if(filesize > maxSize) {			// 파일의 크기 제한
			alert("파일 크기 초과");
			return false;
		}
		if(regex.test(filename)) {		// 확장자 제한
			alert("해당 종류의 파일은 업로드할 수 없습니다.");
			return false;
		}
		return true;
	}
	
	// 첨부파일을 추가하면 이벤트가 걸리도록 함 : change 이벤트
	$("input[type='file']").on("change", function(e) {
		// Ajax를 이용해서 서버로 파일을 전송
		var formData = new FormData();	// 가상의 폼 데이터
		var inputFile = $("input[name='uploadFile']");
		var files = inputFile[0].files;
		console.log(files);
		for(var i = 0;i < files.length;i++) {
			if(!checkExtension(files[i].name, files[i].size)) {	// 유효성 검사
				return;	// 확장자 제한이나 파일 크기 제한에 걸릴 경우 종료
			}
			formData.append("uploadFile", files[i]);
		}
		// Ajax를 이용해서 서버로 전송 : 공식
		$.ajax({
			url: '/uploadAjaxAction',
			processData: false,
			contentType: false,
			data: formData,
			type: 'POST',
			dataType: 'json',	// JSON 객체 형태로 응답을 받겠다.
			success: function(result) {	// result에 JSON 객체 형태로 응답 : List<AttachFileDTO>
				//alert("Uploaded");
				console.log(result);
				showUploadResult(result); //업로드 결과 처리 함수
			}
		});
	});
	
	// 첨부파일에 대한 정보를 표시
	function showUploadResult(uploadResultArr){
		if(!uploadResultArr || uploadResultArr.length == 0){
			return;
		}
		var uploadUL = $(".uploadResult ul");
		var str ="";
			    
		$(uploadResultArr).each(function(i, obj){
			//image type
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
	
	$(".uploadResult").on("click", "button", function(e){
		console.log("delete file");
		var targetFile = $(this).data("file");
		var type = $(this).data("type");
		var targetLi = $(this).closest("li");	// 첨부파일 영역
		$.ajax({
		  url: '/deleteFile',
		  data: {fileName: targetFile, type:type},
		  dataType:'text',
		  type: 'POST',
		  success: function(result){
			alert(result);
		    targetLi.remove();	// 첨부 파일 삭제
		  }
		}); //$.ajax
	});
});
</script>
<%@ include file="../includes/footer.jsp"%>