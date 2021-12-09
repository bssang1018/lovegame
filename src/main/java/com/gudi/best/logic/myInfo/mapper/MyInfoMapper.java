package com.gudi.best.logic.myInfo.mapper;

import com.gudi.best.dto.BoardDTO;
import com.gudi.best.dto.ProFileDTO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface MyInfoMapper {
    @InsertProvider(type = MyInfoSQL.class, method = "insertNullCheck")
    void proFileInput(HashMap<String, Object> map);

    @Insert(MyInfoSQL.PROFILE_PHOTO_INSERT)
    void photoInput(HashMap<String, Object> map);

    @Select("SELECT * FROM proFile WHERE id = #{id}")
    ProFileDTO proFileDetail(String id);

    @Select("SELECT id FROM proFile WHERE id = #{id}")
    String proFileCheck(String id);

    @Select("SELECT newFileName FROM photo WHERE id = #{id} AND division = 'proFile'")
    String photoCheck(String id);

    @Delete("DELETE FROM photo WHERE id = #{id} AND division = 'proFile'")
    void photoDelete(String id);

    @UpdateProvider(type = MyInfoSQL.class, method = "updateNullCheck")
    void proFileUpdate(HashMap<String, Object> map);

    @Select("SELECT pw FROM member WHERE id = #{id}")
    String getPw(String id);

    @Update("UPDATE member SET pw = #{param2} WHERE id = #{param1}")
    void pwChange(String id, String change_enc_pass);

    @Select("SELECT COUNT(boardNum) FROM board WHERE id = #{param1}")
    int myBoardTotal(String loginId);

    @Select("SELECT *,(SELECT COUNT(goodNum) FROM good WHERE boardNum=divisionNum)goodCount FROM board WHERE id =#{param2} ORDER BY boardNum DESC LIMIT #{param1},15")
    ArrayList<BoardDTO> myBoardList(int start, String loginId);

    @Update("UPDATE member SET delCheck = 'Y' WHERE id=#{id}")
    void memberDrop(String id);
}
