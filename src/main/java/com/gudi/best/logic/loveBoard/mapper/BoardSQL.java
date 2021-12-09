package com.gudi.best.logic.loveBoard.mapper;

import org.apache.ibatis.jdbc.SQL;

public class BoardSQL {
    public static final String BOARD_PHOTO_INSERT =
            "INSERT INTO photo(id,division,oriFileName,newFileName,path,divisionNum)" +
                    "VALUES(#{id},'loveBoard',#{oriFileName},#{newFileName},#{path},#{divisionNum})";
    public static final String CM_PAGE_CHECK = "SELECT e.id FROM " +
            "(SELECT * FROM cm WHERE divisionNum = #{param1} AND delCheck='N' ORDER BY cmNum DESC LIMIT #{param3},8) e " +
            "WHERE cmNum = #{param2}";
    public static final String CM_LIST = "SELECT *,(SELECT imgPath FROM proFile WHERE cm.id=id)imgPath " +
            "FROM cm WHERE division='loveBoard' AND divisionNum=#{param2} AND delCheck='N' " +
            "ORDER BY cmNum DESC LIMIT #{param1},8";

    String goodCount = new SQL() {{
        SELECT("COUNT(goodNum)");
        FROM("good");
        WHERE("boardNum=divisionNum");
    }}.toString();
    String photoMIN = new SQL() {{
        SELECT("path");
        FROM("photo");
        WHERE("boardNum=divisionNum");
        ORDER_BY("photoNum");
        LIMIT(1);
    }}.toString();

    public String boardPickSQL(String con, String option) {
        return new SQL() {{
            SELECT("*");
            SELECT("(" + goodCount + ")goodCount");
            FROM("board");
            if (!option.equals("없음")) {
                if (option.equals("제목+내용")) {
                    WHERE("title LIKE CONCAT('%',#{param2},'%')");
                    OR();
                    WHERE("content LIKE CONCAT('%',#{param2},'%')");
                } else if (option.equals("제목")) {
                    WHERE("title LIKE CONCAT('%',#{param2},'%')");
                } else if (option.equals("내용")) {
                    WHERE("content LIKE CONCAT('%',#{param2},'%')");
                } else {
                    WHERE("id LIKE CONCAT('%',#{param2},'%')");
                }
            }
            ORDER_BY(con);
            ORDER_BY("boardHit DESC");
            LIMIT("#{param1},6");
        }}.toString();
    }

    public String proFileBoard(int start, String id) {
        String userBoardList = new SQL() {{
            SELECT("*");
            SELECT("(" + goodCount + ")goodCount");
            FROM("board");
            WHERE("id=#{param2}");
            ORDER_BY("boardNum DESC");
            LIMIT("#{param1},6");
        }}.toString();
        return new SQL() {{
            SELECT("boardNum,boardList.id,content,title,date,boardHit,goodCount,imgPath");
            SELECT("(" + photoMIN + ")path");
            FROM("(" + userBoardList + ")boardList");
            LEFT_OUTER_JOIN("proFile ON boardList.id=proFile.id");
        }}.toString();
    }

    public String boardPick(int start, String division) {
        return new SQL() {{
            SELECT("boardNum,boardList.id,content,title,date,boardHit,goodCount,imgPath");
            SELECT("(" + photoMIN + ")path");
            if (division.equals("all")) {
                FROM("(" + boardPickSQL("boardNum DESC", "없음") + ")boardList");
            } else {
                FROM("(" + boardPickSQL("goodCount DESC", "없음") + ")boardList");
            }
            LEFT_OUTER_JOIN("proFile ON boardList.id=proFile.id");
        }}.toString();
    }

    public String boardSearch(int start, String searchText, String option) {
        return new SQL() {{
            SELECT("boardNum,boardList.id,content,title,date,boardHit,goodCount,imgPath");
            SELECT("(" + photoMIN + ")path");
            FROM("(" + boardPickSQL("boardNum DESC", option) + ")boardList");
            LEFT_OUTER_JOIN("proFile ON boardList.id=proFile.id");
        }}.toString();
    }

    public String boardSearchCount(String searchText, String option) {
        return new SQL() {{
            SELECT("COUNT(boardNum)");
            FROM("board");
            if (option.equals("제목+내용")) {
                WHERE("title LIKE CONCAT('%',#{param1},'%')");
                OR();
                WHERE("content LIKE CONCAT('%',#{param1},'%')");
            } else if (option.equals("제목")) {
                WHERE("title LIKE CONCAT('%',#{param1},'%')");
            } else if (option.equals("내용")) {
                WHERE("content LIKE CONCAT('%',#{param1},'%')");
            } else {
                WHERE("id LIKE CONCAT('%',#{param1},'%')");
            }
        }}.toString();
    }
}
