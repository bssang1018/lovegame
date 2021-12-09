package com.gudi.best.logic.myInfo.mapper;

import org.apache.ibatis.jdbc.SQL;

import java.util.HashMap;

public class MyInfoSQL {
    public static final String PROFILE_PHOTO_INSERT =
            "INSERT INTO photo(id,division,oriFileName,newFileName,path) "
                    + "VALUES(#{id},'proFile',#{oriFileName},#{newFileName},#{path})";

    public String insertNullCheck(HashMap<String, Object> map) {
        return new SQL() {{
            INSERT_INTO("proFile");
            INTO_COLUMNS("id", "imgPath");
            INTO_VALUES("#{id}", "#{path}");
            if (!map.get("hobby").equals("")) {
                INTO_COLUMNS("hobby");
                INTO_VALUES("#{hobby}");
            }
            if (!map.get("intro").equals("")) {
                INTO_COLUMNS("intro");
                INTO_VALUES("#{intro}");
            }
            if (!map.get("name").equals("")) {
                INTO_COLUMNS("name");
                INTO_VALUES("#{name}");
            }
            if (!map.get("age").equals("")) {
                INTO_COLUMNS("age");
                INTO_VALUES("#{age}");
            }

        }}.toString();
    }

    public String updateNullCheck(HashMap<String, Object> map) {
        return new SQL() {{
            UPDATE("proFile");
            if (!map.get("path").equals("")) {
                SET("imgPath = #{path}");
            }
            if (!map.get("hobby").equals("")) {
                SET("hobby = #{hobby}");
            }
            if (!map.get("intro").equals("")) {
                SET("intro = #{intro}");
            } else {
                SET("intro = null");
            }
            if (!map.get("age").equals("")) {
                SET("age = #{age}");
            }
            if (!map.get("name").equals("")) {
                SET("name = #{name}");
            }
            WHERE("id = #{id}");
        }}.toString();
    }
}
