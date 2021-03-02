package com.zhouxug.ftp;

import com.zhouxug.ftp.dao.UserMapper;
import com.zhouxug.ftp.entity.User;
import com.zhouxug.ftp.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FtpApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void MD5Test() {
        String str = "zxG_000106";
        User user = new User("ftpuser1", MD5Util.str2MD5(str));
        userMapper.insert(user);
    }

}
