package com.zhouxug.ftp.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhouxug.ftp.dao.UserMapper;
import com.zhouxug.ftp.entity.User;
import com.zhouxug.ftp.entity.VO.ResultVO;
import com.zhouxug.ftp.service.UserService;
import com.zhouxug.ftp.util.FTPUtil;
import com.zhouxug.ftp.util.MD5Util;
import com.zhouxug.ftp.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.service.Impl
 * @ClassName: UserServiceImpl
 * @Author: ZXG
 * @Date: 2021/2/26 21:37
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public ResultVO login(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        User user1 = userMapper.selectOne(wrapper);

        if (Objects.isNull(user1)) {
            log.error("此用户不存在: " + user.getUsername());
            return ResultVOUtil.fail("用户不存在");
        }

        if (MD5Util.str2MD5(user.getPassword()).equals(user1.getPassword())) {
            user1.setIsLogin(1);
            userMapper.updateById(user1);
            FTPClient ftpClient = FTPUtil.connectFTPServer("152.136.102.20", 21, user.getUsername(), user.getPassword(), "GBK");
            user.setFtpClient(ftpClient);
            return ResultVOUtil.success("登录成功", user.getUsername());
        }

        log.error(user.getUsername() + " :密码错误");
        return ResultVOUtil.fail("密码错误");
    }

    @Override
    public ResultVO logout(User user) {
        user.setIsLogin(0);
        user.setPassword(MD5Util.str2MD5(user.getPassword()));


        userMapper.updateById(user);
        FTPUtil.closeFTPServer(user.getFtpClient());
        return ResultVOUtil.success("退出登录", user.getUsername());
    }

    @Override
    public ResultVO upload(User user, MultipartFile multipartFile, String path, String fileName) throws IOException {
        byte[] byteArr = multipartFile.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArr);

        boolean flag = FTPUtil.uploadSingleFile(user.getFtpClient(), path, fileName, inputStream);
        if (flag) {
            return ResultVOUtil.success("上传成功: ", path + fileName);
        } else {
            return ResultVOUtil.fail("上传失败");
        }
    }

    @Override
    public ResultVO download(HttpServletResponse response, User user, String relativeRemotePathAndName) {
        String fileName = FTPUtil.downloadSingleFile(user.getFtpClient(), "", relativeRemotePathAndName);
        if (fileName != null) {
            File file = new File("/" + fileName);
            if (!file.exists()) {
                return ResultVOUtil.fail("文件不存在");
            }
            response.reset();
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
                byte[] buff = new byte[10240];
                OutputStream os = response.getOutputStream();
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
                FileSystemUtils.deleteRecursively(new File("/" + fileName));
            } catch (IOException e) {
                return ResultVOUtil.fail("下载失败");
            }
            return ResultVOUtil.success("下载成功", null);
        } else {
            return ResultVOUtil.fail("下载失败");
        }
    }

    @Override
    public ResultVO fileList(User user, String basePath, List<Map> nameList) {
        FTPUtil.getFileNameList(user.getFtpClient(), basePath, nameList);
        return ResultVOUtil.success("获取文件列表", nameList);
    }


}
