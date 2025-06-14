package jungle.week13project.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtil {
    public static String saveImageFile(MultipartFile file, String uploadDir) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(uploadDir + File.separator + fileName);
            file.transferTo(dest);
            return "/uploads/" + fileName;  // 실제로는 정적 리소스 매핑 필요 (조금 이따 설명)
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

}
