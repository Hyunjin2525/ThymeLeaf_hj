package com.sist.web.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/*
 * GNO int 
GNAME text 
GADDR text 
GIMAGE text 
GNOTICE text 
GPRICE int
 */
@Entity
@Table(name = "ground_detail")
@Getter
@Setter
public class GroundVO {
	@Id
	private int gno;
	private int gprice,hit;
	private String gaddr,gimage,gnotice,gname;
	
}
