package com.zhouxug.ftp.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @ProjectName: ftp
 * @Package: com.zhouxug.ftp
 * @ClassName: listener
 * @Author: ZXG
 * @Date: 2021/3/2 17:01
 */

//@Configuration
@WebListener
public class listener implements HttpSessionListener {

    private int mount = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        mount ++;
        System.out.println("新建Session mount= " + mount);
        se.getSession().getServletContext().setAttribute("mount", mount);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        if (mount > 0)
            mount --;
        System.out.println("销毁Session mount = " + mount);
        se.getSession().getServletContext().setAttribute("mount", mount);
    }
}
