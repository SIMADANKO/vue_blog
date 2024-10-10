package com.vueblog.controller;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.vueblog.common.dto.PersonalMessageDTO;
import com.vueblog.common.dto.UserDTO;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.User;
import com.vueblog.service.BlogService;
import com.vueblog.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Lu
 * @since 2024-02-06
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    BlogService blogService;

    @RequiresAuthentication
    @GetMapping("/index")
    public Object index(){
       User user=userService.getById(1);
       return Result.succ(user);
    }

    @RequiresAuthentication
    @PostMapping("/saveMessage")
    public Result save(@RequestParam("userId") Long userId, @Validated @RequestBody PersonalMessageDTO message) {
        if (userId == null || userId <= 0) {
            return Result.fail("无效的用户ID");
        }

        User user = userService.getById(userId);
        if (user == null) {
            return Result.fail("未找到用户");
        }

        user.setUsername(message.getUsername());
        user.setSign(message.getSign());

        if (userService.updateById(user)) {
            return Result.succ(user);
        } else {
            return Result.fail("更新用户信息失败");
        }
    }
    @RequiresAuthentication
    @PostMapping("/uploadAvatar")
    public Result uploadAvatar(@RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file) {
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
                User user = userService.getById(userId);
                if (user != null) {
                    user.setAvatar(filePath);
                    userService.update(new UpdateWrapper<User>().eq("id", userId).set("avatar", filePath));
                    return Result.succ("头像上传成功");
                } else {
                    return Result.fail("用户不存在");
                }
            } else {
                // 文件名不包含后缀名时，可以选择使用原始文件名或者默认文件名
                String fileName = UUID.randomUUID().toString()+".png"; // 使用 UUID 生成文件名
                String filePath = ".../source/avatar/" + fileName; // 拼接文件存储路径
                File dest = new File(filePath);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);

                // 更新用户表中的头像地址
                User user = userService.getById(userId);
                if (user != null) {
                    user.setAvatar(filePath);
                    userService.update(new UpdateWrapper<User>().eq("id", userId).set("avatar", filePath));
                    return Result.succ("头像上传成功");
                } else {
                    return Result.fail("用户不存在");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("头像上传失败");
        }


    }

    @GetMapping("/avatar/{userId}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long userId) throws IOException {
        User user = userService.getById(userId);
        // 构建头像文件路径
        Path filePath = Paths.get(user.getAvatar());
        // 读取文件内容并返回给前端new
        byte[] imageBytes = Files.readAllBytes(filePath);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
    }

@GetMapping("/getUser")
public UserDTO getUser(@RequestParam ("userId")Long userId){
        User user=userService.getById(userId);
        UserDTO userDTO=new UserDTO();
        userDTO.setUserName(user.getUsername());
        userDTO.setUserId(user.getId());
        return userDTO;
}

    @GetMapping("/getPersonalMessage")
    public Result personalMessageDTO(@RequestParam("userId") Long userId) {
        if (userId == null || userId <= 0) {
            return Result.fail("无效的用户ID");
        }

        User user = userService.getById(userId);

        if (user == null) {
            return Result.fail("未找到用户信息");
        }

        PersonalMessageDTO personalMessageDTO = new PersonalMessageDTO();
        personalMessageDTO.setUsername(user.getUsername());
        personalMessageDTO.setSign(user.getSign());

        return Result.succ(personalMessageDTO);
    }

}
