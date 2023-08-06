import Vue from 'vue'
import Keycloak from 'keycloak-js'

const options= {
    url: 'http://35.247.139.32:8080',
    realm: 'ricsg',
    clientId: 'ricsg-app',
  };

const _keycloak = new Keycloak(options)

const Plugin = {
  install(Vue) {
    Vue.$keycloak = _keycloak
  }
}

Plugin.install = Vue => {
  Vue.$keycloak = _keycloak
  Object.defineProperties(Vue.prototype, {
    $keycloak: {
      get() {
        return _keycloak
      }
    }
  })
}

Vue.use(Plugin)

export default Plugin