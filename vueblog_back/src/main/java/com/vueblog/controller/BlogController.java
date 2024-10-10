package com.vueblog.controller;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.Blog;
import com.vueblog.entity.User;
import com.vueblog.service.BlogService;
import com.vueblog.shiro.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.directory.SearchResult;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Lu
 * @since 2024-02-06
 */
@RestController
public class BlogController {
    @Autowired
    BlogService blogService;


    @GetMapping("/blogs")
    public Result listUserBlogs(@RequestParam(defaultValue = "1") Integer currentPage, Long userId) {
        if (userId == null) {
            return Result.fail("User ID is required");
        }
        Page page = new Page(currentPage, 5);


        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("created");


        IPage pageData = blogService.page(page, queryWrapper);

        return Result.succ(pageData);
    }

    @PostMapping("/blog/addviews")
    public Result addViews(@RequestParam Long blogId){
        Blog blog = blogService.getById(blogId);
        blog.setView(blog.getView()+1);
        blogService.updateById(blog);
        return Result.succ(null);
    }


    @GetMapping("blog/getviews")
    public Result getViews(@RequestParam Long blogId){

        return Result.succ(blogService.getById(blogId));
    }


    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id")Long id){
        Blog blog=blogService.getById(id);
      Assert.notNull(blog,"该博客已被删除");
        return Result.succ(blog);
  }
  @RequiresAuthentication
  @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog){
        Blog temp;
        if (blog.getId()!=null){
            temp=blogService.getById(blog.getId());
            Assert.isTrue(Objects.equals(temp.getUserId(), ShiroUtil.getProfile().getId()),"没有编辑权限");
        }else {
            temp=new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());

            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }
      BeanUtil.copyProperties(blog,temp,"id","userId","created","status");
        blogService.saveOrUpdate(temp);
      Long blogId = temp.getId();
        return Result.succ(blogId);
  }

    @RequiresAuthentication
    @DeleteMapping("/blog/delete/{id}")
    public Result delete(@PathVariable(name = "id")Long id){
        blogService.deleteWithDependencies(id);
        blogService.removeByIds(Collections.singleton(id));
        return Result.succ(null);
    }

    @GetMapping("/random-blogs")
    public Result getRandomBlogs() {
        List<Map<String, Object>> randomBlogs = blogService.getRandomBlogs();
        return Result.succ(randomBlogs);
    }


    @GetMapping("/searchblogs")
    public ResponseEntity<IPage<Blog>> searchBlogsByKeyword(@RequestParam String keyword,
                                                            @RequestParam(defaultValue = "1") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer size) {
        IPage<Blog> blogs = blogService.searchBlogsByKeyword(keyword, page, size);
        return ResponseEntity.ok(blogs);
    }

    @RequiresAuthentication
    @PostMapping("/uploadImage")
    public Result uploadImage( @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("上传的文件为空");
        }

        try {
            // 获取文件名
            String originalFileName = file.getOriginalFilename();
            if (originalFileName != null && !originalFileName.isEmpty()) {
                // 生成自定义文件名
                String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + originalFileName;
                // 拼接文件存储路径
                String filePath = ".../source/images/" + fileName;
                // 创建文件对象
                File dest = new File(filePath);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                // 将文件写入目标文件路径
                file.transferTo(dest);

                // 返回文件名给前端
                return Result.succ(fileName);
            } else {
                return Result.fail("文件名为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("图片上传失败");
        }
    }
    @RequiresAuthentication
    @PostMapping("/uploadPostPage")
    public Result uploadPostPage(@RequestParam("blogId") Long blogId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("上传的文件为空");
        }

        try {
            // 获取文件名
            String originalFileName = file.getOriginalFilename();
            // 检查文件名是否包含点号
            if (originalFileName != null && originalFileName.contains(".")) {
                // 获取文件后缀名
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                // 使用 UUID 生成唯一的文件名
                String fileName = UUID.randomUUID().toString() + fileExtension;
                // 拼接文件存储路径
                String filePath = ".../source/avatar/" + fileName;
                // 创建文件对象
                File dest = new File(filePath);
                // 检查是否存在目录，如果不存在则创建目录
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                // 将文件写入目标文件路径
                file.transferTo(dest);

                // 更新用户表中的头像地址
                Blog blog = blogService.getById(blogId);
                if (blog != null) {
                    blog.setAvatar(filePath);
                    blogService.update(new UpdateWrapper<Blog>().eq("id", blogId).set("avatar", filePath));
                    return Result.succ("封面上传成功");
                } else {
                    return Result.fail("博客不存在");
                }
            } else {
                // 文件名不包含后缀名时，可以选择使用原始文件名或者默认文件名
                String fileName = UUID.randomUUID().toString() + ".png"; // 使用 UUID 生成文件名
                String filePath ="…/source/avatar/" + fileName;// 拼接文件存储路径
                File dest = new File(filePath);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);

                // 更新博客表中的头像地址
                Blog blog = blogService.getById(blogId);
                if (blog != null) {
                    blog.setAvatar(filePath);
                    blogService.update(new UpdateWrapper<Blog>().eq("id", blogId).set("avatar", filePath));
                    return Result.succ("封面上传成功");
                } else {
                    return Result.fail("博客不存在");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("封面上传失败");
        }

    }

        @GetMapping("/picture/{pictureId}")
    public ResponseEntity<byte[]> getPicture(@PathVariable String pictureId) {
        String filePath = ".../source/images/" + pictureId;
        File file = new File(filePath);

        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=" + pictureId);

            return ResponseEntity.ok().headers(headers).contentType(MediaType.IMAGE_JPEG).body(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getPostPage/{blogId}")
    public ResponseEntity<byte[]> getPostPage(@PathVariable String blogId) {
        String filePath =blogService.getById(blogId).getAvatar();
        File file = new File(filePath);

        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=" +  blogId);

            return ResponseEntity.ok().headers(headers).contentType(MediaType.IMAGE_JPEG).body(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
