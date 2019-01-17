import Vue from 'vue'
import axios from 'axios'
import App from './App'
import router from './router'
import store from './store'
import 'bootstrap/dist/js/bootstrap.min'

Vue.config.productionTip = false //设置为 false 以阻止 vue 在启动时生成生产提示。

axios.defaults.baseURL = store.SERVER_URL
Vue.prototype.$ajax = axios

router.beforeEach((to, from, next) => {
  var token = sessionStorage.getItem('Token')

  if (to.path === '/exit') {
    sessionStorage.removeItem('Token')
    store.tokenState.token = null
    token = null
  }

  if (to.path === '/login' || to.path.indexOf('/collect/') === 0) {
    next()
  } else if (token) { // 登陆成功
    sessionStorage.setItem("Token", token)
    store.tokenState.commit('setToken', token)
    next()
  } else if (!token) { // 没登录就想访问
    router.push({name:'Login'})
  }
})

new Vue({
  el: '#app', //提供一个在页面上已存在的 DOM 元素作为 Vue 实例的挂载目标
  router,
  // 把 store 对象提供给 “store” 选项，这可以把 store 的实例注入所有的子组件
  // 通过在根实例中注册 store 选项，该 store 实例会注入到根组件下的所有子组件中，且子组件能通过 this.$store 访问到
  store,
  components: { App },
  template: '<App/>'
})
