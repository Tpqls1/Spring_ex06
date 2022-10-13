/**
 * 서버와 통신을 담당하는 모듈
 */
console.log("Reply Module....");
// 자체 실행함수 : 함수를 정의를 하고 바로 실행한다. -> /board/get.jsp로 로딩이
// 되면서 실행된다.
var replyService = (function() {
	// 댓글 목록을 가져오는 함수
	// param : 댓글을 가져오기 위한 파라미터 (게시글 번호 bno, page번호)
	// callback : 서버와 통신 후 댓글 목록을 jsp javascript에게 알린다.
	function getList(param, callback, error) {
		var bno = param.bno;
		var page = param.page || 1;	// default가 1 page
		$.getJSON("/replies/pages/" + bno + "/" + page + ".json",
			// 서버로부터 응답을 수신
			function(data) {	// 댓글 목록이 data에 실린다.
				// data : List<ReplyVO> -> ReplyPageDTO(cnt, List<ReplyVO)
				if(callback) {	// callback 함수가 주어지면
					callback(data.replyCnt, data.list);	// callback 함수를 호출하면서 데이터를 전달
				}
		}).fail(function(xhr, status, err) {
			if(error) {	// 에러함수가 주어지면
				error(err);	// 에러 함수를 호출
			}
		});
	}
	
	// 페이스북의 시간하는 방법과 유사하게 출력하는 함수를 추가
	// 하루 미만이면 시간을 출력
	// 하루 이상이면 날짜 출력
	function displayTime(timeValue) {
		var today = new Date();
		var gap = today.getTime() - timeValue;
		var dateObj = new Date(timeValue);
		var str = "";

		if (gap < (1000 * 60 * 60 * 24)) {	// 1일 이하면
			var hh = dateObj.getHours();
			var mi = dateObj.getMinutes();
			var ss = dateObj.getSeconds();
			return [ (hh > 9 ? '' : '0') + hh, ':', (mi > 9 ? '' : '0') + mi,
					':', (ss > 9 ? '' : '0') + ss ].join('');
		} else {
			var yy = dateObj.getFullYear();
			var mm = dateObj.getMonth() + 1; // getMonth() is zero-based
			var dd = dateObj.getDate();
			return [ yy, '/', (mm > 9 ? '' : '0') + mm, '/',
					(dd > 9 ? '' : '0') + dd ].join('');
		}
	}
	
	function add(reply, callback, error) {
		console.log(reply);
		$.ajax({
			type: 'post',
			url: '/replies/new',
			data: JSON.stringify(reply),	// JSON 문자열로 변환
			contentType: "application/json; charset=utf-8",
			success: function(result, status, xhr) {
				if(callback) {
					callback(result);
				}
			},
			error: function(xhr, status, er) {
				if(error) {
					error(er);
				}
			}
		});
	}
	
	// 댓글 가져오기
	function get(rno, callback, error) {
		$.get("/replies/" + rno + ".json", function(result) {
			if(callback) {
				callback(result);
			}
		}).fail(function(xhr, status, err) {
			if(error) {
				error();
			}
		});
	}
	
	function update(reply, callback, error) {
		console.log("RNO: " + reply.rno);
		
		$.ajax({
			type: 'put',
			url: '/replies/' + reply.rno,
			data: JSON.stringify(reply),
			contentType: "application/json; charset=utf-8",
			success: function(result, status, xhr) {
				if(callback) {
					callback(result);
				}
			},
			error: function(xhr, status, er) {
				if(error) {
					error(er);
				}
			}
		});
	}
	
	function remove(rno, callback, error) {
		$.ajax({
			type: 'delete',
			url: '/replies/' + rno,
			success: function(deleteResult, status, xhr) {
				if(callback) {
					callback(deleteResult);
				}
			},
			error: function(xhr, status, er) {
				if(error) {
					error(er);
				}
			}
		});
	}

	return {
		getList: getList,
		displayTime: displayTime,
		add: add,
		get: get,
		update: update,
		remove: remove
	};
})();