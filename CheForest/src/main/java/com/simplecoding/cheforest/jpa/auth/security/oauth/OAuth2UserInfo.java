package com.simplecoding.cheforest.jpa.auth.security.oauth;

import java.util.Map;

public interface OAuth2UserInfo {
    String getId();
    String getName();
    String getEmail();
    String getImageUrl();
    String getIdKey();

    Map<String, Object> getAttributes(); // 원본 attributes 필요할 때 사용
}
