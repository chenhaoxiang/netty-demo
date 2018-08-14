package com.uifuture.unpack.protocol;


import lombok.Data;

import java.io.Serializable;

/**
 * 请求数据
 */
@Data
public class Request implements Serializable {
    private static final long serialVersionUID = -2747321595912488569L;
    private Long requestId;
    private String className;
    private String methodName;
    private Class<?> parameterType;
    private Object parameter;
}