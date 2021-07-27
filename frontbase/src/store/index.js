import Vue from "vue";
import Vuex from "vuex";
// import router from "../router";
import jwt_decode from "jwt-decode";
import { findById } from "@/api/user.js";
import { createInstance } from "../api/teamindex";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        isLogin: false, // 로그인 여부
        memberInfo: null,
        teamLists: [], // 팀 정보
        groupInfo: [], // 각 그룹 정보
    },
    getters: {
        teamLists(state) {
            return state.teamLists;
        },
        // groupInfo(state) {
        //     return state.groupInfo;
        // }
    },
    mutations: {
    setIsLogined(state, isLogin) {
        state.isLogin = isLogin;
    },
    setMemberInfo(state, memberInfo) {
        state.isLogin = true;
        state.memberInfo = memberInfo;
    },
    logout(state) {
        state.isLogin = false;
        state.memberInfo = null;
    },
    setTeamLists(state, teamLists) {
        state.teamLists = teamLists;
    },
    // setGroupInfo(state, groupInfo) {
    //     state.groupInfo = groupInfo;
    // },
},
    actions: {
    async GET_MEMBER_INFO({ commit }, token) {
        let decode = jwt_decode(token);
            console.log(decode);
        await findById(
        decode.memberEmail,
        (response) => {
            if (response.data.message === "success") {
            commit("setMemberInfo", response.data.memberInfo);
            // router.push("/");
            // router.go(router.currentRoute);
            } else {
                console.log("유저 정보 없음!!");
            }
        },
        (error) => {
            console.log(error);
        }
    );
    },
    LOGOUT({ commit }) {
        commit("logout");
        localStorage.removeItem("access-token");
        },
    getTeamLists({ commit }) {
        const instance = createInstance();
        instance
        .get("/team")
        .then((response) => {
            console.log(response.data.object);
            commit("setTeamLists", response.data.object);
        })
        .catch(() => {
            console.log("에러");
        });
        },
    // getGroupInfo({ commit,  }) {
    //     const instance = createInstance();
    //     instance
    //     .get("/team"+teamId)
    //     .then((response) => {
    //         commit("setGroupInfo", response.data.object);
    //     })
    //     .catch(() => {
    //         console.log("에러");
    //     });
    //     },
    },
});
