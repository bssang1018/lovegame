package com.gudi.best.logic.matching.mapper;

import com.gudi.best.dto.ProFileDTO;
import com.gudi.best.logic.matching.dto.MatchingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface MatchingMapper {

    @Select("SELECT hobby FROM proFile WHERE id=#{loginId}")
    String getHobby(String loginId);

    @Select("SELECT * FROM proFile WHERE id!=#{loginId} AND hobby IS NOT NULL")
    ArrayList<ProFileDTO> proFileList(String loginId);

    @Select("SELECT * FROM proFile WHERE id!=#{loginId} ORDER BY rand() LIMIT 4")
    ArrayList<ProFileDTO> randomList(String loginId);
}
