import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Blogs from '../views/Blogs.vue'
import BlogEdit from '../views/BlogEdit.vue'
import BlogDetail from '../views/BlogDetail.vue'
import HomePage from '../views/HomePage.vue'
import Favorite from '../views/Favorite.vue'
import UserInfo from '../views/UserInfo.vue'
import Followers from '../views/Followers.vue'
import Following from '../views/Following.vue'
import SearchPage from '@/views/SearchPage.vue'
import SignUp from '@/views/SignUp.vue'





Vue.use(VueRouter)

const routes=[
  {
    path:'/',
    name:'Index',
    redirect:{name:"HomePage"}
  },
  {
    path:'/blogs/:userId',
    name:'Blogs',
    component:Blogs,
    meta:{
      requireAuth:true
          }
  },
  {
    path:'/login',
    name:'Login',
    component:Login
  },
  {
    path:'/blog/add',
    name:'BlogAdd',
    
    component:BlogEdit,
    meta:{
requireAuth:true
    }

  }
  ,
  {
    path:'/blog/:blogId',
    name:'BlogDetail',
    component:BlogDetail
  }
  ,
  {
    path:'/blog/:blogId/edit',
    name:'BlogEdit',
    component:BlogEdit,
    meta:{
      requireAuth:true
    }
    
  },
  {
    path:'/homePage',
    name:'HomePage',
    component:HomePage
  },
  {
    path:'/favorite',
    name:'Favorite',
    component:Favorite,
    meta:{
      requireAuth:true
    }
    
  },
  {
    path:'/followers/:userId',
    name:'Followers',
    component:Followers,
    meta:{
      requireAuth:true
    }
    
  },
  {
    path:'/following/:userId',
    name:'Following',
    component:Following,
    meta:{
      requireAuth:true
    },
    
    
  },
  {
    path:'/userinfo',
    name:'UserInfo',
    component:UserInfo,
    meta:{
      requireAuth:true
    }
  },
  {
    path:'/searchpage/:keyword',
    name:'SearchPage',
    component:SearchPage
  },
  {
    path:'/signup',
    name:'SignUp',
    component:SignUp
  }
]


const router = new VueRouter({
  mode:'history',
  base:process.env.BASE_URL,
  routes
})

// router.beforeEach((to, from, next) => {
//   to.matched.forEach((route) => {
//     if (route.meta.requireAuth) {
//       // 在需要认证的路由中
//       if (!isAuthenticated()) {
//         // 如果用户未经过身份验证，则重定向到登录页面
//         next({
//           path: '/login',
//           query: { redirect: to.fullPath }
//         });
//       } else {
//         // 如果用户经过身份验证，则继续导航
//         route.meta.key = to.fullPath; // 设置 key 属性为全地址唯一标识
//         next();
//       }
//     } else {
//       route.meta.key = to.fullPath; // 设置 key 属性为全地址唯一标识
//       next();
//     }
//   });
// });

export default router