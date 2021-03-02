package com.zhouxug.ftp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.Config
 * @ClassName: MybatisPlusConfig
 * @Author: ZXG
 * @Date: 2021/2/26 21:26
 */

@Configuration
@MapperScan("com.zhouxug.ftp.dao")
public class MybatisPlusConfig {
}
