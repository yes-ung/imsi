package com.simplecoding.cheforest.jpa.like.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLike is a Querydsl query type for Like
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLike extends EntityPathBase<Like> {

    private static final long serialVersionUID = 1622251764L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLike like = new QLike("like1");

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> likeDate = createDateTime("likeDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> likeId = createNumber("likeId", Long.class);

    public final StringPath likeType = createString("likeType");

    public final com.simplecoding.cheforest.jpa.auth.entity.QMember member;

    public final StringPath recipeId = createString("recipeId");

    public QLike(String variable) {
        this(Like.class, forVariable(variable), INITS);
    }

    public QLike(Path<? extends Like> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLike(PathMetadata metadata, PathInits inits) {
        this(Like.class, metadata, inits);
    }

    public QLike(Class<? extends Like> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.simplecoding.cheforest.jpa.auth.entity.QMember(forProperty("member")) : null;
    }

}

