package com.sist.web.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sist.web.dao.BoardDAO;
import com.sist.web.entity.BoardVO;

@Controller
@RequestMapping("board/")
public class BoardController {
	
	@Autowired
	private BoardDAO dao;
	
	@GetMapping("board_list")
	public String board_list(Model model,String page)
	{
		if(page==null)
			page="1";
			int curpage=Integer.parseInt(page);
			int start=(10*curpage)-10;
		List<BoardVO> list=dao.board_list(start);
		for(BoardVO vo:list)
		{
			String[] date=vo.getRegdate().split(" ");
			vo.setRegdate(date[0].trim());
			
		}
		int totalpage=dao.board_total();
		
		final int BLOCK=5;
		int startpage=((curpage-1)/BLOCK*BLOCK)+1;
		int endpage=((curpage-1)/BLOCK*BLOCK)+BLOCK;
		if(endpage>totalpage)
			endpage=totalpage;
		
		model.addAttribute("curpage",curpage);
		model.addAttribute("totalpage",totalpage);
		model.addAttribute("startpage",startpage);
		model.addAttribute("endpage",endpage);
		model.addAttribute("list",list);
			
		
		model.addAttribute("main_html","board/board_list");
		return "main";
	}
	
	@GetMapping("board_insert")
	public String board_insert(Model model)
	{
			return "board/board_insert";
	}
	
	@PostMapping("board_insert.ok")
	@ResponseBody
	public String board_insert_ok(BoardVO vo)
	{
		dao.save(vo);
		return "";
	}
	
	@GetMapping("board_update")
	public String board_update(int no,Model model)
	{
		model.addAttribute("no",no);
		return "board/board_update";
	}
	
	@GetMapping("board_update_data")
	@ResponseBody
	public String board_update_data(int no)throws Exception
	{
		System.out.println("no="+no);
		BoardVO vo=dao.findByNo(no);
		
		ObjectMapper mapper=new ObjectMapper();
		String json=mapper.writeValueAsString(vo);
		return json;
	}
	
	@PostMapping("board_update.ok")
	@ResponseBody
	public String board_update_ok(BoardVO vo)
	{
		BoardVO bvo=dao.findByNo(vo.getNo());
		if(bvo.getPwd().equals(vo.getPwd()))
		{
			vo.setHit(bvo.getHit());
			dao.save(vo);
		}else {
			return "NO";
		}
		return "OK";
	}
	
	@GetMapping("board_detail")
	public String board_detail(int no,Model model)
	{
		BoardVO vo=dao.findByNo(no);
		vo.setHit(vo.getHit()+1);
		dao.save(vo);//update => save() : update,insert
		vo=dao.findByNo(no);
		model.addAttribute("vo",vo);
		model.addAttribute("main_html","board/board_detail");
		return "main";
	}
	@GetMapping("board_delete")
	public String board_delete(int no,Model model)
	{
		model.addAttribute("no",no);
		return "board/board_delete";
	}
	
	@PostMapping("board_delete.ok")
	@ResponseBody
	public String board_delete_ok(int no,String pwd)
	{
		BoardVO vo=dao.findByNo(no);
		if(vo.getPwd().equals(pwd)) {
			dao.delete(vo);
		}else {
			return "NO";
		}
		return"OK";
	}
}
