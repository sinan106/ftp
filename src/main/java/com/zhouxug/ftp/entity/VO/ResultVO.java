package com.zhouxug.ftp.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.entity.VO
 * @ClassName: ResultVO
 * @Author: ZXG
 * @Date: 2021/2/26 21:38
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO<T> {

    private Integer code;

    private String message;

    private T data;
}
