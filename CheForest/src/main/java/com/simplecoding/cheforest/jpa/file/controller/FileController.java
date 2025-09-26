package com.simplecoding.cheforest.jpa.file.controller;

import com.simplecoding.cheforest.jpa.file.dto.FileDto;
import com.simplecoding.cheforest.jpa.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    // 파일 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok().build();
    }

    // 프로필 업로드
    @PostMapping("/profile-upload")
    public ResponseEntity<FileDto> uploadProfileImage(
            @RequestParam("memberId") Long memberId,
            @RequestParam("profileImage") MultipartFile file
    ) throws IOException {
        // 기존 프로필 삭제
        FileDto oldProfile = fileService.getProfileFileByMemberId(memberId);
        if (oldProfile != null) {
            fileService.deleteFile(oldProfile.getFileId());
        }

        // 새 프로필 저장
        FileDto newFile = fileService.saveFile(file, "MEMBER", memberId, "PROFILE", memberId);

        // 실제 Member 테이블 profile 컬럼 업데이트는 MemberService에서 따로 처리
        return ResponseEntity.ok(newFile);
    }

    // 프로필 이미지 조회
    @GetMapping("/profile/{fileId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);

        if (fileDto == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] fileData = fileService.loadFileData(fileDto); // 실제 byte[] 리턴하는 메서드 필요
        return ResponseEntity.ok()
                .header("Content-Type", fileService.getMimeType(fileDto.getFileType()))
                .header("Content-Disposition", "inline; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileData);
    }
}
