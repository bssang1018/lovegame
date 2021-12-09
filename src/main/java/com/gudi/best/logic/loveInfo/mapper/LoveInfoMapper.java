package com.gudi.best.logic.loveInfo.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoveInfoMapper {

	@InsertProvider(type=LoveInfoSQL.class,method="dataInsert")
	void dataInsert(ArrayList<HashMap<String, Object>> resultMapList);

}
