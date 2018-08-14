/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.hearbest.utils;

import org.hyperic.sigar.Sigar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;

/**
 * Sigar工具类
 * 获取Sigar单例
 * @author chenhx
 * @version SigarUtil.java, v 0.1 2018-08-10 上午 10:20
 */
public class SigarUtil {
    static {
        // Linux MacOS 分隔符 : Windows 是;
        String osName = System.getProperty("os.name", "generic").toLowerCase();
        String splitSymbol = osName.contains("win") ? ";" : ":";

        // 寻找 classpath 根目录下的 sigar 文件夹
        URL sigarURL = SigarUtil.class.getResource("/sigar");
        if (null == sigarURL) {
            // 找不到抛异常
            throw new MissingResourceException("miss classpath:/sigar folder", SigarUtil.class.getName(), "classpath:/sigar");
        }
        File classPath = new File(sigarURL.getFile());
        String oldLibPath = System.getProperty("java.library.path");
        try {
            // 追加库路径
            String newLibPath = oldLibPath + splitSymbol + classPath.getCanonicalPath();
            System.setProperty("java.library.path", newLibPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Sigar getInstance() {
        return SingleSigar.SIGAR;
    }

    private static class SingleSigar {
        private static final Sigar SIGAR = new Sigar();
    }
}