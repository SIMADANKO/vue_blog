<template>
  <div>
    <Header></Header>

    <el-container style="max-width: calc(100% - 100px);margin-left: 100px;">

      

      <div class="block" style="width: calc(100% - 100px); text-align: left;">
        <div v-for="blog in blogs" :key="blog.id">
  <div>
    <router-link :to="{ name: 'BlogDetail', params: { blogId: blog.id } }" style="text-decoration: none;">
      <div style="display: flex; align-items: center;">
        <img :src="`http://localhost:8081/getPostPage/${blog.id}`" style="width: 160px; height: 90px; margin-right: 20px; object-fit: cover;" alt="">
        <div style="flex: 1; border-bottom: 1px solid #ccc; padding-bottom: 10px;">
          <h4 style="font-size: 18px;">{{ blog.title }}</h4>
          <p style="font-size: 14px; color: #666;">{{ blog.description }}</p>
          <p style="font-size: 12px; color: #999;">创建时间: {{ blog.created }}</p>
        </div>
        <el-button @click.stop="handleDeleteFavorite(blog.id)" >取消收藏</el-button>
      </div>
    </router-link>
  </div>
</div>

</div>
</el-container>

<el-pagination class="mpage" background layout="prev, pager, next" :current-page="currentPage" :page-size="pageSize" @current-change="handlePageChange" :total="total">
</el-pagination>
</div>
</template>

<script>
import Header from '../components/Header.vue';

export default {
  name: "Favorite",
  components: {
    Header
  },
  data() {
    return {
      blogs: [], 
      currentPage: 1,
      total: 0,
      pageSize: 5,
      userId: null,
      categories: ["分类一", "分类二", "分类三"] 
    };
  },
  methods: {
    getUserIdFromLocalStorage() {
      const userData = JSON.parse(localStorage.getItem('userInfo'));
      if (userData) {
        this.userId = userData.id;
      } else {
        console.error('User data not found in localStorage');
      }
    },
    handlePageChange(currentPage) {
      this.getUserIdFromLocalStorage();

      this.$axios
        .get(
          `http://localhost:8081/favorite?currentPage=${currentPage}&userId=${this.userId}`
        )
        .then((res) => {
          console.log(res.data)
          this.blogs = res.data.data.content;
          this.currentPage = res.data.data.currentPage;
          this.total = res.data.data.totalElements;
          this.pageSize = res.data.data.pageSize;
        })
        .catch((error) => {
          console.error('Error fetching blogs:', error);
        });
    },
    deleteFavorite(blogId) {
  this.$axios.post(`http://localhost:8081/deletefavorite?blogId=${blogId}&userId=${this.userId}`)
    .then(response => {
      // 处理后端返回的数据
      if (response.data === 1) {
        // 博客尚未收藏
      } else {
        // 取消收藏成功
        // 刷新收藏列表数据
        this.handlePageChange(this.currentPage);
      }
    })
    .catch(error => {
      console.error(error);
    });
},
handleDeleteFavorite(blogId) {
    // 执行取消收藏操作
    this.deleteFavorite(blogId);
    // 阻止事件默认行为和冒泡
    event.preventDefault();
    event.stopPropagation();
  }
  },
  mounted() {
    this.getUserIdFromLocalStorage(); 
    this.handlePageChange(1);
  }
};
</script>
<style scoped>

</style>