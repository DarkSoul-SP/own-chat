package ua.darksoul.testprojects.ownchat.util;/*
 *
 *@author DarkSoul
 */

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

abstract public class FileUtil {
    public static String saveFile(MultipartFile file, String uploadPath) throws IOException {
        File uploadDir = new File(uploadPath);
        String resultFileName = null;

        if (!uploadDir.exists()) {
            if(uploadDir.mkdir()) {
                String uuidFile = UUID.randomUUID().toString();
                resultFileName = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFileName));
            }
        } else {
            String uuidFile = UUID.randomUUID().toString();
            resultFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));
        }

        return resultFileName;
    }
}
