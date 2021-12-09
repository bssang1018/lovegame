package com.gudi.best.logic.loveBoard.mapper;

import com.gudi.best.dto.CmDTO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface CmMapper {
    @Select("SELECT COUNT(cmNum) FROM cm WHERE division='loveBoard' AND divisionNum=#{boardNum} AND delCheck='N'")
    int cmTotal(int boardNum);

    @Select(BoardSQL.CM_LIST)
    ArrayList<CmDTO> cmList(int start, int boardNum);

    @Insert("INSERT INTO cm(id,content,division,divisionNum) VALUES(#{id},#{content},#{division},#{divisionNum})")
    void cmInsert(HashMap<String, String> params);

    @Update("UPDATE cm SET content = #{cmUpdateContent} WHERE division='loveBoard' AND cmNum = #{cmNum}")
    void cmUpdate(HashMap<String, String> params);

    @Select(BoardSQL.CM_PAGE_CHECK)
    String cmPageCheck(String divisionNum, int cmNum, int start);

    @Delete("UPDATE cm SET delCheck = 'Y' WHERE cmNum=#{cmNum}")
    void cmDelete(String cmNum);
}
