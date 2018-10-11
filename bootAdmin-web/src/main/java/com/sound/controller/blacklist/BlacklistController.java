package com.sound.controller.blacklist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sound.controller.base.BaseController;
//import com.sound.entity.BlackObject;
//import com.sound.service.BlacklistService;
//import com.sound.service.RedisService;
//import com.sound.sms.redis.RedisMessage;
//import com.sound.util.Const;
//import com.sound.util.ExportExcel;
//import com.sound.util.FileManageUtils;
//import com.sound.util.FormData;
//import com.sound.util.PageInfo;
//import com.sound.util.Tools;

@Controller
@RequestMapping("blacklist")
public class BlacklistController extends BaseController {/*

	@Autowired
	BlacklistService service;
	@Autowired
	RedisService redisService;

	@RequestMapping("load")
	@ResponseBody
	public Map<String, Object> load(PageInfo page, HttpServletRequest request) throws Exception {
		page.setFormData(getFormData());
		List<Map<String, Object>> data = service.getAllBlacklistPage(page);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("page", page);
		result.put("data", data);
		return result;
	}

	@RequestMapping("showInfo")
	@ResponseBody
	public Map<String, Object> showInfo(HttpSession session) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		FormData formData = getFormData();
		if (formData.get("ID") != null && !"".equals(formData.getString("ID"))) {
			result = service.getBlacklistById(formData);
		}
		return result;
	}

	
	@SuppressWarnings("unchecked")
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(HttpSession session) throws Exception {
		session.getAttribute("");

		FormData formData = getFormData();
		FormData map = formData;
		map.put("sp_id", 0);
		map.put("createtime", new Date());
		map.put("ip", map.getString("lastlogin_ip"));
		map.put("username", map.getString("userName"));
		// 记录系统日志

		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> checkPhone = service.checkPhone(formData);

		if (checkPhone != null && checkPhone.size() != 0) {
			result.put("statusCode", 202);
			return result;
		}

		String ID = formData.getString("ID");
		boolean isEdit = (ID != null && !"".equals(ID));
		map.put("opearte_type", isEdit ? Const.BLACK_EDIT : Const.BLACK_ADD);
		Map<String, Object> oldBlaskListInfo = null;
		String configName = "";
		if (isEdit) {
			oldBlaskListInfo = service.getBlacklistById(formData);
			formData.put("id", formData.get("SPID"));
			FormData oldChannelInfo = new FormData();
			if (oldBlaskListInfo.get("spid") != null) {
				oldChannelInfo.put("id", oldBlaskListInfo.get("spid"));
				Map<String, Object> channel = channelInfoService.getSmsChannelInfoInfoById(oldChannelInfo);
				configName = channel.get("config_name").toString();
			}
		}
		int res = service.saveBlacklist(formData);
		if (isEdit) {
			
			redisService.updBlackListRedis(configName, formData.getString("MDN"), formData.getString("TYPE"),
					formData.getString("SPID"), oldBlaskListInfo);
			redisService.sendBlackInfo(
					oldBlaskListInfo.get("type").toString().equals("1") ? RedisMessage.TYPE_SYSTEM
							: oldBlaskListInfo.get("type").toString().equals("2") ? RedisMessage.TYPE_CHANNEL
									: RedisMessage.TYPE_USER_SP,
					RedisMessage.EVENTTYPE_DELETE, Integer.valueOf(oldBlaskListInfo.get("id").toString()),
					formData.getString("MDN"), oldBlaskListInfo.get("target_id") == null ? null
							: Integer.valueOf(oldBlaskListInfo.get("target_id").toString()), 1);
			redisService.sendBlackInfo(
					formData.getString("TYPE").equals("1") ? RedisMessage.TYPE_SYSTEM
							: formData.getString("TYPE").equals("2") ? RedisMessage.TYPE_CHANNEL
									: RedisMessage.TYPE_USER_SP,
					RedisMessage.EVENTTYPE_ADD, Integer.valueOf(oldBlaskListInfo.get("id").toString()),
					formData.getString("MDN"),
					Integer.valueOf(formData.getString("SPID")), 1
					);
		} else {
			redisService.insertBlackListRedis(map.getString("userName"), formData.getString("MDN"),
					formData.getString("TYPE"), formData.getString("SPID") == null ? "" : formData.getString("SPID"),
					formData.get("ID").toString());
			
			redisService.sendBlackInfo(
					formData.getString("TYPE").equals("1") ? RedisMessage.TYPE_SYSTEM
							: formData.getString("TYPE").equals("2") ? RedisMessage.TYPE_CHANNEL
									: RedisMessage.TYPE_USER_SP,
					RedisMessage.EVENTTYPE_ADD, Integer.valueOf(formData.get("ID").toString()),
					formData.getString("MDN"), Integer.valueOf(formData.getString("SPID")), 1);
		}
		map.put("opearte_result", (res >= 1 ? 1 : 2));
		map.put("opearte_content", (isEdit ? "修改黑名单" + formData.getString("MDN") + (res >= 1 ? "成功" : "失败")
				: "新增黑名单" + formData.getString("MDN") + (res >= 1 ? "成功" : "失败")));
		result.put("statusCode", (res >= 1 ? 200 : 201));
		this.insertOperalog(map);
		return result;
	}

	*//**
	 * 删除黑名单
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("delBlacklist")
	@ResponseBody
	public Map<String, Object> delBlacklist(HttpSession session) throws Exception {
		FormData formData = getFormData();
		Map<String, Object> map = new HashMap<String, Object>();
		formData.put("sp_id", 0);
		formData.put("createtime", new Date());
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> typeResult = service.getBlacklistById(formData);
		redisService.delBlackListRedis(formData.getString("username"), typeResult.get("mdn").toString(),
				typeResult.get("type").toString(),
				typeResult.get("target_id") == null ? "" : typeResult.get("target_id").toString(),
				typeResult.get("id").toString());
		
		String targetId = typeResult.get("target_id") == null ? "0" : typeResult.get("target_id").toString();
		redisService.sendBlackInfo(
				typeResult.get("type").toString().equals("1") ? RedisMessage.TYPE_SYSTEM
						: typeResult.get("type").toString().equals("2") ? RedisMessage.TYPE_CHANNEL
								: RedisMessage.TYPE_USER_SP,
				RedisMessage.EVENTTYPE_DELETE, Integer.valueOf(typeResult.get("id").toString()),
				typeResult.get("mdn").toString(), Integer.parseInt(targetId), 1);
		int res = service.deleteBlacklist(formData);
		result.put("statusCode", (res >= 1 ? 200 : 201));

		map.put("opearte_result", (res >= 1 ? 1 : 2));
		map.put("opearte_content", "删除黑名单" + formData.getString("MDN") + (res >= 1 ? "成功" : "失败"));
		this.insertOperalog(formData);
		return result;
	}

	*//**
	 * 检查ID是否存在
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("check")
	@ResponseBody
	public boolean check(@RequestParam(required = false) String spid, @RequestParam(required = false) String type,
			HttpSession session) throws Exception {
		// 系统级无需检查spid
		if ("1".equals(type)) {
			return true;
			// 通道级检查通道ID是否存在
		} else if ("2".equals(type)) {
			FormData data = new FormData();
			data.put("id", spid);
			Map<String, Object> channel = channelInfoService.getSmsChannelInfoInfoById(data);
			return channel != null && channel.size() != 0;
			// 应用级检查应用ID是否存在
		} else {
			FormData data = new FormData();
			data.put("id", spid);
			SpInfo spinfoById = spinfoService.getSpinfoById(Integer.parseInt(spid));
			return spinfoById != null && spinfoById.getSp_name() != null;
		}
	}

	@RequestMapping("exportBlackListToExcel")
	@ResponseBody
	public void exportBlackListToExcel(HttpServletResponse response) throws Exception {
		String[] rowsName = new String[] { "黑名单ID", "手机号码", "添加时间", "黑名单级别", "生效范围", "备注" };
		List<Object[]> dataList = new ArrayList<Object[]>();
		PageInfo page = new PageInfo();
		page.setPageSize(Const.exportLine);
		page.setCurrentPage(1);
		page.setFormData(getFormData());
		List<Map<String, Object>> data = service.getAllBlacklistPage(page);
		for (Map<String, Object> map : data) {
			Object[] objs = new Object[6];
			String ID = map.get("id").toString();
			objs[0] = map.get("id");
			objs[1] = map.get("mdn");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = df.format(map.get("create_time"));
			objs[2] = date;
			objs[3] = map.get("type");
			objs[4] = map.get("spid") == null ? "" : map.get("spid");
			objs[5] = map.get("descption");
			dataList.add(objs);
		}
		ExportExcel ex = new ExportExcel(rowsName, dataList);
		ex.export(response, "黑名单");
	}

	*//**
	 * 检查手机号是否存在
	 * 
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("checkPhone")
	@ResponseBody
	public boolean checkPhone(@RequestParam(required = false) String id, @RequestParam(required = false) String phone,
			@RequestParam(required = false) String type, @RequestParam(required = false) String spid,
			HttpSession session) throws Exception {
		FormData data = new FormData();
		data.put("ID", id);
		data.put("MDN", phone);
		data.put("TYPE", type);
		data.put("SPID", spid);
		List<Map<String, Object>> checkPhone = service.checkPhone(data);
		return checkPhone == null || checkPhone.size() == 0;
	}

	// 导入黑名单
	@SuppressWarnings("unchecked")
	@RequestMapping("uploadTxt")
	@ResponseBody
	public String updateImage(@RequestParam(required = false) MultipartFile file, HttpServletRequest request,
			HttpSession session) throws Exception {
		return "200";
	}

	*//**
	 * 一键加黑，保存企业黑名单
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping("addBlack")
	@ResponseBody
	public Map<String, Object> addBlack(HttpSession session) throws Exception {
		FormData formData = getFormData();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> checkPhone = service.checkPhone(formData);

		if (checkPhone != null && checkPhone.size() != 0) {
			result.put("statusCode", (202));
			return result;
		}

		formData.put("CREATETIME", new Date());
		formData.put("DESCPTION", "一键加黑");
		if ("0".equals(formData.get("SPID")) || Tools.isEmpty(formData.get("SPID") + "")) {
			formData.put("TYPE", "1");
		} else {
			formData.put("TYPE", "3");
		}
		int res = service.saveBlacklist(formData);
		if(res>0){  // 向redis和底层中添加应用黑名单
//			SpInfo spinfoById = spinfoService.getSpinfoById(Integer.parseInt(formData.getString("SPID")));
			redisService.insertBlackListRedis("", formData.getString("MDN"), "3", formData.getString("SPID"),
					formData.get("ID").toString());
			redisService.sendBlackInfo(RedisMessage.TYPE_USER_SP, RedisMessage.EVENTTYPE_ADD, Integer.parseInt(formData.get("ID").toString()),
					formData.getString("MDN"), Integer.valueOf(formData.getString("SPID")), 1);
		}
		result.put("statusCode", (res >= 1 ? 200 : 201));
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("downloadTemplate")
	@ResponseBody
	public void downloadTemplate(HttpSession session, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		// String path = Const.USERIMAGES + "templete/blackTemplete.txt";
		String path = adminconfig.getService_nginx_url() + "templete/blackTemplete.txt";
		FileManageUtils.exportFile(response, path, "黑名单模板.txt");
	}

	@RequestMapping(value = "/insertBlackTxt", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertBlackTxt(@RequestBody List<Map<String, Object>> listData,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		synchronized (this) {
			try {
				String type = listData.get(0).get("TYPE").toString();
				String SPID = listData.get(0).get("SPID").toString();
				if(type.equals("3")){
					SpInfo spinfo = spinfoService.getSpinfoById(Integer.parseInt(SPID));
					if(spinfo == null){
						result.put("code", "206");
						result.put("message", "应用id没有对应的上线应用！");
						return result;
					}
				}
				FormData data = new FormData();
				data.put("TYPE", Integer.parseInt(type));
				// add by zyq at 20161130 对liestData 进行重复性检查
				if (listData.size() >= 500) {
					List<String> sysPhone = service.selAllSysBlackList(data);
					Set<String> set = new HashSet<String>(sysPhone);
					for (int i = 0; i < listData.size(); i++) {
						if (set.contains(listData.get(i).get("MDN").toString())) {
							listData.remove(i);
							i--;
						}
					}
				} else {
					data.put("SPID", SPID);
					List<String> mobiles = new ArrayList<String>();
					for (int i = 0; i < listData.size(); i++) {
						mobiles.add(listData.get(i).get("MDN").toString());
					}
					data.put("mdns", mobiles);
					List<String> sysPhone = service.selBlackList(data);
					Set<String> set = new HashSet<String>(sysPhone);
					for (int i = 0; i < listData.size(); i++) {
						if (set.contains(listData.get(i).get("MDN").toString())) {
							listData.remove(i);
							i--;
						}
					}
				}
				if (listData.size() == 0) {
					result.put("code", "205");
					result.put("message", "导入的数据与已有数据全部重复！");
					return result;
				}
				List<Map<String, Object>> batchList = new ArrayList<Map<String, Object>>();
				int redis_type = type.equals("1") ? RedisMessage.TYPE_SYSTEM : type.equals("2") ? RedisMessage.TYPE_CHANNEL : RedisMessage.TYPE_USER_SP ;
				for (int i = 1; i <= listData.size(); i++) {
					batchList.add(listData.get(i - 1));
					if (i % 1000 == 0 || i == listData.size()) {
						service.insertBatchBlackList(batchList);
						if("3".equals(type)){
							redisService.insertBlackListToRedis(batchList);
						}
						redisService.sendBatchBlackInfo(redis_type, RedisMessage.EVENTTYPE_ADD,
								batchList);
						batchList.clear();
					}
				}
				
				result.put("code", "0");
				result.put("message", "导入成功");
			} catch (Exception e) {
				e.printStackTrace();
				result.put("code", "-1");
				result.put("message", "batchInsertBlack failed!");
			}
		}
		return result;

	}

	private String formatString(String str) {
		if (str.length() > 10) {
			return str.substring(0, 8) + "…";
		} else {
			return str;
		}
	}

*/}
