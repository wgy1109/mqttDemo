package com.sound.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpUtil {
	private FTPClient ftpClient;
	private String strIp;
	private String user;
	private String password;
	Logger logger = LoggerFactory.getLogger(FtpUtil.class);

	/*
	 * * Ftp构造函数
	 */
	public FtpUtil(String strIp, String user, String Password) {
		this.strIp = strIp;
		this.user = user;
		this.password = Password;
		this.ftpClient = new FTPClient();
	}

	/**
	 * @return 判断是否登入成功
	 */
	public boolean ftpLogin() {
		boolean isLogin = false;
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		this.ftpClient.setControlEncoding("GBK");
		this.ftpClient.configure(ftpClientConfig);
		try {
			this.ftpClient.connect(this.strIp);
			// FTP服务器连接回答
			int reply = this.ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.ftpClient.disconnect();
				logger.error("登录FTP服务失败！");
				return isLogin;
			}
			this.ftpClient.login(this.user, this.password);
			// 设置传输协议
			this.ftpClient.enterLocalPassiveMode();
			this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			logger.info("恭喜" + this.user + "成功登陆FTP服务器");
			isLogin = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(this.user + "登录FTP服务失败！" + e.getMessage());
		}
		this.ftpClient.setBufferSize(1024 * 2);
		this.ftpClient.setDataTimeout(30 * 1000);
		return isLogin;
	}

	/**
	 * @throws IOException
	 * @退出关闭服务器链接
	 */
	public void ftpLogOut() throws IOException {
		if (null != this.ftpClient && this.ftpClient.isConnected()) {
			try {
				boolean reuslt = this.ftpClient.logout();// 退出FTP服务器
				if (reuslt) {
					logger.info("成功退出服务器");
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.info("退出FTP服务器异常！" + e.getMessage());
				throw e;
			} finally {
				try {
					this.ftpClient.disconnect();// 关闭FTP服务器的连接
				} catch (IOException e) {
					e.printStackTrace();
					logger.info("关闭FTP服务器的连接异常！");
				}
			}
		}
	}

	/***
	 * 下载文件
	 * 
	 * @param remoteFileName
	 *            待下载文件名称
	 * @param localDires
	 *            下载到当地那个路径下
	 * @param remoteDownLoadPath
	 *            remoteFileName所在的路径
	 * @throws Exception
	 */
	public boolean downloadFile(String remoteFileName, String localDires, String remoteDownLoadPath) throws Exception {
		String strFilePath = localDires + remoteFileName;
		BufferedOutputStream outStream = null;
		boolean success = false;
		try {
			this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
			outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
			logger.info(remoteFileName + "开始下载....");
			success = this.ftpClient.retrieveFile(remoteFileName, outStream);
			if (success == true) {
				logger.info(remoteFileName + "成功下载到" + strFilePath);
				return success;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(remoteFileName + "下载失败");
			throw e;
		} finally {
			if (null != outStream) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (success == false) {
			logger.info(remoteFileName + "下载失败!!!");
		}
		return success;
	}

	/***
	 * @下载文件夹
	 * @param localDirectoryPath本地地址
	 * @param remoteDirectory
	 *            远程文件夹
	 * @throws Exception
	 */
	public boolean downLoadDirectory(String localDirectoryPath, String remoteDirectory) throws Exception {
		try {
			localDirectoryPath = localDirectoryPath + "//";
			new File(localDirectoryPath).mkdirs();
			FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (!allFile[currentFile].isDirectory()) {
					downloadFile(allFile[currentFile].getName(), localDirectoryPath, remoteDirectory);
				}
			}
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (allFile[currentFile].isDirectory()) {
					String strremoteDirectoryPath = remoteDirectory + "/" + allFile[currentFile].getName();
					downLoadDirectory(localDirectoryPath, strremoteDirectoryPath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("下载文件夹失败", e.getMessage());
			return false;
		}
		return true;
	}

	// FtpClient的Set 和 Get 函数
	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

}
