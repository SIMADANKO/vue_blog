package com.vueblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueblog.common.dto.UserDTO;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.User;
import com.vueblog.entity.UserFollow;
import com.vueblog.service.UserFollowService;
import com.vueblog.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class FollowController {
    @Autowired
    UserFollowService followService;
    @Autowired
    UserService userService;
    @RequiresAuthentication
    @PostMapping("/follow")
    public Boolean follow(@RequestParam Long followerId, @RequestParam Long followingId){
        Boolean follow = followService.selectByFollowerIdAndFollowingId(followerId, followingId);
        System.out.println(follow);
        if((follow==null&& !Objects.equals(followerId, followingId))){
            UserFollow subscribe=new UserFollow();
            subscribe.setFollowerId(followerId);
            subscribe.setFollowingId(followingId);
            subscribe.setCreated(LocalDateTime.now());
            followService.save(subscribe);
            return true;
        }else {
            return false;
        }

    }
    @RequiresAuthentication
    @DeleteMapping("/deletefollow")
    public Boolean cancalFollow(@RequestParam Long followerId, @RequestParam Long followingId){
         followService.deleteByFollowerIdAndFollowingId(followerId, followingId);
            return true;

    }

    @GetMapping("/getfollowing")
    public Result getFollowing(@RequestParam Long followerId) {
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", followerId);

        List<UserFollow> userFollowList = followService.list(queryWrapper);

            if (userFollowList.isEmpty()){
                return Result.succ(null);
            }
        List<Long> userIdList = userFollowList.stream().map(UserFollow::getFollowingId).collect(Collectors.toList());

        List<User> userList = (List<User>) userService.listByIds(userIdList);


            List<UserDTO> userDTOList = userList.stream().map(user -> {
                // 这里转换成UserDTO对象
                UserDTO userDTO = new UserDTO();

                // 设置UserDTO的属性
                userDTO.setUserId(user.getId());

                userDTO.setUserName(user.getUsername());
                return userDTO;
            }).collect(Collectors.toList());

            return Result.succ(userDTOList);
        }

    @GetMapping("/getfollower")
    public Result getFollower(@RequestParam Long followingId){
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("following_id", followingId);



        List<UserFollow> userFollowList = followService.list(queryWrapper);
if (userFollowList.isEmpty()){
    return Result.succ(null);
}

        List<Long> userIdList = userFollowList.stream().map(UserFollow::getFollowerId).collect(Collectors.toList());

        List<User> userList = (List<User>) userService.listByIds(userIdList);


            List<UserDTO> userDTOList = userList.stream().map(user -> {
                // 这里转换成UserDTO对象
                UserDTO userDTO = new UserDTO();
                // 设置UserDTO的属性
                userDTO.setUserId(user.getId());

                userDTO.setUserName(user.getUsername());
                return userDTO;
            }).collect(Collectors.toList());

            return Result.succ(userDTOList);
        }


    @GetMapping("/getfollowingnumber")
    public Result getFollowingNumber(@RequestParam Long followingId){
        QueryWrapper<UserFollow> queryWrapper=new QueryWrapper<>();
        int count = followService.count(queryWrapper.eq("following_id", followingId));
        return Result.succ(count);
    }

    @GetMapping("/getfollowernumber")
    public Result getFollowerNumber(@RequestParam Long followerId){
        QueryWrapper<UserFollow> queryWrapper=new QueryWrapper<>();
        int count = followService.count(queryWrapper.eq("follower_id", followerId));
        return Result.succ(count);
    }

    @GetMapping("/havefollow")
    public Boolean havefollow(@RequestParam Long followerId, @RequestParam Long followingId){
        QueryWrapper<UserFollow> queryWrapper=new QueryWrapper<>();
        UserFollow follow = followService.getOne(queryWrapper.eq("follower_id", followerId).eq("following_id", followingId));
        return follow != null;
    }


}
