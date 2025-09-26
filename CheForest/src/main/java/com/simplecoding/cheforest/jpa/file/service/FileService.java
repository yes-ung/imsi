package com.simplecoding.cheforest.jpa.file.service;

import com.simplecoding.cheforest.jpa.common.MapStruct;
import com.simplecoding.cheforest.jpa.file.dto.FileDto;
import com.simplecoding.cheforest.jpa.file.entity.File;
import com.simplecoding.cheforest.jpa.file.repository.FileRepository;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final FileRepository fileRepository;
    private final MapStruct mapStruct;
    private final EntityManager em; // Member 참조용

    // 실제 파일 저장 경로 (외부 경로)
    private final String uploadDir = "C:/upload/";

    // 단일 파일 저장
    public FileDto saveFile(MultipartFile file, String useType, Long useTargetId, String usePosition, Long uploaderId) throws IOException {
        if (file.isEmpty()) return null;

        String orgFileName = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID() + "_" + orgFileName;

        // 실제 저장 위치
        java.io.File dest = new java.io.File(uploadDir + storedFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);

        // 업로더 Member 참조
        Member uploader = em.getReference(Member.class, uploaderId);

        // DB에 저장할 경로는 웹에서 접근 가능한 상대경로
        String webPath = "/upload/" + storedFileName;

        File fileEntity = File.builder()
                .fileName(storedFileName)
                .filePath(webPath) // 웹 접근 경로 저장
                .fileType(getFileExtension(orgFileName))
                .useType(useType)
                .useTargetId(useTargetId)
                .usePosition(usePosition)
                .uploader(uploader)
                .build();

        return mapStruct.toDto(fileRepository.save(fileEntity));
    }

    // 여러 파일 저장 + 첫 번째 파일 ID 반환
    public Long saveBoardFiles(Long boardId, Long uploaderId, List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) return null;

        Long firstFileId = null;
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            FileDto dto = saveFile(file, "BOARD", boardId, "CONTENT", uploaderId);
            if (firstFileId == null && dto != null) {
                firstFileId = dto.getFileId();
            }
        }
        return firstFileId;
    }

    // 파일 단건 조회
    public FileDto getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .map(mapStruct::toDto)
                .orElse(null);
    }

    // 실제 파일 읽어오기
    public byte[] loadFileData(FileDto fileDto) throws IOException {
        java.io.File file = new java.io.File(uploadDir + fileDto.getFileName());
        if (!file.exists()) {
            return null;
        }
        return java.nio.file.Files.readAllBytes(file.toPath());
    }

    // 게시글별 파일 목록 조회
    public List<FileDto> getFilesByBoardId(Long boardId) {
        return fileRepository.findByUseTypeAndUseTargetId("BOARD", boardId)
                .stream()
                .map(mapStruct::toDto)
                .collect(Collectors.toList());
    }

    // 파일 삭제 (DB + 실제 파일)
    public void deleteFile(Long fileId) {
        fileRepository.findById(fileId).ifPresent(file -> {
            java.io.File f = new java.io.File(uploadDir + file.getFileName());
            if (f.exists()) {
                f.delete();
            }
            fileRepository.delete(file);
        });
    }

    // 게시글 삭제 시 전체 삭제
    public void deleteAllByTargetIdAndType(Long targetId, String useType) {
        fileRepository.findByUseTypeAndUseTargetId(useType, targetId)
                .forEach(file -> {
                    java.io.File f = new java.io.File("C:/upload/" + file.getFileName());
                    if (f.exists()) {
                        f.delete();
                    }
                });
        fileRepository.deleteByUseTargetIdAndUseType(targetId, useType);
    }

    // 프로필 최신 파일 조회
    public FileDto getProfileFileByMemberId(Long memberId) {
        return fileRepository.findTop1ByUseTypeAndUseTargetIdOrderByInsertTimeDesc("MEMBER", memberId)
                .map(mapStruct::toDto)
                .orElse(null);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    // 회원 프로필 수정용
    @Transactional
    public FileDto replaceProfileImage(Long memberId, MultipartFile profileImage) throws IOException {
        // 기존 프로필 최신 파일 조회 후 삭제
        FileDto oldFile = getProfileFileByMemberId(memberId);
        if (oldFile != null) {
            deleteFile(oldFile.getFileId());
        }
        // 새 프로필 저장 (웹에서 접근 가능한 상대경로로 저장됨)
        return saveFile(profileImage, "MEMBER", memberId, "PROFILE", memberId);
    }
//   확장자를 MIME 타입으로 변환
    public String getMimeType(String extension) {
        if (extension == null) return "application/octet-stream"; // 기본값
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }

}
