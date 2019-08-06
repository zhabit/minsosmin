package com.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 用于实现基于ftp的上传于下载
 * 
 * @author zhdd
 * 
 */
public class FTPUtil {

	public static String ftpIp = null; // ftp的Ip
	public static String ftpPort = "21";// 端口
	private static String ftpUsername = null; // 用户名
	private static String ftpUserpassword = null;// 密码
	public static String path="ESBMANAGE/";
	
	/**
	 * 根据配置文件config.properties获取ftp配置信息
	 */
	static {
		
		ftpIp ="10.98.0.33";
		ftpUsername ="nhctftp1"; 
		ftpUserpassword ="nhctftp1";
	}

	/**
	 * 连接到FTP服务器
	 * 
	 * @param hostname
	 *            主机名
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @throws IOException
	 */
	public static FTPClient connect() {
		FTPClient ftpClient = new FTPClient();
		try {
			//System.out.println("正在进行连接..........");
			ftpClient.connect(ftpIp, Integer.parseInt(ftpPort));
			//System.out.println("连接成功..........");
			ftpClient.setControlEncoding("GBK");
			FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
			conf.setServerLanguageCode("zh");
			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				if (ftpClient.login(ftpUsername, ftpUserpassword)) {
				//	System.out.println("---------------------------------登陆成功------------------------------------");
					return ftpClient;
				} else {
				//	System.out.println("---------------------------------登陆失败------------------------------------");
				}
			}
			// ftpClient.logout();
		} catch (SocketException e) {
		//	System.out.println("-------------------------连接服务器ftp失败---------------------------");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// disconnect(ftpClient);
		return null;
	}

	/**
	 * 上传文件到ftp
	 * 
	 * @param filename
	 *            上传文件名
	 * @param inputStream
	 *            输入流
	 * @param fileCatalog
	 *            文件保存路径
	 * @param zllx
	 *            上传文件类型 用于区分在ftp上存放的文件夹
	 * @return
	 */
	public static boolean ftpUpload(String filename, InputStream inputStream,
			String fileCatalog) {
		FTPClient ftpClient = null;
		try {
			ftpClient = connect();
			if (ftpClient != null) {
				// 设置被动模式
				ftpClient.enterLocalPassiveMode();
				// 设置以二进制方式传输
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				if (FTPUtil.createDirecroty(ftpClient, fileCatalog)) {
					ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
					ftpClient.storeFile(new String(filename.getBytes(),"iso-8859-1"), inputStream);
					//ftpClient.storeFile(filename, inputStream);
				}
			
			
			//	System.out.println("------------上传成功----------------");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			disconnect(ftpClient);	
		}
	//	System.out.println("------------上传失败 请稍后----------------");
		
		return false;
	}

	/**
	 * 从ftp上下载文件
	 * 
	 * @param filename
	 *            文件存放在ftp上的显示名
	 * @param response
	 * @param viewName
	 *            文件在本地的显示名
	 * @param zllx
	 *            文件类型
	 * @return
	 */
	public static String ftpDownload(String filename,
			HttpServletResponse response, String viewName, String fileCatalog) {

		String downStatus = "内部错误";// 错误由于ftp服务未开启或连接不成功

		if (viewName == null || "".equals(viewName)) {
			viewName = filename;
		}
	//	byte[] bytes = null;
		ByteArrayOutputStream out = null;
		FTPClient ftpClient = null;
		boolean flag = false;
		try {
			ftpClient = FTPUtil.connect();

			if (ftpClient != null) {
				// FTPFile[] remoteFiles = ftpClient.listFiles(remoteDir);
				// 设置被动模式
				ftpClient.enterLocalPassiveMode();
				// 设置以二进制方式传输
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				String remoteDir = getRomoteDir(fileCatalog); // 资料所在文件夹
				String remoteFile = remoteDir + filename;
				String downFilePath = new String(remoteFile.getBytes("GBK"),"iso-8859-1");
//				System.out.println("downFilePath---" + downFilePath);
				FTPFile[] remoteFiles = ftpClient.listFiles(downFilePath);
				// System.out.println(remoteFiles1.length);
				// ////////
				if (remoteFiles != null && remoteFiles.length > 0) {

					if (response != null)
						try {
							OutputStream os = null;
							os = response.getOutputStream();
							response.reset();
							response.addHeader("Content-Disposition",
									"attachment;filename="
											+ new String((viewName)
													.getBytes("gbk"),
													"ISO-8859-1"));
							response.addHeader("Content-Length", remoteFiles[0]
									.getSize()
									+ "");
							response.setCharacterEncoding("utf-8");
							response.setContentType("application/octec-stream");
							boolean status = ftpClient.retrieveFile(
									downFilePath, os);
							if (status) {
								downStatus = "下载完成！";
							} else {
								downStatus = "下载失败";
							}
							os.flush();
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					else {
					//	System.out.println("指定目录下载D:/downFtpTest.txt");
						OutputStream os = new FileOutputStream(
								"D:/downFtpTest.txt");
						ftpClient.retrieveFile(downFilePath, os);
						/*
						 * os.write(bytes); os.flush();
						 */
						os.close();
				//		System.out.println("-------------下载完成-------------");
					}
				} else {
					downStatus = "系统找不到指定文件！";// 找不到指定文件
				}
				// ftpClient.logout();
			}
		} catch (Exception e) {
			// disconnect(ftpClient);
			e.printStackTrace();
		}finally {  
			disconnect(ftpClient);
		}
		return downStatus;
	}
	
	
	public static byte[] ftpDownloadH3c(String filename,String fileCatalog) {

		byte[] bytes = null;
		ByteArrayOutputStream out = null;
		FTPClient ftpClient = null;
		try {
			ftpClient = FTPUtil.connect();

			if (ftpClient != null) {
				
				ftpClient.enterLocalPassiveMode();
				// 设置以二进制方式传输
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				String remoteDir = getRomoteDir(fileCatalog); // 资料所在文件夹
				String remoteFile = remoteDir + filename;
				String downFilePath = new String(remoteFile.getBytes("GBK"),"iso-8859-1");
				FTPFile[] remoteFiles = ftpClient.listFiles(downFilePath);
				
				if (remoteFiles != null && remoteFiles.length > 0) {
		
					
					InputStream in=ftpClient.retrieveFileStream(new
					  String(remoteFile.getBytes("GBK"),"iso-8859-1"));
					  //（AAA）获得该文件的输入流 
					out = new ByteArrayOutputStream(1024);
					  bytes = new byte[1024]; 
					  int n=0; 
					  while ((n =in.read(bytes)) >0) { out.write(bytes, 0, n);
					  out.flush(); } 
					  in.close(); 
					  out.close();
					  ftpClient.completePendingCommand();
					
				} else {

				//	downStatus = "系统找不到指定文件！";// 找不到指定文件
				}
				// ftpClient.logout();
			}
		} catch (Exception e) {
			// disconnect(ftpClient);
			e.printStackTrace();
		}finally {
			disconnect(ftpClient);
		}
		
		return out==null?new byte[1024]:out.toByteArray();
	}
	
	//复制文件 
	//srcPath：原文件路径  
	//destPath：复制后生成的新路径
	public static void ftpCopy(String fileName,String srcPath, String destPath) throws IOException {  
		
		FTPClient ftpClient = null;
		try {
			ftpClient = FTPUtil.connect();
			if (ftpClient != null) {
				ftpClient.enterLocalPassiveMode();
				// 设置以二进制方式传输
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				String remoteDir = getRomoteDir(srcPath); // 资料所在文件夹
				String remoteFile = remoteDir + fileName;
				String downFilePath = new String(remoteFile.getBytes("GBK"),"iso-8859-1");
				FTPFile[] remoteFiles = ftpClient.listFiles(downFilePath);
				if (remoteFiles != null && remoteFiles.length > 0) {
					InputStream fis = ftpClient.retrieveFileStream(new String(remoteFile.getBytes("GBK"),"iso-8859-1"));
					FTPUtil.ftpUpload(fileName, fis, destPath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {  
			disconnect(ftpClient);
		}
	}  
	
    /** 
* 复制文件. 
*  
* @param sourceFileName 未标题-1.jpg
* @param newFileCatalog 
* @param targetFile ftp://10.8.1.186:21/FTP013/CI/4f02621aeb094c6ebd61c2f3fdc55dd5/9F1E37961DEEBACDA4D52822210ADC24/0231A8HF/未标题-1.jpg
* @throws IOException 
*/  
public static void copyFile(String sourceFileName, String sourceDir, String targetDir, String newFileCatalog) throws IOException {  
  ByteArrayInputStream in = null;  
  ByteArrayOutputStream fos = new ByteArrayOutputStream(); 
  FTPClient ftpClient = null;
  try {  
	  ftpClient = FTPUtil.connect();
	  if (ftpClient != null) {
	      createDirecroty(ftpClient, newFileCatalog);
		  ftpClient.setBufferSize(1024 * 2);  
	      // 变更工作路径  
		  ftpClient.changeWorkingDirectory("/");
		  
	      ftpClient.changeWorkingDirectory(sourceDir);  
	      // 设置以二进制流的方式传输  
	      ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
	      // 将文件读到内存中  
	      ftpClient.retrieveFile(new String(sourceFileName.getBytes("GBK"), "iso-8859-1"), fos);  
	      in = new ByteArrayInputStream(fos.toByteArray());  
	      if (in != null) {
	    	  ftpClient.changeWorkingDirectory("/");
	          ftpClient.changeWorkingDirectory(targetDir);  
	          ftpClient.storeFile(new String(sourceFileName.getBytes("GBK"), "iso-8859-1"), in);  
	      }  
	  }
  } finally {  
      // 关闭流  
      if (in != null) {  
          in.close();  
      }  
      if (fos != null) {  
          fos.close();  
      }
      disconnect(ftpClient);
  }  
 
}  
	
	/**
	 * 从ftp上获取文件输入流
	 * @param filename
	 *            文件存放在ftp上的显示名
	 * @param fileCatalog
	 * @param zllx
	 *            文件类型
	 * @return
	 */
	public static InputStream getFtpInputStream(String filename, String fileCatalog) {
		
		InputStream is = null;
		FTPClient ftpClient = null;
		try {
			ftpClient = FTPUtil.connect();

			if (ftpClient != null) {
				
				// 设置被动模式
				ftpClient.enterLocalPassiveMode();
				// 设置以二进制方式传输
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				String remoteDir = getRomoteDir(fileCatalog); // 资料所在文件夹
				String remoteFile = remoteDir + filename;
				String downFilePath = new String(remoteFile.getBytes("GBK"),"iso-8859-1");
				FTPFile[] remoteFiles = ftpClient.listFiles(downFilePath);
				if (remoteFiles != null && remoteFiles.length > 0) {
				//	System.out.println("输出的文件为RemoteFile ==="
				//			+ remoteFiles[0].getName() + "---");
					is = ftpClient.retrieveFileStream(new String(remoteFile.getBytes("GBK"), "iso-8859-1"));
				} else {
				//	System.out.println("--------------------系统找不到指定文件-------------------");
				}
				// ftpClient.logout();
			}
		} catch (Exception e) {
			// disconnect(ftpClient);
			e.printStackTrace();
		}finally {  
			disconnect(ftpClient);
		}
		
		return is;
	}

	/**
	 * 根据文件路径 删除ftp上目标文件
	 * 
	 * @param filename
	 *            文件路径
	 * @param wjlx
	 *            文件类型（用于判断文件所在路径）
	 * @return 删除成功否
	 */
	public static boolean ftpDelFile(String filename, String fileCatalog) {
		boolean flag = false;
		String pathname = getRomoteDir(fileCatalog) + filename;
		FTPClient ftpClient = null;
		try {
			ftpClient = connect();
			if (ftpClient != null) {
				ftpClient.deleteFile(new String(pathname.getBytes("GBK"),
						"iso-8859-1"));// 若服务器上不存在该文件则为false;
				// int fileCount = ftpClient.dele(pathname);//删除需有权限
				flag = true;
				disconnect(ftpClient);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	//	System.out.println("删除结果：" + flag);
		return flag;
	}



	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param ftpClient
	 *            FTPClient对象
	 * @param fileCatalog
	 *            上传的文件路径
	 * @param zllx
	 *            上传的文件类型（
	 * @return 当已有目录hh时 现在需要新建目录hh/dd时则会认为是已有目录
	 *         不会新建.而若没有hh的话则会递归新建目录
	 * @throws IOException
	 */
	public static boolean createDirecroty(FTPClient ftpClient,
			String fileCatalog) throws IOException {
		
		String remote = getRomoteDir(fileCatalog);
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
	//	System.out.println("directory.equalsIgnoreCase(/)="
	//			+ directory.equalsIgnoreCase("/"));
		if (!directory.equalsIgnoreCase("/")
				&& !ftpClient.changeWorkingDirectory(new String(directory
						.getBytes("GBK"), "iso-8859-1"))) {
			// System.out.println("如果远程目录不存在，则递归创建远程服务器目录    "+directory.equalsIgnoreCase("/"));
			// 如果远程目录不存在，则递归创建远程服务器目录
		//	System.out.println("目录不存在，即将创建指定目录....");
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
	//		System.out.println("directory.startsWith(/)="
	//				+ directory.startsWith("/") + "---stsrt---" + start
	//				+ "---end--" + end);
			while (true) {
				String subDirectory = new String(remote.substring(start, end)
						.getBytes("GBK"), "iso-8859-1");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						return false;
					}
				}

				start = end + 1;
				end = directory.indexOf("/", start);
				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		} else {
		//	System.out.println("已有目录");
		}
		return true;
	}

	/**
	 * 根据资料类型获得 相应的存放路径 文件目录需以 / 结束
	 * 
	 * @param zllx
	 *            资料类型：图片，文档，音频，视频
	 * @param history 
	 * @return
	 */
	public static String getRomoteDir(String fileCatalog) {
		String remote =path+fileCatalog;
		return remote;
	}


	/** */
	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws IOException
	 */
	public static void disconnect(FTPClient ftpClient) {
		if (ftpClient != null) {
			try {
				if (ftpClient.isConnected()) {
					//ftpClient.logout();
					ftpClient.disconnect();
					// System.out.println("-----------------ftp连接已断开-------------------------");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获得ftp地址
	 * 
	 * @return
	 */
	public static String getHostname() {
		String hostName = "ftp://" + FTPUtil.ftpIp + ":" + FTPUtil.ftpPort
				+ "/";
		return hostName;
	}

	/**
	 * 判断是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNullOrBlank(final String s) {
		if (s == null) {
			return true;
		}
		if (s.trim().equals("")) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
	
		try {
			/**
			 * 测试自动创建目录
			 */
			/*
			 * FTPClient ftpClient; ftpClient = FTPUtil.connect(); //仅仅测试连接
			 * createDirecroty(ftpClient, "静安资料目录","d");
			 */
			
			/**
			 * 测试本地上传文件
			 * // 测试连接上传文件 若ftp上已存在同名文件 则会覆盖原有文件
			 */
//			 File file = new File("D:/testFTP.txt"); 
//			 InputStream is = new FileInputStream(file); 
//			 ftpUpload("testFTP123.txt", is,"静安资料目录","d"); //
//			 System.out.print(FTPUtil.getHostname()+FTPUtil.getRomoteDir("静安资料目录",null)+"testFTP123.txt");
			 
			/**
			 * 下载指定文件到本地测试
			 */
//			 ftpDownload("testFTP123.txt", null,"testFTP123.txt","静安资料目录","d");//下载测试 指定目录下载D:/downFtpTest.txt
			 
			/**
			 * 删除测试
			 */
//			ftpDelFile("testFTP.txt", "静安资料目录", "d");
			 
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	


}