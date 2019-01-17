import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const tokenState = new Vuex.Store({
  // 定义状态
  state: {
    token: null
  },
  mutations: {
    setToken(state, token) {
      state.token = token
    }
  },
  getters: {
    token(state) {
      return state.token
    }
  }
})

const COLLECT_URL = "http://main.melodyl.xyz/table/#/collect"
const SERVER_URL = "http://main.melodyl.xyz/tableService/notify"
const TOKEN_ATTRIBUTE_NAME = "Token"

const responseContext = new Map([
  ["OK", { code: 200, message: "执行成功" }],
  ["CREATED", { code: 201, message: "创建成功" }],
  ["ACCEPTED", { code: 202, message: "接受资源" }],
  ["NON_AUTHORITATIVE_INFORMATION", { code: 203, message: "非官方信息" }],
  ["NO_CONTENT", { code: 204, message: "无内容" }],
  ["FOUND", { code: 302, message: "资源存在" }],
  ["BAD_REQUEST", { code: 400, message: "客户端提交异常，请检查提交格式" }],
  ["UNAUTHORIZED", { code: 401, message: "客户端认证失败" }],
  ["NOT_FOUND", { code: 404, message: "资源不存在" }],
  ["CONFLICT", { code: 409, message: "资源存在冲突" }],
  ["INTERNAL_SERVER_ERROR", { code: 500, message: "服务异常" }]
])

const responseCodeContext = new Map([
  [200, "OK"], [201, "CREATED"], [202, "ACCEPTED"], [203, "NON_AUTHORITATIVE_INFORMATION"], [204, "NO_CONTENT"], 
  [302, "FOUND"], 
  [400, "BAD_REQUEST"], [401, "UNAUTHORIZED"],[404,"NOT_FOUND"],[409,"CONFLICT"],
  [500, "INTERNAL_SERVER_ERROR"]
])

function getResponseCode(key) {
  return responseContext.get(key).code
}

function getResponseMessage(key) {
  return responseContext.get(key).message
}

function getResponseMsgFromCode(key) {
  if(key>=500) 
    key = 500
  return responseContext.get(responseCodeContext.get(key)).message
}

export default {
  tokenState,
  SERVER_URL, TOKEN_ATTRIBUTE_NAME,COLLECT_URL,
  getResponseCode,
  getResponseMessage,
  getResponseMsgFromCode
}
