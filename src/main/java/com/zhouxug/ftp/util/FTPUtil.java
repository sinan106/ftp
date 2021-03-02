package com.zhouxug.ftp.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.Util
 * @ClassName: FTPUtil
 * @Author: ZXG
 * @Date: 2021/2/26 12:37
 */
public class FTPUtil {

    /***
     *
     * @param address IP地址
     * @param port 端口号
     * @param username 用户名
     * @param password 密码
     * @param encoding 编码
     * @return
     */
    public static FTPClient connectFTPServer(String address, int port,
                                             String username, String password, String encoding) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(encoding);

        try {
            /*  连接失败会抛出异常 */
            ftpClient.connect(address, port);
            ftpClient.login(username, password);

            /* 设置传输的文件类型
             *  BINARY_FILE_TYPE：二进制文件类型 */
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            /**
             * 设置被动模式
             */
            ftpClient.enterLocalPassiveMode();

            /* 确认是否正确完成相应 */
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {

                /* Abort a transfer in progress.
                 *  终止当前传输 */
                ftpClient.abort();
                /* 断开连接 */
                ftpClient.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("登陆失败");
        }

        return ftpClient;
    }

    public static void closeFTPServer(FTPClient ftpClient) {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.abort();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadSingleFile(FTPClient ftpClient, String absoluteLocalDirectory, String relativeRemotePathAndName) {
        if (!ftpClient.isConnected() || !ftpClient.isAvailable()) {
            System.out.println("链接无效");
        }
        if (StringUtils.isBlank(absoluteLocalDirectory) || StringUtils.isBlank(relativeRemotePathAndName)) {
            System.out.println("本地路径或远程路径为空");
        }

        try {
            /**
             * 切换工作目录
             */
            ftpClient.changeWorkingDirectory(relativeRemotePathAndName);

            /* 获得下载文件名称 */
            String targetFileName = relativeRemotePathAndName.substring(relativeRemotePathAndName.lastIndexOf("/") + 1);

            File localFile = new File(absoluteLocalDirectory + File.separatorChar + targetFileName);
            OutputStream os = new FileOutputStream(localFile);
            ftpClient.retrieveFile(targetFileName, os);
            os.flush();
            os.close();
            System.out.println(">>>>>FTP服务器文件下载完毕*********" + relativeRemotePathAndName);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean uploadSingleFile(FTPClient ftpClient, String relativeRemotePath, String fileName, InputStream input) {
        if (!ftpClient.isConnected() || !ftpClient.isAvailable()) {
            System.out.println(">>>>>FTP服务器连接已经关闭或者连接无效*****放弃文件上传****");
            return false;
        }


        try {
            ftpClient.changeWorkingDirectory(relativeRemotePath);

            ftpClient.storeFile(fileName, input);
            input.close();
            System.out.println("上传成功:" + fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void getFileNameList(FTPClient ftpClient, String basePath, List<Map> nameList) {
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(basePath);
            if (ftpFiles != null && ftpFiles.length > 0) {
                for (FTPFile ftpFile : ftpFiles) {
                    if (ftpFile.isFile()) {
                        String fileName = basePath + ftpFile.getName();
                        Map<String, Long> map = new HashMap<>();
                        map.put(fileName, ftpFile.getSize());
                        nameList.add(map);
                    } else {
                        /* 递归遍历所有文件名称 */
                        getFileNameList(ftpClient, basePath + ftpFile.getName() + "/", nameList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //测试连接关闭
    public static void main(String[] args) {
//        System.out.println("-----------------------应用启动------------------------");
//        FTPClient ftpClient = FTPUtil.ConnectFTPServer("152.136.102.20", 21, "ftpuser", "zxG_000106", "GBK");
//        System.out.println("FTP 连接是否成功：" + ftpClient.isConnected());
//        System.out.println("FTP 连接是否有效：" + ftpClient.isAvailable());
//        CloseFTPServer(ftpClient);
//        System.out.println("-----------------------应用关闭------------------------");

//        System.out.println("-----------------------应用启动------------------------");
//        FTPClient ftpClient = FTPUtil.ConnectFTPServer("152.136.102.20", 21, "ftpuser", "zxG_000106", "GBK");
//        System.out.println("FTP 连接是否成功：" + ftpClient.isConnected());
//        System.out.println("FTP 连接是否有效：" + ftpClient.isAvailable());
//        DownloadSingleFile(ftpClient, "G:\\code\\IdeaProjects\\ftp", "test/5.jpg");
//        DownloadSingleFile(ftpClient, "G:\\code\\IdeaProjects\\ftp", "2.png");
//        CloseFTPServer(ftpClient);
//        System.out.println("-----------------------应用关闭------------------------");

//        System.out.println("-----------------------应用启动------------------------");
//        FTPClient ftpClient = FTPUtil.ConnectFTPServer("152.136.102.20", 21, "ftpuser", "zxG_000106", "GBK");
//        System.out.println("FTP 连接是否成功：" + ftpClient.isConnected());
//        System.out.println("FTP 连接是否有效：" + ftpClient.isAvailable());
//        FileInputStream in=new FileInputStream(new File("C:\\Users\\zhoux\\Desktop\\3.jpg"));
//        UploadSingleFile(ftpClient, "test/", "3.jpg", in);
//        CloseFTPServer(ftpClient);
//        System.out.println("-----------------------应用关闭------------------------");

        System.out.println("-----------------------应用启动------------------------");
        FTPClient ftpClient = FTPUtil.connectFTPServer("152.136.102.20", 21, "ftpuser1", "zxG_000106", "GBK");
        System.out.println("FTP 连接是否成功：" + ftpClient.isConnected());
        System.out.println("FTP 连接是否有效：" + ftpClient.isAvailable());

        List<Map> nameList = new ArrayList<>(10);
        getFileNameList(ftpClient, "", nameList);
        System.out.println("nameList = " + nameList);
        closeFTPServer(ftpClient);
        System.out.println("-----------------------应用关闭------------------------");

    }
}


