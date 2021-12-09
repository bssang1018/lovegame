package com.gudi.best.logic.loveInfo.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.jdbc.SQL;

public class LoveInfoSQL {
	
	public String dataInsert(ArrayList<HashMap<String, Object>> resultMapList) {
		String sql = new SQL() {{
			INSERT_INTO("info");
			INTO_COLUMNS("readcount","contentid","firstimage","title","areacode", "mapy", 
					"mapx", "contenttypeid","cat1","cat2","cat3", "modifiedtime","mlevel","sigungucode");
			for (HashMap<String, Object> map : resultMapList) {
				INTO_VALUES("('"+map.get("readcount")+"'");
				INTO_VALUES("'"+map.get("contentid")+"'");
				INTO_VALUES("'"+ map.get("firstimage")+"'");
				if(map.get("title").toString().contains("'")) {
					INTO_VALUES("'"+map.get("title").toString().replace("'", "")+"'");
				}else {
					INTO_VALUES("'"+map.get("title")+"'");
				}
				INTO_VALUES("'"+ map.get("areacode")+"'");
				INTO_VALUES("'"+ map.get("mapy")+"'");
				INTO_VALUES("'"+ map.get("mapx")+"'");
				INTO_VALUES("'"+ map.get("contenttypeid")+"'");
				INTO_VALUES("'"+map.get("cat1")+"'");
				INTO_VALUES("'"+map.get("cat2")+"'");
				INTO_VALUES("'"+ map.get("cat3")+"'");
				INTO_VALUES("'"+map.get("modifiedtime")+"'");
				INTO_VALUES("'"+ map.get("mlevel")+"'");
				INTO_VALUES("'"+ map.get("sigungucode")+"')");
			}
		}}.toString();
		sql = sql.substring(0,sql.length()-1).replace("VALUES ((", "VALUES (");
		
		return sql;
	}
}
