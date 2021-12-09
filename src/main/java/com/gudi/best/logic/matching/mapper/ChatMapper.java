package com.gudi.best.logic.matching.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.gudi.best.dto.memberDTO;
import com.gudi.best.logic.matching.dto.ChatDTO;
import com.gudi.best.logic.matching.dto.ChatRoomDTO;

@Mapper
public interface ChatMapper {
	
	@Insert("INSERT INTO chatRoom(id, person, dates, lastChatDates) VALUES(#{param1}, #{param2}, SYSDATE(), SYSDATE())")
	void createRoom(String loginId, String person);

	@Select("SELECT * FROM\r\n"
			+ "(SELECT roomNum, id, person, dates, lastChatDates, lastMessage,\r\n"
			+ "(SELECT imgPath FROM proFile WHERE chatRoom.person=proFile.id)idPath,(SELECT imgPath FROM proFile WHERE chatRoom.id=proFile.id)personPath  FROM chatRoom WHERE id = #{param1}  OR person = #{param1} )sub\r\n"
			+ "ORDER BY lastChatDates DESC")
	List<ChatRoomDTO> findAllRooms(String loginId);
	
	@Select("SELECT * FROM chatRoom WHERE roomNum = #{param1}")
	ChatRoomDTO findRoomByNum(String roomNum);

	@Insert("INSERT INTO chat(roomNum, id, message, dates) VALUES(#{param1}, #{param2}, #{param3}, SYSDATE())")
	void chatInsert(Integer roomNum, String id, String message);

	@Update("UPDATE chatRoom SET lastchatdates = SYSDATE() WHERE roomNum = #{param1}")
	void lastChatDatesUpdate(Integer roomNum);

	/*
	@Select("SELECT * FROM\r\n"
			+ "(SELECT roomnum, id, message, dates FROM chat WHERE roomNum = #{param1}) AS sub\r\n"
			+ "ORDER BY dates ASC")
	List<ChatDTO> chatFind(String roomNum);
	*/
	
	// 채팅 기록불러오기 세트 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	@Select("  SELECT *\r\n"
			+ "    FROM (SELECT @rowNum := @rowNum + 1 AS rowNum, roomnum, id, message, dates FROM\r\n"
			+ "    (SELECT roomnum, id, message, dates FROM chat WHERE roomNum = #{param1}) AS sub\r\n"
			+ "    ORDER BY dates ASC) AS sub2\r\n"
			+ "    WHERE rowNum BETWEEN (#{param2}-100) AND #{param2}\r\n")
	List<ChatDTO> chatFind(String roomNum, int maxRowNum);
	
	@Update("SET @rowNum := 0")
	void initRownum();

	@Select("SELECT MAX(@rowNum := @rowNum + 1) AS maxRowNum\r\n"
			+ "FROM (SELECT roomnum, id, message, dates FROM chat WHERE roomNum = #{param1}) AS sub")
	ChatDTO getMaxRownum(String roomNum);
	
	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	@Update("UPDATE chatRoom SET lastMessage = #{param2} WHERE roomNum = #{param1}")
	void lastMessageUpdate(Integer integer, String message);

	@Select("SELECT * FROM chatRoom WHERE (id = #{param1} AND person = #{param2}) OR (id = #{param2} AND person = #{param1})")
	ChatRoomDTO findOverlap(String loginId, String person);

	@Select("SELECT * FROM member WHERE id = #{param1}")
	memberDTO findPerson(String person);

	@Update("UPDATE chat SET readState = 'y' WHERE roomNum = #{param1}")
	void readStateUpdate(String roomNum);

	@Select("SELECT id FROM member WHERE id like '${param1}%'")
	ArrayList<String> searchId(String id);

	@Delete("DELETE FROM chatRoom WHERE roomNum = #{param1}")
	void deleteRoom(String roomNum);

	@Delete("DELETE FROM chat WHERE roomNum = #{param1}")
	void deleteChat(String roomNum);

	
	
	
	

	
	
}
