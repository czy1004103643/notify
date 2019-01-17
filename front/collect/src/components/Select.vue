<template>
  <div class="col-sm-12 main-content">
        <div class="table-list panel panel-default col-xs-12 col-sm-2">
            <div class="panel-heading text-center"><b>列&nbsp;表</b></div>
            <div class="list-group panel-body">
                <a class="list-group-item  text-center" v-for="(value, key) in tableContextList" v-bind:key="key" v-on:click="changTableContextIndex(key)">
                  {{ value.tableName }}
                </a>
            </div>
            <div class="panel-footer">
                <a class="mt-export list-group-item  text-center">
                  假装有分页
                </a>
            </div>
        </div>
        <div class="col-xs-12 col-sm-10">
            <div class="page-header">
                <h3>
                    {{ index>=0 ? tableContextList[index].tableName : "表名&nbsp;" }} 
                    <small>{{ index>=0 ? tableContextList[index].description : "表格描述" }}</small>
                </h3>
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-6 control-label">访问地址</label>
                    <div class="col-sm-6" style="margin-top:10px">
                        <a class="form-control-static" v-bind:href="accessPath" target="_Blank">
                            {{ index>=0 ? "点击访问" : "" }}
                        </a>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-6 control-label">表格创建时间</label>
                    <div class="col-sm-6">
                        <p class="form-control-static">
                            {{ index>=0 ? tableContextList[index].createTime : timeNow }}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-6 control-label">口&nbsp;&nbsp;&nbsp;&nbsp;令</label>
                    <div class="col-sm-6">
                        <p class="form-control-static text-success">
                            {{ index>=0 ? tableContextList[index].command : "HERO" }}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-6 control-label">有效期</label>
                    <div class="col-sm-6">
                        <p class="form-control-static text-danger">1个月</p>
                    </div>
                </div>
            </div>            
            <hr/>
            <rice-loader :loading="loadingConfig.loading" :color="loadingConfig.color" :size="loadingConfig.size"></rice-loader>
            <hr/>
            <div class="table-responsive" v-if="index>=0">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th class="text-center">
                                id
                            </th>
                            <th class="text-center" v-for="(value, key) in tableContextList[index].columns" v-bind:key="key">
                                {{ value.name }}
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        
                            <template v-for="(item, index) in tableContent">
                                <tr v-bind:key="index">
                                    <td class="text-center">
                                        {{ item["id"] }}
                                    </td>
                                    <td class="text-center" v-for="(value, key) in columnList" v-bind:key="key">
                                        {{ item[value.name] }}
                                    </td>
                                </tr>
                            </template>
                    </tbody>
                </table>
            </div>
            <div class="" style="float:none" v-else>
                [ 曾经，这里有一份表格 ]
                <br/>
                <hr/>
            </div>
            <ul class="pagination pull-right">
                <li>
                    <a>&laquo;</a>
                </li>
                <li>
                    <a>分页没做</a>
                </li>
                <li>
                    <a>&raquo;</a>
                </li>
            </ul>
        </div>
  </div>
</template>

<script>
import SkewLoader from "vue-spinner/src/SkewLoader.vue";

export default {
  name: "Select",
  components: {
    "rice-loader": SkewLoader
  },
  data: function() {
    return {
      loadingConfig: {
        size: "15px",
        color: "#5a5a5a",
        loading: true
      },
      index: -1,
      ticket: {},
      tableContextList: [
        {
          id: "",
          tableName: "",
          description: "",
          createTime: "",
          command: "",
          columns: [
            {
              type: "",
              name: "",
              length: ""
            }
          ]
        }
      ],
      tableContent: []
    };
  },
  computed: {
    timeNow: function() {
      var date = new Date();
      var seperator1 = "-";
      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      var strDate = date.getDate();
      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
      }
      var currentdate = year + seperator1 + month + seperator1 + strDate;
      return currentdate;
    },
    columnList: function() {
      return this.tableContextList[this.index].columns;
    },
    accessPath: function() {
        return this.index>=0?this.$store.COLLECT_URL + '/' + this.ticket[this.tableContextList[this.index].tableName]:'';
    }
  },
  methods: {
    getTableContext: function() {
      var self = this;
      this.$ajax({
        url: "/table/context",
        method: "get",
        headers: {
          Token: self.$store.tokenState.getters.token
        },
        timeout: 10000
      })
        .then(function(response) {
          if (response.status === self.$store.getResponseCode("OK")) {
            self.ticket = response.data.ticket;
            self.tableContextList = response.data.table;
            self.changTableContextIndex(0);
          } else {
            alert(self.$store.getResponseMsgFromCode(response.status));
          }
        })
        .catch(function(error) {
          alert(self.$store.getResponseMsgFromCode(error.response.status));
        });
    },
    changTableContextIndex: function(index) {
      this.index = index;
      this.getTableContent(index);
    },
    getTableContent: function(index) {
      var self = this;
      this.$ajax({
        url: "/table/data/" + self.tableContextList[index].tableName,
        method: "get",
        headers: {
          Token: self.$store.tokenState.getters.token
        },
        timeout: 10000
      })
        .then(function(response) {
          if (response.status === self.$store.getResponseCode("OK")) {
            self.tableContent = response.data;
          } else {
            self.tableContent = [];
          }
        })
        .catch(function(error) {
          self.tableContent = [];
          console.log(
            self.$store.getResponseMsgFromCode(error.response.status)
          );
        });
    }
  },
  created: function() {
    this.getTableContext();
  }
};
</script>

<style scoped>
.page-header h3 {
  text-align: center;
}

.main-content {
  border-color: #ffffff;
  margin-top: 40px;
  padding-right: 15%;
  padding-left: 10%;
}

.main-content .panel-heading {
  font-size: 18px;
}

.list-group-item {
  border: 0px;
}

.table-list {
  padding-left: 0px;
  padding-right: 0px;
}

.table-list a {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.mt-export {
  background-color: #f5f5f5;
}

.form-horizontal .control-label {
  padding-bottom: 7px;
}

</style>
