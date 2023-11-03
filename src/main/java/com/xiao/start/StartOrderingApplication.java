package com.xiao.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/26 11:50
 * @Description:
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableCaching // 开始Spring Cache 注解方式的缓存功能
public class StartOrderingApplication {
    public static void main(String[] args) throws Exception {
        // 入口
        SpringApplication.run(StartOrderingApplication.class);
        log.info("StartOrderingApplication is running");
    }
}
