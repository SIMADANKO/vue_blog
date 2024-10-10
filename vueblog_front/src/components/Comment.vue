<template>
  <div>
    <div class="article-info">
      <el-button class="info-item" type="text" style="font-size: 20px;">
        <!-- 添加浏览量图标 -->
        <i class="fas fa-eye" ></i>
        <span>浏览: {{ views }}</span>
      </el-button>
      <el-button class="info-item" @click="toggleLike" type="text" style="font-size: 20px;">
        <!-- 添加点赞量图标 -->
        <i class="fas fa-thumbs-up" :class="{ 'hovered': isHover }"></i>
        <span>点赞: {{ likes }}</span>
      </el-button>
      <el-button class="info-item" type="text" style="font-size: 20px;">
        <!-- 添加评论数图标 -->
        <i class="fas fa-comment" @click.stop></i>
        <span>评论: {{ comments.length }}</span>
      </el-button>
    </div>
    <!-- 点击评论数后显示评论输入框和列表 -->
    <div  class="comment-section">
      <ul class="comment-list">
        <li v-for="comment in comments" :key="comment.id" class="comment-item">
          <router-link :to="{ name: 'Blogs', params: { userId: comment.userId } }">
          <!-- 添加评论用户的头像 -->
          <img class="comment-avatar" :src="comment.user.avatar" alt="User Avatar">
          </router-link>
          <div class="comment-info">
            <!-- 添加评论用户的用户名 -->
            <div class="comment-username">{{ comment.user.username }}</div> 
            <!-- 添加评论时间 -->
            <div class="comment-time">{{ comment.created }}</div>
            <!-- 显示评论内容 -->
            <div class="comment-content">{{ comment.content }}</div>
          </div>
          <hr class="comment-divider">
        </li>
      </ul>
      <div class="comment-container">
        <div class="comment-title">评论</div>
        
        <div class="comment-input-wrapper">
          
          <el-input
 
  placeholder="请输入内容"
  v-model="newComment"
  maxlength="300"
  show-word-limit
  @click="toggleCommentInput"
>
</el-input>
          <el-button @click="addComment" class="comment-submit" style="height: 40px;">发布</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "Comment",
  data() {
  return {
    likeCount: 10,
    comments: [],
    newComment: "",
    showCommentInput: false,
    blogId: null,
    views: 0,
    likes: 20,
    LikeOrNot: false,
    showComments: false,
    users: [] ,// 新增的数组，用于存储用户信息
  };
},
  created() {
    this.fetchBlogId();
    this.showlike();
  },
  methods: {
    // fetchArticleInfo() {
    //   // 模拟获取文章浏览量、点赞量等信息
    //   this.views = 100; // 假设浏览量为100
    //   this.likes = 50; // 假设点赞量为50
    // },
    toggleLike() {
      const id=JSON.parse(localStorage.getItem('userInfo')).id;
      if(id!=null){ 
        this.isLiked = !this.isLiked; // 切换点赞状态
      if (this.isLiked) {
        this.likes++; // 点赞数加一
      
        this.addlike();
    
      } else {
        this.likes--; // 取消点赞，点赞数减一
        this.deletelike();
      }}else{
        alert("请先登录");
    this.$router.push("/login");
      }
     
    },
    fetchBlogId() {
      this.blogId = this.$route.params.blogId;
      if (!this.blogId || isNaN(this.blogId)) {
        console.error('Invalid blogId:', this.blogId);
        return;
      }
      this.fetchComments();
    },
    fetchComments() {
  this.$axios.get(`http://localhost:8081/comments?blogId=${this.blogId}`)
    .then(response => {
      const comments = response.data.data;

      // 为了处理异步请求，可以使用 Promise.all 来发送并行请求
      const promises = comments.map(comment => {
        const userId = comment.userId;

        return this.$axios.get(`http://localhost:8081/user/getUser?userId=${userId}`)
          .then(userResponse => {
            const user = userResponse.data;
            if (user && user.userId) {
              this.users.push({
                userId: user.userId,
                username: user.userName,
                avatar: `http://localhost:8081/user/avatar/${user.userId}`
              });
              comment.user = {
                userId: user.userId,
                username: user.userName,
                avatar: `http://localhost:8081/user/avatar/${user.userId}`
              };
            }
          });
      });

      // 等待所有请求完成后再更新评论信息
      Promise.all(promises).then(() => {
        this.comments = comments;
      });
    })
    .catch(error => {
      console.error('Failed to fetch comments:', error);
    });
},
    toggleCommentInput() {
      this.showCommentInput = true;
    },
    addComment() {

    
  // 检查本地存储中是否存在用户信息
  const userInfo = localStorage.getItem('userInfo');
  if (JSON.parse(userInfo).id) {
    // 存在用户信息，获取用户ID
    if (!this.newComment.trim()) {
        alert('评论内容不能为空');
        return;
      }

    // 发送评论请求
    this.$axios.post(`http://localhost:8081/addcomments`, {
      blogId: this.blogId,
      userId: this.$store.getters.getUser?.id, // 假设当前用户ID为1，实际项目中应替换为真实用户ID
      content: this.newComment,
      created: new Date().toISOString(),
      })
      .then(response => {
        // 如果添加评论成功，将评论数据添加到评论列表中
        this.comments.push(response.data.data);
        // 清空输入框
        this.newComment = "";
        // 隐藏评论输入框
        this.showCommentInput = false;
        alert("评论发布成功");
        
        // 发布成功后导航回当前页面
        this.$router.go(); // 刷新当前页面
      })
      .catch(error => {
        console.error('Failed to add comment:', error);
      });
  } else {
    // 不存在用户信息，提示用户请先登录，并导航到登录页面
    alert("请先登录");
    this.$router.push('/login'); // 导航到登录页面
  }

  
},
  addlike(){
    const userId=JSON.parse(localStorage.getItem('userInfo')).id
    const blogId= this.$route.params.blogId
    if(userId){

    
    this.$axios.post(`http://localhost:8081/like?blogId=`+blogId+ '&userId='+userId ).then(
      this.likeOrNot=true
    )
    }else{
      alert("请先登录");
    this.$router.push("/login");
    }
  },
  showlike(){
    const blogId= this.$route.params.blogId
    this.$axios.get('http://localhost:8081/likednumber?blogId=' + blogId).then(
      (res)=>{
        this.likes=res.data.data
        console.log(res.data)
      }
    );
  },
  deletelike(){
    const blogId= this.$route.params.blogId
    this.axios.delete('http://localhost:8081/havelike?blogId=' + this.blogId + '&userId=' + this.userId).then(
      this.likeOrNot=false
    )
  },
  addviews(){
    const blogId= this.$route.params.blogId
    this.$axios.post(`http://localhost:8081/blog/addviews?blogId=`+blogId)
   
  },
  getviews(){
    const blogId= this.$route.params.blogId
    this.$axios.get(`http://localhost:8081/blog/getviews?blogId=`+blogId).then((res)=>{
      this.views=res.data.data.view
    })
  }

  },
  mounted(){
    this.showlike();
    this.getviews();
    this.addviews();
    console.log(this.likes)
    // 模拟数据，实际项目中根据需求获取真实数据

    this.likeCount = this.$axios.get(`http://localhost:8081/likednumber?blogId=${this.$route.params.blogId}`);
    this.commentCount = 20;
    this.favoriteCount = 10;
  }
};
</script>

<style scoped>
.comment-list {
  list-style: none;
  padding: 0;
}

.comment-item {
  margin-bottom: 20px;
  padding: 10px;
  background-color: #fff; /* 修改评论容器背景色为白色 */
  text-align: left; /* 让评论居中显示 */
  display: flex; /* 将评论内容设置为弹性布局 */
  align-items: flex-start; /* 垂直居上显示 */
}

.comment-avatar {
  width: 50px; /* 设置头像宽度 */
  height: 50px; /* 设置头像高度 */
  border-radius: 50%; /* 将头像设置为圆形 */
  margin-right: 10px; /* 设置头像与其他内容的间距 */
}

.comment-info {
  flex-grow: 1; /* 让评论信息占据剩余空间 */
}

.comment-username {
  font-weight: bold;
}

.comment-time {
  color: #888; /* 设置评论时间颜色为灰色 */
  font-size: 0.8em; /* 设置评论时间字体大小 */
}

.comment-content {
  text-align: left;
}

.comment-divider {
  border: none;
  height: 1px;
  background-color: #ccc;
}

.comment-container {
  text-align: center;
}

.comment-title {
  text-align: left;
  font-weight: bold;
  margin-bottom: 10px;
}

.comment-input-wrapper {
  display: flex;
  align-items: center;
  justify-content: center; /* 让评论框居中显示 */
  margin-top: 20px;
}

.comment-input {
  width: 75%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px;
  text-align: left; /* 文本框靠左显示 */
}

.comment-submit {
  padding: 8px 16px;
  background-color: #38c495;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}



.article-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.info-item {
  cursor: pointer;
}

.comment-section {
  margin-top: 10px;
}

.comment-list {
  list-style-type: none;
  padding: 0;
}

.comment-item {
  margin-bottom: 10px;
}

.comment-input-wrapper {
  margin-top: 10px;
}

.like-button {
  margin-top: 10px;
}



</style>