package com.fpt.etc.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String upload(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary upload failed", e);
        }
    }

    public void delete(String url) {
        try {
            // Lấy publicId từ url (Cloudinary url format: .../image/upload/v1234567890/<publicId>.jpg)
            String publicId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary delete failed", e);
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));
        return uploadResult.get("secure_url").toString();
    }

    // Xoá file theo public_id (ví dụ: "folder/filename")
    public String deleteFile(String publicId) throws IOException {
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return result.get("result").toString(); // "ok" nếu xoá thành công
    }
}

