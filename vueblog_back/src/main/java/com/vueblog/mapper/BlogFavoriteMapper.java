package com.vueblog.mapper;

import com.vueblog.entity.BlogFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */
public interface BlogFavoriteMapper extends BaseMapper<BlogFavorite> {
    int countFavoriteByUserIdAndBlogId(@Param("userId") Long userId, @Param("blogId") Long blogId);

    List<Long> findBlogIdsByUserId(@Param("userId")Long userId);

    void deleteByBlogId( @Param("blogId")Long id);
}
