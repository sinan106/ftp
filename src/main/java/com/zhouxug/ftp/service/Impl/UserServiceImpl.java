package com.zhouxug.ftp.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhouxug.ftp.dao.UserMapper;
import com.zhouxug.ftp.entity.User;
import com.zhouxug.ftp.entity.VO.ResultVO;
import com.zhouxug.ftp.service.UserService;
import com.zhouxug.ftp.util.MD5Util;
import com.zhouxug.ftp.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            return ResultVOUtil.success("登录成功", user.getUsername());
        }

        log.error(user.getUsername() + " :密码错误");
        return ResultVOUtil.fail("密码错误");
    }

    @Override
    public ResultVO logout(User user) {
        user.setIsLogin(0);
        userMapper.updateById(user);
        return ResultVOUtil.success("退出登录", user.getUsername());
    }
}
