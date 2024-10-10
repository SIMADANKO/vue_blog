<template>
  <div>
    <Header></Header>
    <el-dialog
  title="上传封面"
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

    <div class="m-content" style="height: 500px;margin-left: 10%;margin-right: 10%;margin-top: 3%;">
      <el-form ref="editForm" status-icon :model="editForm" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="editForm.title"></el-input>
        </el-form-item>
        <el-form-item label="摘要" prop="description">
          <el-input type="textarea" v-model="editForm.description"></el-input>
        </el-form-item>
        
        <el-form-item label="内容" prop="content">
          <mavon-editor v-model="editForm.content" @imgAdd="imgAdd" ref="md" :ishljs="true" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm()">立即创建</el-button>
          
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import Header from "../components/Header.vue";
import VueCropper from 'vue-cropperjs';
import 'cropperjs/dist/cropper.css';


export default {
  name: "BlogEdit",
  components: { Header, VueCropper },
  data() {
    return {
      dialogVisible: false,
      imageUrl: '',
      editForm: {
        title: '',
        description: '',
        content: '',
        
      },
      blogId:'',
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入摘要', trigger: 'blur' }
        ]
      }
      
    }
  },
  created() {
    const blogId = this.$route.params.blogId;
    if (blogId) {
      this.$axios.get('http://localhost:8081/blog/' + blogId).then((res) => {
        const blog = res.data.data;
        this.editForm.id = blog.id;
        this.editForm.title = blog.title;
        this.editForm.description = blog.description;
        this.editForm.content = blog.content;
      });
    }
  },
  methods: {
    submitForm() {
    this.$refs.editForm.validate((valid) => {
      if (valid) {
        const userId = this.$store.getters.getUser?.id;
        this.$axios.post('http://localhost:8081/blog/edit', this.editForm, {
          headers: {
            "Authorization": localStorage.getItem("token")
          }
        }).then((res) => {
          this.blogId = res.data.data; // 博客id应为res.data
          console.log(this. dialogVisible)

          this.$confirm('是否上传封面图片?', '提示', {
  confirmButtonText: '确认',
  cancelButtonText: '取消',
  type: 'warning'
}).then(() => {
            // 用户确认上传封面
            this.showDialog();
          }).catch(() => {
            // 用户取消上传封面
            this.finalizeSubmission();
          });
        }).catch(() => {
  // 用户取消上传封面
  this.finalizeSubmission();
});console.log(this. dialogVisible)
          this.$alert('操作成功', '提示', {
            confirmButtonText: '确定',
          }).catch(() => {
          alert("发布失败");
        });
      } else {
        console.log('error submit!!');
        alert("发布失败");
        return false;
      }
    });
  },
    imgAdd(pos, $file) {
      var formdata = new FormData();
      formdata.append('file', $file);
      this.$axios({
        url: `http://localhost:8081/uploadImage`,
        method: 'post',
        data: formdata,
        headers: { 'Content-Type': 'multipart/form-data' },
      }).then((response) => {
        let url = response.data.data;
        this.$refs.md.$img2Url(pos, `http://localhost:8081/picture/${url}`);
      }).catch((error) => {
        console.error("上传图片出错", error);
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

    // 上传裁剪后的图片
    // 发送到服务器的逻辑
    const formData = new FormData();
    formData.append('file', blob); // 使用 "file" 作为参数名
    formData.append('blogId', this.blogId); // 添加 userId
    this.$axios
      .post('http://localhost:8081/uploadPostPage', formData)
      .then(response => {
        // 处理上传成功的逻辑
        console.log('Avatar uploaded successfully:', response.data);
        this.$message({
          message: '上传成功',
          type: 'success'
        });
     
        this.$router.push(`/blogs/${JSON.parse(localStorage.getItem('userInfo')).id}`);
        this.dialogVisible=false
      })
      .catch(error => {
        console.error('Error uploading avatar:', error);
        
      });
      
  },
  showDialog() {
    

    
      this.dialogVisible = true;

      // 弹窗提示用户ID不匹配或其他操作
 
    },
    finalizeSubmission(){
      this.$router.push(`/blogs/${JSON.parse(localStorage.getItem('userInfo')).id}`);
    }
    
    
  }
  }

</script>