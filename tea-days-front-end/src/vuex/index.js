import { createStore } from 'vuex'
import createPersistedState from "vuex-persistedstate";

import SecureLS from "secure-ls";
const ls = new SecureLS({ isCompression: false });

const store = createStore({
    state () {
        return {
            userId: null,
            role: null,
            help: null,
        }
    },
    plugins: [createPersistedState({
        storage: {
            getItem: (key) => ls.get(key),
            setItem: (key, value) =>
                ls.set(key, value),
            removeItem: (key) => ls.remove(key),
        },
    })],
    mutations: {
        setUserId (state, userId) {
            state.userId = userId;
        },
        setRole (state, role) {
            state.role = role;
        },
        setHelpShown(state) {
            state.help = state.userId;
        }
    },
    getters: {
        isUserLogin: function (state) {
            return state.userId !== null;
        },
        getUserId: function (state) {
          return state.userId;
        },
        getRole: function (state) {
            return state.role;
        },
        getHelpShown: function (state) {
            return state.help === state.userId;
        }
    }
})

export default store
