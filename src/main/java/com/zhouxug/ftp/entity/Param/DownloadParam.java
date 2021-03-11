package com.zhouxug.ftp.entity.Param;

import lombok.Data;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp.entity.Param
 * @ClassName: DownloadParam
 * @Author: ZXG
 * @Date: 2021/3/8 12:20
 */

@Data
public class DownloadParam {

//    private String absoluteLocalDirectory;

    private String relativeRemotePathAndName;

}
