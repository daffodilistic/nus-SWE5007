<script>
 
</script>

<template>
  <div class="qualify">
    <p class="selected-users">{{selectedUsers}}</p>
 <table>
   <thead>
     <tr>
       <th>
         <input type="checkbox" v-model="isAllSelected" @click="selectAll">
       </th>
       <th>ID</th>
       <th>Name</th>
       <th>Email</th>
       <th>Age</th>
     </tr>
   </thead>
   <tbody>
     <tr v-for="user in users" :key="user.id">
       <td>
         <input type="checkbox" v-model="selectedUsers" :value="user.name">
       </td>
       <td>{{ user.id }}</td>
       <td>{{ user.name }}</td>
       <td>{{ user.email }}</td>
       <td>{{ user.age }}</td>
     </tr>
   </tbody>
 </table>
 <vs-pagination :total-pages="20" @change="changePage"></vs-pagination>
</div>
</template>

<style scoped>
table {
  border-collapse: collapse;
  width: 100%;
}

th,
td {
  text-align: left;
  padding: 8px;
}

thead th {
  background-color: #f2f2f2;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

input[type="checkbox"] {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  display: inline-block;
  position: relative;
  width: 22px;
  height: 22px;
  background-color: #fff;
  border: 2px solid #d9d9d9;
  border-radius: 5px;
  margin-right: 8px;
  cursor: pointer;
}

input[type="checkbox"]:checked {
  background-color: #2196F3;
  border-color: #2196F3;
}

input[type="checkbox"]:after {
  content: "";
  position: absolute;
  display: none;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 12px;
  height: 12px;
  background-color: #fff;
  border-radius: 3px;
  box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.2);
}

input[type="checkbox"]:checked:after {
  display: block;
}

input[type="checkbox"]:hover {
  background-color: #f2f2f2;
}

input[type="checkbox"]:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(33, 150, 243, 0.2);
}

table th:first-child input[type="checkbox"],
table td:first-child input[type="checkbox"] {
  margin-right: 4px;
}

table th:first-child input[type="checkbox"]:hover,
table td:first-child input[type="checkbox"]:hover {
  background-color: transparent;
}

.selected-users {
  font-weight: bold;
  margin-bottom: 20px;
}
</style>

<script>

export default {

   data() {
     return {
       users: [
         { id: 1, name: 'John', email: 'john@example.com', age: 25 },
         { id: 2, name: 'Jane', email: 'jane@example.com', age: 30 },
         { id: 3, name: 'Bob', email: 'bob@example.com', age: 35 },
         { id: 4, name: 'Alice', email: 'alice@example.com', age: 40 }
       ],
       selectedUsers: [],
       isAllSelected: false
     }
   },
   methods: {
     selectAll() {
       this.selectedUsers = [];

       if (!this.isAllSelected) {
         this.users.forEach(user => {
           this.selectedUsers.push(user.name);
         });
       }
       this.isAllSelected = !this.isAllSelected;
     }
   },
   watch: {
     selectedUsers(value) {
       if (value.length === 0) {
         this.isAllSelected = false;
       } else if (value.length === this.users.length) {
         this.isAllSelected = true;
       } else {
         this.isAllSelected = false;
       }
     }
   }}
 ;
</script>