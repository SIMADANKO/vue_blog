import Vue from 'vue'
import Vuex from 'vuex'


Vue.use(Vuex)

export default new Vuex.Store({
    state:{

            token:localStorage.getItem("token"),
        userInfo:JSON.parse(localStorage.getItem("userInfo"))
    },
    userInfo:{

    },
    mutations:{
        SET_TOKEN:(state,token)=>{
          
            state.token=token
            localStorage.setItem("token",token)
        },
        SET_USERINFO:(state,userInfo)=>{
            state.userInfo=userInfo
            localStorage.setItem("userInfo",JSON.stringify(userInfo))
        },
        REMOVE_INFO:(state)=>{
            state.token=''
            state.userInfo={}
            localStorage.setItem("token",'')
            localStorage.setItem('userInfo',JSON.stringify(''))
        }
    },
    getters:{
getUser:state=>{
    return state.userInfo
}
    },
    actions:{

    },
    modules:{

    }
})
