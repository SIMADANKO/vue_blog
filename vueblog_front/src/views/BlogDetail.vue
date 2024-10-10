<template>
<div style="margin: 0 auto 0 auto; ">
  <Header></Header>
  <el-container style="height: 100%; border: 1px solid #eee;max-width: calc(100% - 400px);margin-left: 200px;" class="comment-container">
    
    <!-- <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu :default-openeds="['1', '3']">
        <el-submenu index="1">
          <template slot="title"><i class="el-icon-message"></i>导航一</template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="1-1">选项1</el-menu-item>
            <el-menu-item index="1-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="1-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="1-4">
            <template slot="title">选项4</template>
            <el-menu-item index="1-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title"><i class="el-icon-menu"></i>导航二</template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="2-1">选项1</el-menu-item>
            <el-menu-item index="2-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="2-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="2-4">
            <template slot="title">选项4</template>
            <el-menu-item index="2-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title"><i class="el-icon-setting"></i>导航三</template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="3-1">选项1</el-menu-item>
            <el-menu-item index="3-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="3-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="3-4">
            <template slot="title">选项4</template>
            <el-menu-item index="3-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
      </el-menu>
    </el-aside> -->
  
    <el-container>
      <el-header style="text-align: right; font-size: 20px; gap: 30px">
        
          <el-row justify="center" style="margin-top: 20px">

 
            <el-button type="success" icon="el-icon-edit" circle size="small" v-if="ownBlog" @click="gotoBlogEdit(blog.id)"></el-button>

  <el-button type="warning" icon="el-icon-star-off" circle size="small" @click="addToFavorites"></el-button>
  <el-button type="danger" icon="el-icon-delete" circle size="small" @click="deleteBlog" v-if="ownBlog"></el-button>

</el-row>
          
          
      
      
        <span>{{  }}</span>
      </el-header>
      
      <el-main>
         <!-- 示例： -->
         <div style="padding: 20px;" >
           
              <el-card style="margin-bottom: 20px" class="card-container" >
                <div   style="display: flex; align-items: center;">
                  <router-link :to="{ name: 'Blogs', params: { userId: blog.userId } }">
                    <img :src="user.avatar" style="width: 50px; height: 50px; border-radius: 50%; margin-right: 20px;">
                  </router-link>
             
                  <div style="text-align: left;color:white;size: 15px; font-weight: bold; ">
                    <p>ID: {{ user.username }}</p>
                    <p>个性签名: {{ user.sign }}</p>
                    <!-- 其他粉丝信息... -->
                  </div>
                  <el-button v-if="!submitOrNot&&!ownBlog" type="primary" style="margin-left: auto;" @click="follow">关注</el-button>
                <el-button v-else-if="!ownBlog" type="primary" style="margin-left: auto;" @click="cancel">取消关注</el-button>
                </div>
                <!-- <div style="display: flex; justify-content: flex-end;color:white;margin-top: 20px;">
  
  <div style="margin-top: 25px;margin-left: 20px;">

     <p>
                    
    <router-link :to="'/followers/' +$route.params.userId" style=" text-decoration: none;
    color: white;
    font-weight: bold;">粉丝数：{{ following }}</router-link> 

    &nbsp;&nbsp;
    <router-link :to="'/following/' +$route.params.userId" style=" text-decoration: none;
    color: white;
    font-weight: bold;">关注数：{{ followers }}</router-link> 
</p>

</div>
</div> -->
              </el-card>
            </div>
        <div class="mblog" style="width: 100%; margin: 0 auto;">
          <h2>{{ blog.title }}</h2>
       
          <el-divider></el-divider>
          <!-- 使用 v-html 显示渲染好的 Markdown 内容 -->
          <mavon-editor
    :value="blog.content"
    :subfield="false"
    :defaultOpen="'preview'"
    :toolbarsFlag="false"
    :boxShadow="true"
    :transition="true"
></mavon-editor>
        </div>
        <div style="width: 100%; margin: 0 auto; margin-top: 20px;">
          <el-divider></el-divider>
          <Comment></Comment>
        </div>
      </el-main>
    </el-container>
  </el-container>
</div>
  
</template>
<script>
import '../../node_modules/github-markdown-css/github-markdown.css';
import Header from '../components/Header.vue';
import MarkdownIt from 'markdown-it';
import Comment from '../components/Comment.vue';

export default {
  name: "BlogDetail",
  components: { Header, Comment },
  data() {
    return {
      followers: '',
      following: '',
      blog: {
        userId: "",
        title: "moren",
        content: "nerong",
        description: "",
      },
      ownBlog: false,
      renderedContent: "",
      hovered: '', // 添加 hovered 属性
      viewCount: 0, // 添加浏览量
      likeCount: 0, // 添加点赞量
      commentCount: 0, // 添加评论数
      favoriteCount: 0, // 添加收藏量
      likeOrNot: false,
      submitOrNot:false,
      fans: [
        { id: 2, avatar: 'avatar2.jpg', signature: '我是2的个性签名' }
        // 其他粉丝信息...
      ],
      user: {
        userId: '',
        username: '',
        avatar: '',
        sign:''
      }
    };
  },
  created() {
    this.getBlog();
 
},
  methods: {
    getBlog() {
  const blogId = this.$route.params.blogId;
  this.getFollow();
  this.getFollowing();
  this.$axios.get(`http://localhost:8081/blog/${blogId}`).then((res) => {
    // 检查响应数据
    if (res.data && res.data.data) {
      this.blog = res.data.data;
      console.log(this.blog)
      this.user.userId = this.blog.userId;
      console.log(this.blog.userId)
      // 使用 markdown-it 渲染 Markdown 文本
      const md = new MarkdownIt();

      this.renderedContent = md.render(this.blog.content);
      this.ownBlog = this.blog.userId === this.$store.getters.getUser.id;

      // 在获取到博客信息后立即获取用户信息
      this.getUser();
      this.checkFollowStatus();
    } else {
      console.error('Failed to fetch blog: Blog data is null');
      // 处理博客数据为空的情况，可以显示错误信息或跳转到其他页面
    }
  }).catch(error => {
    console.error('Failed to fetch blog:', error);
    // 在这里可以添加错误处理逻辑，例如显示错误信息
  })
 
},
  addToFavorites() {
  const blogId = this.blog?.id;
  const userId = this.$store.getters.getUser?.id;

  // 确保 blogId 和 userId 不为 null
  if (blogId && userId) {
    this.$axios.post(`http://localhost:8081/favorites/add`, { blogId, userId })
      .then((response) => {
        const data = response.data;
        console.log(data.data);
        if (data.data === 0) {
          this.$message({
            message: '收藏成功',
            type: 'success'
          });
        } else {
          this.$message({
            message: '这篇博客已经收藏过啦',
            type: 'warning'
          });
        }
      })
      .catch(error => {
        console.error('添加收藏时发生错误:', error);
        this.$message({
          message: '添加收藏时发生错误，请稍后重试',
          type: 'error'
        });
      });
  } else{
    alert("请先登录");
    this.$router.push("/login");
    
  }
},
  deleteBlog() {
    const token = localStorage.getItem("token");
    if (this.blog?.id) {
      this.$axios.delete(`http://localhost:8081/blog/delete/${this.blog.id}`, {
        headers: {
          Authorization: token
        }
      }).then(response => {
        alert('博文删除成功');
        this.$router.push('/blogs/'+this.blog.userId);
      }).catch(error => {
        alert('博文删除失败');
        console.error('Failed to delete blog:', error);
      });
    } else {
      console.error('Blog ID is null');
      // 处理博客ID为空的情况
    }
  },
  getUser(){
    const userId=this.user.userId
    console.log(userId)
    this.$axios.get(`http://localhost:8081/user/getUser?userId=${this.user.userId}`).then(
      (res)=>{
        this.user.username=res.data.userName;
        this.user.avatar=`http://localhost:8081/user/avatar/${this.user.userId}`;
       

      }
      
    )
    this.$axios.get(`http://localhost:8081/user/getPersonalMessage?userId=${this.blog.userId}`).then(
     (res)=>{
         this.user.sign=res.data.data.sign
      }
     )
  },
  follow() {
    const followerId = JSON.parse(localStorage.getItem('userInfo')).id;
if(followerId){
    this.$axios.post(`http://localhost:8081/follow?followerId=${followerId}&followingId=${this.blog.userId}`).then(() => {
   
      alert("关注成功");
      this.submitOrNot=true;
    });
  }else{
    alert("请先登录");
    this.$router.push("/login");
  }
  },
  cancel(userId) {
    const followerId = JSON.parse(localStorage.getItem('userInfo')).id;
    this.$axios.delete(`http://localhost:8081/deletefollow?followerId=${followerId}&followingId=${this.blog.userId}`).then(() => {
     
      alert("取消关注成功");
      this.submitOrNot=false;
    });
  },
  checkFollowStatus() {
    const followerId = JSON.parse(localStorage.getItem('userInfo')).id;
    if(followerId!=null){
    const userId=this.user.userId
    console.log(followerId);
    console.log(this.user.userId)
    try {
       this.$axios.get(`http://localhost:8081/havefollow?followerId=${followerId}&followingId=${userId}`).then((res)=>{
        this.submitOrNot=res.data;
        console.log(res)
      console.log(this.submitOrNot)
      this.submitOrNot= res.data;
      console.log(res.data)
      console.log(this.submitOrNot)
      });

    } catch (error) {
      console.error(error);
      return false;
    }
  }else{
    return;
  }
  },
  getFollow(){
  const userId = this.blog.userId;
  this.$axios.get(`http://localhost:8081/getfollowernumber?followerId=${userId}`).then((res)=>{
    this.followers=res.data.data
  })
},
getFollowing(){
  const userId = this.blog.userId
  this.$axios.get(`http://localhost:8081/getfollowingnumber?followingId=${userId}`).then((res)=>{
    this.following=res.data.data
  })
},
gotoBlogEdit(blogId) {
    this.$router.push({ name: 'BlogEdit', params: { blogId } });
  },
  getPersonalInfo() {
  const userId = this.blog.userId;
  if (!userId) {
 
    return;
  }

  this.$axios.get(`http://localhost:8081/user/getPersonalMessage?userId=${userId}`)
    .then(res => {
      if (res.data.code === 200) {
        const data = res.data.data;
  
        this.user.sign = data.sign;
      } else {
        this.$message.error('获取个人信息失败');
      }
    })
    .catch(error => {
      this.$message.error('请求出错，请稍后重试');
    });
}
}, mounted() {
  this.getPersonalInfo();
  this.getBlog();
 
    // 模拟数据，实际项目中根据需求获取真实数据
    this.viewCount = 100;
    this.likeCount = 50;
    this.commentCount = 20;
    this.favoriteCount = 10;
  }
};
</script>

<style>
.comment-container {
  text-align: center;
  margin-top: 0px; /* Adding margin-top to move the container down */
  overflow-y: auto; /* Remove the right scrollbar and enable vertical scrolling */
  height: auto; /* Allow the container to expand dynamically */
}

.card-container {
  width: 100%;
  height: 200px;
  background-image: url("../assets/logo4.png"); 
  background-size: cover; 
  background-position:20%; 
  
  
}
</style>