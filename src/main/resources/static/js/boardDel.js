$('#loveBoardBtn').attr("aria-expanded", "true");
$('#boardDel').on('click', function () {
    let boardNum = $(this).attr('boardNum');
    let confirmFlag = confirm("작성된 게시글,댓글,이미지 모두 복구가 불가합니다. 정말 게시글을 삭제하시겠습니까? ");
    if (confirmFlag) {
        LoadingWithMask();
        $.ajax({
            url: '/loveBoard/boardDelete/' + boardNum,
            type: 'get',
            success: function (suc) {
                alert('삭제완료');
                location.href = "/loveBoard/list/all/1"
            }
        });
    }
})