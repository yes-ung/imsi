package com.simplecoding.cheforest.jpa.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1578051274L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.simplecoding.cheforest.jpa.common.QBaseTimeEntity _super = new com.simplecoding.cheforest.jpa.common.QBaseTimeEntity(this);

    public final com.simplecoding.cheforest.jpa.board.entity.QBoard board;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> insertTime = _super.insertTime;

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public final com.simplecoding.cheforest.jpa.auth.entity.QMember writer;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.simplecoding.cheforest.jpa.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
        this.writer = inits.isInitialized("writer") ? new com.simplecoding.cheforest.jpa.auth.entity.QMember(forProperty("writer")) : null;
    }

}

