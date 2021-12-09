if ($('.good').attr('goodCheck') == "true") {
    $('.good').attr("class", "good btn btn-danger rounded-0");
}
$(document).on('click', '.good', function () {
    let divisionNum = $('.divisionNum').attr("divisionNum");
    let goodDivision = $('.divisionNum').attr("goodDivision");
    $.ajax({
        type: "GET",//방식
        url: "/good/goodData/" + divisionNum + "/" + goodDivision,
        dataType: 'JSON',
        success: function (map) { //성공시
            if (map.check == false) {
                // 좋아요 안눌린상태
                $(".good").attr("goodCheck", "false");
                $(".good").attr("class", "good btn btn-outline-danger rounded-0");
            } else {
                // 좋아요 눌린상태
                $(".good").attr("goodCheck", "true");
                $(".good").attr("class", "good btn btn-danger rounded-0");
            }
            $('#goodCount').text('좋아요 ' + map.goodCount);
        },
        error: function (e) { //실패시
            console.log(e);
        }
    })
})