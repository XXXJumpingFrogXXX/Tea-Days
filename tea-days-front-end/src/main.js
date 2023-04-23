import { createApp } from 'vue'
import App from './App.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"
import "bootstrap-icons/font/bootstrap-icons.css"
import './styles/main.css';
import router from "./router/index"
import store from "./vuex/index";

let app = createApp(App)
app.use(router)
app.use(store)
router.$store = store
app.mount('#app')

