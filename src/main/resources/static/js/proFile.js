// 자기소개,이름 글자수 보여주기
$(document).ready(function () {
    $('.nameNum').text($("input[name='name']").val().length + "/100");
    $('#textareaNum').text($("textarea").text().length + "/1000");
})

// 버튼 전체 디자인 추가
$(".hobbyPickBtn").addClass('btn btn-outline-danger rounded-0 my-1');

// 프로필 이미지 클릭시 사진등록
$('.proFileImg').on('click', function () {
    $('#imgBtn').click();
})
// 프로필 등록버튼 클릭시 유효성 검사
$('#proFileInputBtn').on('click', function () {
    if ($("input[name='name']").val().trim().length >= 100) {
        alert("이름은 100자 이하입니다!");
    } else if ($('.intro').val().trim().length >= 1000) {
        alert("자기소개는 1000자 이하입니다!");
    } else {
        alert("프로필 수정 완료!");
        $('#proFileInputForm').submit();
    }
})

// 콘텐츠 수정 :: 사진 수정 시 이미지 미리보기 및 유효성 체크
$("input[name='proFileImg']").on('change', function () {
    let fileForm = /(.*?)\.(jpg|jpeg|png|gif|bmp)$/;
    if (this.files.length > 0) {
        if (!$(this).val().match(fileForm)) {
            alert("이미지 파일만 업로드 해주세요");
            $('.proFileImg').attr('src', '/img/noImg.png');
            return;
        }
        $('#imgViewArea').css({'display': ''});
        readURL(this);
    }
});

function readURL(input) {
    if (input.files && input.files[0]) {
        let reader = new FileReader();
        reader.onload = function (e) {
            $('.proFileImg').attr('src', e.target.result);
        }
        reader.readAsDataURL(input.files[0]);
    }
}


$("input[name='name']").on("change keyup paste input", function () {
    $('.nameNum').text($("input[name='name']").val().trim().length + "/100");
})
$('.intro').on("change keyup paste input", function () {
    $('#textareaNum').text($(this).val().trim().length + "/1000");
})