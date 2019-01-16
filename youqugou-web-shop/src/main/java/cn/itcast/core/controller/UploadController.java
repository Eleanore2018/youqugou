package cn.itcast.core.controller;

import cn.itcast.common.util.FastDFSClient;
import cn.itcast.core.entity.Result;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String url;

    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) {
        try {
            // 使用工具类
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            // apache自带的文件名工具类获取扩展名
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());

            // 上传文件
            String filePath = fastDFSClient.uploadFile(file.getBytes(), ext);
            return new Result(true,url + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "文件上传失败");
        }
    }
}
