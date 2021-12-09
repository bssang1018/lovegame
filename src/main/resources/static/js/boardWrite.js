// 글쓰기
$(document).on('click', '#boardWriteBtn', function () {
    let check = 0;
    $.each(uploadFiles, function (i, file) {
        if (file != 'disable') {
            check++;
        }
    });
    if (check > 10) {
        alert('이미지는 9장까지만 등록가능합니다!')
    } else {
        if (check > 0) {
            let title = $('#title').val().trim();
            let contents = $('#contents').val().trim();
            if ($('#title').hasClass('is-valid') && $('#contents').hasClass('is-valid')) {
                LoadingWithMask();
                let formData = new FormData();
                // 파일 넣기
                $.each(uploadFiles, function (i, file) {
                    if (file != 'disable') {
                        formData.append('files', file, file.name);
                    }
                });
                // 파라미터 넣기
                formData.append("title", title);
                formData.append("content", contents);
                $.ajax({
                    url: '/loveBoard/boardWrite',
                    enctype: 'multipart/form-data',
                    data: formData,
                    type: 'post',
                    contentType: false,
                    processData: false,
                    success: function (suc) {
                        closeLoadingWithMask();
                        alert("등록 성공!")
                        location.href = '/loveBoard/boardDetail/' + suc + '/1/all'
                    }
                });
            } else {
                if (title == "") {
                    $('#title').nextAll('.invalid-feedback').text('한글자 이상 작성해주세요');
                    $('#title').attr('class', 'form-control is-invalid');
                }
                if (contents == "") {
                    $('#contents').nextAll('.invalid-feedback').text('한글자 이상 작성해주세요');
                    $('#contents').attr('class', 'form-control is-invalid');
                }
            }
        } else {
            alert("이미지를 등록해주세요!")
        }
    }
})

$("#fileSecond").on("change", function (e) {
    let fileForm = /(.*?)\.(jpg|jpeg|png|gif|bmp)$/;
    let files = $(this)[0].files;
    fileCheck(files);
})