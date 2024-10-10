package com.vueblog.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vueblog.entity.Comment;
import com.vueblog.mapper.CommentMapper;
import com.vueblog.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lu
 * @since 2024-04-02
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
