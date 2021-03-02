package com.zhouxug.ftp.service;

import com.zhouxug.ftp.entity.User;
import com.zhouxug.ftp.entity.VO.ResultVO;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.service
 * @ClassName: UserService
 * @Author: ZXG
 * @Date: 2021/2/26 21:37
 */
public interface UserService {

    ResultVO login(User user);

    ResultVO logout(User user);
}
