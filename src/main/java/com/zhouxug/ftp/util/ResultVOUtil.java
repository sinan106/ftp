package com.zhouxug.ftp.util;

import com.zhouxug.ftp.entity.VO.ResultVO;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.util
 * @ClassName: ResultVOUtil
 * @Author: ZXG
 * @Date: 2021/3/2 16:21
 */
public class ResultVOUtil {

    public static ResultVO success(String msg, Object object) {
        return new ResultVO<>(1, msg, object);
    }

    public static ResultVO fail(String msg) {
        return new ResultVO<>(0, msg, null);
    }
}
