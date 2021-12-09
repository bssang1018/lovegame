package com.gudi.best.logic.loveBoard.mapper;

import com.gudi.best.dto.GoodDTO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface GoodMapper {

    @Select("SELECT COUNT(goodNum) FROM good WHERE divisionNum = #{param1}")
    int goodCount(String divisionNum);

    @Select("SELECT id FROM good WHERE divisionNum=#{param1} AND id=#{param2}")
    String goodCheck(String divisionNum, String id);

    @Delete("DELETE FROM good WHERE divisionNum=#{param1} AND id=#{param2} AND division=#{param3}")
    void goodDelete(String boardNum, String id, String division);

    @Insert("INSERT INTO good(divisionNum,id,division) VALUES(#{param1},#{param2},#{param3})")
    void goodInsert(String boardNum, String id, String division);

    @SelectProvider(type = GoodSQL.class, method = "goodChoice")
    ArrayList<GoodDTO> goodList(String loginId, String division);
}
