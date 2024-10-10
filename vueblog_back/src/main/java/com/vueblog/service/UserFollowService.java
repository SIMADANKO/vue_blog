package com.vueblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vueblog.entity.UserFollow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */

public interface UserFollowService extends IService<UserFollow> {
    void deleteByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    Boolean selectByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

}
