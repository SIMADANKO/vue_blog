package com.vueblog.mapper;

import com.vueblog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */
public interface CommentMapper extends BaseMapper<Comment> {

    void deleteByBlogId( @Param("blogId")Long id);
}
