package com.vueblog.common.dto;

public class FavoriteDTO {
    private Long blogId;
    private Long userId;

    // Getters and Setters
    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}