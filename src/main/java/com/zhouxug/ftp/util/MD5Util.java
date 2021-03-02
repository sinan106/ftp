package com.zhouxug.ftp.util;

import cn.hutool.crypto.SecureUtil;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.Util
 * @ClassName: MD5Util
 * @Author: ZXG
 * @Date: 2021/2/26 21:24
 */
public class MD5Util {

    // 加密盐
    private static final String salt = "ftp";

    // str to md5
    public static String str2MD5(String str){
        String s = str + salt;
        return SecureUtil.md5(s);
    }
}
