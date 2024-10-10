<template>
  <div>
    <Header></Header>
    <el-dialog
  title="上传头像"
  :visible.sync="dialogVisible"
  width="30%"
  center>
  
  <vue-cropper
    v-if="imageUrl"
    class="cropper"
    ref="cropper"
    :src="imageUrl"
    :options="cropperOptions"
    style="width: 100%; height: 300px;"
  ></vue-cropper>
  <el-upload
    v-else
    class="avatar-uploader"
    action="#"
    :show-file-list="false"
    :on-change="handleAvatarUpload"
    drag
    ref="upload">
    <i class="el-icon-plus avatar-uploader-icon"></i>
  </el-upload>
  <span slot="footer" class="dialog-footer">
    <el-button @click="applyCrop">应用</el-button>
    <el-button @click="dialogVisible = false">取消</el-button>
  </span>
</el-dialog>


      <el-container>
  <!-- 头部区域 -->
        <el-header>
          <el-card style="margin-bottom: 20px;height: 200px;width: 80%;margin-left: 12.5%;" shadow="hover" class="blogscard-container">
                      <div style="display: flex; align-items: center;">
                <img :src="user.avatar" style="width: 50px; height: 50px; border-radius: 50%; margin-right: 20px; object-fit: cover;" @click="showDialog" :fit="fill">
                <div style="text-align: left;color:white;size: 15px; font-weight: bold;">
                  <p>ID: {{ user.username }}</p>
                  <p>个性签名: {{ user.sign }}</p>
            
                   <div>
                  
              </div>
              
                <div></div>
                  <!-- 其他粉丝信息... -->
                </div>
                <el-button v-if="!submitOrNot&&!ownPageOrNot" type="primary" style="margin-left: auto;" @click="follow(user.userId)">关注</el-button>
                <el-button v-else-if="!ownPageOrNot" type="primary" style="margin-left: auto;" @click="cancel(user.userId)">取消关注</el-button>
      
              </div>
              <div style="display: flex; justify-content: flex-end;color:white;margin-top: 20px;">
  
                <div style="margin-top: 25px;margin-left: 20px;">
  
                  <p>
                    
    <router-link :to="'/followers/' +$route.params.userId" style=" text-decoration: none;
    color:white;
    font-weight: bold;">粉丝数：{{ following }}</router-link> 

    &nbsp;&nbsp;
    <router-link :to="'/following/' +$route.params.userId" style=" text-decoration: none;
    color:white;
    font-weight: bold;">关注数：{{ followers }}</router-link> 
</p>

  </div>
 
    <router-link v-for="blog in blogs" :key="blog.id" :to="{ name: 'BlogDetail', params: { blogId: blog.id } }" style="text-decoration: none;">
      <!-- 添加相应的内容 -->
    </router-link>
  
</div>
            </el-card>
          </el-header>
          <el-container style="margin-top: 175px;margin-left: 30px;">
      <!-- 左侧区域 -->
      
        <!-- <el-aside width="200px" style="background-color: rgb(238, 241, 246);margin-bottom: 10px;height: 400px;">
          <el-card >
      <el-menu :default-openeds="['1', '3']">
      
       
      
      </el-menu>
    </el-card>
    </el-aside> -->
    <el-card style="width: 80%;margin-left: 11.5%;margin-bottom: 10px;margin-right: 20px;">
      <el-main style="margin-top:-30px ;">
        <div style="width: calc(100% - 100px); text-align: left;">
        <router-link v-for="blog in blogs" :key="blog.id" :to="{ name: 'BlogDetail', params: { blogId: blog.id } }" style="text-decoration: none;">
          <div style="display: flex; align-items: center;">
            <img :src="`http://localhost:8081/getPostPage/${blog.id}`" style="width: 160px; height: 90px; margin-right: 20px; object-fit: cover;" alt="" >
            <div style="flex: 1; border-bottom: 1px solid #ccc; padding-bottom: 10px;">
              <h4 style="font-size: 18px;">{{ blog.title }}</h4>
              <p style="font-size: 14px; color: #666;">{{ blog.description }}</p>
              <p style="font-size: 12px; color: #999;">创建时间: {{ blog.created }}</p>
            </div>
            <div style="text-align: right;">
      </div>
          </div>
        </router-link>
      </div></el-main>
    </el-card>
    </el-container>
     
   </el-container>

    <el-pagination class="mpage" background layout="prev, pager, next" :current-page="currentPage" :page-size="pageSize" @current-change="page" :total="total">
    </el-pagination>

  </div>
</template>

<script>
import Header from '../components/Header.vue';
import VueCropper from 'vue-cropperjs';
import 'cropperjs/dist/cropper.css';
import { RouterLink } from 'vue-router';
import router from '@/router';

export default {
  name: "Blogs.vue",
  components: {
    Header,
    VueCropper 
  },
  data() {
    return {
      like: true,
      followers: '',
      following: '',
      blogs: [], // initialize as empty array or with actual data
    fill: '', // initialize with default value or actual data
    粉丝数: 0, // initialize with default value or actual data
    关注数: 0 ,// initialize with default value or actual data
      
        ownPageOrNot:false,
        submitOrNot:false,
      user:{
        userId:'',
        username:'',
        avatar:'',
        sign:''
      },
      currentPage: 1,
      total: 0,
      pageSize: 5,
      dialogVisible: false,
      imageUrl: '',// 图片地址
      categories: ["分类一", "分类二", "分类三"] ,// 示例分类数据，你需要替换为实际数据
      cropperOptions: { // 裁剪选项
        aspectRatio: 1,  // 定义裁剪框的宽高比
        autoCropArea: 1,  // 裁剪区域占图片的比例
        movable: false,
        zoomable: false,
        scalable: false,
        
      }
    };
  },
  created() {
  this.getFollow();
  this.getFollowing();
  this.loadData();
},
  methods: {
    page(currentPage) {
  this.getUser().then(() => {
    this.$axios.get(`http://localhost:8081/blogs?currentPage=${currentPage}&userId=${this.user.userId}`)
      .then((res) => {
        this.blogs = res.data.data.records;
        this.currentPage = res.data.data.current;
        this.total = res.data.data.total;
        this.pageSize = res.data.data.size;
      })
      .catch((error) => {
        console.error('Error fetching blogs:', error);
      });
  
  });
 
},
    handleAvatarUpload(file) {
    // 处理文件上传
    // 假设 file 是通过 input 获取的文件对象
    const reader = new FileReader();
    reader.onload = e => {
      this.imageUrl = e.target.result;
    };
    reader.readAsDataURL(file.raw);
  },
  applyCrop() {
    const croppedCanvas = this.$refs.cropper.getCroppedCanvas();
    croppedCanvas.toBlob(blob => {
     
      this.uploadCroppedImage(blob);   // 假设这是上传到服务器的函数
    });
  },
  uploadCroppedImage(blob) {
    const userData = JSON.parse(localStorage.getItem('userInfo'));
    // 上传裁剪后的图片
    // 发送到服务器的逻辑
    const formData = new FormData();
    formData.append('file', blob); // 使用 "file" 作为参数名
    formData.append('userId', userData.id); // 添加 userId
    this.$axios
      .post('http://localhost:8081/user/uploadAvatar', formData)
      .then(response => {
        // 处理上传成功的逻辑
        console.log('Avatar uploaded successfully:', response.data);
        this.$message({
          message: '上传成功',
          type: 'success'
        });
        this.dialogVisible=false
      })
      .catch(error => {
        console.error('Error uploading avatar:', error);
      });
  },
  showDialog() {
    
    const storedUserId = JSON.parse(localStorage.getItem('userInfo')).id;
    console.log(storedUserId);
    console.log(this.$route.params.userId);
    if (storedUserId == this.user.userId) {
      this.dialogVisible = true;
    } else {
      // 弹窗提示用户ID不匹配或其他操作
      console.log('用户ID不匹配');
    }
  },
  getUser() {
  const userId = this.$route.params.userId;
  console.log(userId);
  return this.$axios.get(`http://localhost:8081/user/getUser?userId=${userId}`).then((res) => {
    this.user.userId = userId;
    this.user.username = res.data.userName;
    this.user.avatar = `http://localhost:8081/user/avatar/${userId}`;
  });
},getFollow(){
  const userId = this.$route.params.userId;
  this.$axios.get(`http://localhost:8081/getfollowernumber?followerId=${userId}`).then((res)=>{
    this.followers=res.data.data
  })
},
getFollowing(){
  const userId = this.$route.params.userId
  this.$axios.get(`http://localhost:8081/getfollowingnumber?followingId=${userId}`).then((res)=>{
    this.following=res.data.data
  })
},
checkFollowStatus() {
    const followingId = this.$route.params.userId;
    const followerId=JSON.parse(localStorage.getItem('userInfo')).id
    console.log(followerId);
    console.log(followingId)
    console.log("------------------------------------------------------------------------------------")
    try {
       this.$axios.get(`http://localhost:8081/havefollow?followerId=${followerId}&followingId=${followingId}`).then((res)=>{
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
  },
  checkOwnPage(){
    const localId = this.$route.params.userId;
    const userId=JSON.parse(localStorage.getItem('userInfo')).id
    if(localId==userId){
this.ownPageOrNot=true;
    }else{
      this.ownPageOrNot=false
    }
  },
  follow() {
    const followerId = JSON.parse(localStorage.getItem('userInfo')).id;

    this.$axios.post(`http://localhost:8081/follow?followerId=${followerId}&followingId=${this.user.userId}`).then(() => {
   
      alert("关注成功");
      this.submitOrNot=true;
    });
  },
  cancel(userId) {
    const followerId = JSON.parse(localStorage.getItem('userInfo')).id;
    this.$axios.delete(`http://localhost:8081/deletefollow?followerId=${followerId}&followingId=${userId}`).then(() => {
      alert("取消关注成功");
      this.submitOrNot=false;
    });
  },
  getPersonalInfo() {

  const userId = this.$route.params.userId;
  if (!userId) {
    this.$message.error('用户 ID 不存在');
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
},

loadData() {
    // 在这里根据新的userId参数加载对应的数据
   this.user.userId = this.$route.params.userId;
 
  }
  },
  mounted() {
   
    
  //   this.getFollow();
  //   this.getFollowing();
  this. getPersonalInfo();
this.checkFollowStatus();
 this.checkOwnPage();
    // 在组件挂载时获取 userId
    this.page(1);
  
  },
  
  watch: {
  '$route': {
    immediate: true,
    handler() {
      // 监听路由参数的变化
      // 在这里可以根据新的userId参数进行逻辑处理或重新获取数据
      this.loadData();
    }
  }
}
};
</script>
<style scoped>
  .like {
    cursor: pointer;
    font-size: 25px;
    display: inline-block;
  }

  .blogscard-container {
  width: 100%;
  height: 200px;
  background-image: url("../assets/logo4.png"); 
  background-size: cover; 
  background-position:20%; 
  
  
}
</style>