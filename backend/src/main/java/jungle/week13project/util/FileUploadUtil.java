package jungle.week13project.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public class FileUploadUtil {
    public static String saveImageFile(MultipartFile file, String uploadDir, String subDir) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            // 하위 디렉토리를 포함해서 저장
            String fullPath = uploadDir + File.separator + subDir;
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(fullPath + File.separator + fileName);

            // 임시파일을 읽어올 때 안정성을 높임 (transferTo 대신 stream copy)
            try (InputStream in = file.getInputStream();
                 OutputStream out = new FileOutputStream(dest)) {
                in.transferTo(out);
            }


            // DB에는 상대경로만 저장
            return subDir + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

}
