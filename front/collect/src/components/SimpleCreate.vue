<template>
  <div class="container-fluid main-content">
    <div class="page-header">
        <h3>表格结构</h3>
    </div>
    <div class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-2 control-label">表&nbsp;名</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" placeholder="仅限英文和数字" v-model='tableDescription.tableName'>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">属性名</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" placeholder="英文字母组成，逗号分隔(不可为id)" v-model='tableDescription.columnsStr'>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">口&nbsp;令</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" placeholder="验证身份的口令" v-model='tableDescription.command'>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">描&nbsp;述</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" placeholder="表格描述，可选，长度50个字" v-model="tableDescription.description">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <!-- <button id="insertbtn" class="btn btn-default btn-block btn-primary" data-toggle="modal" data-target="#myModal">添&nbsp;加</button> -->
                <button id="insertbtn" class="btn btn-default btn-block btn-primary" data-toggle="modal" v-on:click="showModal">添&nbsp;加</button>
            </div>
        </div>
    </div>

    <!-- model  -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="myModalLabel">说明</h4>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 text-center">表&nbsp;名</label>
                            <p class="col-sm-8 text-warning">{{ tableDescription.tableName }}</p>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 text-center">表的属性列表</label>
                            <p class="col-sm-8 text-warning">{{ columnsList }}</p>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 text-center">表的验证口令</label>
                            <p class="col-sm-8 text-warning">{{ tableDescription.command }}</p>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 text-center">表格描述</label>
                            <p class="col-sm-8 text-warning">{{ tableDescription.description }}</p>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 text-center">表的创建时间</label>
                            <p class="col-sm-8 text-warning">{{ tableDescription.createTime }}</p>
                        </div>
                        <div class="form-group" v-if="errorMsg">
                            <label class="col-sm-4 text-center text-danger">错误信息</label>
                            <p class="col-sm-8 text-danger">{{errorMsg}}</p>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <beat-loader :loading="loadingConfig.loading" :color="loadingConfig.color" :size="loadingConfig.size"></beat-loader>
                    <button v-show="!loadingConfig.loading" type="button" class="btn btn-default" v-on:click="closeModal">关闭页面</button>
                    <button v-show="!loadingConfig.loading" type="button" class="btn btn-primary" v-on:click="registForm">提交更改</button>
                </div>
            </div>
        </div>
    </div>
  </div>
</template>

<script>
import BeatLoader from "vue-spinner/src/BeatLoader.vue";

export default {
  name: "SimpleCreate",
  components: {
    "beat-loader": BeatLoader
  },
  data: function() {
    return {
      loadingConfig: {
        size: "20px",
        color: "#5a5a5a",
        loading: false
      },
      tableDescription: {
        tableName: null,
        description: null,
        columnsStr: null,
        command: null,
        createTime: this.formatedDate(new Date())
      },
      ticket: null,
      errorMsg: null
    };
  },
  computed: {
    columnsList: function() {
      return (this.tableDescription.columnsStr || "").replace(",", " + ");
    },
    accessUrlForClient: function() {
      return this.ticket ? this.$store.COLLECT_URL + "/" + this.ticket : "";
    }
  },
  methods: {
    registForm: function() {
      var self = this;
      if (!self.errorMsg) {
        this.loadingConfig.loading = !this.loadingConfig.loading;
        this.$ajax({
          url: "/table/v1",
          method: "post",
          data: self.tableDescription,
          headers: {
            Token: self.$store.tokenState.getters.token
          },
          timeout: 10000
        })
          .then(function(response) {
            if (response.status === self.$store.getResponseCode("CREATED")) {
              self.ticket = response.data.ticket;
              self.changeNavgationIndex(0);
              alert("添加成功！访问路径为：" + self.accessUrlForClient);
            } else if(response.status === self.$store.getResponseCode("OK")){
              self.changeNavgationIndex(0);
              alert("欢迎，客人。")
            } else {
              alert(self.$store.getResponseMsgFromCode(response.status));
              self.changeNavgationIndex(1);
              self.changeLoadingStatus();
            }
            self.closeModal()
          })
          .catch(function(error) {
            self.changeLoadingStatus();
            alert(self.$store.getResponseMsgFromCode(error.response.status));
          });
      }
    },
    changeNavgationIndex: function(index) {
      this.$emit("indexChangedEvent", index);
    },
    changeLoadingStatus: function() {
      this.loadingConfig.loading = !this.loadingConfig.loading;
    },
    formatedDate: function(today) {
      return (
        today.getFullYear() +
        "-" +
        (today.getMonth() + 1) +
        "-" +
        today.getDate() +
        " " +
        today.getHours() +
        ":" +
        today.getMinutes() +
        ":" +
        today.getSeconds()
      );
    },
    showModal: function() {
      if (
        !this.tableDescription.tableName ||
        this.tableDescription.tableName.length > 20
      ) {
        this.errorMsg = "表名不允许为空，且长度不超过20";
      } else if (
        !this.tableDescription.columnsStr ||
        !this.tableDescription.columnsStr.split(" ").join("").length
      ) {
        this.errorMsg = "属性不允许为空";
      }else if (!this.tableDescription.command) {
        this.errorMsg = "口令不允许为空";
      }
      $("#myModal").modal("show");
    },
    closeModal: function() {
      this.errorMsg = null;
      $("#myModal").modal("hide");
    }
  }
};
</script>

<style scoped>
.form-group .control-label {
  float: left;
}

.main-content {
  border-color: #ffffff;
  margin-top: 40px;
  margin-right: 15%;
  margin-left: 10%;
  border: 1px #eee solid;
}

.main-content .page-header h3 {
  text-align: left;
}

.modal-title {
  float: left;
}

.modal-body {
  text-align: left;
}

.modal-body .form-horizontal .form-group label {
  margin: 0 0 2px 0;
  padding: 0 0 0 10px;
}

.modal-body .form-horizontal .form-group p {
  margin: 0 0 2px 0;
  padding: 0 0 0 10px;
}
</style>
