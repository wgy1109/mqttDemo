package com.sound.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GZipUtil {
	/*
	 * 执行入口,rarFileName为需要解压的文件路径(具体到文件),destDir为解压目标路径
	 */
	static Logger logger = LoggerFactory.getLogger(GZipUtil.class);

	public static void unTargzFile(List<Map<String, Object>> allList, String rarFileName, String destDir) {
		unzipOarFile(allList, rarFileName, destDir);

	}

	public static void unzipOarFile(List<Map<String, Object>> allList, String rarFileName, String outputDirectory) {
		FileInputStream fis = null;
		ArchiveInputStream in = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			fis = new FileInputStream(rarFileName);
			GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
			in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
			bufferedInputStream = new BufferedInputStream(in);
			TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
			while (entry != null) {
				String name = entry.getName();
				String[] names = name.split("/");
				String fileName = outputDirectory;
				for (int i = 0; i < names.length; i++) {
					String str = names[i];
					fileName = fileName + File.separator + str;
				}
				if (name.endsWith("/")) {
					mkFolder(fileName);
				} else {
					File file = mkFile(fileName);
					logger.info("读取文件" + fileName);
					BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
					int b;
					while ((b = bufferedInputStream.read()) != -1) {
						bufferedOutputStream.write(b);
					}
					bufferedOutputStream.flush();
					bufferedOutputStream.close();
					Map<String, Object> map = new HashMap<String, Object>();
					List<String> phoneInfo = new ArrayList<String>();
					if (file.isFile() && file.exists()) { // 判断文件是否存在
						InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");// 考虑到编码格式
						BufferedReader bufferedReader = new BufferedReader(read);
						String lineTxt = null;
						while ((lineTxt = bufferedReader.readLine()) != null) {
							phoneInfo.add(lineTxt);
						}
						read.close();
					}
					clearFiles(fileName);
					map.put(file.getName(), phoneInfo);
					allList.add(map);
				}
				entry = (TarArchiveEntry) in.getNextEntry();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArchiveException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void mkFolder(String fileName) {
		File f = new File(fileName);
		if (!f.exists()) {
			f.mkdir();
		}
	}

	public static File mkFile(String fileName) {
		File f = new File(fileName);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public static void clearFiles(String workspaceRootPath) {
		File file = new File(workspaceRootPath);
		if (file.exists()) {
			deleteFile(file);
		}
	}

	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}
}
