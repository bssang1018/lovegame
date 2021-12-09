package com.gudi.best.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewApiUtil {

	public static String sendSeverMsg(String url, HashMap<String, String> header, HashMap<String, String> param,
			String method) {

		// 헤더에 속성값 넣어주기
		HttpHeaders headers = new HttpHeaders();
		if (header != null && header.size() > 0) {// 헤더 값이 있는가?
			for (String key : header.keySet()) {// 있다면 있는 만큼 헤더값을 넣어 준다.
				headers.add(key, header.get(key));
			}
		}

		// 저 params 에다가 파라미터 삽입하기
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		if (param != null && param.size() > 0) { // 파라미터 값이 있는가?
			for (String key : param.keySet()) { // 있다면 있는 만큼 파라미터값을 넣어준다
				params.add(key, param.get(key));
			}
		}
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
		// 헤더랑 파라미터 담은녀석
		HttpEntity<MultiValueMap<String, String>> httpEntity = null;
		if (headers.size() > 0 && header != null) {
			httpEntity = new HttpEntity<>(params, headers);
		} else {
			httpEntity = new HttpEntity<>(params);
		}

		// 어떤 메서드인지 확인
		HttpMethod httpMethod = null;
		if (method.toUpperCase().equals("GET") && param != null) {
			httpMethod = HttpMethod.GET;
		}
		if (method.toUpperCase().equals("POST") && param != null) {
			httpMethod = HttpMethod.POST;
		}
		// response 받기
		RestTemplate restTemplate = new RestTemplate();
		/*
		 * restTemplate.getMessageConverters().add(0, new
		 * StringHttpMessageConverter(Charset.forName("UTF-8")));
		 */
		ResponseEntity<String> response = null;
		if (method.toUpperCase().equals("GET") && param != null) {
			response = restTemplate.exchange(uriBuilder.toUriString(), // url
					httpMethod, // method 종류
					httpEntity, // 파라미터와 헤더 담은값
					String.class);
		} else {
			response = restTemplate.exchange(url, // url
					httpMethod, // method 종류
					httpEntity, // 파라미터와 헤더 담은값
					String.class);
		}

		System.out.println(response);
		return response.getBody();
	}

	// 지역기반 관광정보 조회
	/*
	 * public static void main(String[] args) throws Exception { StringBuilder
	 * urlBuilder = new StringBuilder(
	 * "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList");
	 * URL urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") +
	 * "=서비스키"); Service Key urlBuilder.append("&" +
	 * URLEncoder.encode("ServiceKey","UTF-8") + "=" +
	 * URLEncoder.encode("인증키 (URL- Encode)", "UTF-8")); 공공데이터포털에서 발급받은 인증키
	 * urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" +
	 * URLEncoder.encode("1", "UTF-8")); 현재 페이지 번호 urlBuilder.append("&" +
	 * URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1",
	 * "UTF-8")); 한 페이지 결과 수 urlBuilder.append("&" +
	 * URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode("AppTest",
	 * "UTF-8")); 서비스명=어플명 urlBuilder.append("&" +
	 * URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode("ETC",
	 * "UTF-8")); IOS (아이폰), AND (안드로이드),WIN (원도우폰), ETC urlBuilder.append("&" +
	 * URLEncoder.encode("arrange","UTF-8") + "=" + URLEncoder.encode("A",
	 * "UTF-8")); (A=제목순, B=조회순, C=수정일순, D=생성일순) , 대표이미지가 반드시 있는 정렬 (O=제목순, P=조회순,
	 * Q=수정일순, R=생성일순) urlBuilder.append("&" + URLEncoder.encode("cat1","UTF-8") +
	 * "=" + URLEncoder.encode("", "UTF-8")); 대분류 코드 urlBuilder.append("&" +
	 * URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode("32",
	 * "UTF-8")); 관광타입(관광지, 숙박 등) ID urlBuilder.append("&" +
	 * URLEncoder.encode("areaCode","UTF-8") + "=" + URLEncoder.encode("4",
	 * "UTF-8")); 지역코드 urlBuilder.append("&" +
	 * URLEncoder.encode("sigunguCode","UTF-8") + "=" + URLEncoder.encode("4",
	 * "UTF-8")); 시군구코드(areaCode 필수) urlBuilder.append("&" +
	 * URLEncoder.encode("cat2","UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
	 * 중분류 코드(cat1필수) urlBuilder.append("&" + URLEncoder.encode("cat3","UTF-8") +
	 * "=" + URLEncoder.encode("", "UTF-8")); 소분류 코드(cat1,cat2필수)
	 * urlBuilder.append("&" + URLEncoder.encode("listYN","UTF-8") + "=" +
	 * URLEncoder.encode("Y", "UTF-8")); 목록 구분 (Y=목록, N=개수) urlBuilder.append("&" +
	 * URLEncoder.encode("modifiedtime","UTF-8") + "=" + URLEncoder.encode("",
	 * "UTF-8")); 콘텐츠 수정일 URL url = new URL(urlBuilder.toString());
	 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	 * conn.setRequestMethod("GET"); conn.setRequestProperty("Content-type",
	 * "application/json"); System.out.println("Response code: " +
	 * conn.getResponseCode()); BufferedReader rd; if(conn.getResponseCode() >= 200
	 * && conn.getResponseCode() <= 300) { rd = new BufferedReader(new
	 * InputStreamReader(conn.getInputStream())); } else { rd = new
	 * BufferedReader(new InputStreamReader(conn.getErrorStream())); } StringBuilder
	 * sb = new StringBuilder(); String line; while ((line = rd.readLine()) != null)
	 * { sb.append(line); } rd.close(); conn.disconnect();
	 * System.out.println(sb.toString()); }
	 */

	// 검색용 API
	public static String searchApi(String keyword) throws Exception {
		StringBuilder urlBuilder = new StringBuilder(
				"http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword"); /* URL */
		urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "="
				+ URLEncoder.encode(
						"YRc1yhIuj+SEq19P4LqBXRmFAtACpby0jiZKx+pSOyMnQ+5EX18dxJ+heYZ+4Ls/hYTVS6+FqoIZDjj2XmsmRg==",
						"UTF-8")); /* 공공데이터에서 발급받은 인증키 */
		urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "="
				+ URLEncoder.encode("AppTest", "UTF-8")); /* 서비스명=어플명 */
		urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "="
				+ URLEncoder.encode("ETC", "UTF-8")); /* IOS (아이폰), AND(안드로이드), ETC */
		urlBuilder.append(
				"&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 현재 페이지 번호 */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
				+ URLEncoder.encode("10", "UTF-8")); /* 한 페이지 결과 수 */
		urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "="
				+ URLEncoder.encode("Y", "UTF-8")); /* 목록 구분 (Y=목록, N=개수) */
		urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("A",
				"UTF-8")); /*
							 * (A=제목순, B=조회순, C=수정일순, D=생성일순) 대표이미지가 반드시 있는 정렬(O=제목순, P=조회순, Q=수정일순, R=생성일순)
							 */
		urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "="
				+ URLEncoder.encode("12", "UTF-8")); /* 관광타입(관광지, 숙박 등) ID */
		urlBuilder
				.append("&" + URLEncoder.encode("areaCode", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /* 지역코드 */
		urlBuilder.append("&" + URLEncoder.encode("sigunguCode", "UTF-8") + "="
				+ URLEncoder.encode("", "UTF-8")); /* 시군구코드(areaCode 필수) */
		urlBuilder.append("&" + URLEncoder.encode("cat1", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /* 대분류 코드 */
		urlBuilder.append(
				"&" + URLEncoder.encode("cat2", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")); /* 중분류 코드(cat1필수) */
		urlBuilder.append("&" + URLEncoder.encode("cat3", "UTF-8") + "="
				+ URLEncoder.encode("", "UTF-8")); /* 소분류 코드(cat1,cat2필수) */
		urlBuilder.append("&" + URLEncoder.encode("keyword", "UTF-8") + "="
				+ URLEncoder.encode(keyword, "UTF-8")); /* 검색 요청할 키워드 (국문=인코딩 필요) */
		urlBuilder.append(
				"&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /* json변환 */
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		System.out.println(sb.toString());
		return sb.toString();
	}

	// 관광코드, 지역코드, 페이지
	public static ArrayList<HashMap<String, Object>> culList(String tour, String code, String page) throws Exception {
		HashMap<String, String> header = new HashMap<String, String>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("ServiceKey",
				"YRc1yhIuj+SEq19P4LqBXRmFAtACpby0jiZKx+pSOyMnQ+5EX18dxJ+heYZ+4Ls/hYTVS6+FqoIZDjj2XmsmRg==");
		params.put("MobileApp", "culList");
		params.put("pageNo", page);
		params.put("numOfRows", "15");
		params.put("MobileOS", "ETC");
		params.put("areaCode", code);
		params.put("contentTypeId", tour);
		params.put("_type", "json");
		String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList";
		String result = sendSeverMsg(url, header, params, "GET");
		ObjectMapper mapper = new ObjectMapper();

		HashMap<String, Object> map = mapper.readValue(result, HashMap.class);
		result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map.get("response"));
		HashMap<String, Object> map2 = mapper.readValue(result, HashMap.class);
		result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map2.get("body"));
		HashMap<String, Object> map3 = mapper.readValue(result, HashMap.class);
		result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map3.get("items"));
		HashMap<String, Object> map4 = mapper.readValue(result, HashMap.class);
		String totalCount = String.valueOf(map3.get("totalCount"));
		result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map4.get("item"));
		TypeReference<ArrayList<HashMap<String, Object>>> typeRef = new TypeReference<ArrayList<HashMap<String, Object>>>() {
		};
		HashMap<String, Object> good = new HashMap<String, Object>();
		good.put("totalCount", totalCount);
		ArrayList<HashMap<String, Object>> list = mapper.readValue(result, typeRef);
		list.add(good);
		return list;
	}

	public static HashMap<String, Object> culList(String contentid, String contenttypeid) throws Exception {

		HashMap<String, String> header = new HashMap<String, String>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("ServiceKey",
				"YRc1yhIuj+SEq19P4LqBXRmFAtACpby0jiZKx+pSOyMnQ+5EX18dxJ+heYZ+4Ls/hYTVS6+FqoIZDjj2XmsmRg==");
		params.put("MobileApp", "culList");
		params.put("MobileOS", "ETC");
		// params.put("contentTypeId", contenttypeid);
		params.put("contentId", contentid);
		params.put("_type", "json");
		params.put("numOfRows", "1");
		params.put("defaultYN", "Y");
		params.put("firstImageYN", "Y");
		params.put("addrinfoYN", "Y");
		params.put("mapinfoYN", "Y");
		params.put("overviewYN", "Y");
		String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon";
		String result = sendSeverMsg(url, header, params, "GET");

		ObjectMapper mapper = new ObjectMapper();

		HashMap<String, Object> map = mapper.readValue(result, HashMap.class);
		result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map.get("response"));
		HashMap<String, Object> map2 = mapper.readValue(result, HashMap.class);
		result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map2.get("body"));
		HashMap<String, Object> map3 = mapper.readValue(result, HashMap.class);
		result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map3.get("items"));
		HashMap<String, Object> map4 = mapper.readValue(result, HashMap.class);
		result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map4.get("item"));
		TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
		};
		HashMap<String, Object> resultMap = mapper.readValue(result, typeRef);

		return resultMap;
	}
}
