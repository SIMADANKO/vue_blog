<template>
  <div class="outer-container">
    <Header></Header>

    <div ref="blogContainer" v-infinite-scroll="load" class="blog-container" style="overflow: auto; height: 600px">
      <el-row :gutter="5" style="width: 100%;">
        <el-col :span="6" v-for="blog in blogs" :key="blog.id">
          <router-link :to="{ name: 'BlogDetail', params: { blogId: blog.id }}" class="blog-link">
            <el-card class="blog-item" :body-style="{ padding: '0px'}">
              <img :src="`http://localhost:8081/getPostPage/${blog.id}`" alt="" style="width: 100%; height: 180px; object-fit: cover;">
              <div style="padding: 10px;height: 70px;">
                <h4 class="blog-description">{{ blog.title }}</h4>
                <p class="blog-description"  style="font-size: 12px;">{{ blog.description }}<br><br>{{ blog.created }}</p>
                <div class="bottom clearfix" style="margin: 10px;padding: 10px">
                  <time class="time" style="font-size: 12px;margin-left: -15px;">{{ blog.created }}</time>
                </div>
              </div>
            </el-card>
          </router-link>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import Header from '../components/Header.vue';

export default {
  name: "HomePage",
  components: { Header },
  data() {
    return {
      blogs: [],
      loading: false,
      count: 0
    };
  },
  methods: {
    fetchBlogs() {
      this.$axios.get(`http://localhost:8081/random-blogs`)
        .then((response) => {
          this.totalBlogs = response.data.total; 
          this.blogs.push(...response.data.data); 
        })
        .catch((error) => {
          console.error('Error fetching blogs:', error);
          this.loading = false;
        });
    },
    load () {
      const container = this.$refs.blogContainer;
      if (container && container.scrollHeight - container.clientHeight <= container.scrollTop+30) {
        this.fetchBlogs();
        this.count += 1;
      }
    }
  },
  mounted() {
    this.fetchBlogs();
  }
};
</script>

<style scoped>
.outer-container {
  padding: 10px; /* 保持整体容器有适当的留白 */
}

.blog-container {
  width: 100%;
}

.blog-link {
  display: block;
  width: 100%;
  padding: 10px; /* 确保有足够的空间 */
  box-sizing: border-box;
  text-decoration: none;
}

.blog-item img {
  margin: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.blog-title, .time, .blog-description {
  display: block;
  white-space: nowrap; 
  margin: 4px 0; /* 统一设置上下边距 */
  padding: 0; /* 清除内边距 */
  text-align: left; /* 保持左对齐 */
}

.blog-description {
  overflow: hidden;
  white-space: nowrap; /* 确保简介只占用一行 */
  text-overflow: ellipsis; /* 超出部分显示省略号 */
}
.infinite-list {
  list-style: none;
  padding: 0;
}

.infinite-list-item {
  
  border-bottom: 1px solid #ccc;
}

.outer-container {
  height: 0vh;
}
</style>