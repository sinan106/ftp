package com.zhouxug.ftp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouxug.ftp.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.dao
 * @ClassName: UserMapper
 * @Author: ZXG
 * @Date: 2021/2/26 21:33
 */

@Repository
public interface UserMapper extends BaseMapper<User> {
}
