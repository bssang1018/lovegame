$(document).ready(function () {
    $('#contentNum').text($("#contents").text().length + "/2000");
    $('#titleNum').text($("#title").val().length + "/100");
})
// 이미지 선택 삭제
$(document).on('click', '.imgDel', function () {
    $(this).remove();
    let photoNum = $(this).children('.imgList').attr('photoNum');
    let newFileName = $(this).children('.imgList').attr('newFileName');
    let boardNum = $(this).children('.imgList').attr('boardNum');
    $.ajax({
        url: '/loveBoard/imgDel',
        data: {
            photoNum: photoNum,
            newFileName: newFileName,
            boardNum: boardNum
        },
        dataType: 'JSON',
        type: 'post',
        success: function (suc) {
            $('#photoCountText').text('현재 내가 등록한 이미지 ' + suc + '장');
            $('#boardUpdateBtn').attr('photoCount', suc);
        }
    });
})
// 글수정 어차피 똑같다
$(document).on('click', '#boardUpdateBtn', function () {
    let title = $('#title').val().trim();
    let contents = $('#contents').val().trim();
    let boardNum = $(this).attr('boardNum');
    let page = $(this).attr('page');
    if ($('#title').val().trim() != "" && $('#contents').val().trim() != "") {
        LoadingWithMask();
        let formData = new FormData();
        // 파라미터 넣기
        formData.append("title", title);
        formData.append("content", contents);
        formData.append("boardNum", boardNum);
        $.ajax({
            url: '/loveBoard/boardUpdate',
            enctype: 'multipart/form-data',
            data: formData,
            type: 'post',
            contentType: false,
            processData: false,
            success: function (suc) {
                closeLoadingWithMask();
                alert("수정 성공!")
                location.href = '/loveBoard/boardDetail/' + boardNum + '/' + page + '/all'
            }
        });
    } else {
        if (title === "") {
            $('#title').nextAll('.invalid-feedback').text('한글자 이상 작성해주세요');
            $('#title').attr('class', 'form-control is-invalid');
        }
        if (contents == "") {
            $('#contents').nextAll('.invalid-feedback').text('한글자 이상 작성해주세요');
            $('#contents').attr('class', 'form-control is-invalid');
        }
    }

})