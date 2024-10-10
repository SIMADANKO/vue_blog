package com.vueblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vueblog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lu
 * @since 2024-02-06
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
    @Select("SELECT * FROM m_blog ORDER BY RAND() LIMIT 16")
    List<Map<String, Object>> getRandomBlogs();

    List<Blog> searchBlogs(@Param("keyword") String keyword);





}
