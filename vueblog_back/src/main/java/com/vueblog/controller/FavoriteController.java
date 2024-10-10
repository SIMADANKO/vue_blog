package com.vueblog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vueblog.common.dto.FavoriteDTO;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.Blog;
import com.vueblog.entity.BlogFavorite;
import com.vueblog.entity.User;
import com.vueblog.service.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */
@RestController
public class FavoriteController {

    @Autowired
    BlogFavoriteService favoriteService;

    @Autowired
    BlogService blogService;



    @RequiresAuthentication
    @GetMapping("/favorite")
    public Result favoriteIndex(@RequestParam(defaultValue = "1") Integer currentPage,
                                @RequestParam(defaultValue = "5") Integer pageSize,
                                @RequestParam @NotNull Long userId) {
        if (userId == null) {
            return Result.fail("User ID is required");
        }

        // 查询用户收藏的博客ID集合
        List<Long> blogIds = favoriteService.findBlogIdsByUserId(userId);

        // 如果用户没有收藏博客，则直接返回空列表
        if (blogIds.isEmpty()) {
            return Result.succ(Collections.emptyList());
        }

        // 计算起始索引和结束索引
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, blogIds.size());

        // 如果 startIndex 大于等于 endIndex，则直接返回空列表
        if (startIndex >= endIndex) {
            return Result.succ(Collections.emptyList());
        }

        // 截取当前页的博客ID列表
        List<Long> pagedBlogIds = blogIds.subList(startIndex, endIndex);

        // 查询对应的博客对象
        List<Blog> pagedBlogs = (List<Blog>) blogService.listByIds(pagedBlogIds);

        // 构造返回结果，包含分页信息
        Map<String, Object> result = new HashMap<>();
        result.put("currentPage", currentPage);
        result.put("pageSize", pageSize);
        result.put("totalPages", (int) Math.ceil((double) blogIds.size() / pageSize));
        result.put("totalElements", blogIds.size());
        result.put("content", pagedBlogs);

        return Result.succ(result);
    }
    //查看收藏博客
    @RequiresAuthentication
    @GetMapping("/favorite/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        BlogFavorite favorite = favoriteService.getById(id);
        Assert.notNull(favorite, "该收藏记录已被删除");
        return Result.succ(favorite);
    }

    //添加收藏博客
    @RequiresAuthentication
    @PostMapping("favorites/add")
    public Result collect(@Validated @RequestBody FavoriteDTO favoriteRequest) {
        BlogFavorite favorite = new BlogFavorite();
        Long blogId = favoriteRequest.getBlogId();
        Long userId = favoriteRequest.getUserId();

        favorite.setBlogId(blogId);
        favorite.setUserId(userId);

        try {
            favoriteService.save(favorite);
            return Result.succ(0); // 返回0表示收藏成功
        } catch (DataIntegrityViolationException e) {
            // 捕获唯一约束违反的异常，表示已经存在相同的收藏记录
            return Result.fail(1); // 返回1给前端表示已收藏
        }
    }
//    //删除收藏博客
   @RequiresAuthentication
    @PostMapping("deletefavorite")
    public Result deleteFavorite(@RequestParam Long blogId, @RequestParam Long userId) {
        BlogFavorite favorite = new BlogFavorite();


        favorite.setBlogId(blogId);
        favorite.setUserId(userId);
       QueryWrapper<BlogFavorite> queryWrapper=new QueryWrapper<>();
       queryWrapper.eq("blog_id",blogId);
       queryWrapper.eq("user_id",userId);
        if (favoriteService.isFavoriteExists(blogId, userId)) {
            return Result.fail(1);//返回1给前端表示博客尚未收藏，前端根据返回的数值在渲染时表现博客是否被收藏
        } else {
            favoriteService.remove(queryWrapper);
            return Result.succ(0);

        }}

    }