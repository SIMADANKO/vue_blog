package com.vueblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.Blog;
import com.vueblog.mapper.BlogMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lu
 * @since 2024-02-06
 */

public interface BlogService extends IService<Blog> {


    public IPage<Blog> getUserFavoriteBlogs(Long userId, Integer currentPage, Integer pageSize);


    public List<Blog> searchBlogs(String keyword);


    List<Map<String, Object>> getRandomBlogs();

    IPage<Blog> searchBlogsByKeyword(String keyword, Integer currentPage, Integer pageSize);
    void deleteWithDependencies(@Param("blogId") Long id);






}
