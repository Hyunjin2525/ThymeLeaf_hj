package com.sist.web.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sist.web.entity.BoardVO;

@Repository
public interface BoardDAO extends JpaRepository<BoardVO, Integer>{
	@Query(value = "SELECT * FROM board ORDER BY no DESC LIMIT :start,10",nativeQuery = true)
	public List<BoardVO> board_list(Integer start);
	
	@Query(value = "SELECT CEIL(COUNT(*)/10.0) FROM board",nativeQuery = true)
	public int board_total();
	
	@Query(value = "SELECT * FROM board ORDER BY hit DESC LIMIT 1,4",nativeQuery = true)
	public List<BoardVO> board_main_list();
	
	//상세보기
	public BoardVO findByNo(int no);
	
}
