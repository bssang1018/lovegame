package com.gudi.best.util;


import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class Scheduler {
    // 스케줄러 사용할 클래스 사용할 메서드는 여기에
//	@Scheduled(cron = "0 0 0 1 * *")
    public String dataInsert(int i) throws Exception {
        /*HashMap<String, String> header = new HashMap<String, String>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("ServiceKey", "J0GWcsmSKRep08rNleYyFbyLzNo8BcbkWmzsOxqFeJIyHVD3456iyVy+ULCnhVw5y9WsuzCa4ZjugOCCfA0JOA==");
        params.put("MobileOS", "ETC");
        params.put("MobileApp", "test");
        params.put("_type", "json");
        String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword";
        String response = NewApiUtil.sendSeverMsg(url, header, params, "GET");
        System.out.println(response);*/
        return NewApiUtil.searchApi("서울");
    }
}
