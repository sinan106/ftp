package com.zhouxug.ftp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.net.ftp.FTPClient;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.Dao
 * @ClassName: User
 * @Author: ZXG
 * @Date: 2021/2/26 20:28
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId
    private String username;

    private String password;

    private Integer isLogin;

    @JsonIgnore
    @TableField(exist = false)
    private FTPClient ftpClient = null;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLogin = 0;
    }
}
