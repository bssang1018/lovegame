package com.gudi.best.logic.matching.mapper;

import com.gudi.best.dto.ProFileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface RestMapper {

    @Select("SELECT * FROM (SELECT * FROM proFile WHERE id!=#{param1})pro ORDER BY rand() DESC LIMIT 1")
    ProFileDTO matchingProfile(String loginId);
}
