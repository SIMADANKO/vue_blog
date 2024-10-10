package com.vueblog.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vueblog.entity.Key.BlogFavoriteKey;
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
@TableName("m_blog_favorite")
@Accessors(chain = true)
public class BlogFavorite implements Serializable {
    @TableField(exist = false) // 标记该字段不是数据库表中的列
    private BlogFavoriteKey id;

    private static final long serialVersionUID = 1L;
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;

    @TableId(value = "blog_id", type = IdType.INPUT)
    private Long blogId;
    @NotBlank
    private LocalDateTime created;

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
        return "MBlogFavorite{" +
                "userId=" + userId +
                ", blogId=" + blogId +
                ", created=" + created +
                '}';
    }
}
