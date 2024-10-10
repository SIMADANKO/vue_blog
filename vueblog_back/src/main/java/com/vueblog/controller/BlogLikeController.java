package com.vueblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.BlogLike;
import com.vueblog.service.BlogLikeService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
public class BlogLikeController {

    @Autowired
    BlogLikeService likeService;
    @RequiresAuthentication
    @PostMapping("/like")
    public Boolean like(@RequestParam Long blogId,@RequestParam Long userId){
        Boolean like = likeService.selectLikeByUserIdAndBlogId(blogId,userId);
        if (like==null){
            BlogLike newLike=new BlogLike(blogId,userId,LocalDateTime.now());
            likeService.save(newLike);
            return true;
        }else {
            return false;
        }
    }
    @RequiresAuthentication
    @DeleteMapping("/deletelike")
    public Boolean deleteLike(@RequestParam Long blogId, @RequestParam Long userId) {

            likeService.deleteLikeByUserIdAndBlogId(userId,blogId);
            return true;

    }


    @GetMapping("/likednumber")
    public Result countLike(@RequestParam Long blogId){
        QueryWrapper<BlogLike> queryWrapper = new QueryWrapper<>();
        int count = likeService.count(queryWrapper.eq("blog_id", blogId));
        return Result.succ(count);
        }

    @RequiresAuthentication
   @GetMapping("/havelike")
   public Boolean haveLikeOrNot(@RequestParam Long blogId,@RequestParam Long userId) {

       return likeService.selectLikeByUserIdAndBlogId(blogId, userId);

   }
}
