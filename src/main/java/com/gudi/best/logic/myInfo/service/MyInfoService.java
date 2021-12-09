package com.gudi.best.logic.myInfo.service;

import com.gudi.best.dto.ProFileDTO;
import com.gudi.best.logic.myInfo.mapper.MyInfoMapper;
import com.gudi.best.util.PageNation;
import com.gudi.best.util.S3Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Service
public class MyInfoService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MyInfoMapper mapper;
    @Autowired
    S3Uploader s3Uploader;

    // 프로필 확인해서 업데이트 아니면 인풋
    @Transactional
    public void proFileInput(String[] hobby, String intro, MultipartFile proFileImg,
                             String id, String name, String age) {
        HashMap<String, Object> map = null;
        String hobbyText = "";
        // 프로필 있는지 확인
        String check = mapper.proFileCheck(id);
        // 체크박스 값 받아서 쉼표 단위로 문자열 하나로 합치기
        if (hobby != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String text : hobby) {
                stringBuilder.append(text);
                stringBuilder.append(",");
            }
            hobbyText = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        }
        if (!proFileImg.isEmpty()) {
            // 업로드 하기
            try {
                // 포토 테이블에 사진이 있을경우 파일삭제 + DB삭제
                String newFileName = mapper.photoCheck(id);
                if (newFileName != null) {
                    s3Uploader.delete(newFileName);
                    mapper.photoDelete(id);
                }
                map = s3Uploader.upload(proFileImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("id", id);
            // 포토 테이블 넣기
            mapper.photoInput(map);
        } else {
            map = new HashMap<String, Object>();
            map.put("id", id);
            if (check == null) {
                map.put("path", "/img/noImg.png");
            }
            map.put("path", "");
        }
        map.put("hobby", hobbyText);
        map.put("intro", intro);
        map.put("name", name);
        map.put("age", age);
        if (check == null) {
            // 프로필 테이블 넣기
            mapper.proFileInput(map);
        } else {
            // 프로필 테이블 업데이트
            mapper.proFileUpdate(map);
        }
    }

    // 프로필 상세
    public ProFileDTO proFileDetail(String id) {
        return mapper.proFileDetail(id);
    }

    // 프로필 등록여부 체크
    public String proFileCheck(String id) {
        return mapper.proFileCheck(id);
    }

    // 비밀번호 변경
    public boolean pwChange(String id, String pw, String changePw) {
        String enc_pass = mapper.getPw(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean check = encoder.matches(pw, enc_pass);
        if (check) {
            String change_enc_pass = encoder.encode(changePw);
            mapper.pwChange(id, change_enc_pass);
        }
        return check;
    }

    public HashMap<String, Object> myBoardList(int page, String loginId) {
        int total = mapper.myBoardTotal(loginId);
        int start = 0;
        HashMap<String, Object> map = PageNation.pagination(page, 15, total);
        if (page == 1) {
            start = 0;
        } else {
            start = (page - 1) * 15;
        }
        map.put("list", mapper.myBoardList(start, loginId));
        map.put("division", "all");
        map.put("page", "1");
        return map;
    }
}
