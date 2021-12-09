package com.gudi.best.logic.loveBoard.mapper;

import org.apache.ibatis.jdbc.SQL;

public class GoodSQL {

    public String goodChoice(String loginId, String division) {
        return new SQL() {{
            if (division.equals("my")) {
                SELECT("goodNum,proFile.id,dates,intro,imgPath,name,age");
                FROM("good");
                LEFT_OUTER_JOIN("proFile ON good.divisionNum=proFile.id");
                WHERE("division='people'");
                WHERE("good.id=#{param1}");
                ORDER_BY("goodNum DESC");
            } else {
                SELECT("goodNum,proFile.id,dates,intro,imgPath,name,age");
                FROM("good");
                LEFT_OUTER_JOIN("proFile ON good.id=proFile.id");
                WHERE("division='people'");
                WHERE("good.divisionNum=#{param1}");
                ORDER_BY("goodNum DESC");
            }
        }}.toString();
    }
}
