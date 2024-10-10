package com.vueblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vueblog.entity.UserFollow;
import com.vueblog.mapper.UserFollowMapper;

import com.vueblog.service.UserFollowService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {
@Autowired
UserFollowMapper followMapper;
    @Override
    public boolean saveBatch(Collection<UserFollow> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    public void deleteByFollowerIdAndFollowingId( Long followerId,  Long followingId) {
        followMapper.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    public Boolean selectByFollowerIdAndFollowingId( Long followerId,  Long followingId) {

        return followMapper.selectByFollowerIdAndFollowingId(followerId,followingId);
    }
}
