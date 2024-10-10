package com.vueblog.entity.Key;

public class BlogFavoriteKey {
    private Long userId;
    private Long blogId;

    public BlogFavoriteKey() {
    }

    public BlogFavoriteKey(Long userId, Long blogId) {
        this.userId = userId;
        this.blogId = blogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }
}
