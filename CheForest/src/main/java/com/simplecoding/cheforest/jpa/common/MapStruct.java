package com.simplecoding.cheforest.jpa.common;

import com.simplecoding.cheforest.es.integratedSearch.dto.IntegratedSearchDto;
import com.simplecoding.cheforest.es.integratedSearch.entity.IntegratedSearch;
import com.simplecoding.cheforest.jpa.auth.dto.MemberDetailDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberSignupDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberUpdateDto;
import com.simplecoding.cheforest.jpa.chatbot.dto.ChatbotFaqDto;
import com.simplecoding.cheforest.jpa.chatbot.entity.ChatbotFaq;
import com.simplecoding.cheforest.jpa.file.entity.File;
import com.simplecoding.cheforest.jpa.board.dto.BoardDetailDto;
import com.simplecoding.cheforest.jpa.board.dto.BoardListDto;
import com.simplecoding.cheforest.jpa.board.dto.BoardSaveReq;
import com.simplecoding.cheforest.jpa.board.dto.BoardUpdateReq;
import com.simplecoding.cheforest.jpa.like.dto.LikeRes;
import com.simplecoding.cheforest.jpa.like.dto.LikeSaveReq;
import com.simplecoding.cheforest.jpa.like.entity.Like;
import com.simplecoding.cheforest.jpa.review.dto.ReviewDto;
import org.mapstruct.*;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.board.entity.Board;
import com.simplecoding.cheforest.jpa.review.entity.Review;
import com.simplecoding.cheforest.jpa.like.dto.LikeDto;
import com.simplecoding.cheforest.jpa.file.dto.FileDto;
import com.simplecoding.cheforest.jpa.recipe.dto.RecipeDto;
import com.simplecoding.cheforest.jpa.recipe.entity.Recipe;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapStruct {
    // ================= Member =================
    // 회원가입 DTO -> Member 엔티티
    Member toEntity(MemberSignupDto dto);

    // Member 엔티티 -> 상세 DTO (세션/조회용)
    MemberDetailDto toDetailDto(Member member);

    // 회원정보 수정 DTO -> Member 엔티티 (필드 덮어쓰기)
    void updateEntity(MemberUpdateDto dto, @MappingTarget Member member);

    // ================= Board =================
    // 목록 조회 DTO 변환
    @Mapping(target = "nickname", source = "writer.nickname")
    @Mapping(target = "writerIdx", source = "writer.memberIdx")
    BoardListDto toListDto(Board board);

    // 상세 조회 DTO 변환
    @Mapping(target = "boardId", source = "boardId")
    @Mapping(target = "nickname", source = "writer.nickname")
    @Mapping(target = "profile", source = "writer.profile")
    @Mapping(target = "writerIdx", source = "writer.memberIdx")
    BoardDetailDto toDetailDto(Board board);

    // 저장용 DTO → 엔티티
    @Mapping(target = "boardId", ignore = true)   // PK 자동 생성
    @Mapping(target = "writer", ignore = true)    // 서비스에서 Member 주입
    @Mapping(target = "viewCount", constant = "0L")
    @Mapping(target = "likeCount", constant = "0L")
    @Mapping(target = "thumbnail", ignore = true) // 파일 업로드 후 서비스에서 세팅
//  prepare / prepareAmount는 Service에서 문자열로 합쳐서 세팅할 예정
    Board toEntity(BoardSaveReq dto);

    // 수정용 DTO → 기존 엔티티 업데이트
    @Mapping(target = "writer", ignore = true)    // 작성자 교체는 서비스에서 처리
    @Mapping(target = "viewCount", ignore = true) // 조회수는 건드리지 않음
    @Mapping(target = "likeCount", ignore = true) // 좋아요 수도 건드리지 않음
    void updateEntity(BoardUpdateReq dto, @MappingTarget Board entity);

    // ================= Review =================
    // Review → ReviewDto
    @Mapping(source = "board.boardId", target = "boardId")   // Board 엔티티의 PK를 DTO로 변환
    ReviewDto toDto(Review entity);

    // ReviewDto → Review
    @Mapping(source = "boardId", target = "board.boardId")   // DTO의 boardId를 엔티티 Board 참조로 매핑
    Review toEntity(ReviewDto dto);
    // 유틸 함수 - 임시 닉네임 매핑
    default String getNickname(Long writerIdx) {
        // TODO: 실제 Member 엔티티에서 닉네임 조회하는 서비스와 연동
        return "익명";
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ReviewDto dto, @MappingTarget Review entity);

    // ================= File =================
    // Entity → DTO
    @Mapping(source = "uploader.memberIdx", target = "uploaderIdx")
    @Mapping(source = "uploader.loginId", target = "uploaderLoginId")
    FileDto toDto(File file);

    // DTO → Entity
    @Mapping(source = "uploaderIdx", target = "uploader.memberIdx")
    @Mapping(source = "uploaderLoginId", target = "uploader.loginId")
    File toEntity(FileDto fileDto);

    // Like
    // Entity → DTO
    LikeDto toDto(Like like);

    // DTO → Entity
    Like toEntity(LikeDto likeDto);

    // SaveReq → Entity
    Like toEntity(LikeSaveReq likeSaveReq);

    // DTO → Res
    LikeRes toRes(LikeDto likeDto);

    // Recipe
    Recipe toEntity(RecipeDto dto);
    RecipeDto toDto(Recipe entity);
    List<RecipeDto> toDtoList(List<Recipe> entities);
    List<Recipe> toEntityList(List<RecipeDto> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(RecipeDto dto, @MappingTarget Recipe entity);

    //    TODO: 통합검색관련) IntegratedSearch <-> IntegratedSearchDto
    IntegratedSearchDto toDto(IntegratedSearch integratedSearch);
    IntegratedSearch toEntity(IntegratedSearchDto integratedSearchDto);
    @Mapping(target = "ingredients",
            expression = "java(com.simplecoding.cheforest.jpa.common.util.StringUtil.joinList(boardSaveReq.getIngredientName()))")
    IntegratedSearch boardToEntity(BoardSaveReq boardSaveReq);

    // -- chat bot ----
    // Entity <-> DTO
    ChatbotFaqDto toDto(ChatbotFaq chatbotFaq);
    ChatbotFaq toEntity(ChatbotFaqDto chatbotFaqDto);
}
