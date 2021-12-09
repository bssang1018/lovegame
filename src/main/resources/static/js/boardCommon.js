$('#loveBoardBtn').attr("aria-expanded", "true");
// 글자수 체킹 해주는거 및 유효성
$('#contents').on("change keyup paste input", function () {
    if ($(this).val().trim().length > 0) {
        $(this).attr("class", "form-control is-valid");
    } else {
        $(this).nextAll('.invalid-feedback').text('한글자 이상 작성해주세요');
        $(this).attr("class", "form-control is-invalid");
    }
    if ($(this).val().trim().length >= 2000) {
        $(this).nextAll('.invalid-feedback').text('2000 글자 미만으로 작성해주세요');
        $(this).attr("class", "form-control is-invalid");
    }
    $('#contentNum').text($(this).val().trim().length + "/2000");
})
$('#title').on("change keyup paste input", function () {
    if ($(this).val().trim().length > 0) {
        $(this).attr("class", "form-control is-valid");
    } else {
        $(this).nextAll('.invalid-feedback').text('한글자 이상 작성해주세요');
        $(this).attr("class", "form-control is-invalid");
    }
    if ($(this).val().trim().length >= 100) {
        $(this).nextAll('.invalid-feedback').text('100 글자 미만으로 작성해주세요');
        $(this).attr("class", "form-control is-invalid");
    }
    $('#titleNum').text($(this).val().trim().length + "/100");
})

let uploadFiles = [];
// 드래그 이벤트 주기
$(document).on("dragleave", "#drop", function (e) { //드래그 요소가 나갔을때
    $(this).removeClass('bg-light');
    e.stopPropagation();
    e.preventDefault();
}).on("dragover", "#drop", function (e) { // 드래그 요소가 들어왔을때
    $(this).addClass('bg-light');
    e.stopPropagation();
    e.preventDefault();
}).on('drop', "#drop", function (e) { //드래그한 항목을 떨어뜨렸을때
    e.preventDefault();
    $(this).removeClass('bg-light');
    let files = e.originalEvent.dataTransfer.files;
    fileCheck(files);
});

// 이미지 삭제
$(document).on("click", ".close", function (e) {
    let idx = $(this).attr('data-idx');
    uploadFiles[idx] = 'disable'; //삭제시 자기 배열에 disable 첨가
    $(this).remove(); //이미지 삭제
});

// 이미지 띄우기라는데 이해안됨 걍 복붙 ㅅㄱ
function preview(file, idx) {
    let reader = new FileReader();
    reader.onload = (function (f, idx) {
        return function (e) {
            let content = "";
            content += '<div class="d-flex flex-column close" data-idx="' + idx + '" style="cursor: pointer">';
            content += '<div class="thumb">';
            content += '<img style="width: 150px;height: 150px;" class="img-fluid img-count" src="' + e.target.result + '" title="' + escape(f.name) + '"/>';
            content += '</div>';
            content += '<div class="text-center">';
            content += '<i class="bi bi-x-lg fs-3"></i>';
            content += '</div>';
            content += '</div>';
            $("#thumbnails").append(content);
        };
    })(file, idx);
    reader.readAsDataURL(file);
}

function fileCheck(files) {
    let fileForm = /(.*?)\.(jpg|jpeg|png|gif|bmp)$/;
    for (let i = 0; i < files.length; i++) {
        let file = files[i];
        if (file.name.match(fileForm)) {
            $('#imgText').remove();
            let size = uploadFiles.push(file); //업로드 목록에 추가
            preview(file, size - 1); //미리보기 만들기
        } else {
            alert("이미지 파일만 등록해주세요!")
            $('#thumbnails').empty();
            return;
        }
    }
}

