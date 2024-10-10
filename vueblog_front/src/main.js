// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import axios from 'axios'
import "./permission"
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import "./axios"
import '@fortawesome/fontawesome-free/css/all.css';
import Cropper from 'cropperjs';
import autoTextarea from 'auto-textarea'

Vue.use(autoTextarea);

// 全局注册



Vue.prototype.$Cropper = Cropper;
Vue.prototype.$axios=axios
Vue.use(Element)
Vue.config.productionTip = false
Vue.use(mavonEditor)


/* eslint-disable no-new */
new Vue({

  router,
  store,
  render:h =>h (App),
}).$mount('#app')
