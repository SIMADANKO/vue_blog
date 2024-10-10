package com.vueblog.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */
@Data
@TableName("m_blog_like")
@Accessors(chain = true)
public class BlogLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;

    @TableId(value = "blog_id", type = IdType.INPUT)
    private Long blogId;

    @NotBlank
    private LocalDateTime created;

    public BlogLike(Long blogId, Long userId, LocalDateTime now) {
        this.userId = userId;
        this.blogId = blogId;
        this.created=now;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "MBlogLike{" +
                "userId=" + userId +
                ", blogId=" + blogId +
                ", created=" + created +
                '}';
    }
}