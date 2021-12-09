package com.gudi.best.util;

import java.util.HashMap;

public class PageNation {
    /* 페이지네이션 jsp버전 복붙 고 */
    /*<ul class="pagination justify-content-center">
            <c:if test="${map.startPage ne 1}">
                <li class="page-item">
                <a class="page-link" href="?page=${map.startPage-1}" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
                </a>
                </li>
            </c:if>
            <c:forEach var="i" begin="${map.startPage}" end="${map.endPage}">
                <c:if test="${i ne map.currPage}">
                    <li class="page-item"><a class="page-link" href="?page=${i}">${i}</a></li>
                </c:if>
                <c:if test="${i eq map.currPage}">
                    <li class="page-item active"><a class="page-link">${i}</a></li>
                </c:if>
            </c:forEach>
            <c:if test="${map.totalPage ne map.endPage}">
                <li class="page-item">
                <a class="page-link" href="?page=${map.endPage+1}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
                </a>
                </li>
            </c:if>
    </ul>*/

    // 타임리프 버전
  /* <ul class="pagination justify-content-center">
        <li class="page-item" th:if="${map.startPage ne 1}">
            <a aria-label="Previous" class="page-link" th:href="@{/list/{page}(page=${map.startPage - 1})}">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
        <th:block th:each="num: ${#numbers.sequence(map.startPage,map.endPage)}">
            <li th:if="${num ne map.currPage}" class="page-item"><a th:text="${num}" class="page-link"
                                                                    th:href="@{/list/{page}(page=${num})}"></a>
            </li>
            <li th:if="${num eq map.currPage}" class="page-item active"><a th:text="${num}" class="page-link"></a>
            </li>
        </th:block>
        <li class="page-item" th:if="${map.totalPage ne map.endPage}">
            <a class="page-link" th:href="@{/list/{page}(page=${map.endPage + 1})}" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </ul>*/
    /**
     * 페이지네이션 데이터 보내줌
     * 작성자: 박한솔
     *
     * @param page       int 현재페이지
     * @param pagePerCnt int 페이지당 데이터 갯수
     * @param total      int 데이터 총 갯수
     * @return 전송 결과 값 HashMap<String,Object>
     */
    // 페이지 네이션 map 반환
    public static HashMap<String, Object> pagination(int page, int pagePerCnt, int total) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        // 한블럭당 페이지 갯수
        int pageLength = 5;
        // 블럭 인덱스
        int currentBlock = page % pageLength == 0 ? page / pageLength : (page / pageLength) + 1;
        // 시작페이지
        int startPage = (currentBlock - 1) * pageLength + 1;
        // 끝페이지
        int endPage = startPage + pageLength - 1;
        // 총 게시글 수에 나올 페이지수 나눠서 짝수면 나눠주고 홀수면 +1
        int totalPages = total % pagePerCnt == 0 ? total / pagePerCnt : (total / pagePerCnt) + 1;
        if (totalPages == 0) {
            totalPages = 1;
        }
        // 끝지점을 맨 마지막 페이지로 지정
        if (endPage > totalPages) {
            endPage = totalPages;
        }
        map.put("totalPage", totalPages);
        map.put("currPage", page);
        map.put("pageLength", pageLength);
        map.put("startPage", startPage);
        map.put("endPage", endPage);
        return map;
    }

}
