package com.gudi.best.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ApiUtil {
    /**
     * 요청한 내용을 다른 서버로 전송하여 결과값을 받아 오는 메서드 작성자: 박한솔
     *
     * @param urls   ArrayList<String>
     * @param header HashMap<String,String>
     * @param params String
     * @param method String
     * @return 전송 결과 값 String
     */
    public static String sendSeverMsg(ArrayList<String> urls, HashMap<String, String> header, String params,
                                      String method) {
        String result = "";
        StringBuffer sb = new StringBuffer();
        HttpURLConnection con = null;
        BufferedReader reader = null; // 메시지를 받아올 Buffer 보조 스트림

        // 만약 파라메터가 있고 GET 방식을 왔다면...
        if (method.toUpperCase().equals("GET") && params != null) {
            urls.add("?" + params);// URL 뒤에 ? 와 파라메터를 붙인다.
        }

        for (String url : urls) {// arrayList 에 있는 url 을 하나씩 꺼내서
            sb.append(url);// StringBuffer 에 붙인다.(문자열을 붙을때 이 방법을 쓰면 객체 낭비를 막는다.)
            // 만약 위 방법이 싫다면 Strign url += url 방식으로 해도 된다.
        }
        try {
            URL url = new URL(sb.toString());// SttinrBuffer 에서 문자열을 꺼내 URL 객체로 만들고...
            // 이 객체를 이용해 대상 서버와 연결 한다.
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method.toUpperCase());// 어떤 메서드로 전송 하는지[GET | POST]
            con.setDoOutput(true);// 결과 값을 받아야 하는지?

            if (header != null && header.size() > 0) {// 헤더 값이 있는가?
                for (String key : header.keySet()) {// 있다면 있는 만큼 헤더값을 넣어 준다.
                    con.setRequestProperty(key, header.get(key));
                }
            }

            // Body 값이 있는가?(POST 인 경우 parameter 가 BODY 에 담겨 온다.)
            if (method.toUpperCase().equals("POST") && params != null) {
                con.getOutputStream().write(params.getBytes("UTF-8"));
                con.getOutputStream().flush();
            }

            int resultCode = con.getResponseCode(); // 결과 코드 받기
            System.out.println(("code : " + resultCode));// 200

            if (resultCode == 200) {// 성공일 경우... 성공 메시지를
                reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {// 실패일 경우...실패 메시지를
                reader = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }

            String readLine = "";
            sb = new StringBuffer();
            while ((readLine = reader.readLine()) != null) {
                sb.append(readLine);
            }


            result = sb.toString();// 최종 메시지(성공 메시지 | 실패 메시지)

            if (resultCode != 200) {
                System.out.println("Fail Message : " + result);
                result = "Fail Message : " + result;
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "Fail Message : " + e.toString();
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
