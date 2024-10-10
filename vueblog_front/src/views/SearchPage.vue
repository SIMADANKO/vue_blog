<template>
    <div>
      <Header></Header>
  
      <el-container>
        <!-- 头部区域 -->
  
        <el-container style="margin-left: 30px;">
          <!-- 左侧区域 -->
  
          <!-- <el-aside width="200px" style="background-color: rgb(238, 241, 246);margin-bottom: 10px;">
            <el-card>
              <el-menu :default-openeds="['1', '3']">
                <template slot="title"><i class="el-icon-message"></i>导航一</template>
                <el-menu-item index="1-1">选项1</el-menu-item>
                <el-menu-item index="1-2">选项2</el-menu-item>
                <el-menu-item index="1-3">选项3</el-menu-item>
                <template slot="title">选项4</template>
                <el-menu-item index="1-4-1">选项4</el-menu-item>
                <el-menu-item index="2-1">选项1</el-menu-item>
                <el-menu-item index="2-2">选项2</el-menu-item>
                <template slot="title">选项4</template>
                <el-menu-item index="3-1">选项1</el-menu-item>
                <el-menu-item index="3-2">选项2</el-menu-item>
                <el-menu-item index="3-3">选项3</el-menu-item>
                <template slot="title">选项4</template>
              </el-menu>
            </el-card>
          </el-aside> -->
          <el-card style="width: 80%;margin-left: 10%;margin-bottom: 10px;margin-right: 10%;">
            <el-main style="margin-top:-30px ;">
            
                <router-link v-for="blog in blogs" :key="blog.id" :to="{ name: 'BlogDetail', params: { blogId: blog.id } }" style="text-decoration: none;">
    <div style="display: flex; align-items: center;">
      <img :src="`http://localhost:8081/getPostPage/${blog.id}`" style="width: 160px; height: 90px; margin-right: 20px; object-fit: cover;" alt="" >
      <div style="flex: 1; border-bottom: 1px solid #ccc; padding-bottom: 10px;">
        <h4 style="font-size: 18px;">{{ blog.title }}</h4>
        <p style="font-size: 14px; color: #666;">{{ blog.description }}</p>
        <p style="font-size: 12px; color: #999;">创建时间: {{ blog.created }}</p>
      </div>
    </div>
  </router-link>
             
            </el-main>
            <!-- 放置pagination -->
            <el-pagination class="mpage" background layout="prev, pager, next" :current-page="currentPage" :page-size="pageSize" @current-change="page" :total="total">
            </el-pagination>
          </el-card>
        </el-container>
      </el-container>
    </div>
  </template>
  
  <script>
  import Header from '@/components/Header.vue';
  
  export default {
    name: "SearchPage",
    components: {
      Header
    },
    data() {
      return {
        searchKeyword: '', // 初始化为空字符串
        blogs: [],
        currentPage: 1,
        pageSize: 10,
        total: 0
      };
    },
    created() {
      this.searchKeyword = this.$route.params.keyword; // 在 created 钩子中手动更新 searchKeyword
      this.fetchSearchResults();
    },
    watch: {
      '$route'(to, from) {
        // 监听路由参数的变化
        this.searchKeyword = to.params.keyword; // 更新 searchKeyword
        this.fetchSearchResults(); // 重新获取搜索结果
      }
    },
    methods: {
      fetchSearchResults() {
        const keyword = this.searchKeyword.trim();
        if (keyword) {
          this.$axios.get(`http://localhost:8081/searchblogs?keyword=${keyword}&page=${this.currentPage}&pageSize=${this.pageSize}`)
            .then((res) => {
              this.blogs = res.data.records;
              this.total = res.data.total;
            })
            .catch((error) => {
              console.error("An error occurred while fetching search results:", error);
              // Handle error as needed
            });
        }
      },
      page(currentPage) {
        this.currentPage = currentPage;
        this.fetchSearchResults();
      }
    }
  };
  </script>