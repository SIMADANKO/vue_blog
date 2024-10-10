<template>
  <div>
    <!-- <el-dialog

  :visible.sync="dialogVisible"
  width="40%" 
  style="margin-top: 10%;padding: 0;"
 
  center >
  
 

          <div style="display: flex; justify-content: center;" class="logincard">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm" style="align-items:center;margin-right: 80px;margin-top: 30px;height: 200px;">
              <el-form-item prop="username">
                <el-input v-model="ruleForm.username" class="input-span" placeholder="请输入用户名" style="width: 350px;"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="ruleForm.password" class="input-span" show-password placeholder="请输入密码" style="width: 350px;"></el-input>
              </el-form-item>
              <el-form-item style="margin-left: 100px;">
                <el-button  @click="submitForm('ruleForm')" round>登录</el-button>
                <el-button @click="resetForm('ruleForm')" round>注册</el-button>
              </el-form-item>
            </el-form>
          </div>
      

</el-dialog> -->

    <el-menu class="el-menu-demo" mode="horizontal" style="justify-content: center;">
      <el-button @click="goTo('/homePage')" type="text"></el-button><el-menu-item index="1"><el-button @click="goTo('/homePage')" type="text">首页</el-button></el-menu-item>
      <el-menu-item index="2"><el-button @click="goTo(`/blogs/${user.userId}`)" type="text" :key="$route.fullPath">我的主页</el-button></el-menu-item>
      <el-menu-item index="3"><el-button @click="goTo('/blog/add')" type="text">创作博客</el-button></el-menu-item>
  
      <el-submenu index="5">
        <template slot="title">我的</template>
        <el-menu-item index="4-1"><el-button @click="goTo('/favorite')" type="text">我的收藏</el-button></el-menu-item>
        <el-menu-item index="4-2"><el-button @click="goTo('/followers/'+userId)" type="text">我的粉丝</el-button></el-menu-item>
        <el-menu-item index="4-3"><el-button @click="goTo('/following/'+userId)" type="text">我的关注</el-button></el-menu-item>
        <el-menu-item index="4-4"><el-button @click="goTo('/userinfo')" type="text">我的个人资料</el-button></el-menu-item>
      </el-submenu>

     <el-menu-item v-if="loginStatus" index="5" @click="logout" class="logout-button"><el-button type="text">退出登录</el-button></el-menu-item>
      <el-menu-item v-if="!loginStatus" index="6" class="logout-button" @click="goTo('/login')"><el-button @click="login" type="text">登录</el-button></el-menu-item>

      <el-menu-item index="7" style="margin-left: 10px;">
        <input v-model="searchKeyword" placeholder="请输入关键字搜索" style="width: 300px;"  class="input"></input>
        <!-- 搜索按钮 -->
        <button type="primary" @click="search" class="searchbutton">搜索</button>
        <el-avatar v-if="user.avatar" :size="45" :src="user.avatar" style="margin-left: 10px;object-fit: cover;" ></el-avatar>
      </el-menu-item>
    </el-menu>
    
  </div>
</template>

<script>
export default {
  name: "Header.vue",

  data() {
    return {
      user: {
        userId:'',
        username: '',
        avatar: '',
        squareUrl: "",
        sizeList: ["large", "medium", "small"],
        blogs: [],
        currentPage: 1,
        pageSize: 10,
        total: 100,
        page: 1,
       
      },
      searchKeyword: '' ,
      dialogVisible:false,
        ruleForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
        ]
      },
      loginStatus:false
    };
  },

  methods: {
    logout() {
      const _this = this;
      this.$axios.get('http://localhost:8081/logout', {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then((res) => {
        _this.$store.commit('REMOVE_INFO');
        _this.$router.push('/login');
      });
    },
    goTo(path) {
      this.$router.push(path);
    },
    search() {
      const keyword = this.searchKeyword.trim();
      if (keyword) {
        this.$router.push(`/searchpage/${keyword}`);
      } else {
        // 处理空搜索关键字的情况
      }
    },
    getavatar() {
      this.user.userId = JSON.parse(localStorage.getItem('userInfo')).id;
      this.user.avatar = `http://localhost:8081/user/avatar/${this.user.userId}`;
    },
    // login(){
    //   this.dialogVisible=true
    // },
    checkUserStatus() {
      
      const id = JSON.parse(localStorage.getItem('userInfo')).id;
  if (id!=null) {
  
    this.loginStatus=true;
    }else{
      this.loginStatus=false;
    }
  },
  //   submitForm(formName) {
  //     const _this = this;
  
  // this.$axios.post('http://localhost:8081/login', this.ruleForm)
  //   .then(res => {
  //     // 判断后端返回的状态码是否为 200
  //     if (res.data.code === 200) {
  //       this.dialogVisible=false
  //       const jwt = res.headers['authorization'];
  //       const userInfo = res.data.data;
  //       _this.$store.commit("SET_TOKEN", jwt);
  //       _this.$store.commit("SET_USERINFO", userInfo);
  //       _this.$router.push("/homePage");
  //       alert("登录成功");
  //     } else {
  //       // 后端返回状态码不为 200，处理登录失败的情况
  //       const errorMessage = res.data.msg ;
  //       console.log(errorMessage);
  //       alert(errorMessage);
  //     }
  //   }).catch(error => {
  //       if (error.response && error.response.status === 400) {
  //         // 如果用户名已存在，服务器返回状态码 400
  //         alert(res.data.msg); // 显示后端返回的错误信息
  //       } 
  //     });
      
  //   },
  //   resetForm(formName) {
  //     this.$refs[formName].validate((valid) => {
  //     if (valid) {
  //       const _this = this;
  //       this.$axios.post('http://localhost:8081/register', this.ruleForm)
  //         .then(res => {
  //           const jwt = res.headers['authorization'];
  //           const userInfo = res.data.data;
  //           _this.$store.commit("SET_TOKEN", jwt);
  //           _this.$store.commit("SET_USERINFO", userInfo);
  //           _this.$router.push("/homePage");
  //           alert("用户注册成功");
  //         })
  //         .catch(error => {
  //           if (error.response && error.response.status === 409) {
  //             // 如果用户名已存在，服务器返回状态码 409
  //             alert(error.response.data.message); // 显示后端返回的错误信息
  //           } else {
  //             console.error('注册失败：', error);
             
  //           }
  //         });
  //     } else {
  //       console.log('error submit!!');
  //       alert("用户名或密码不合法");
  //       return false;
  //     }
  //   });
  // },
  
  },

  mounted() {
    this.userId = JSON.parse(localStorage.getItem('userInfo')).id;
    this.getavatar();

  },
  created(){
   this.checkUserStatus();
  }
};
</script>

<style>
.el-menu-demo .el-button,
.el-menu-demo .el-menu-item,
.el-menu-demo .el-submenu__title,
.el-menu-demo .el-submenu .el-menu-item > a {
  color: #a2a0a0; /* 修改颜色为灰色 */
  cursor: pointer;
}
.el-menu-demo .el-button:hover,
.el-menu-demo .el-menu-item:hover,
.el-menu-demo .el-submenu__title:hover,
.el-menu-demo .el-submenu .el-menu-item:hover > a {
  color: #1b1e21; /* 鼠标悬停时颜色改为主题色 */
}

.searchbutton {
margin-left: 5px;
  padding: 0.90em 2.5em;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 2.5px;
  font-weight: 500;
  color: #95ada3;
  background-color: #c6e6d0;
  border: none;
  border-radius: 45px;
  box-shadow: 0px 8px 15px rgba(205, 205, 205, 0.1);
  transition: all 0.3s ease 0s;
  cursor: pointer;
  outline: none;
}

.searchbutton:hover {
  background-color: #23c483;
  box-shadow: 0px 15px 20px rgba(52, 154, 113, 0.4);
  color: #424242;
  transform: translateY(-7px);
}

.searchbutton:active {
  transform: translateY(-1px);
}

.input {
  margin-left: 250px;
 border: none;
 padding: 0.65rem;
 border-radius: 1rem;
 background: #bbccc9;

 transition: 0.3s;
}

.input:focus {
 outline-color: #9cbe93;
 background: #ffffff;
 box-shadow: inset 20px 20px 60px #c5dbd8,
		inset -20px -20px 60px #acacac;
 transition: 0.3s;
}

.logincard{
  background-image: url('../assets/logo2.png');
  background-size: cover;
}
</style>