package com.sist.web.controller;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sist.web.dao.GroundDAO;
import com.sist.web.entity.GroundVO;

@Controller
@RequestMapping("ground/")
public class FootballController {
	@Autowired 
	private GroundDAO dao;
	
	@RequestMapping("list")
	public String good_list(Model model,String page,String fd)
	{
		if(fd==null)
			fd="서울";
		if(page==null)
			page="1";
		int curpage=Integer.parseInt(page);
		int start=(9*curpage)-9;
		List<GroundVO> list=dao.ground_list_find(fd, start);
		
		for(GroundVO vo:list)
		{
			String img=vo.getGimage();
			if(img.contains("^"))
			{
				img=img.substring(0,img.indexOf("^"));
				vo.setGimage(img);
			}
			
		}
		
		
		int totalpage=dao.ground_total_page(fd);
		
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
		model.addAttribute("fd",fd);
		
		model.addAttribute("main_html","ground/list");
		return "main";
	}
	
	@GetMapping("detail")
	public String ground_detail(Model model,int gno,HttpServletRequest request,HttpServletResponse response)
	{
	      Cookie[] cookies=request.getCookies();
	      if(cookies!=null) {
	         for(int i=cookies.length-1;i>=0;i--) {
	            String key=cookies[i].getName();
	            if(key.equals("ground_"+gno)) {
	               cookies[i].setMaxAge(0); // 쿠키를 삭제하기 위해 만료시간을 0으로 설정
	                   cookies[i].setPath("/");
	                   response.addCookie(cookies[i]);
	                   break;
	            }
	         }
	      }
	      ////// ^ 같은 쿠키 삭제(최신 기록이 위로 가기 위해)
	      
	      
	      //// v 쿠키 출력
	      cookies=request.getCookies();
	      List<GroundVO> ckList=new ArrayList<GroundVO>();
	      if(cookies!=null) {
	         for(int i=cookies.length-1;i>=0;i--) {
	            String key=cookies[i].getName();
	            if(key.startsWith("ground_")) {
	               String data=cookies[i].getValue();
	               GroundVO cvo=new GroundVO();
	               cvo=dao.findByGno(Integer.parseInt(data));
	               String name=cvo.getGname();
	               if(name.length()>7) {
	                  cvo.setGname(name.substring(0,7)+"..");
	               }
	               String img=cvo.getGimage();
		   			if(img.contains("^"))
		   			{
		   				img=img.substring(0,img.indexOf("^"));
		   				cvo.setGimage(img);
		   			}
	               ckList.add(cvo);
	               if(ckList.size()==4) {
	                  break;
	               }
	            }
	         }
	         
	      }
	      
	      

	      model.addAttribute("ckList", ckList);
	      
	      //// v 쿠키 추가
	      Cookie cookie=new Cookie("ground_"+gno, String.valueOf(gno));
	      cookie.setPath("/");
	      cookie.setMaxAge(60*60*24);
	      response.addCookie(cookie);
	      
			GroundVO vo=dao.findByGno(gno);
			String img=vo.getGimage();
			if(img.contains("^"))
			{
				img=img.substring(0,img.indexOf("^"));
				vo.setGimage(img);
			}
			model.addAttribute("vo",vo);
			model.addAttribute("main_html","ground/detail");
			return "main";
	}
	
	@GetMapping("weather")
	public String seoul_weather(Model model)
	{
		  String html="";
		 try
		  {
			  Document doc=Jsoup.connect("https://korean.visitseoul.net/weather").get();
			  Element section=doc.selectFirst("section#content");
			  html="<section id=\"content\">";
			  html+=section.html();
			  html+="</section>";
			  //<img src="https://korean.visitseoul.net/resources/theme/images/weather/img-weather10.png" alt="흐리고 비">
			  html=html.replace("src=\"","src=\"https://korean.visitseoul.net" );
			  html=html.replace("제공 : 케이웨더(Kweather)","" );
		  }catch(Exception ex) {}
		model.addAttribute("main_html","ground/weather");
		model.addAttribute("html",html);
		return "main";
	}
}
