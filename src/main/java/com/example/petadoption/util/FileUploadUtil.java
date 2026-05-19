package com.example.petadoption.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传工具类
 * 将上传的文件保存到本地指定目录，返回访问路径
 */
@Component
public class FileUploadUtil {

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 上传单个文件
     * @param file   上传的文件
     * @param subDir 子目录名（如 "pet" 表示宠物图片）
     * @return 数据库中存储的相对路径（如 /uploads/pet/abc123.jpg）
     */
    public String upload(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // ① 确保目标目录存在
        File dir = new File(uploadPath, subDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ② 生成唯一文件名（避免重名覆盖）
        String originalName = file.getOriginalFilename();
        String suffix = "";
        if (originalName != null && originalName.contains(".")) {
            suffix = originalName.substring(originalName.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        // ③ 保存到本地
        File dest = new File(dir, newFileName);
        file.transferTo(dest);

        // ④ 返回访问路径（配合 WebConfig 中配置的虚拟路径映射）
        return "/uploads/" + subDir + "/" + newFileName;
    }
}
