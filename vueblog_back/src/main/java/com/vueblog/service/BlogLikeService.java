package com.vueblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vueblog.entity.BlogLike;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */

public interface BlogLikeService extends IService<BlogLike> {
    void deleteLikeByUserIdAndBlogId(Long userId,Long blogId);

    Boolean selectLikeByUserIdAndBlogId(Long userId,Long blogId);
}
