package com.zhouxug.ftp.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhouxug.ftp.dao.UserMapper;
import com.zhouxug.ftp.entity.Param.UploadParam;
import com.zhouxug.ftp.entity.User;
import com.zhouxug.ftp.entity.VO.ResultVO;
import com.zhouxug.ftp.service.UserService;
import com.zhouxug.ftp.util.FTPUtil;
import com.zhouxug.ftp.util.ResultVOUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.controller
 * @ClassName: UserController
 * @Author: ZXG
 * @Date: 2021/3/2 16:32
 */

@CrossOrigin(origins = "*", maxAge = 3600)
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
        }
        return login;
    }

    @PostMapping("logout")
    public ResultVO logout(HttpSession session,
                           @RequestBody String username) {
        JSONObject jsonObject = new JSONObject(username);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", jsonObject.get("username"));
        User user = userMapper.selectOne(wrapper);
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
                           String uploadParam) throws IOException {
        User user = (User)session.getAttribute("user");
        if (Objects.isNull(user)) {
            return ResultVOUtil.fail("登录失效，请重新登录");
        }
        JSONObject jsonObject = new JSONObject(uploadParam);

        return userService.upload(user, file, jsonObject.get("path").toString(), jsonObject.get("fileName").toString());
    }
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
