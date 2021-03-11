package com.zhouxug.ftp.service;

import com.zhouxug.ftp.entity.User;
import com.zhouxug.ftp.entity.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    ResultVO download(HttpServletResponse response, User user, String relativeRemotePathAndName);

    ResultVO fileList(User user, String basePath, List<Map> nameList);
}
