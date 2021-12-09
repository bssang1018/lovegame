let boardNum = $("#boardNum").attr("boardNum");
let division = "loveBoard";
/* 댓글 등록 ajax */
$('#cmInsertBtn').on('click', function() {
	// 여기에 받을 변수 써주세용
	let cmContent = $('#commentContent').val();
	if (cmContent.trim() != "" && cmContent.length < 1000) {
		$('#commentContent').removeClass('is-invalid');
		$.ajax({
			type: "POST",//방식
			url: "/cm/cmInsert",//주소
			data: {
				content: cmContent,
				divisionNum: boardNum,
				division: division,
			},
			dataType: 'JSON',
			success: function(map) { //성공시
				commentList(map);
				$('#commentContent').val("");
			},
			error: function(e) { //실패시
				console.log(e);
			}
		});
	} else {
		$('#commentContent').addClass('is-invalid');
	}
})

/* 댓글 삭제 ajax */
$(document).on('click', '.cmDelBtn', function() {
	let cmNum = $(this).attr('cmNum');
	$.ajax({
		type: "POST",//방식
		url: "/cm/cmDelete",//주소
		data: {
			cmNum: cmNum,
			divisionNum: boardNum,
		},
		dataType: 'JSON',
		success: function(map) { //성공시
			commentList(map);
		},
		error: function(e) { //실패시
			console.log(e);
		}
	});
})

/* 댓글 페이지네이션 클릭시 ajax */
$(document).on('click', '.page-info', function() {
	let page = $(this).attr('page');
	$.ajax({
		type: "GET",
		url: "/cm/cmList" + "/" + page + "/" + boardNum,
		dataType: 'JSON',
		success: function(map) { //성공시
			commentList(map);
		},
		error: function(e) { //실패시
			console.log(e);
		}
	});
})

/* 댓글 업데이트 ajax */
$(document).on('click', '.cmUpdateBtn', function() {
	let cmNum = $(this).attr('cmNum');
	let cmUpdateContent = $(this).parent().prev().children('.cmUpdateContent').val();
	if (cmUpdateContent.trim() != "" && cmUpdateContent.length < 1000) {
		$.ajax({
			type: "POST",//방식
			url: "/cm/cmUpdate",//주소
			data: {
				divisionNum: boardNum,
				cmNum: cmNum,
				cmUpdateContent: cmUpdateContent,
			},
			dataType: 'JSON',
			success: function(map) { //성공시
				commentList(map);
			},
			error: function(e) { //실패시
				console.log(e);
			}
		});
	} else {
		$(this).parent().prev().children('.cmUpdateContent').addClass('is-invalid');
	}
})

/* 댓글 리스트 메서드 */
function commentList(map) {
	let content = '';
	/*댓글리스트 불러오기*/
	$.each(map.cmList, function(i, dto) {
		content += '<div class="listForm">'
		if (map.loginId !== dto.id) {
			content += '<div class = "dropdown-toggle-split d-inline-flex align-items-center" data-bs-toggle = "dropdown" style = "cursor: pointer" >'
			content += '<img onError = "src=/img/noImg.png" class = "rounded-circle" src=' + dto.imgPath + ' style = "width: 40px;height: 40px" >'
			content += '<div class= "fw-bold ms-1">' + dto.id + '</div>'
			content += '</div>'
			content += '<ul class="dropdown-menu mt-2">'
			content += '<li>'
			content += '<a class="dropdown-item" href="/member/proFileDetail?id=' + dto.id + '">프로필</a>'
			content += '</li>'
			content += '<li><a class="dropdown-item" href="/chat/chatCon?person=' + dto.id + '">러브톡</a></li>'
			content += '<li><a class="dropdown-item" href="/couple/applyCouple?id=' + dto.id + '">커플맺기신청</a></li>'
			content += '<li><a class="dropdown-item" href="/report?id=' + dto.id + '">신고하기</a></li>'
			content += '</ul>'
		} else {
			content += '<div class="dropdown-toggle-split d-inline-flex align-items-center">'
			content += '<img onError = "src=/img/noImg.png" class = "rounded-circle" src=' + dto.imgPath + ' style = "width: 40px;height: 40px" >'
			content += '<div class= "fw-bold ms-1">' + dto.id + '</div>'
			content += '</div>'
		}
		content += '<div class="lh-sm mt-1 ms-2">' + dto.content + '</div>'
		content += '<div class="d-flex justify-content-end">'
		content += '<div>'
		if (map.loginId !== dto.id) {
			content += '<a class="btn btn-danger rounded-0 btn-sm" href="">신고</a>'
		} else {
			content += '<a class="cmUpdateBtnForm btn btn-danger rounded-0 btn-sm">수정</a>'
			content += '<a cmNum="' + dto.cmNum + '" class="cmDelBtn btn btn-danger rounded-0 btn-sm ms-1">삭제</a>'
		}
		content += '</div>'
		content += '</div>'
		content += '<div class="d-flex justify-content-end text-muted mt-2" style = "font-size: 13px">'
		content += '작성일 :' + dto.date
		content += '</div>'
		content += '<hr/>'
		content += '</div>'
		content += '<div class="updateForm visually-hidden">'
		content += '<div class="form-floating flex-grow-1 px-2">'
		content += '<textarea class="cmUpdateContent form-control rounded-0" style="height: 100px; resize: none;">' + dto.content + '</textarea>'
		content += '<label>수정할 댓글을 작성하세요</label>'
		content += '<div class="invalid-feedback">1자 이상 1000자 미만 입력해주세요</div>'
		content += '</div>'
		content += '<div class="d-flex justify-content-end mt-2">'
		content += '<a cmNum="' + dto.cmNum + '" class="cmUpdateBtn btn btn-danger rounded-0 btn-sm">등록</a>'
		content += '<a class="cmUpdateCancel btn btn-danger rounded-0 btn-sm ms-1">취소</a>'
		content += '</div>'
		content += '<hr/>'
		content += '</div>'
	})
	$('#commentLists').empty();
	$('#commentLists').append(content);

	/* 페이지네이션 불러오기 욕나오네 */
	content = '';
	content += '<ul class="pagination justify-content-center">'
	if (map.startPage != 1) {
		content += '<li class="page-item">'
		content += '<a class="page-link page-info rounded-0" page="' + (map.startPage - 1) + '" aria-label="Previous" style="cursor:pointer;">'
		content += '<span aria-hidden="true">&laquo;</span>'
		content += '</a>'
		content += '</li>'
	}
	for (let i = map.startPage; i <= map.endPage; i++) {
		if (map.currPage !== i) {
			content += '<li class="page-item"><a style="cursor:pointer;" class="page-link page-info rounded-0" page="' + i + '" >' + i + '</a></li>'
		} else {
			content += '<li class="page-item active"><a class="page-link rounded-0">' + i + '</a></li>'
		}
	}
	if (map.totalPage !== map.endPage) {
		content += '<li class="page-item">'
		content += '<a class="page-link page-info rounded-0" page="' + (map.endPage + 1) + '" aria-label="Next" style="cursor:pointer;">'
		content += '<span aria-hidden="true">&raquo;</span>'
		content += '</a>'
		content += '</li>'
	}
	content += '</ul>'
	$('#paginationBox').empty();
	$('#paginationBox').append(content);
}

// 수정하기 폼 나타내기
$(document).on('click', '.cmUpdateBtnForm', function() {
	$(this).parents('.listForm').addClass('visually-hidden');
	$(this).parents('.listForm').next().removeClass('visually-hidden');
})
$(document).on('click', '.cmUpdateCancel', function() {
	$(this).parents('.updateForm').addClass('visually-hidden');
	$(this).parents('.updateForm').prev().removeClass('visually-hidden');
})