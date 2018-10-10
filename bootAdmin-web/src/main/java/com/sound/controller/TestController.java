package com.sound.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sound.controller.base.BaseController;
//import com.sound.entity.Notice;
//import com.sound.taskService.FtpTaskService;

@Controller
public class TestController extends BaseController {/*

	@Autowired
	private FtpTaskService ftpTaskService;
	@RequestMapping(value = "testBigData/{count}")
	@ResponseBody
	public String testBigData(@PathVariable Integer count) {
		List<Notice> list = new ArrayList<Notice>();
		for (int i = 0; i < count; i++) {
			Notice notice = new Notice();
			notice.setId(200000000);
			notice.setNoticeName("通知名称，是指中国农历中表示季节变迁的24个特定节令，是根据地球在黄道（即地球绕太阳公转的轨道）上的位置变化而制定的，每一个分别相应于地球在黄道，二十四节气包含:立春、雨水、惊蛰、春分、清明、谷雨、立夏、小满、芒种、夏至二十四节气为您提供二十四节气,24节气,节气,二十四节气歌,二十四节气表,节气歌,24节气歌,24节气表,二十四节气的含义,二十四节气图,24节气图,二十四节气歌2017年24节气表,2017年二十四节气查询2017年,鸡年,中华人民共和国建国68周年。2017年1月28日是春节(农历2017年正月初一),1月27日是除夕(农历2016年腊月三十)");
			notice.setNoticeType(10);
			notice.setNoticeContent("年关将至，临近春节。央视春晚各项工作也进入冲刺阶段。其实早在2016年4月，2017年春晚便已成立办公室，启动创意类节目、语言类节目、歌舞类节目征集。导演组历时半年，跑遍大江南北进行调研，向广大老百姓虚心征集对2017鸡年春晚的意见鸡年春晚主持阵容已确定，朱军、董卿、康辉、朱迅、尼格买提坐镇主会场，而四大分会场的主持人则有管彤、孟盛楠、张蕾三人，另有知情人透露，其中四川分会场的主持阵容或有变动，张泽群待定中今年的央视春晚，除了主会场外，节目组还分别在四川西昌、上海、桂林、哈尔滨四地设置了分会场。眼下，四大分会场的彩排工作也在陆续进行中");
			notice.setUserId(10);
			notice.setCreateTime(new Date());
			notice.setUserName("admin");
			list.add(notice);
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("在Java堆中创建了" + list.size() + "个Notice对象");
		return "在Java堆中创建了" + list.size() + "个Notice对象";
	}

	@RequestMapping(value = "testFtp")
	@ResponseBody
	public void testFtp() {
		try {
			ftpTaskService.ftpTask();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/}
