import axios from "axios";
import Element from "element-ui";
import router from "./router";
import store from './store';

axios.defaults.baseURL = "http://localhost:8081";

// 前置拦截器
axios.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = token;
    }
    console.log(config)
    console.log(token)
    return config;
});

// 后置拦截器
axios.interceptors.response.use(
    response => {
        // 对于成功的响应，直接返回响应数据
        return response;
    },
    error => {
        // 对于错误的响应，根据状态码进行不同的处理
        const { response } = error;
        if (response) {
            switch (response.status) {
                case 401:
                    store.commit("REMOVE_INFO");
                    router.push("/login");
                    error.message = '请重新登录';
                    break;
                case 403:
                    error.message = '权限不足，无法访问';
                    break;
                default:
                    if (response.data && response.data.msg) {
                        error.message = response.data.msg;
                    } else {
                        error.message = '请求错误';
                    }
            }
        } else {
            error.message = '网络错误，请稍后重试';
        }
        
        Element.Message({
            message: error.message,
            type: 'error',
            duration: 2 * 1000
        });

        return Promise.reject(error);
    }
);

export default axios;