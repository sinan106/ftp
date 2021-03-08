package com.zhouxug.ftp.service;

import com.zhouxug.ftp.entity.User;
import com.zhouxug.ftp.entity.VO.ResultVO;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    ResultVO upload(User user, MultipartFile multipartFile, String path, String fileName) throws IOException;

    ResultVO download(User user, String absoluteLocalDirectory, String relativeRemotePathAndName);
}
