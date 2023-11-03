package com.xiao.start.utils;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/27 17:25
 * @Description:
 *      基于ThreadLocal 封装的工具类，用来保存当前用户的登录信息
 *
 */
public class BaseContext {

    private static ThreadLocal threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的员工信息
     * @param obj 员工信息
     */
    public static void setCurrent(Object obj){

        threadLocal.set(obj);
    }

    /**
     * 获取当前线程的员工信息
     * @return 员工信息
     */
    public static Object get(){

        return threadLocal.get();
    }




}
