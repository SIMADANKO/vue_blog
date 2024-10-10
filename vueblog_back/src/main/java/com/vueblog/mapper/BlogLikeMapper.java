package com.vueblog.mapper;

import com.vueblog.entity.BlogLike;
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
public interface BlogLikeMapper extends BaseMapper<BlogLike> {

    void deleteByBlogId( @Param("blogId")Long id);

    @Delete("DELETE FROM m_blog_like WHERE blog_id = #{blogId} AND user_id = #{userId}")
    void deleteByBlogIdAndUserId(@Param("blogId") Long blogId, @Param("userId") Long userId);

    @Select("SELECT * FROM m_blog_like WHERE blog_id = #{blogId} AND user_id = #{userId}")
    Boolean selectByBlogIdAndUserId(@Param("blogId") Long blogId, @Param("userId") Long userId);
}
