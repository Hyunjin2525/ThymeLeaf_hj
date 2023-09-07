package com.sist.web.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sist.web.entity.GroundVO;

@Repository
public interface GroundDAO extends JpaRepository<GroundVO, Integer>{
	
	@Query(value ="SELECT * FROM ground_detail ORDER BY hit DESC LIMIT 0,3" ,nativeQuery = true)
	public List<GroundVO> ground_main();
	
	@Query(value ="SELECT * FROM ground_detail WHERE gname LIKE CONCAT('%',:gname,'%') ORDER BY gno LIMIT :start,9" ,nativeQuery = true)
	public List<GroundVO> ground_list_find(@Param("gname") String gname,@Param("start") int start);
	
	
	@Query(value = "SELECT CEIL(COUNT(*)/9.0) FROM ground_detail WHERE gname LIKE CONCAT('%',:gname,'%')",nativeQuery = true)
	public int ground_total_page(String gname);
	
	public GroundVO findByGno(int gno);
}
