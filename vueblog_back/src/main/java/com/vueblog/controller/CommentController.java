package com.vueblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.Blog;
import com.vueblog.entity.Comment;
import com.vueblog.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping("/comments")
    public Result getCommentsByBlogId(@RequestParam Long blogId) {
        if (blogId == null) {
            return Result.fail("Blog ID is required");
        }
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id", blogId).orderByDesc("created");

        // Fetch comments for the specified blog

        List<Comment> comments = commentService.list(queryWrapper);

        return Result.succ(comments);
    }
    @RequiresAuthentication
    @PostMapping("/addcomments")
    public Result addComment(@Valid @RequestBody Comment comment) {
        comment.setCreated(LocalDateTime.now()); // 设置创建时间为当前时间
        commentService.save(comment); // 保存新的评论
        return Result.succ("Comment added successfully"); // 返回成功结果
    }


    @RequiresAuthentication
    @DeleteMapping("/comments/{id}")
    public Result deleteComment(@PathVariable(name = "id") Long id) {
        // Check if the comment exists
        Comment existingComment = commentService.getById(id);
        if (existingComment == null) {
            return Result.fail("Comment not found");
        }

        // Delete the comment
        commentService.removeById(id);

        return Result.succ("Comment deleted successfully");
    }
}