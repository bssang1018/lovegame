package com.gudi.best.logic.loveBoard.mapper;

import com.gudi.best.dto.BoardDTO;
import com.gudi.best.dto.PhotoDTO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface BoardMapper {
    @Select("SELECT COUNT(boardNum) FROM board")
    int boardTotal();

    @SelectProvider(type = BoardSQL.class, method = "boardPick")
    ArrayList<BoardDTO> list(int start, String division);

    @Insert(BoardSQL.BOARD_PHOTO_INSERT)
    void photoInsert(HashMap<String, Object> map);

    @Insert("INSERT INTO board(id,title,content) VALUES(#{id},#{title},#{content})")
    @Options(useGeneratedKeys = true, keyProperty = "boardNum")
    void boardWrite(BoardDTO dto);

    @Select("SELECT * FROM board WHERE boardNum = #{boardNum}")
    BoardDTO boardDetail(int boardNum);

    @Select("SELECT * FROM photo WHERE division='loveBoard' AND divisionNum=#{boardNum}")
    ArrayList<PhotoDTO> boardPhoto(int boardNum);

    @Select("SELECT COUNT(photoNum) FROM photo WHERE division='loveBoard' AND divisionNum=#{boardNum}")
    int photoCount(int boardNum);

    @Delete("DELETE FROM photo WHERE photoNum=#{photoNum}")
    void photoDel(int photoNum);

    @Update("UPDATE board SET title=#{param2},content=#{param3} WHERE boardNum=#{param1}")
    void boardUpdate(String boardNum, String title, String content);

    @Update("UPDATE board SET boardHit = boardHit + 1 WHERE boardNum = #{boardNum}")
    void boardHit(int boardNum);

    @Select("SELECT newFileName FROM photo WHERE divisionNum=#{boardNum}")
    ArrayList<String> photoNewFileName(int boardNum);

    @Delete("DELETE FROM board WHERE boardNum=#{boardNum}")
    void boardDelete(int boardNum);

    @SelectProvider(type = BoardSQL.class, method = "boardSearch")
    ArrayList<BoardDTO> search(int start, String searchText, String option);

    @SelectProvider(type = BoardSQL.class, method = "boardSearchCount")
    int boardSearchTotal(String searchText, String option);

    @Select("SELECT imgPath FROM proFile WHERE id = (SELECT id FROM board WHERE boardNum=#{boardNum})")
    String proFileImg(int boardNum);
}
