<template>
  <div id="app">
    {{ items.data.data }}
    <b-table :items="items.data.data" :fields="fields">
      <template #cell(name)="data">
          <b-form-input v-if="items.data[data.index].isEdit && selectedCell === 'firstName'" type="text" v-model="items[data.index].firstName"></b-form-input>
          <span v-else @click="editCellHandler(data, 'firstName')">{{data.value}}</span>
      </template>
    </b-table>
     <pre>
    </pre>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "App",
  components: {},
  data() {
    return {
      fields: [
        { key: "firstName", label: "First Name"},
        { key: 'edit', label: ''}
      ],
       items: [
        ],
        selectedCell: null
    };
  },
  async mounted() {
    this.items = await axios.get(`http://localhost:8081/userinfo/users/`);
  },
  methods: {
      editCellHandler(data, name) {
        this.items = this.items.map(item => ({...item, isEdit: false}));
        this.items[data.index].isEdit = true;
        this.selectedCell = name
      }
    }
  }
</script>
<style scoped>
#app {
  text-align: center;
  margin: 60px;
}
thead, tbody, tfoot, tr, td, th {
  text-align: left;
  width: 100px;
  vertical-align: middle;
}
pre {
  text-align: left;
  color: #d63384;
}
</style>