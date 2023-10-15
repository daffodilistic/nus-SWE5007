import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/manageContestant',
    name: 'ManageContestant',

    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/manageContestant.vue')
    }
  },
  {
    path: '/manageTeam',
    name: 'ManageTeam',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/manageTeam.vue')
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/Login.vue')
    }
  },
  {
    path: '/registration',
    name: 'Registration',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/Registration.vue')
    }
  },
  {
    path: '/manageMetric',
    name: 'ManageMetric',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/manageMetric.vue')
    }
  },
  {
    path: '/contestantProfile',
    name: 'ContestantProfile',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/contestantProfile.vue')
    }
  },
  {
    path: '/manageGroup',
    name: 'ManageGroup',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/manageGroup.vue')
    }
  }
  ,
  {
    path: '/upload',
    name: 'upload',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/Upload.vue')
    }
  },
  {
    path: '/mainScore',
    name: 'mainScore',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/mainScore.vue')
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
