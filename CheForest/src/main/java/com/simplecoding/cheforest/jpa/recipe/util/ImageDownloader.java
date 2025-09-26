package com.simplecoding.cheforest.jpa.recipe.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
public class ImageDownloader {

    // application.properties 에서 주입받음
    @Value("${recipe.image.root-path}")
    private String rootPath;

    public void downloadImage(String imageUrl, String localRelativePath) throws Exception {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            log.warn("❗ 무시됨 - 이미지 URL이 null 또는 빈 문자열입니다.");
            return;
        }

        imageUrl = imageUrl.trim();

        if (!(imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))) {
            log.warn("❗ 무시됨 - 유효하지 않은 이미지 URL: {}", imageUrl);
            return;
        }

        // 확장자 결정
        String extension = ".jpg";
        String lowerUrl = imageUrl.toLowerCase();
        if (lowerUrl.endsWith(".png")) extension = ".png";
        else if (lowerUrl.endsWith(".jpeg")) extension = ".jpeg";

        // 확장자 보정
        if (!(localRelativePath.toLowerCase().endsWith(".jpg") ||
                localRelativePath.toLowerCase().endsWith(".jpeg") ||
                localRelativePath.toLowerCase().endsWith(".png"))) {
            localRelativePath += extension;
        }

        Path absolutePath = Paths.get(rootPath, localRelativePath);
        log.info("📁 절대 저장 경로: {}", absolutePath.toAbsolutePath());

        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.createDirectories(absolutePath.getParent());
            Files.copy(in, absolutePath, StandardCopyOption.REPLACE_EXISTING);

            if (Files.exists(absolutePath)) {
                log.info("✅ 다운로드 완료: {}", absolutePath.toAbsolutePath());
            } else {
                log.error("❌ 파일 저장 실패: {}", absolutePath.toAbsolutePath());
            }
        } catch (Exception e) {
            log.error("❌ 이미지 다운로드 실패 - URL: {}, 경로: {}", imageUrl, absolutePath, e);
            throw e;
        }
    }
}
