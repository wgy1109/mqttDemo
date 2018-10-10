package com.sound.util;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	
	public static String updaloadPic (MultipartFile file,String src,String filename) {
		if(file == null || file.isEmpty()) return "";
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(file.getInputStream());
			Iterator<?> it = ImageIO.getImageReaders(iis);
			if(!it.hasNext()){
				return "";
			}
			return uploadFile(file, src,filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String uploadFile (MultipartFile file,String src,String filename) {
		String fileDir = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//		String path = Const.USERIMAGES+src;
//		String path =  adminconfig.getService_nginx_url() + src;
		String path = src ;
		path  += "/" + fileDir;
		try {
			String fileName = file.getOriginalFilename();
			if(filename != null && filename.length()>0){
				String ex = "";
				if(file.getOriginalFilename().lastIndexOf(".")>0){
					ex = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				}
				fileName = filename  +ex;
			}
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path, fileName));
			String url = src+"/"+fileDir+"/"+fileName;
			return url;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
