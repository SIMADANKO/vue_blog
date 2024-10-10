<template>
  <div style="margin: 0 auto; ">
    <Header></Header>
    <el-container style="height: 100%; border: 1px solid #eee; max-width: calc(100% - 400px);margin-left: 200px" class="comment-container">
      
      <!-- 侧边栏 -->
      <el-aside width="200px" style="background-color: rgb(238, 241, 246);">
        <el-menu >
     
          <!-- 其他分组... -->
        </el-menu>
      </el-aside>
      
      <!-- 主内容区域 -->
      <el-container>

        <!-- 粉丝列表 -->
        <el-main>
          <!-- 粉丝列表内容在这里 -->
          <!-- 示例： -->
          <div style="padding: 20px;">
            <h2>关注列表</h2>
            <el-card v-for="(fan, index) in fans" :key="index" style="margin-bottom: 20px">
              <div style="display: flex; align-items: center;">
                <router-link :to="{ name: 'Blogs', params: { userId: fan.userId } }">
                  <img :src="fan.avatar" style="width: 50px; height: 50px; border-radius: 50%; margin-right: 20px;">
                </router-link>
                <div style="text-align: left">
                  <p>ID: {{ fan.userName }}</p>
                  <p>个性签名: {{ fan.signature }}</p>
                  <!-- 其他粉丝信息... -->
                </div>
                <el-button v-if="fan.userId !== $store.getters.getUser?.id && !fan.followed" type="text" style="margin-left: auto;" @click="follow(fan.userId)">关注</el-button>
<el-button v-else-if="fan.userId !== $store.getters.getUser?.id && fan.followed" type="text" style="margin-left: auto;" @click="cancel(fan.userId)">取消关注</el-button>
              </div>
            </el-card>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
// 引入 Header 组件
import Header from '../components/Header.vue'; // 替换成你的 Header 组件路径

export default {
  name:"Following",

components: {
  Header
},
data() {
  return {
    fans: []
  };
},
methods: {
  // 获取粉丝列表
  async listFollowing() {
    const userId = this.$route.params.userId;
    try {
      const response = await this.$axios.get(`http://localhost:8081/getfollowing?followerId=${userId}`);
      this.fans = response.data.data.map(fan => {
        return {
          id: fan.id,
          avatar: `http://localhost:8081/user/avatar/${fan.userId}`,
          signature: fan.signature,
          userName: fan.userName,
          userId: fan.userId,
          followed: fan.followed
        };
      });
      // 判断粉丝列表中每个粉丝的关注状态
      for (const fan of this.fans) {
        fan.followed = await this.checkFollowStatus(fan.userId);
      }
    } catch (error) {
      console.error(error);
    }
  },
  // 取消关注
  cancel(userId) {
    const followerId = this.$route.params.userId;
    this.$axios.delete(`http://localhost:8081/deletefollow?followerId=${followerId}&followingId=${userId}`).then(() => {
      this.listFollowing(); // 刷新粉丝列表
      alert("取消关注成功");
    });
  },
  async checkFollowStatus(userId) {
    const followerId = JSON.parse(localStorage.getItem('userInfo')).id;
    try {
      const response = await this.$axios.get(`http://localhost:8081/havefollow?followerId=${followerId}&followingId=${userId}`);
      return response.data;
    } catch (error) {
      console.error(error);
      return false;
    }
  },
    follow(userId) {
    const followerId = JSON.parse(localStorage.getItem('userInfo')).id;
    this.$axios.post(`http://localhost:8081/follow?followerId=${followerId}&followingId=${userId}`).then(() => {
      this.listFollowing(); // 刷新粉丝列表
      alert("关注成功");
    });
  }
},
mounted() {
  this.listFollowing();
}
};
</script>