package com.sound.entity;

import java.util.Date;

import com.sound.util.Page;

public class BlackObject {
	private  Integer id;
	private String mdn;
	private Integer sp_id;
	private Date createtime;
	private String descption;
	private String sp_ids;
	private Page page;
	
	
	public String getSp_ids() {
		return sp_ids;
	}
	public void setSp_ids(String sp_ids) {
		this.sp_ids = sp_ids;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMdn() {
		return mdn;
	}
	public void setMdn(String mdn) {
		this.mdn = mdn;
	}
	public Integer getSp_id() {
		return sp_id;
	}
	public void setSp_id(Integer sp_id) {
		this.sp_id = sp_id;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getDescption() {
		return descption;
	}
	public void setDescption(String descption) {
		this.descption = descption;
	}

}
