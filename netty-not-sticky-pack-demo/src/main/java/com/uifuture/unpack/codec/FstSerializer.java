/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.unpack.codec;

import org.nustaq.serialization.FSTConfiguration;

/**
 * @author chenhx
 * @version FstSerializer.java, v 0.1 2018-08-08 下午 2:39
 */
public class FstSerializer {
    private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        return (T) conf.asObject(data);
    }

    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj) {
        return conf.asByteArray(obj);
    }
}