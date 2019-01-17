<template>
    <div class="form-horizontal main-content">
        <div class="page-header">
            <h3>
                {{ submitted === false && loadingSuccess ? tableInfo.tableName : '初次相遇，以后多多指教' }}
                <small>
                    {{ submitted === false && loadingSuccess ? tableInfo.description : '' }}
                </small>
            </h3>
        </div>
        <div class="form-group" v-show="!submitted && loadingSuccess">
            <label class="col-sm-2 control-label">command</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="tableData.command" placeholder="请输入验证命令">
            </div>
        </div>
        <div class="form-group" v-for="(value, index) in tableInfo.columns" v-bind:key="index">
            <label class="col-sm-2 control-label" v-show="!submitted && loadingSuccess">{{ value.name }}</label>
            <div class="col-sm-10" v-show="!submitted && loadingSuccess">
                <input type="text" class="form-control" v-bind:placeholder="returnInputPrompt(value)" v-model="tableData.data[value.name]">
            </div>
        </div>
        <div class="form-group" v-show="!submitted && loadingSuccess">
            <div class="col-sm-offset-2 col-sm-10">
                <button class="btn btn-default btn-block btn-primary" v-on:click="submitData">提交数据</button>
            </div>
        </div>
        <div class="form-group" v-show="submitted">
            <div class="text-center">
                提交成功，刷新可以继续提交。
            </div>
        </div>
    </div>
</template>

<script>
export default {
  data: function() {
    return {
      ticket: this.$route.params.ticket,
      tableInfo: {
        columns: [
          {
            length: null,
            name: null,
            type: null
          }
        ],
        tableName: null,
        description: null
      },
      submitted: false,
      loadingSuccess: false,
      tableData: {
        data: {},
        command: null
      }
    };
  },
  methods: {
    loading: function() {
      var self = this;
      this.$ajax({
        url: "/table/context/" + self.ticket,
        method: "get",
        headers: { "content-type": "application/json" },
        timeout: 10000
      })
        .then(function(response) {
          if (response.status === self.$store.getResponseCode("OK")) {
            self.tableInfo = response.data;
            self.loadingSuccess = true;
          } else {
            alert(self.$store.getResponseMsgFromCode(response.status));
          }
        })
        .catch(function(error) {
          alert(self.$store.getResponseMsgFromCode(error.response.status));
        });
    },
    returnInputPrompt: function(value) {
      return "长度至多为 " + value.length / 5;
    },
    submitData: function() {
      var self = this;
      this.$ajax({
        url: "/table/data/" + self.ticket,
        method: "post",
        data: self.tableData,
        headers: { "content-type": "application/json" },
        timeout: 10000
      })
        .then(function(response) {
          if (response.status === self.$store.getResponseCode("CREATED")) {
            self.submitted = true;
          } else {
            alert(self.$store.getResponseMsgFromCode(response.status));
          }
        })
        .catch(function(error) {
          alert(self.$store.getResponseMsgFromCode(error.response.status));
        });
    }
  },
  created: function() {
    this.loading();
  }
};
</script>

<style type="text/css" scoped>
.main-content {
  margin: 10% 10% 10% 10%;
}
</style>