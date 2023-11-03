package com.xiao.start.controller;

import com.xiao.start.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 师晓峰
 * @version V1.0
 * @date 2023/10/28 22:28
 * @Description: 通用的用户上传/下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {


    @Value("${imgPath}")
    private String imgPath;


    /**
     * 文件上传
     *
     * @param file 接受文件
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        // file 是一个临时文件，需要转存到指定的位置，不然就不会保留下拉
        log.info("file:{} ", file);
        // 原始文件名（01.jpg)
        String originName = file.getOriginalFilename();
        //String prefix = originName.split("\\.")[1];
        // .jpg
        String prefix = originName.substring(originName.lastIndexOf("."));
        // 使用UUID重新生成文件名
        String fileName = UUID.randomUUID().toString() + prefix;
        // 创建一个目录对象
        File dir = new File(imgPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            // 需要转存到指定的位置
            file.transferTo(new File(imgPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        // 返回的类型
        response.setContentType("image/jpeg");
        try {
            // 输入流读取文件内容
            fileInputStream = new FileInputStream(new File(imgPath + name));
            // 输出流将文件写会浏览器
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;

            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            fileInputStream.close();
            outputStream.close();
        }

    }
}
