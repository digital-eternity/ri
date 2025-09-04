import { createApp } from 'vue'
import VueApexCharts from 'vue3-apexcharts'
import './registerServiceWorker'
import router from './router'
import App from './App.vue'
import '@mdi/font/css/materialdesignicons.css'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi'
  }
})

createApp(App)
  .use(router)
  .use(vuetify)
  .use(VueApexCharts)
  .mount('#app')
