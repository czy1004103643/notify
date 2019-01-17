<template>
    <div id="body-container">
        <nav class="navbar navbar-inverse title-header">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <span class="navbar-brand brand-img-span">
                        <img src="http://main.melodyl.xyz/static/image/batman.png" height="48"/>
                    </span>
                </div>
                <div class="collapse navbar-collapse" id="navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a>
                                <span class="glyphicon glyphicon-user"></span>
                                <span>&nbsp;&nbsp;{{ userName }}</span>
                            </a>
                        </li>
                        <li>
                            <a v-on:click="exit">
                                <span class="glyphicon glyphicon-log-in"></span>
                                &nbsp;&nbsp;退出
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <nav class="navbar navbar-default main-header">
            <div class="container-fluid">
                <div class="navbar-header main-header-son">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-2" aria-expanded="true">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </div>
                <div class="navbar-collapse collapse" id="navbar-collapse-2" aria-expanded="true">
                    <ul class="nav nav-justified menu">
                        <li>
                            <a v-on:click='currentTab = tabs[0]'>
                                查看表格
                            </a>
                        </li>
                        <li>
                            <a v-on:click='currentTab = tabs[1]'>
                                简单表格
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <component v-bind:is="currentTab" class="container-fluid main-content" v-on:indexChangedEvent="changeTabIndex"></component>
    </div>
</template>

<script>
import SimpleCreate from "./SimpleCreate.vue";
import Select from "./Select.vue";

export default {
  components: {
    SimpleCreate,
    Select
  },
  data: function() {
    return {
      currentTab: Select,
      tabs: [Select, SimpleCreate],
      userName: null
    };
  },
  methods: {
    changeTabIndex: function(index) {
      this.currentTab = this.tabs[index];
    },
    exit: function() {
      this.$router.push("/exit");
    },
    getUserName: function() {
      var self = this;
      self
        .$ajax({
          url: "/user/name",
          method: "get",
          headers: {
            "Token": self.$store.tokenState.getters.token
          },
          timeout: 10000
        })
        .then(function(response) {
          if (response.status === self.$store.getResponseCode("OK")) {
              self.userName = response.data.name
          }
        })
        .catch(function(error) {
        })
    }
  },
  mounted: function() {
    this.getUserName();
  }
};
</script>

<style type="text/css" scoped>
.navbar {
  margin-bottom: 0;
}

.title-header {
  margin-bottom: 0;
  border-radius: 0;
}

.title-header .container-fluid {
  padding-right: 10%;
  padding-left: 10%;
}

div.main-header-son {
  width: 100%;
  height: 100%;
}

.menu {
  padding-left: 30%;
  padding-right: 30%;
  margin-top: 10px;
  margin-bottom: 10px;
}

.main-content {
  margin-right: 15%;
  border: 1px #eee solid;
}

.brand-img-span {
    padding: 0px;
}
</style>
