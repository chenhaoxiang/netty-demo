package com.uifuture.unpack.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应数据
 */
@Data
public class Response implements Serializable {
    private static final long serialVersionUID = -3136380221020337915L;
    private Long requestId;
    private String error;
    private Object result;
}
