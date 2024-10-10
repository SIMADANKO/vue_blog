package com.vueblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vueblog.entity.BlogFavorite;
import com.vueblog.mapper.BlogFavoriteMapper;

import com.vueblog.service.BlogFavoriteService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */
@Service
public class BlogFavoriteServiceImpl extends ServiceImpl<BlogFavoriteMapper, BlogFavorite> implements BlogFavoriteService {
    @Autowired
    private BlogFavoriteMapper favoriteMapper;

    public boolean isFavoriteExists(Long userId, Long blogId) {
        int count = favoriteMapper.countFavoriteByUserIdAndBlogId(userId, blogId);
        return count > 0;
    }

    public List<Long> findBlogIdsByUserId(Long userId){
        return favoriteMapper.findBlogIdsByUserId(userId);
    }
}
