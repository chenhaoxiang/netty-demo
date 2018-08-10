/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.hearbest.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author chenhx
 * @version RequestInfo.java, v 0.1 2018-08-10 上午 9:50
 */
@Data
public class RequestInfo implements Serializable {
    /**
     * 以ip为标识
     */
    private String ip;
    private HashMap<String, Object> cpuPercMap;
    private HashMap<String, Object> memoryMap;
}