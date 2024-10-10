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
@TableName("m_user_follow")
@Accessors(chain = true)
public class UserFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "follower_id", type = IdType.INPUT)
    private Long followerId;

    @TableId(value = "following_id", type = IdType.INPUT)
    private Long followingId;

    @NotBlank
    private LocalDateTime created;



    public UserFollow() {

    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "MUserFollow{" +
                "followerId=" + followerId +
                ", followingId=" + followingId +
                ", created=" + created +
                '}';
    }


}