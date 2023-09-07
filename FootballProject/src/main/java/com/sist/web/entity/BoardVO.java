package com.sist.web.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="board")
@Getter
@Setter
@DynamicUpdate
public class BoardVO {
	@Id
	private int no;
	private String name,subject,content;
	@Column(insertable = true,updatable = false)//insert는 허용하고 update는 허용하지 않는다는 뜻 (insert만 되게 만들기)
	private String pwd;
	private int hit;
	@Column(insertable = true,updatable = false)
	private String regdate;
	
	@PrePersist
	public void regdate() {
		this.regdate=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		
	}
	
}
