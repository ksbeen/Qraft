package com.example.Qraft.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.Qraft.service.FileStorageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;

    /**
     * 영상 파일을 업로드하는 API
     * @param file 'video'라는 이름으로 전송된 MultipartFile
     * @return 서버에 저장된 파일 이름
     */
    @PostMapping("/api/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("video") MultipartFile file) {
        // FileStorageService를 사용하여 파일을 저장하고, 저장된 파일 이름을 받습니다.
        String fileName = fileStorageService.storeFile(file);

        // 성공 응답으로 저장된 파일 이름을 반환합니다.
        return ResponseEntity.ok(fileName);
    }
}