<template>
    <div id="login-container">
        <div class="container">
            <div class="container-h1">
                <img src="http://main.melodyl.xyz/static/image/hello.jpeg" height="200"/>
            </div>
            <h1 class="container-h1">欢&nbsp;迎</h1>
            <div class="container-form form-horizontal">
                <div class="form-group">
                    <div class="col-md-12">
                        <div class="control-wrapper">
                            <label class="control-label">
                                <i class="glyphicon glyphicon-user" aria-hidden="true"></i>
                            </label>
                            <input type="text" class="form-control" id="userName" placeholder="请输入用户名" v-model='user.userName'>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-12">
                        <div class="control-wrapper">
                            <label class="control-label">
                                <i class="glyphicon glyphicon-lock" aria-hidden="true"></i>
                            </label>
                            <input type="password" class="form-control" id="password" placeholder="请输入密码" v-model='user.password'>
                        </div>
                    </div>
                </div>
                <hr/>
                <div class="form-group">
                    <div class="col-sm-12">
                        <button id="loginbtn" class="btn btn-default btn-block btn-primary" v-on:click="login">登录</button>
                    </div>
                </div>
            </div>
            <div class="text-center container-bottom">
                <p  data-toggle="modal" data-target="#myModal">
                    <a>申请账号？</a>
                </p>
                <p v-if="validateMsg" style="color:red">
                    {{validateMsg}}
                </p>
                <br/>
            </div>
        </div>

        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel">说明</h4>
                    </div>
                    <div class="modal-body">
                        <p>
                            > 创建这个项目的是为了方便收集集体信息。
                            其实如果不是隐私信息可以用腾讯文档，这个可以支持多人协作。
                            班级的每个人的信息还是只有班长知道的好。
                            所以就有了这款产品的诞生。
                        </p>
                        <p>
                            > 腾讯文档近期已经提供具有隐私的收集功能了。┑(￣Д ￣)┍
                        </p>
                        <p>
                            > 如果你想使用这个功能，可以微信联系我。微信:double_babybaby；邮箱:lovemyprincess@vip.qq.com
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <iframe src="http://main.melodyl.xyz/static/remarks/notify.html" width="70%" height="300px" frameborder="0" scrolling="auto" style="margin-left:10%;margin-bottom:20%"></iframe>
    </div>
</template>

<script>
export default {
  name: "Login",
  data: function() {
    return {
      user: {
        userName: null,
        password: null
      },
      validateMsg: null
    };
  },
  methods: {
    login: function() {
      var self = this;
      if (self.user.userName && self.user.password) {
        self
          .$ajax({
            url: "/user/authorize",
            method: "post",
            data: self.user,
            headers: { "Content-type": "application/json" },
            timeout: 10000
          })
          .then(function(response) {
            if (response.status === self.$store.getResponseCode("NO_CONTENT")) {
              var token = response.request.getResponseHeader(
                self.$store.TOKEN_ATTRIBUTE_NAME
              );
              sessionStorage.setItem("Token", token)
              self.$store.tokenState.commit("setToken", token);
              self.$router.push({
                path: "/service"
              });
            } else {
              alert(self.$store.getResponseMsgFromCode(response.status));
            }
          })
          .catch(function(error) {
            alert(self.$store.getResponseMsgFromCode(error.response.status));
          })
      }

      if (!self.user.userName || !self.user.password) {
        self.validateMsg = "请输入用户名密码！";
      }
    }
  }
};
</script>

<style type="text/css" scoped>
body {
  background-color: #eee;
}

.container-h1 {
  margin: 5% auto 10px auto;
  text-align: center;
}

.container-form {
  background-color: #ffffff;
  border: 1px solid #dedede;
  border-radius: 8px;
  padding: 20px;
  margin: 0 auto;
  max-width: 500px;
}

.control-wrapper {
  position: relative;
  padding-left: 30px;
}

.control-wrapper label.control-label {
  position: absolute;
  left: 4px;
  top: 10px;
  margin-bottom: 10px;
  padding-top: 0px;
}

.container-bottom {
  margin-top: 20px;
}

.modal-title {
  float: left;
}

.modal-body {
  text-align: left;
}
</style>
