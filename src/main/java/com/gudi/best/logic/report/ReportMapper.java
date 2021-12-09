package com.gudi.best.logic.report;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface ReportMapper {

    @Insert("INSERT INTO report(id,reporter,status,reason) VALUES(#{reportId},#{reporter},'N',#{reason})")
    void report(HashMap<String, String> params);
}
