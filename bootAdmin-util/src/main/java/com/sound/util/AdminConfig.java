package com.sound.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class AdminConfig {/*

	private String register_url;
	private String s_nginx_url;
	private String nginx_url;

	// @Value("${service_nginx_url:1}")
	private String service_nginx_url;
	
	private Integer default_company_id;
	
	private String default_company_name;

	private Integer default_sp_id;

	private String default_sp_account;

	private String channel_info_url;

	private String jump_client_url;

	private Integer channel_priority;

	private String default_channe_id;
	private String default_charge_num;
	private String inter_default_charge_num;
	private String file_url;
	private String file_loc;
	
	private String toAddress;
	
	private String default_inter_channe_id;
	private String default_voice_channe_id;
	
	public String getDefault_inter_channe_id() {
		return default_inter_channe_id;
	}

	public void setDefault_inter_channe_id(String default_inter_channe_id) {
		this.default_inter_channe_id = default_inter_channe_id;
	}

	public String getDefault_voice_channe_id() {
		return default_voice_channe_id;
	}

	public void setDefault_voice_channe_id(String default_voice_channe_id) {
		this.default_voice_channe_id = default_voice_channe_id;
	}
	
	@Value("${spring.datasource.driver-class-name}")
	public String driver_class_name;
	
	@Value("${spring.datasource.url}")
	public String url;
	
	public String export_username;
	
	@Value("${spring.datasource.password}")
	public String password;
	
	@Value("${send_message_url}")
	private String sendMessageUrl;
	@Value("${sp_account}")
	private String spAccount;
	@Value("${sp_password}")
	private String spPassword;
	@Value("${sp_userid}")
	private String userId;
	
	@Value("${office_url}")
	private String office_url;
	
	private Integer default_voice_sp_id;
	private String default_voice_sp_account;
	
	
	//default_inter_sp_id=3
    //default_inter_sp_account=morenguoji
			
	private Integer default_inter_sp_id;
	private String default_inter_sp_account;
	
	public String getOffice_url() {
		return office_url;
	}

	public void setOffice_url(String office_url) {
		this.office_url = office_url;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	
	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public String getFile_loc() {
		return file_loc;
	}

	public void setFile_loc(String file_loc) {
		this.file_loc = file_loc;
	}

	
	
	public String getInter_default_charge_num() {
		return inter_default_charge_num;
	}

	public void setInter_default_charge_num(String inter_default_charge_num) {
		this.inter_default_charge_num = inter_default_charge_num;
	}

	public String getDefault_channe_id() {
		return default_channe_id;
	}

	public void setDefault_channe_id(String default_channe_id) {
		this.default_channe_id = default_channe_id;
	}

	public String getDefault_charge_num() {
		return default_charge_num;
	}

	public void setDefault_charge_num(String default_charge_num) {
		this.default_charge_num = default_charge_num;
	}

	public String getS_nginx_url() {
		return s_nginx_url;
	}

	public void setS_nginx_url(String s_nginx_url) {
		this.s_nginx_url = s_nginx_url;
	}

	public String getRegister_url() {
		return register_url;
	}

	public void setRegister_url(String register_url) {
		this.register_url = register_url;
	}

	public String getNginx_url() {
		return nginx_url;
	}

	public void setNginx_url(String nginx_url) {
		this.nginx_url = nginx_url;
	}

	public String getService_nginx_url() {
		return service_nginx_url;
	}

	public void setService_nginx_url(String service_nginx_url) {
		this.service_nginx_url = service_nginx_url;
	}

	public String getDefault_sp_account() {
		return default_sp_account;
	}

	public void setDefault_sp_account(String default_sp_account) {
		this.default_sp_account = default_sp_account;
	}

	public Integer getDefault_sp_id() {
		return default_sp_id;
	}

	public void setDefault_sp_id(Integer default_sp_id) {
		this.default_sp_id = default_sp_id;
	}

	public String getChannel_info_url() {
		return channel_info_url;
	}

	public void setChannel_info_url(String channel_info_url) {
		this.channel_info_url = channel_info_url;
	}

	public String getJump_client_url() {
		return jump_client_url;
	}

	public void setJump_client_url(String jump_client_url) {
		this.jump_client_url = jump_client_url;
	}

	public Integer getChannel_priority() {
		return channel_priority;
	}

	public void setChannel_priority(Integer channel_priority) {
		this.channel_priority = channel_priority;
	}

	public String getDriver_class_name() {
		return driver_class_name;
	}

	public void setDriver_class_name(String driver_class_name) {
		this.driver_class_name = driver_class_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExport_username() {
		return export_username;
	}

	public void setExport_username(String export_username) {
		this.export_username = export_username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSendMessageUrl() {
		return sendMessageUrl;
	}

	public void setSendMessageUrl(String sendMessageUrl) {
		this.sendMessageUrl = sendMessageUrl;
	}

	public String getSpAccount() {
		return spAccount;
	}

	public void setSpAccount(String spAccount) {
		this.spAccount = spAccount;
	}

	public String getSpPassword() {
		return spPassword;
	}

	public void setSpPassword(String spPassword) {
		this.spPassword = spPassword;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getDefault_voice_sp_id() {
		return default_voice_sp_id;
	}

	public void setDefault_voice_sp_id(Integer default_voice_sp_id) {
		this.default_voice_sp_id = default_voice_sp_id;
	}

	public String getDefault_voice_sp_account() {
		return default_voice_sp_account;
	}

	public void setDefault_voice_sp_account(String default_voice_sp_account) {
		this.default_voice_sp_account = default_voice_sp_account;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Integer getDefault_company_id() {
		return default_company_id;
	}

	public void setDefault_company_id(Integer default_company_id) {
		this.default_company_id = default_company_id;
	}

	public String getDefault_company_name() {
		return default_company_name;
	}

	public void setDefault_company_name(String default_company_name) {
		this.default_company_name = default_company_name;
	}

	public Integer getDefault_inter_sp_id() {
		return default_inter_sp_id;
	}

	public void setDefault_inter_sp_id(Integer default_inter_sp_id) {
		this.default_inter_sp_id = default_inter_sp_id;
	}

	public String getDefault_inter_sp_account() {
		return default_inter_sp_account;
	}

	public void setDefault_inter_sp_account(String default_inter_sp_account) {
		this.default_inter_sp_account = default_inter_sp_account;
	}

*/}
