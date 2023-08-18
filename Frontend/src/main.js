import Vue from 'vue'
import App from './App.vue'
import router from './router'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
// Import Bootstrap an BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import vueCountryRegionSelect from 'vue3-country-region-select'
import authentication from "@/plugins/authentication"

Vue.use(authentication)
// Make BootstrapVue available throughout your project
Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin)
Vue.config.productionTip = false


Vue.$keycloak
  .init({ 
    // onLoad: 'login-required',
    redirectUri:'http://localhost:8080/',
    pkceMethod: 'S256', 
    checkLoginIframe: false, 
  })
  .then(() => {
    new Vue({
      router,
      render: h => h(App)
    }).$mount('#app')
  })

// Vue.$keycloak.onAuthSuccess = () => {
//   router.replace({ name: "Secure" });
// }

Vue.$keycloak.onAuthLogout = () => {
  console.log('log out');
}

Vue.$keycloak.onTokenExpired = () => {
  console.log('token expired', keycloak.token);
  keycloak.updateToken(30).success(() => {
      console.log('successfully get a new token', keycloak.token);
  }).error(() => {
    console.log('fail to get a new token', keycloak.token);});
}

new Vue({
  router,
  render: function (h) { return h(App) }
}).$mount('#app')