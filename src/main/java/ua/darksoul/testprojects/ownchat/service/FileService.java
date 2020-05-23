package ua.darksoul.testprojects.ownchat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.darksoul.testprojects.ownchat.domain.Message;
import ua.darksoul.testprojects.ownchat.util.FileUtil;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileService {
    @Value("${upload.path}")
    private String uploadPath;
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    public void saveImg(Message message, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            Matcher matcher = IMG_REGEX.matcher(file.getOriginalFilename());

            if (matcher.find()) {
                String filename = FileUtil.saveFile(file, uploadPath);
                if (filename != null) {
                    message.setFilename(filename);
//                    message.setFile(file.getBytes());
                }
            }
        }
    }

    public void deleteFile(String fileName) {
        if(!StringUtils.isEmpty(fileName)) {
            File file = new File(uploadPath + "/" + fileName);

            if(file.exists()) {
                file.delete();
            }
        }
    }
}
