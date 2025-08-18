package com.example.Qraft.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Service: 이 클래스가 파일 저장과 관련된 비즈니스 로직을 처리하는 서비스 계층임을 나타냅니다.
 */
@Service
public class FileStorageService {

    // Path: 파일 시스템의 경로를 객체로 나타냅니다.
    // final: 이 변수는 생성 시에만 초기화되고, 이후에는 변경되지 않습니다.
    private final Path fileStorageLocation;

    public FileStorageService() {
        // 1. 'uploads' 라는 이름의 폴더를 파일 저장 위치로 지정합니다.
        //    - Paths.get("uploads"): 상대 경로를 나타내는 Path 객체를 생성합니다.
        //    - .toAbsolutePath(): 상대 경로를 절대 경로로 변환합니다. (예: /Users/user/project/uploads)
        //    - .normalize(): 경로 문자열을 정리합니다. (예: '..' 같은 부분 처리)
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            // 2. 'uploads' 폴더가 존재하지 않으면 생성합니다.
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            // 폴더 생성에 실패하면 예외를 발생시킵니다.
            throw new RuntimeException("파일을 업로드할 디렉토리를 생성할 수 없습니다.", ex);
        }
    }

    /**
     * 파일을 서버에 저장하는 메소드
     * @param file 클라이언트로부터 업로드된 파일
     * @return 서버에 저장된 고유한 파일 이름
     */
    public String storeFile(MultipartFile file) {
        // 1. 파일 이름이 중복되지 않도록 UUID를 사용하여 고유한 파일 이름을 생성합니다.
        //    - UUID.randomUUID().toString(): 'a1b2c3d4-...' 와 같은 랜덤 문자열을 생성합니다.
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        try {
            // 2. 파일이 저장될 최종 경로를 결정합니다. (예: /Users/.../uploads/a1b2c3d4.webm)
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            // 3. 파일의 입력 스트림을 읽어와서 지정된 경로에 복사(저장)합니다.
            //    - StandardCopyOption.REPLACE_EXISTING: 만약 같은 이름의 파일이 이미 있다면 덮어씁니다.
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException ex) {
            // 파일 저장 중 입출력 에러가 발생하면 예외를 발생시킵니다.
            throw new RuntimeException("파일 " + uniqueFileName + "을(를) 저장할 수 없습니다. 다시 시도해주세요.", ex);
        }
    }
}