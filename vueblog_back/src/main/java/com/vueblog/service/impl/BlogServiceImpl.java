package com.vueblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.Blog;
import com.vueblog.entity.BlogFavorite;
import com.vueblog.mapper.BlogFavoriteMapper;
import com.vueblog.mapper.BlogLikeMapper;
import com.vueblog.mapper.BlogMapper;
import com.vueblog.mapper.CommentMapper;
import com.vueblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lu
 * @since 2024-02-06
 */

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private BlogMapper blogMapper; // 假设您有一个名为 BlogMapper 的 Mapper 接口用于操作博客表

    @Autowired
    private BlogFavoriteMapper blogFavoriteMapper; // 假设您有一个名为 BlogFavoriteMapper 的 Mapper 接口用于操作博客收藏表
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BlogLikeMapper likeMapper;

    @Transactional(rollbackFor = Exception.class) // 添加 @Transactional 注解，确保操作的原子性
    public void deleteWithDependencies(Long id) {
        try {
            // 删除评论
            commentMapper.deleteByBlogId(id);
            // 删除点赞
            likeMapper.deleteByBlogId(id);
            // 删除收藏
            blogFavoriteMapper.deleteByBlogId(id);
            // 最后删除博客
            blogMapper.deleteById(id);
        } catch (Exception e) {
            // 如果出现异常，可以选择抛出自定义异常或者记录日志
            // 抛出自定义异常
            throw new RuntimeException("删除博客失败，ID：" + id, e);
        }
    }
    public IPage<Blog> getUserFavoriteBlogs(Long userId, Integer currentPage, Integer pageSize) {
        // 首先从收藏表查询指定用户收藏的博客ID列表
        List<Long> blogIds = blogFavoriteMapper.selectList(new QueryWrapper<BlogFavorite>().eq("user_id", userId)).stream()
                .map(BlogFavorite::getBlogId)
                .collect(Collectors.toList());

        // 判断收藏列表是否为空
        if (blogIds == null || blogIds.isEmpty()) {
            return new Page<>();
        }

        // 构建查询条件，查询对应的博客信息
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", blogIds); // 根据收藏的博客ID列表查询对应的博客信息
        queryWrapper.orderByDesc("created"); // 按创建时间倒序排序

        // 执行查询并返回分页结果
        return blogMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }


    public IPage<Blog> searchBlogsByKeyword(String keyword, Integer currentPage, Integer pageSize) {
        // 判断关键字是否为空
        if (keyword == null || keyword.trim().isEmpty()) {
            return new Page<>();
        }

        // 构建查询条件，查询对应的博客信息
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", keyword.trim()); // 根据标题模糊查询关键字

        queryWrapper.orderByDesc("created"); // 按创建时间倒序排序

        // 执行查询并返回分页结果
        return blogMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }

    public List<Map<String, Object>> getRandomBlogs() {
        return blogMapper.getRandomBlogs();
    }



    public List<Blog> searchBlogs(String keyword) {
        return blogMapper.searchBlogs(keyword);
    }



}
