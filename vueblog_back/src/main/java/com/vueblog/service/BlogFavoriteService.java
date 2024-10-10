package com.vueblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vueblog.entity.BlogFavorite;
import com.vueblog.mapper.BlogFavoriteMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */

public interface BlogFavoriteService extends IService<BlogFavorite> {

    boolean isFavoriteExists(@Param("userId") Long userId, @Param("blogId") Long blogId);

   List<Long> findBlogIdsByUserId(@Param("userId")Long userId);

}
