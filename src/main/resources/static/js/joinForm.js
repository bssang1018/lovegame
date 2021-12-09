// 중복확인
$('#idCheckBtn').on('click', function () {
    let id = $('#id').val();
    if (id == "") {
        $('#id').nextAll('.invalid-feedback').text("1자이상 입력해주세요");
        $('#id').attr("class", "form-control nullCheck is-invalid");
    } else {
        $.ajax({
            type: "POST",
            url: "/member/idCheck",
            data: {id: id},
            dataType: 'JSON',
            success: function (suc) {
                if (suc) {
                    $('#id').attr('readonly', 'readonly');
                    $('#id').nextAll('.valid-feedback').text("사용해도 좋습니다");
                    $('#id').attr("class", "form-control nullCheck is-valid");
                } else {
                    $('#id').nextAll('.invalid-feedback').text("8자 이상 혹은 중복확인을 다시 해주세요");
                    $('#id').attr("class", "form-control nullCheck is-invalid");
                }
            },
            error: function (data) {
                console.log(data);
            }
        })
    }
})
// 유효성 검사
$('#joinBtn').on('click', function () {
    let check = true;
    $('.nullCheck').each(function (index, el) {
        if ($(this).hasClass('is-valid')) {
            return true;
        } else {
            check = false;
            if ($(this).attr("id") == "id") {
                $(this).nextAll('div.invalid-feedback').text("중복확인 해주세요");
            } else if ($(this).attr("id") == "email") {
                $(this).nextAll('div.invalid-feedback').text("이메일 인증 해주세요");
            } else if ($(this).attr("id") == "pw") {
                $(this).nextAll('div.invalid-feedback').text("8자 이상 입력해주세요");
            } else {
                $(this).nextAll('div.invalid-feedback').text("1자이상 입력해주세요");
            }
            $(this).attr("class", "form-control is-invalid nullCheck");
            return false;
        }
    })
    if (check) {
        $("#joinForm").submit();
    }
})
$("#pw").on("change keyup paste input", function () {
    if ($(this).val().length >= 8) {
        $(this).attr('class', 'form-control nullCheck is-valid');
        $(this).nextAll('div.valid-feedback').text('사용 가능합니다');
    } else {
        $(this).attr('class', 'form-control nullCheck is-invalid');
        $(this).nextAll('div.invalid-feedback').text('8자 이상 입력해주세요');
    }
    if ($(this).val().length >= 100) {
        $(this).attr('class', 'form-control nullCheck is-invalid');
        $(this).nextAll('div.invalid-feedback').text('100자 미만으로 입력해주세요');
    }
});
$("#pwCheck").on("change keyup paste input", function () {
    if ($(this).val() == $("#pw").val()) {
        $(this).attr('class', 'form-control nullCheck is-valid');
        $(this).nextAll('div.valid-feedback').text('동일합니다');
    } else {
        $(this).attr('class', 'form-control nullCheck is-invalid');
        $(this).nextAll('div.invalid-feedback').text('다시 입력해주세요');
    }
});
// 이메일 인증
$("#emailSendBtn").on('click', function () {
    let emailSender = $("#emailSender").val();
    $("#emailSender").attr("class", "form-control is-valid");
    $("#emailSender").nextAll('.valid-feedback').text("전송중입니다 잠시 기다려주세요..");
    $(this).attr("disabled", "disabled");
    $.ajax({
        type: "GET",
        url: "/member/emailCheck",
        data: {
            email: emailSender
        },
        dataType: 'JSON',
        success: function (suc) {
            if (suc) {
                $("#emailSendBtn").removeAttr("disabled");
                $("#emailSender").nextAll('.valid-feedback').text("전송되었습니다");
                $("#emailSender").attr("class", "form-control is-valid");
            } else {
                $("#emailSendBtn").removeAttr("disabled");
                $("#emailSender").attr("class", "form-control is-invalid");
            }
        },
        error: function (data) {
            console.log(data);
        }
    })
});
// 이메일 인증 번호 확인
$('#emailNumCheckBtn').on('click', function () {
    let code = $('#code').val();
    $.ajax({
        type: "GET",
        url: "/member/codeCheck",
        data: {
            code: code
        },
        dataType: 'JSON',
        success: function (map) {
            if (map.email == "없음") {
                $('#code').addClass("is-invalid");
            } else {
                $('#email').val(map.email);
                $('#email').attr('readonly', 'readonly');
                $('#email').attr("class", "form-control nullCheck is-valid");
                $('#close').click();
            }
        },
        error: function (data) {
            console.log(data);
        }
    })
})