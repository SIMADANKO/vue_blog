<template>
  <div style="margin: 0 auto;">
    <Header></Header>
    <el-container style="height: 600px; border: 1px solid #eee; max-width: calc(100% - 600px);margin-left: 300px" class="comment-container">
<!-- 
      <el-menu default-active="我的信息" class="el-menu-vertical-demo" style="background-color: rgb(238, 241, 246); width: 200px;">
  <el-menu-item index="我的信息">我的信息</el-menu-item>
  <el-menu-item index="头像与背景">头像与背景</el-menu-item>
  <el-menu-item index="修改密码">修改密码</el-menu-item>
</el-menu> -->
      <!-- Main Content Area -->
      <el-main>
        <div style="padding: 20px;">
          <h2>个人资料详情</h2>
          <br>
          <el-form ref="personalForm" :model="formData" :rules="formRules" label-width="80px">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="formData.name" :disabled="!editMode"></el-input>
            </el-form-item>
            <br>
            <!-- <el-form-item label="ID">
              <el-input v-model="formData.id" :disabled="true"></el-input>
            </el-form-item> -->
            <el-form-item label="个性签名" prop="sign">
              <el-input v-model="formData.sign" :disabled="!editMode"></el-input>
            </el-form-item>
            <br>
              <el-form-item>
              <el-button v-if="!editMode" type="primary" @click="toggleEditMode" style="margin-right: 60px;">编辑</el-button>
              <el-button v-else type="primary" @click="savePersonalInfo">保存</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
// Import Header component
import Header from '../components/Header.vue';

export default {
  components: {
    Header
  },
  data() {
    return {
      editMode: false,
      formData: {
        name: 'Admin',
      
        sign: '这是我的个性签名', // Initialize with current email value
        // Add other personal information fields here...
      },
      formRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 3, max: 15, message: '长度必须在3到15个字符之间', trigger: 'blur' }
        ],
        sign: [
          { required: false, message: '请输入个性签名', trigger: 'blur' },
        
        ]
        // Add other validation rules as needed
      }
    };
  },
  methods: {
    // Method to save or update personal information
    savePersonalInfo() {
  this.$refs.personalForm.validate(valid => {
    if (valid) {
      const formData = {
      // Set the user ID dynamically or get it from the session
        username: this.formData.name,
        sign: this.formData.sign
        // Add other personal information fields as needed
      };
      
      this.$axios.post(`http://localhost:8081/user/saveMessage?userId=${this.$store.getters.getUser?.id}`, formData)
        .then(res => {
          if (res.data.code === 200) {
            this.$message.success('个人信息已保存');
            this.editMode = false;
          }
        })
        .catch(error => {
          this.$message.error('保存个人信息失败');
        });
    } else {
      this.$message.error('请填写正确的信息');
    }
  });
},
    toggleEditMode() {
      // Toggle edit mode
      this.editMode = !this.editMode;
    },
    getPersonalInfo() {
  const userId = this.$store.getters.getUser?.id;
  if (!userId) {
    this.$message.error('用户 ID 不存在');
    return;
  }

  this.$axios.get(`http://localhost:8081/user/getPersonalMessage?userId=${userId}`)
    .then(res => {
      if (res.data.code === 200) {
        const data = res.data.data;
        this.formData.name = data.username;
        this.formData.sign = data.sign;
      } else {
        this.$message.error('获取个人信息失败');
      }
    })
    .catch(error => {
      this.$message.error('请求出错，请稍后重试');
    });
}
  },
  created(){
    this.getPersonalInfo()
  }
};
</script>

<style>
/* Add your custom styles here if needed */
</style>