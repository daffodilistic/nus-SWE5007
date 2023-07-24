<script>
</script>

<template>
  
  <div class="upload"><form>
  <div class="form-row">
  <label for="upload-type">Upload Type</label>
    <select name="upload-type" id="upload-type">
      <option value="" disabled selected>Select an option</option>
      <option value="GM">Game Manual</option>
      <option value="TT">Time Table</option>
    </select>
</div>
<div class="form-row">
  <label for="game-type">Game Type</label>
    <select name="game-type" id="game-type">
      <option value="" disabled selected>Select an option</option>
      <option value="GA">Grand Arena</option>
      <option value="TC">Technical Competition</option>
      <option value="ID">Innovation Design Challenge</option>
    </select>
</div>

</form></div>
</template>

<style scoped>
#choice option { color: black; }
.empty { color: gray; }

label {
    display: block;
    margin-bottom: 10px;
    font-weight: bold;
    align-content: left;
}
.form-row {
    margin-bottom: 20px;
    text-align: left;
}
.form-row label {
    display: block;
    margin-bottom: 5px;

}
.form-row input,
.form-row select {
    width: 50%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
    font-size: 14px;
}
</style>

<script>


</script>