package com.shine.hotels.io;

/**
 * 处理网络请求的数据 
 *
 * @param <T>
 */
public interface IResponseHandler<T> {
    public T handleResponse(String response);
}
