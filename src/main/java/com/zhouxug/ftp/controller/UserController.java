package com.zhouxug.ftp.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhouxug.ftp.dao.UserMapper;
import com.zhouxug.ftp.entity.Param.DownloadParam;
import com.zhouxug.ftp.entity.Param.UploadParam;
import com.zhouxug.ftp.entity.User;
import com.zhouxug.ftp.entity.VO.ResultVO;
import com.zhouxug.ftp.service.UserService;
import com.zhouxug.ftp.util.FTPUtil;
import com.zhouxug.ftp.util.ResultVOUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.controller
 * @ClassName: UserController
 * @Author: ZXG
 * @Date: 2021/3/2 16:32
 */

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("login")
    public ResultVO login(HttpSession session,
                          @RequestBody User user) {
        ResultVO login = userService.login(user);
        if (login.getCode().equals(1)) {
            session.setAttribute("user", user);
            System.out.println("session.getId() = " + session.getId());
        }
        return login;
    }

    // todo
    @PostMapping("logout")
    public ResultVO logout(HttpSession session) {
        User user = (User)session.getAttribute("user");


        if (Objects.isNull(user)) {
            return ResultVOUtil.fail("已退出登录");
        }
        ResultVO logout = userService.logout(user);
        session.removeAttribute("user");
        return logout;
    }

    @GetMapping("mount")
    public ResultVO getMount() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("is_login", 1);
        Integer count = userMapper.selectCount(wrapper);
        return ResultVOUtil.success("在线人数", count);
    }


    @PostMapping(value = "upload")
    public ResultVO upload(HttpSession session,
                           @RequestParam MultipartFile file,
                           // 注意这里是String 对接口时要注意
                           String uploadParam) throws IOException {
        User user = (User)session.getAttribute("user");
        if (Objects.isNull(user)) {
            return ResultVOUtil.fail("登录失效，请重新登录");
        }
        JSONObject jsonObject = new JSONObject(uploadParam);

        return userService.upload(user, file, jsonObject.get("path").toString(), jsonObject.get("fileName").toString());
    }

    @GetMapping("download")
    public ResultVO download(HttpServletResponse response,
                             HttpSession session,
                             @RequestParam String relativeRemotePathAndName) {
        User user = (User)session.getAttribute("user");
        if (Objects.isNull(user)) {
            return ResultVOUtil.fail("登录失效，请重新登录");
        }
        return userService.download(response, user,
                relativeRemotePathAndName);
    }

    @GetMapping("fileList")
    public ResultVO fileList(HttpSession session,
                             @RequestParam String bathPath) {
        User user = (User)session.getAttribute("user");
        if (Objects.isNull(user)) {
            return ResultVOUtil.fail("登录失效，请重新登录");
        }
        List<Map> list = new ArrayList<>(10);
        return userService.fileList(user, bathPath, list);
    }

//    @GetMapping("downLocal")
//    public ResultVO downLocal(HttpServletResponse response,
//                              @RequestParam String pathAndName) {
//        File file = new File(pathAndName);
//        if(!file.exists()){
//            return ResultVOUtil.fail("文件不存在");
//        }
//        response.reset();
//        response.setContentLength((int) file.length());
//        response.setHeader("Content-Disposition", "attachment;filename=" + pathAndName );
//
//        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
//            byte[] buff = new byte[10240];
//            OutputStream os  = response.getOutputStream();
//            int i = 0;
//            while ((i = bis.read(buff)) != -1) {
//                os.write(buff, 0, i);
//                os.flush();
//            }
//        } catch (IOException e) {
//            return ResultVOUtil.fail("下载失败");
//        }
//        return ResultVOUtil.success("下载成功", null);
//    }


//
//    @GetMapping("ll")
//    public String login(HttpSession session){
//        //模拟一个用户调用了登录接口，进入系统
//        return "用户登录";
//    }
//
//    @GetMapping("session")
//    public ResultVO getSession(HttpSession session){
//        Object user = session.getAttribute("user");
//        System.out.println("user = " + user);
//        return ResultVOUtil.success(" ", user.toString());
//    }

}
