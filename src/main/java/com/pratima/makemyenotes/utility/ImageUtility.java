package com.pratima.makemyenotes.utility;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class ImageUtility {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        String imageUrl = null;
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "my-app")
            );

            imageUrl = uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    }


    public String deleteImageByUrl(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            return result.get("result").toString(); // "ok", "not found"
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private String extractPublicId(String imageUrl) {
        try {
            // Example URL:
            // https://res.cloudinary.com/.../image/upload/v12345/my-app/sample.png
           // https://res.cloudinary.com/dqkvjft06/image/upload/v1775717776/my-app/obpqovrshmkv4a73edgk.png
            String[] parts = imageUrl.split("/upload/");

            if (parts.length < 2) {
                throw new RuntimeException("Invalid Cloudinary URL");
            }

            String path = parts[1];
            // v1775717776/my-app/obpqovrshmkv4a73edgk.png

            // Remove version (v12345/)
            String withoutVersion = path.replaceFirst("v\\d+/", "");

            // Remove file extension (.png, .jpg etc.)
            int dotIndex = withoutVersion.lastIndexOf(".");
            String publicId = (dotIndex != -1)
                    ? withoutVersion.substring(0, dotIndex)
                    : withoutVersion;

            return publicId;

        } catch (Exception e) {
            throw new RuntimeException("Failed to extract public_id from URL");
        }
    }

}
