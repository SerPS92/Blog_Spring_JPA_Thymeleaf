package com.example.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileService {

    private static final String folder = "src/main/resources/static/images//";
    private static final String defaultImage = "default.jpg";
    private static String fileName = defaultImage;

    public String saveImage(MultipartFile file) throws IOException{
        if(!file.isEmpty()){
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folder + file.getOriginalFilename());
            Files.write(path, bytes);
            fileName = file.getOriginalFilename();
        }
        return fileName;
    }

    public void deleteImage(String name){
        File file = new File(folder + name);
        file.delete();
    }
}
