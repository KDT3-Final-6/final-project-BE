package com.travel.post.repository.qnapost;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.post.entity.QQnAPost;
import com.travel.post.entity.QnAPost;

import javax.persistence.EntityManager;
import java.util.List;

public class QnACustomRepositoryImpl implements QnACustomRepository {

    private final JPAQueryFactory queryFactory;

    public QnACustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<QnAPost> findQnAsByKeyword(String keyword) {
        QQnAPost qnAPost = QQnAPost.qnAPost;
        return queryFactory.selectFrom(qnAPost)
                .where(qnAPost.postTitle.containsIgnoreCase(keyword)
                        .or(qnAPost.postContent.containsIgnoreCase(keyword))
                        .or(qnAPost.member.memberName.containsIgnoreCase(keyword)))
                .fetch();
    }
}
