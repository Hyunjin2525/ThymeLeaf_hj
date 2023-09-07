package com.sist.web.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.web.dao.BoardDAO;
import com.sist.web.dao.GroundDAO;
import com.sist.web.entity.BoardVO;
import com.sist.web.entity.GroundVO;
@Controller
public class MainController {
	@Autowired
	private GroundDAO dao;
	@Autowired
	private BoardDAO bdao;
	@GetMapping("/")
	public String main(Model model)
	{
		List<GroundVO> list=dao.ground_main();
		for(GroundVO vo:list)
		{
			String img=vo.getGimage();
			if(img.contains("^"))
			{
				img=img.substring(0,img.indexOf("^"));
				vo.setGimage(img);
			}
			
		}
		
		List<BoardVO> blist=bdao.board_main_list();
		for(BoardVO vo:blist)
		{
			String cont=vo.getContent();
			if(cont.length()>=100)
			{
				cont=cont.substring(0,98)+"...";
				vo.setContent(cont);
			}
		}
		
		model.addAttribute("blist",blist);
		model.addAttribute("list",list);
		model.addAttribute("main_html","main/home");
		return "main";
	}
	
}
