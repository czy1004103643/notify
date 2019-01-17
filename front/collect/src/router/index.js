import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Main',
      component:resolve => require(['@/components/Login'], resolve)
    },
    {
      path: '/login',
      name: 'Login',
      component:resolve => require(['@/components/Login'], resolve)
    },
    {
      path: '/exit',
      name: 'Exit',
      component:resolve => require(['@/components/Login'], resolve)
    },
    {
      path: '/service',
      name: 'Service',
      component:resolve => require(['@/components/Navigation'], resolve)
    },
    {
      path: '/collect/:ticket',
      name: 'Collect',
      component: resolve => require(['@/components/Collect'], resolve)
    },
    {
      path:'*',
      component:resolve => require(['@/components/NotFound'], resolve)
    }
  ]
})
