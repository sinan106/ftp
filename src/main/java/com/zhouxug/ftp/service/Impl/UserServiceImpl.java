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
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        userMapper.updateById(user);
        FTPUtil.closeFTPServer(user.getFtpClient());
        return ResultVOUtil.success("退出登录", user.getUsername());
    }

    @Override
    public ResultVO upload(User user, MultipartFile multipartFile, String path, String fileName) throws IOException {
        byte [] byteArr=multipartFile.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArr);

        boolean flag = FTPUtil.uploadSingleFile(user.getFtpClient(), path, fileName, inputStream);
        if (flag) {
            return ResultVOUtil.success("上传成功: ", path + fileName);
        }
        else {
            return ResultVOUtil.fail("上传失败");
        }
    }

    @Override
    public ResultVO download(User user, String absoluteLocalDirectory, String relativeRemotePathAndName) {
        boolean flag = FTPUtil.downloadSingleFile(user.getFtpClient(), absoluteLocalDirectory, relativeRemotePathAndName);
        if (flag) {
            return ResultVOUtil.success("下载成功: ", absoluteLocalDirectory);
        }
        else {
            return ResultVOUtil.fail("下载失败");
        }
    }


}
