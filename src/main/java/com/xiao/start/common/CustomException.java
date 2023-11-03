package com.xiao.start.common;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 15:53
 * @Description:
 *      自定义业务异常
 */
public class CustomException extends RuntimeException {

    /**
     * 自定义业务异常
     * @param message 错误信息
     */
    public CustomException(String message){
        super(message);
    }
}
