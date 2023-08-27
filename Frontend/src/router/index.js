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
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/manageContestant.vue')
    }
  },
  ,
  {
    path: '/registration',
    name: 'Registration',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/Registration.vue')
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
    path: '/score',
    name: 'Score',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/Score.vue')
    }
  },
  {
    path: '/manageGroup',
    name: 'ManageGroup',
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/manageGroup.vue')
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
