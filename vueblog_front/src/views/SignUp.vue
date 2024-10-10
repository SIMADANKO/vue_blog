<template>
    <div>
      <Header></Header>
      <div style="display: flex; justify-content: center;" :style="form-box">
        <transition name="slide" appear>
          <form class="form">
      <span class="title">SignUp</span>
      <span class="subtitle">Create Your Own Account.</span>
      <div class="form-container">
  
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm" style="align-items:center;margin-right: 600px;margin-top: 30px;">
                <el-form-item prop="username">
                  <el-input v-model="ruleForm.username" class="input" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input v-model="ruleForm.password" class="input" show-password placeholder="请输入密码"></el-input>
                </el-form-item>
        </el-form>
      </div>
      <el-button  @click="resetForm('ruleForm')">Sign Up</el-button>
      <div class="form-section">
    <p>Do have an account? <a href="/login">Login</a> </p>
  </div>
  </form>
        </transition>
      </div>
    
  
  
  
  </div>
  
  </template>
  <script>
  import Header from '../components/Header.vue';
  import 'element-ui/lib/theme-chalk/base.css';
  
  export default {
    name: 'SignUp',
    components: { Header },
    data() {
      return {
        showCard: false,
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
        }
      };
    },
    methods: {
      submitForm(formName) {
        const _this = this;
    
    this.$axios.post('http://localhost:8081/login', this.ruleForm)
      .then(res => {
        // 判断后端返回的状态码是否为 200
        if (res.data.code === 200) {
          const jwt = res.headers['authorization'];
          const userInfo = res.data.data;
          _this.$store.commit("SET_TOKEN", jwt);
          _this.$store.commit("SET_USERINFO", userInfo);
          _this.$router.push("/homePage");
          alert("登录成功");
        } else {
          // 后端返回状态码不为 200，处理登录失败的情况
          const errorMessage = res.data.msg ;
          console.log(errorMessage);
          alert(errorMessage);
        }
      }).catch(error => {
          if (error.response && error.response.status === 400) {
            // 如果用户名已存在，服务器返回状态码 400
            alert(res.data.msg); // 显示后端返回的错误信息
          } 
        });
        
      },
      resetForm(formName) {
        this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this;
          this.$axios.post('http://localhost:8081/register', this.ruleForm)
            .then(res => {
              const jwt = res.headers['authorization'];
              const userInfo = res.data.data;
              _this.$store.commit("SET_TOKEN", jwt);
              _this.$store.commit("SET_USERINFO", userInfo);
              _this.$router.push("/homePage");
              alert("用户注册成功");
            })
            .catch(error => {
              if (error.response && error.response.status === 409) {
                // 如果用户名已存在，服务器返回状态码 409
                alert(error.response.data.message); // 显示后端返回的错误信息
              } else {
                console.error('注册失败：', error);
               
              }
            });
        } else {
          console.log('error submit!!');
          alert("用户名或密码不合法");
          return false;
        }
      });
    }
    },
    mounted() {
      setTimeout(() => this.showCard = true, 100); // 小延迟确保渐显效果
    }
  }
  </script>
  
  <style scoped>
  
  .slide-enter-active, .slide-leave-active {
    transition: transform 1.0s;
  }
  .slide-enter, .slide-leave-to {
    transform: translateY(-50px);
    opacity: 0;
  }
  
  
  .form-box {
    max-width: 300px;
    background: #f1f7fe;
    overflow: hidden;
    border-radius: 16px;
    color: #010101;
  }
  
  .form {
    position: relative;
    display: flex;
    flex-direction: column;
    padding: 32px 24px 24px;
    gap: 16px;
    text-align: center;
  }
  
  /*Form text*/
  .title {
    font-weight: bold;
    font-size: 1.6rem;
  }
  
  .subtitle {
    font-size: 1rem;
    color: #666;
  }
  
  /*Inputs box*/
  .form-container {
    overflow: hidden;
  
    background-color: #fff;
    margin: 1rem 0 .5rem;
    width: 100%;
  }
  
  .input {
    background: none;
    border: 0;
    outline: 0;
    height: 40px;
    width: 100%;
    border-bottom: 1px solid #eee;
    font-size: .9rem;
    padding: 8px 15px;
  }
  
  .form-section {
    padding: 16px;
    font-size: .85rem;
    background-color: #e0ecfb;
    box-shadow: rgb(0 0 0 / 8%) 0 -1px;
    width: 40%;
    margin-left: 30%;
  }
  
  .form-section a {
    font-weight: bold;
    color: #b2ede3;
    transition: color .3s ease;
  }
  
  .form-section a:hover {
    color: #a4d5d8;
    text-decoration: underline;
  }
  
  /*Button*/
  .form button {
    background-color: #98e8d5;
    color: #fff;
    border: 0;
    border-radius: 24px;
    padding: 10px 16px;
    font-size: 1rem;
    font-weight: 600;
    cursor: pointer;
    transition: background-color .3s ease;
    width: 300px;
    margin-left: 455px;
  }
  
  .form button:hover {
    background-color: #00e2e6;
  }
  </style>