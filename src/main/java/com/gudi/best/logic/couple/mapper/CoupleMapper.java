package com.gudi.best.logic.couple.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.gudi.best.dto.BoardDTO;
import com.gudi.best.dto.CalenderDTO;
import com.gudi.best.dto.PhotoDTO;

@Mapper
public interface CoupleMapper {

	@Insert("INSERT INTO calender (id,start,end,title,content,color,division) VALUES(#{id},#{start},#{end},#{title},#{content},#{color},'C')")
	void calenderEnter(CalenderDTO Cdto);

	@Select("select Cnum,start,end,title,color from calender where id=#{param1} AND del='0' AND division ='C' ")
	ArrayList<CalenderDTO> readCalender(String id);
	
	@Select("select * from calender where id=#{param1} AND cnum = #{param2} AND del='0' AND division ='C'")
	HashMap<String, String> detail(String id, int cnum);

	@Update("UPDATE calender SET start = #{start}, end = #{end}, title=#{title}, content = #{content}, color = #{color} WHERE id=#{id} AND Cnum=#{cNum} AND division ='C'")
	void calenderUpdate(CalenderDTO cdto);
	
	@Update("UPDATE calender SET del = '1' WHERE id=#{param1} AND Cnum=#{param2} AND division ='C'")
	void calenderDel(String id, int cnum);

	@Select("select coupleId from member where id = #{param1}")
	String coupleChk(String id);

	@Select("select * from calender where (id=#{param1} or id=#{param2}) AND del='0' AND division ='M' ")
	ArrayList<CalenderDTO> readMomory(String id, String chk);

	@Select("select id from member where coupleId = '없음' AND id like '${param1}%' limit 5")
	ArrayList<String> search(String id);
	
	@Select("SELECT * FROM proFile WHERE id = #{param1}")
	HashMap<String, Object> display(String id);

	@Update("UPDATE member SET coupleId = #{param1} where id = #{param2}")
	void applyCouple(String p1, String p2);

	@Select("select id from member where coupleId = '없음' AND id = #{param1}")
	ArrayList<String> searchF(String id);

	@Insert("INSERT INTO calender(id,title,content,start,end,color,division) VALUES(#{id},#{title},#{content},#{start},#{end},#{color},'M')")
    @Options(useGeneratedKeys = true, keyProperty = "cNum")
	void memoryWrite(CalenderDTO cdto);

	@Insert("INSERT INTO photo(id,division,oriFileName,newFileName,path,divisionNum) VALUES(#{id},'loveMemory',#{oriFileName},#{newFileName},#{path},#{divisionNum})")
	void photoInsert(HashMap<String, Object> map);

	@Select("SELECT * FROM calender WHERE cNum = #{cNum}")
	HashMap<String, Object> memoryDetail(int cNum);
	
	    @Select("SELECT * FROM photo WHERE division='loveMemory' AND divisionNum=#{cNum}")
	    ArrayList<PhotoDTO> boardPhoto(int cNum);

	    @Select("SELECT COUNT(photoNum) FROM photo WHERE division='loveMemory' AND divisionNum=#{cNum}")
	    int photoCount(int cNum);

	    @Delete("DELETE FROM photo WHERE newFileName=#{newFileName}")
		void photoDel(String newFileName);

	    @Select("SELECT divisionNum FROM photo WHERE division='loveMemory' AND newFileName=#{newFileName}")
		int divisionNum(String newFileName);

		@Update("UPDATE calender SET title = #{title}, content=#{content}, start=#{start}, end=#{end},color=#{color} where cNum = #{cNum}")
		void memoryUpdate(CalenderDTO cdto);

		@Select("select newFileName from photo where division='loveMemory' AND divisionNum=#{cNum}")
		ArrayList<String> photoName(int cNum);

		@Update("update calender set del='1' where cnum=#{cNum}")
		void memoryDel(int cNum);

		@Select("select *  from calender where id=#{param1} AND division='M' AND del='0' LIMIT #{param2},10")
		ArrayList<CalenderDTO> history(String id, int page);

		@Select("select count(cNum) from calender where id=#{id} AND division='M' AND del='0'")
		int historyTotal(String id);

		@Select("select count(cNum) from calender where (id=#{param1} or id=#{param2}) AND del='0' AND division ='M'")
		int totalMemory(String id, String chk);

		@Select("select * from calender where (id=#{param1} or id=#{param2}) AND del='0' AND division ='M' LIMIT #{param3},10 ")
		ArrayList<CalenderDTO> readMomoryP(String id, String chk, int page);
}
