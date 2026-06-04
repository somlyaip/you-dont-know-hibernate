package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.repository;

import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
