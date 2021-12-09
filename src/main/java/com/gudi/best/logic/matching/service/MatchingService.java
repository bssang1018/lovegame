package com.gudi.best.logic.matching.service;

import com.gudi.best.dto.ProFileDTO;
import com.gudi.best.logic.matching.dto.MatchingDTO;
import com.gudi.best.logic.matching.mapper.MatchingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class MatchingService {
    private final MatchingMapper mapper;

    // 관심사가 일치하는 순서 4개 가져오기
    public List<ProFileDTO> matchingHobby(String loginId) {
        String hobby = mapper.getHobby(loginId);
        if (hobby != null) {
            ArrayList<String> word = new ArrayList<>(Arrays.asList(hobby.split(",")));
            ArrayList<ProFileDTO> proFileList = mapper.proFileList(loginId);
            HashMap<ProFileDTO, Integer> map = new HashMap<>();

            // 나의 관심사 단어 배열과 문자열 비교해서 문자열과 일치하는 갯수로 맵에 담기
            for (ProFileDTO dto : proFileList) {
                int result = 0;
                for (String words : word) {
                    result += StringCount(words, dto.getHobby());
                }
                map.put(dto, result);
            }

            // 일치하는 갯수의 map 내림차순 정렬하기
            LinkedHashMap<ProFileDTO, Integer> resultMap = new LinkedHashMap<>();
            List<ProFileDTO> listKeySet = new ArrayList<>(map.keySet());
            ArrayList<ProFileDTO> list = new ArrayList<>();
            Collections.sort(listKeySet, (value1, value2) -> (map.get(value2).compareTo(map.get(value1))));
            for (ProFileDTO key : listKeySet) {
                list.add(key);
            }
            List<ProFileDTO> listResult = list.subList(0, 4);
            return listResult;
        } else {
            return mapper.randomList(loginId);
        }
    }

    public int StringCount(String str1, String str2) {
        int count = 0;
        if (str2.contains(str1)) {
            count++;
        }
        return count;
    }
}
