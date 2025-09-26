package com.simplecoding.cheforest.jpa.common;

import com.simplecoding.cheforest.es.integratedSearch.dto.IntegratedSearchDto;
import com.simplecoding.cheforest.es.integratedSearch.entity.IntegratedSearch;
import com.simplecoding.cheforest.jpa.auth.dto.MemberDetailDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberSignupDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberUpdateDto;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.board.dto.BoardDetailDto;
import com.simplecoding.cheforest.jpa.board.dto.BoardListDto;
import com.simplecoding.cheforest.jpa.board.dto.BoardSaveReq;
import com.simplecoding.cheforest.jpa.board.dto.BoardUpdateReq;
import com.simplecoding.cheforest.jpa.board.entity.Board;
import com.simplecoding.cheforest.jpa.file.dto.FileDto;
import com.simplecoding.cheforest.jpa.file.entity.File;
import com.simplecoding.cheforest.jpa.like.dto.LikeDto;
import com.simplecoding.cheforest.jpa.like.dto.LikeRes;
import com.simplecoding.cheforest.jpa.like.dto.LikeSaveReq;
import com.simplecoding.cheforest.jpa.like.entity.Like;
import com.simplecoding.cheforest.jpa.recipe.dto.RecipeDto;
import com.simplecoding.cheforest.jpa.recipe.entity.Recipe;
import com.simplecoding.cheforest.jpa.review.dto.ReviewRes;
import com.simplecoding.cheforest.jpa.review.dto.ReviewSaveReq;
import com.simplecoding.cheforest.jpa.review.dto.ReviewUpdateReq;
import com.simplecoding.cheforest.jpa.review.entity.Review;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-24T18:07:08+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Azul Systems, Inc.)"
)
@Component
public class MapStructImpl implements MapStruct {

    @Override
    public Member toEntity(MemberSignupDto dto) {
        if ( dto == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.loginId( dto.getLoginId() );
        member.password( dto.getPassword() );
        member.email( dto.getEmail() );
        member.nickname( dto.getNickname() );

        return member.build();
    }

    @Override
    public MemberDetailDto toDetailDto(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberDetailDto memberDetailDto = new MemberDetailDto();

        memberDetailDto.setMemberIdx( member.getMemberIdx() );
        memberDetailDto.setLoginId( member.getLoginId() );
        memberDetailDto.setEmail( member.getEmail() );
        memberDetailDto.setNickname( member.getNickname() );
        memberDetailDto.setProfile( member.getProfile() );
        if ( member.getRole() != null ) {
            memberDetailDto.setRole( member.getRole().name() );
        }

        return memberDetailDto;
    }

    @Override
    public void updateEntity(MemberUpdateDto dto, Member member) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getMemberIdx() != null ) {
            member.setMemberIdx( dto.getMemberIdx() );
        }
        if ( dto.getNickname() != null ) {
            member.setNickname( dto.getNickname() );
        }
        if ( dto.getProfile() != null ) {
            member.setProfile( dto.getProfile() );
        }
    }

    @Override
    public BoardListDto toListDto(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardListDto.BoardListDtoBuilder boardListDto = BoardListDto.builder();

        boardListDto.nickname( boardWriterNickname( board ) );
        boardListDto.writerIdx( boardWriterMemberIdx( board ) );
        boardListDto.boardId( board.getBoardId() );
        boardListDto.category( board.getCategory() );
        boardListDto.title( board.getTitle() );
        boardListDto.viewCount( board.getViewCount() );
        boardListDto.likeCount( board.getLikeCount() );
        boardListDto.thumbnail( board.getThumbnail() );
        boardListDto.insertTime( board.getInsertTime() );
        boardListDto.difficulty( board.getDifficulty() );

        return boardListDto.build();
    }

    @Override
    public BoardDetailDto toDetailDto(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardDetailDto.BoardDetailDtoBuilder boardDetailDto = BoardDetailDto.builder();

        boardDetailDto.boardId( board.getBoardId() );
        boardDetailDto.nickname( boardWriterNickname( board ) );
        boardDetailDto.profile( boardWriterProfile( board ) );
        boardDetailDto.writerIdx( boardWriterMemberIdx( board ) );
        boardDetailDto.category( board.getCategory() );
        boardDetailDto.title( board.getTitle() );
        boardDetailDto.prepare( board.getPrepare() );
        boardDetailDto.content( board.getContent() );
        boardDetailDto.thumbnail( board.getThumbnail() );
        boardDetailDto.viewCount( board.getViewCount() );
        boardDetailDto.likeCount( board.getLikeCount() );
        boardDetailDto.insertTime( board.getInsertTime() );
        boardDetailDto.updateTime( board.getUpdateTime() );
        boardDetailDto.cookTime( board.getCookTime() );
        boardDetailDto.difficulty( board.getDifficulty() );

        return boardDetailDto.build();
    }

    @Override
    public Board toEntity(BoardSaveReq dto) {
        if ( dto == null ) {
            return null;
        }

        Board.BoardBuilder board = Board.builder();

        board.category( dto.getCategory() );
        board.title( dto.getTitle() );
        board.content( dto.getContent() );
        board.thumbnail( dto.getThumbnail() );
        board.prepare( dto.getPrepare() );
        board.cookTime( dto.getCookTime() );
        board.difficulty( dto.getDifficulty() );

        board.viewCount( (long) 0L );
        board.likeCount( (long) 0L );

        return board.build();
    }

    @Override
    public void updateEntity(BoardUpdateReq dto, Board entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getBoardId() != null ) {
            entity.setBoardId( dto.getBoardId() );
        }
        if ( dto.getCategory() != null ) {
            entity.setCategory( dto.getCategory() );
        }
        if ( dto.getTitle() != null ) {
            entity.setTitle( dto.getTitle() );
        }
        if ( dto.getContent() != null ) {
            entity.setContent( dto.getContent() );
        }
        if ( dto.getThumbnail() != null ) {
            entity.setThumbnail( dto.getThumbnail() );
        }
        if ( dto.getPrepare() != null ) {
            entity.setPrepare( dto.getPrepare() );
        }
        if ( dto.getCookTime() != null ) {
            entity.setCookTime( dto.getCookTime() );
        }
        if ( dto.getDifficulty() != null ) {
            entity.setDifficulty( dto.getDifficulty() );
        }
    }

    @Override
    public Review toEntity(ReviewSaveReq dto) {
        if ( dto == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.board( reviewSaveReqToBoard( dto ) );
        review.writer( reviewSaveReqToMember( dto ) );
        review.content( dto.getContent() );

        return review.build();
    }

    @Override
    public ReviewRes toDto(Review entity) {
        if ( entity == null ) {
            return null;
        }

        ReviewRes reviewRes = new ReviewRes();

        reviewRes.setBoardId( entityBoardBoardId( entity ) );
        reviewRes.setWriterIdx( entityWriterMemberIdx( entity ) );
        reviewRes.setNickname( entityWriterNickname( entity ) );
        reviewRes.setReviewId( entity.getReviewId() );
        reviewRes.setContent( entity.getContent() );
        reviewRes.setInsertTime( entity.getInsertTime() );
        reviewRes.setUpdateTime( entity.getUpdateTime() );

        return reviewRes;
    }

    @Override
    public void updateEntityFromDto(ReviewUpdateReq dto, Review entity) {
        if ( dto == null ) {
            return;
        }

        if ( entity.getBoard() == null ) {
            entity.setBoard( Board.builder().build() );
        }
        reviewUpdateReqToBoard( dto, entity.getBoard() );
        if ( dto.getReviewId() != null ) {
            entity.setReviewId( dto.getReviewId() );
        }
        if ( dto.getContent() != null ) {
            entity.setContent( dto.getContent() );
        }
    }

    @Override
    public FileDto toDto(File file) {
        if ( file == null ) {
            return null;
        }

        FileDto fileDto = new FileDto();

        fileDto.setUploaderIdx( fileUploaderMemberIdx( file ) );
        fileDto.setUploaderLoginId( fileUploaderLoginId( file ) );
        fileDto.setFileId( file.getFileId() );
        fileDto.setFileName( file.getFileName() );
        fileDto.setFilePath( file.getFilePath() );
        fileDto.setFileType( file.getFileType() );
        fileDto.setUseType( file.getUseType() );
        fileDto.setUseTargetId( file.getUseTargetId() );
        fileDto.setUsePosition( file.getUsePosition() );
        fileDto.setInsertTime( file.getInsertTime() );
        fileDto.setUpdateTime( file.getUpdateTime() );

        return fileDto;
    }

    @Override
    public File toEntity(FileDto fileDto) {
        if ( fileDto == null ) {
            return null;
        }

        File.FileBuilder file = File.builder();

        file.uploader( fileDtoToMember( fileDto ) );
        file.fileId( fileDto.getFileId() );
        file.fileName( fileDto.getFileName() );
        file.filePath( fileDto.getFilePath() );
        file.fileType( fileDto.getFileType() );
        file.useType( fileDto.getUseType() );
        file.useTargetId( fileDto.getUseTargetId() );
        file.usePosition( fileDto.getUsePosition() );

        return file.build();
    }

    @Override
    public LikeDto toDto(Like like) {
        if ( like == null ) {
            return null;
        }

        LikeDto.LikeDtoBuilder likeDto = LikeDto.builder();

        likeDto.likeId( like.getLikeId() );
        likeDto.boardId( like.getBoardId() );
        likeDto.recipeId( like.getRecipeId() );
        likeDto.likeType( like.getLikeType() );
        likeDto.likeDate( like.getLikeDate() );

        return likeDto.build();
    }

    @Override
    public Like toEntity(LikeDto likeDto) {
        if ( likeDto == null ) {
            return null;
        }

        Like.LikeBuilder like = Like.builder();

        like.likeId( likeDto.getLikeId() );
        like.boardId( likeDto.getBoardId() );
        like.recipeId( likeDto.getRecipeId() );
        like.likeType( likeDto.getLikeType() );
        like.likeDate( likeDto.getLikeDate() );

        return like.build();
    }

    @Override
    public Like toEntity(LikeSaveReq likeSaveReq) {
        if ( likeSaveReq == null ) {
            return null;
        }

        Like.LikeBuilder like = Like.builder();

        like.boardId( likeSaveReq.getBoardId() );
        like.recipeId( likeSaveReq.getRecipeId() );
        like.likeType( likeSaveReq.getLikeType() );

        return like.build();
    }

    @Override
    public LikeRes toRes(LikeDto likeDto) {
        if ( likeDto == null ) {
            return null;
        }

        LikeRes.LikeResBuilder likeRes = LikeRes.builder();

        likeRes.likeId( likeDto.getLikeId() );
        likeRes.likeType( likeDto.getLikeType() );
        likeRes.boardId( likeDto.getBoardId() );
        likeRes.recipeId( likeDto.getRecipeId() );
        likeRes.likeCount( likeDto.getLikeCount() );
        likeRes.likeDate( likeDto.getLikeDate() );

        return likeRes.build();
    }

    @Override
    public Recipe toEntity(RecipeDto dto) {
        if ( dto == null ) {
            return null;
        }

        Recipe.RecipeBuilder recipe = Recipe.builder();

        recipe.recipeId( dto.getRecipeId() );
        recipe.titleKr( dto.getTitleKr() );
        recipe.categoryKr( dto.getCategoryKr() );
        recipe.instructionKr( dto.getInstructionKr() );
        recipe.ingredientKr( dto.getIngredientKr() );
        recipe.measureKr( dto.getMeasureKr() );
        recipe.titleEn( dto.getTitleEn() );
        recipe.categoryEn( dto.getCategoryEn() );
        recipe.instructionEn( dto.getInstructionEn() );
        recipe.ingredientEn( dto.getIngredientEn() );
        recipe.measureEn( dto.getMeasureEn() );
        recipe.thumbnail( dto.getThumbnail() );
        recipe.area( dto.getArea() );
        recipe.likeCount( dto.getLikeCount() );
        recipe.viewCount( dto.getViewCount() );
        recipe.cookTime( dto.getCookTime() );
        recipe.difficulty( dto.getDifficulty() );

        return recipe.build();
    }

    @Override
    public RecipeDto toDto(Recipe entity) {
        if ( entity == null ) {
            return null;
        }

        RecipeDto.RecipeDtoBuilder recipeDto = RecipeDto.builder();

        recipeDto.recipeId( entity.getRecipeId() );
        recipeDto.titleKr( entity.getTitleKr() );
        recipeDto.categoryKr( entity.getCategoryKr() );
        recipeDto.instructionKr( entity.getInstructionKr() );
        recipeDto.ingredientKr( entity.getIngredientKr() );
        recipeDto.measureKr( entity.getMeasureKr() );
        recipeDto.titleEn( entity.getTitleEn() );
        recipeDto.categoryEn( entity.getCategoryEn() );
        recipeDto.instructionEn( entity.getInstructionEn() );
        recipeDto.ingredientEn( entity.getIngredientEn() );
        recipeDto.measureEn( entity.getMeasureEn() );
        recipeDto.thumbnail( entity.getThumbnail() );
        recipeDto.area( entity.getArea() );
        recipeDto.likeCount( entity.getLikeCount() );
        recipeDto.viewCount( entity.getViewCount() );
        recipeDto.difficulty( entity.getDifficulty() );
        recipeDto.cookTime( entity.getCookTime() );

        return recipeDto.build();
    }

    @Override
    public List<RecipeDto> toDtoList(List<Recipe> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RecipeDto> list = new ArrayList<RecipeDto>( entities.size() );
        for ( Recipe recipe : entities ) {
            list.add( toDto( recipe ) );
        }

        return list;
    }

    @Override
    public List<Recipe> toEntityList(List<RecipeDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Recipe> list = new ArrayList<Recipe>( dtos.size() );
        for ( RecipeDto recipeDto : dtos ) {
            list.add( toEntity( recipeDto ) );
        }

        return list;
    }

    @Override
    public void updateFromDto(RecipeDto dto, Recipe entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getRecipeId() != null ) {
            entity.setRecipeId( dto.getRecipeId() );
        }
        if ( dto.getTitleKr() != null ) {
            entity.setTitleKr( dto.getTitleKr() );
        }
        if ( dto.getCategoryKr() != null ) {
            entity.setCategoryKr( dto.getCategoryKr() );
        }
        if ( dto.getInstructionKr() != null ) {
            entity.setInstructionKr( dto.getInstructionKr() );
        }
        if ( dto.getIngredientKr() != null ) {
            entity.setIngredientKr( dto.getIngredientKr() );
        }
        if ( dto.getMeasureKr() != null ) {
            entity.setMeasureKr( dto.getMeasureKr() );
        }
        if ( dto.getTitleEn() != null ) {
            entity.setTitleEn( dto.getTitleEn() );
        }
        if ( dto.getCategoryEn() != null ) {
            entity.setCategoryEn( dto.getCategoryEn() );
        }
        if ( dto.getInstructionEn() != null ) {
            entity.setInstructionEn( dto.getInstructionEn() );
        }
        if ( dto.getIngredientEn() != null ) {
            entity.setIngredientEn( dto.getIngredientEn() );
        }
        if ( dto.getMeasureEn() != null ) {
            entity.setMeasureEn( dto.getMeasureEn() );
        }
        if ( dto.getThumbnail() != null ) {
            entity.setThumbnail( dto.getThumbnail() );
        }
        if ( dto.getArea() != null ) {
            entity.setArea( dto.getArea() );
        }
        if ( dto.getLikeCount() != null ) {
            entity.setLikeCount( dto.getLikeCount() );
        }
        if ( dto.getViewCount() != null ) {
            entity.setViewCount( dto.getViewCount() );
        }
        if ( dto.getCookTime() != null ) {
            entity.setCookTime( dto.getCookTime() );
        }
        if ( dto.getDifficulty() != null ) {
            entity.setDifficulty( dto.getDifficulty() );
        }
    }

    @Override
    public IntegratedSearchDto toDto(IntegratedSearch integratedSearch) {
        if ( integratedSearch == null ) {
            return null;
        }

        IntegratedSearchDto integratedSearchDto = new IntegratedSearchDto();

        integratedSearchDto.setId( integratedSearch.getId() );
        integratedSearchDto.setType( integratedSearch.getType() );
        integratedSearchDto.setTitle( integratedSearch.getTitle() );
        integratedSearchDto.setThumbnail( integratedSearch.getThumbnail() );
        integratedSearchDto.setIngredients( integratedSearch.getIngredients() );
        integratedSearchDto.setCategory( integratedSearch.getCategory() );

        return integratedSearchDto;
    }

    @Override
    public IntegratedSearch toEntity(IntegratedSearchDto integratedSearchDto) {
        if ( integratedSearchDto == null ) {
            return null;
        }

        IntegratedSearch integratedSearch = new IntegratedSearch();

        integratedSearch.setId( integratedSearchDto.getId() );
        integratedSearch.setType( integratedSearchDto.getType() );
        integratedSearch.setTitle( integratedSearchDto.getTitle() );
        integratedSearch.setThumbnail( integratedSearchDto.getThumbnail() );
        integratedSearch.setIngredients( integratedSearchDto.getIngredients() );
        integratedSearch.setCategory( integratedSearchDto.getCategory() );

        return integratedSearch;
    }

    @Override
    public IntegratedSearch boardToEntity(BoardSaveReq boardSaveReq) {
        if ( boardSaveReq == null ) {
            return null;
        }

        IntegratedSearch integratedSearch = new IntegratedSearch();

        integratedSearch.setIngredients( boardSaveReq.getPrepare() );
        integratedSearch.setTitle( boardSaveReq.getTitle() );
        integratedSearch.setThumbnail( boardSaveReq.getThumbnail() );
        integratedSearch.setCategory( boardSaveReq.getCategory() );

        return integratedSearch;
    }

    private String boardWriterNickname(Board board) {
        if ( board == null ) {
            return null;
        }
        Member writer = board.getWriter();
        if ( writer == null ) {
            return null;
        }
        String nickname = writer.getNickname();
        if ( nickname == null ) {
            return null;
        }
        return nickname;
    }

    private Long boardWriterMemberIdx(Board board) {
        if ( board == null ) {
            return null;
        }
        Member writer = board.getWriter();
        if ( writer == null ) {
            return null;
        }
        Long memberIdx = writer.getMemberIdx();
        if ( memberIdx == null ) {
            return null;
        }
        return memberIdx;
    }

    private String boardWriterProfile(Board board) {
        if ( board == null ) {
            return null;
        }
        Member writer = board.getWriter();
        if ( writer == null ) {
            return null;
        }
        String profile = writer.getProfile();
        if ( profile == null ) {
            return null;
        }
        return profile;
    }

    protected Board reviewSaveReqToBoard(ReviewSaveReq reviewSaveReq) {
        if ( reviewSaveReq == null ) {
            return null;
        }

        Board.BoardBuilder board = Board.builder();

        board.boardId( reviewSaveReq.getBoardId() );

        return board.build();
    }

    protected Member reviewSaveReqToMember(ReviewSaveReq reviewSaveReq) {
        if ( reviewSaveReq == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.memberIdx( reviewSaveReq.getWriterIdx() );

        return member.build();
    }

    private Long entityBoardBoardId(Review review) {
        if ( review == null ) {
            return null;
        }
        Board board = review.getBoard();
        if ( board == null ) {
            return null;
        }
        Long boardId = board.getBoardId();
        if ( boardId == null ) {
            return null;
        }
        return boardId;
    }

    private Long entityWriterMemberIdx(Review review) {
        if ( review == null ) {
            return null;
        }
        Member writer = review.getWriter();
        if ( writer == null ) {
            return null;
        }
        Long memberIdx = writer.getMemberIdx();
        if ( memberIdx == null ) {
            return null;
        }
        return memberIdx;
    }

    private String entityWriterNickname(Review review) {
        if ( review == null ) {
            return null;
        }
        Member writer = review.getWriter();
        if ( writer == null ) {
            return null;
        }
        String nickname = writer.getNickname();
        if ( nickname == null ) {
            return null;
        }
        return nickname;
    }

    protected void reviewUpdateReqToBoard(ReviewUpdateReq reviewUpdateReq, Board mappingTarget) {
        if ( reviewUpdateReq == null ) {
            return;
        }

        if ( reviewUpdateReq.getBoardId() != null ) {
            mappingTarget.setBoardId( reviewUpdateReq.getBoardId() );
        }
    }

    private Long fileUploaderMemberIdx(File file) {
        if ( file == null ) {
            return null;
        }
        Member uploader = file.getUploader();
        if ( uploader == null ) {
            return null;
        }
        Long memberIdx = uploader.getMemberIdx();
        if ( memberIdx == null ) {
            return null;
        }
        return memberIdx;
    }

    private String fileUploaderLoginId(File file) {
        if ( file == null ) {
            return null;
        }
        Member uploader = file.getUploader();
        if ( uploader == null ) {
            return null;
        }
        String loginId = uploader.getLoginId();
        if ( loginId == null ) {
            return null;
        }
        return loginId;
    }

    protected Member fileDtoToMember(FileDto fileDto) {
        if ( fileDto == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.memberIdx( fileDto.getUploaderIdx() );
        member.loginId( fileDto.getUploaderLoginId() );

        return member.build();
    }
}
