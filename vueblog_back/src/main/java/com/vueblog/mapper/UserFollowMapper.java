package com.vueblog.mapper;

import com.vueblog.entity.UserFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    @Delete("DELETE FROM m_user_follow WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    void deleteByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Select("SELECT * FROM m_user_follow WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    Boolean selectByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
}
