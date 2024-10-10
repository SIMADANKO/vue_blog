package com.vueblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vueblog.entity.Blog;
import com.vueblog.entity.BlogLike;
import com.vueblog.entity.User;
import com.vueblog.mapper.BlogLikeMapper;

import com.vueblog.service.BlogLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */
@Service
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike> implements BlogLikeService {
@Autowired
    BlogLikeMapper likeMapper;
public void deleteLikeByUserIdAndBlogId(Long userId,Long blogId){
    likeMapper.deleteByBlogIdAndUserId(userId,blogId);
}

    @Override
    public Boolean selectLikeByUserIdAndBlogId(Long userId, Long blogId) {
        return likeMapper.selectByBlogIdAndUserId(userId,blogId);
    }

}
