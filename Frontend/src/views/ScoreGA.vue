<template>
      <div>
        <!-- show GA history Modal START-->
        <b-modal
          v-model="showGAHistoryModal"
          id="showGAHistoryModal"
          modal-class="modal-lg"
          title="Competition Score"
          hide-footer
        >
            <!-- List of users to be displayed inside the modal -->
            <table class="modal-table">
                  <thead>
                    <tr>
                      <th>Qualification Stage</th>
                    </tr>
                    <tr>
                      <th>&nbsp;</th>
                    </tr>
                    <tr>
                      <th class="center-align">Team Name</th>
                      <th class="center-align">Match Played</th>
                      <th class="center-align">Total Credits</th>
                      <th class="center-align">Points</th>
                    </tr>
                  </thead>
                  <tbody>
                    <template v-if="gameTeamList.length === 0">
                      <tr>
                        <td colspan="10" class="centered-cell"><br><br>No records available.</td>
                      </tr>
                      <tr><br><br></tr>
                    </template>
                    <template v-else>
                      <tr v-for="(gameTeam, gameTeamIndex) in gameTeamList" :key="gameTeam.id">
                        <td class="center-align">{{ gameTeam.teamName }}</td>
                        <td class="center-align">{{ gameTeam.qualificationRoundNumMatchesPlayed }}</td>
                        <td class="center-align">{{ gameTeam.qualificationRoundScore }}</td>
                        <td class="center-align">{{ gameTeam.qualificationRoundPoint }}</td>
                      </tr>
                    </template>
                  </tbody>
                </table>

        </b-modal>
        <!-- show GA history Modal END-->
            <!-- Start of Accordion -->
    <b-card no-body class="mb-2">
      <b-card-header header-tag="header" class="p-2" role="tab">
        <b-button
          block
          v-b-toggle.first-accordion
          variant="info"
          class="accordion-button"
        >
          Click to view Qualification Round
        </b-button>
      </b-card-header>
      <b-collapse id="first-accordion" accordion="my-accordion" role="tabpanel">
        <div v-if="groups && groups.length > 0">
        <table class="instruction-table">
          <tr><u>Instructions :</u></tr>
          <tr>1. Click on <b-icon icon="collection-play"></b-icon> to create matchups for all the teams in the group</tr>
          <tr>2. Click on <i :class="'fas fa-plus'" class="expand-icon"></i> to view team matchups <b-icon icon="arrow-right-circle-fill"></b-icon> Click on <b-icon icon="play-circle"></b-icon> to start the match</tr>
          <tr>3. After the match is completed, enter score for participating teams <b-icon icon="arrow-right-circle-fill"></b-icon> Click on <b-icon icon="save"></b-icon> to submit the score</tr>
          <tr>4. After scores are submitted for all the matchups, click on  <b-icon icon="star"></b-icon> to qualify the top 2 teams in the group</tr>
          <tr>5. Repeat steps 1 to 4 for all groups</tr>
        </table>

        <table class="main-table">
          <thead>
            <tr>
              <th></th>
               <th>S/No</th>
              <th>Group Name</th>
              <th>Qualification Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody v-for="(group, index) in paginatedTeams" :key="index">
            <tr :class="{'parent-row': true, 'active-row': activeRow === index}" @click="group.editing ? null : toggleRow(index)">
              <td>
                <i :class="activeRow === index ? 'fas fa-minus' : 'fas fa-plus'" class="expand-icon"></i>
              </td>
              <td>{{ startIndex + index }}</td>
              <td v-if="!group.editing">
                {{ group.id}}
              </td>
              <td v-else>
                <input type="text" v-model="group.editingGroupName" class="form-control editing-textbox" />
              </td>
              <td>
                 <div v-if="group.gameTeamResponses.filter(team => team.isQualifiedForElimination).length >= 2">
                  Completed
                </div>
                <div v-else>
                 In-Progress
                </div>
              </td>
              <td>
                <b-button @click="viewGAScore(group)" id="viewGAScore" variant="outline-primary" class="delete-button" v-b-tooltip.hover="'Click to view scores of all teams in the group'">
                  <b-icon icon="eye"></b-icon>
                </b-button>
              <b-button
                @click="createQualiGame(startIndex + index - 1)"
                variant="outline-primary"
                class="delete-button"
                v-b-tooltip.hover="'Click to create matchups for all teams in the group'"
                v-bind:disabled="group.gameTeamResponses.filter(team => team.isQualifiedForElimination).length > 1"
              >
                 <b-icon icon="collection-play"></b-icon>
                </b-button>
                <b-button @click="qualifyTeam(group)" variant="outline-primary" class="delete-button" v-b-tooltip.hover="'Click to qualify top 2 teams'"
                 v-bind:disabled="group.gameTeamResponses.filter(team => team.isQualifiedForElimination).length >= 2"
                >
                 <b-icon icon="star"></b-icon>
                 </b-button>
              </td>
            </tr>
            <!-- Child rows -->
            <tr v-if="activeRow === index" class="child-row">
              <td :colspan="10"> <!-- Use colspan to span all columns in the row -->
                <table class="team-table">
                  <thead>
                    <tr>
                      <th>Matchup</th>
                      <th class="longer-td">Team Name</th>
                      <th>Team Score</th>
                      <th></th>
                      <th>Team Score</th>
                      <th class="longer-td">Team Name</th>
                      <th>Game Status</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-if="groupedData.length === 0">
                      <td colspan="11"  style="color: red;">0 game created in the group. Click on <b-icon icon="collection-play"></b-icon> to create games!</td>
                    </tr>
                     <template v-for="(group, groupIndex) in groupedData">

                      <tr v-for="(item, rowIndex) in group" :key="`${groupIndex}`">
                        <td>{{ groupIndex+1}}</td>
                        <td class="host-td">
                         <span v-if="item.gameOutcome === 'win' && item.gameStatus==='done'"
                          >
                            <i class="fas fa-star gold-star"></i>
                          </span> <br>
                        {{ item.hostTeamName  }}</td>

                        <td v-if="item.gameStatus!=='done'">
                        <input type="number" v-model="item.enteredHostScore" :min="0" :disabled="item.gameStatus !== 'ongoing'" class="narrow-input">
                        </td>
                        <td v-else-if ="item.gameStatus==='done'" class="host-score-td">
                          {{ item.hostScore }}
                        </td>
                        <td>  <img src="../assets/Versus_icon.png" alt="Versus"></td>
                        <td v-if="item.gameStatus!=='done'">
                        <input type="number" v-model="item.enteredOppoScore" :min="0" :disabled="item.gameStatus !== 'ongoing'" class="narrow-input"></td>
                        <td v-else-if ="item.gameStatus==='done'" class="oppo-score-td">{{ item.oppoScore }}</td>

                        <td class="oppo-td"><span v-if="item.gameOutcome === 'lose' && item.gameStatus==='done'"
                          >
                            <i class="fas fa-star gold-star"></i><br>
                          </span>{{ item.oppoTeamName  }} </td>

                        <td>{{ gameStatusTextMap[item.gameStatus] }}</td>
                        <td v-if="gameStatusTextMap[item.gameStatus]==='Not Yet Started'">
                           <b-button @click="startGame(item.id, groupIndex)" variant="outline-primary" class="delete-button" v-b-tooltip.hover="'Click to start game for this matchup'">
                            <b-icon icon="play-circle"></b-icon>&nbsp;Start
                            </b-button>
                        </td>

                        <td
                          v-if="item.gameStatus == 'ongoing'">
                           <b-button @click="submitScore(item.hostTeamId,item.oppoTeamId,item, groupIndex,)" variant="outline-primary" class="delete-button" v-b-tooltip.hover="'Click to submit score for this matchup'">
                            <b-icon icon="save"></b-icon>&nbsp;Score
                            </b-button>
                        </td>
                        <td
                          v-if="item.gameStatus == 'done'"
                        >
                          &nbsp;
                        </td>

                      </tr>
                    </template>
                  </tbody>
                </table>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-else>
          <!-- Show a loading message or spinner while the data is being fetched -->
          <div class="loader-container">
            <i class="fas fa-spinner fa-spin"></i>
          </div>
        </div>
        </b-collapse>
    </b-card>
    <!-- End of Accordion -->

    <b-card no-body class="mb-2">
      <b-card-header header-tag="header" class="p-2" role="tab">
        <b-button
          block
          v-b-toggle.second-accordion
          variant="info"
          class="accordion-button"
          @click="loadElimination"
        >
          Click to view Elimination Round
        </b-button>
      </b-card-header>
      <b-collapse id="second-accordion" accordion="my-accordion" role="tabpanel">
         <b-button
      @click="startElimination()"
      variant="outline-primary"
      v-b-tooltip.hover="'Click to Kick Start Elimination Round!'"
      v-if="shouldShowStartButton"
       style="width: 100%; height: 80px;"
    >
            <b-icon icon="play-circle" style="font-size: 36px; line-height: 148px;"></b-icon>&nbsp;Start
            </b-button>
      <br>
      <div v-if="!shouldShowStartButton">
        <div v-if="groups && groups.length > 0">
          <div class="tournament-bracket">
            <div v-for="(stage, index) in filteredStages" :key="stage">

               <Round
                  :roundMatches="getRoundMatches(stage)"
                  :isSecondRound="index < filteredStages.length - 1"
                  :isLastRound="index === filteredStages.length - 1"
                />
            </div>
          </div>
        </div>
      <div v-else>
          <!-- Show a loading message or spinner while the data is being fetched -->
          <div class="loader-container">
            <i class="fas fa-spinner fa-spin"></i>
          </div>
        </div></div>
       </b-collapse>
    </b-card>

      </div>
</template>


<script>
import axios from "axios";
import { ageGroupOptions, competitionChoiceOptions} from "../dropdownOptions";
import { VIEW_GAME_GROUP_BASE_URL,GET_ALL_GAME_GROUP_BASE_URL,GET_ALL_GAME_TEAM_BASE_URL,CREATE_GAME_BASE_URL,GET_ALL_GAMES_BASE_URL,UPDATE_GAME_ONGOING_STATUS_BASE_URL,UPDATE_GAME_SCORE_BASE_URL,CHECK_GAME_QUALIFICATION_STATUS_BASE_URL,QUALIFY_GAME_TEAM_BASE_URL} from '@/api';
import Swal from 'sweetalert2';
import Vue from 'vue'
import Round from './Round.vue';
import eventBus from '../utils/eventBus.js';
import { delay } from '../utils/utils.js';

export default {

  components: {
    Round,
  },
  head() {
    return {
      link: [
        {
          rel: 'stylesheet',
          href: 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css',
          integrity: 'sha512-...',
          crossorigin: 'anonymous',
          referrerpolicy: 'no-referrer',
        },
      ],
    }
  },

  data() {
    return {
      currentPageModal: 1,
      itemsPerPageModal: 10,
      currentGroupId: "",
      currentGroupName: "",
      selectedTeams: [],
      searchQuery: '',
      searchQueryModal: '',
      groups:[],
      gameTeamResponses:[],
      activeRow: null,
      itemsPerPage: 10, // Number of groups per page
      currentPage: 1, // Current page
      editingStatus: null, // Control the visibility of the modal
      showAddTeamModal: false,
      teamList: [],
      groupMembers:[],
      objectsWithIdAndTeamArray : [],
      gameTeamList: [],
      showGAHistoryModal: false,
      allGameData: [], //all game objects
      filteredStages: [], //distinc stages of elimination rd
      rounds:[],
      filteredGames:[]
    };
  },
  created() {
    // Listen for the 'start-game' event
    eventBus.$on('start-game', this.startGame);
    eventBus.$on('load-group-data', this.loadGroup);
    eventBus.$on('load-elimination-data', this.loadElimination);
    eventBus.$on('submit-score', this.submitScore);
    eventBus.$on('check-advance', this.checkAdvanceRound);
  },
  computed: {

     shouldShowStartButton() {
      // Calculate whether to show the button based on your condition
      const elim01Count = this.allGameData.filter(item => item.stage === "Elim-01").length;
      return elim01Count <= 3;
    },

    gameStatusTextMap() {
    // Define a mapping of age group values to their corresponding text
    const gameStatusMap = {
      'pending': 'Not Yet Started',
      'ongoing': 'In-Progress',
      'done': 'Completed',
      // Add more entries as needed for other age groups
    };
    return gameStatusMap;
  },

     groupedData() {
      const grouped = {};
      this.objectsWithIdAndTeamArray.forEach((item) => {
        const id = item.id;
        if (!grouped[id]) {
          grouped[id] = [];
        }
        grouped[id].push(item);
      });

      return Object.values(grouped);
    },
    totalRecordsModal() {
      return this.filteredModalTeamList.length;

    },
    filteredGroups() {
    // If the groups data is not available yet, return an empty array
    if (!this.groups || this.groups.length === 0) {
      return [];
    }

    // If the search query is empty, show all groups
    if (this.searchQuery.trim() === '') {
      return this.groups;
    }

    // Otherwise, filter groups based on the search query
    const query = this.searchQuery.trim().toLowerCase();
    return this.groups.filter((group) => group.groupName.toLowerCase().includes(query));
  },
    filteredCompetitionChoices() {
      return competitionChoiceOptions;
    },
    filteredAgeGroupChoices() {
      return ageGroupOptions;
    },
    filteredStatusChoices() {
      return QualificationOptions;
    },
    totalPages() {
      return Math.ceil(this.filteredGroups.length / this.itemsPerPage);
    },
    paginatedTeams() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.filteredGroups.slice(startIndex, endIndex);
    },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage + 1;
    },
    endIndex() {
      const endIndex = this.currentPage * this.itemsPerPage;
      return endIndex > this.totalRecords ? this.totalRecords : endIndex;
    },
    totalRecords() {
      return this.filteredGroups.length;
    },
    filteredModalTeamList() {
      if (!this.teamList) {
        return [];
      }

    // Apply search query filter
    const query = this.searchQueryModal.trim().toLowerCase();
    if (!query) {
      return this.teamList; // Return the entire team list if the query is empty
    }

    return this.teamList.filter((team) => {
      // Check if team.teamName and team.lastName are defined before using toLowerCase()
      const teamName = team.teamName ? team.teamName.toLowerCase() : '';
      const lastName = team.lastName ? team.lastName.toLowerCase() : '';

      return teamName.includes(query) || lastName.includes(query);
    });
  },
  paginatedModalTeamList() {
    const startIndex = (this.currentPageModal - 1) * this.itemsPerPageModal;
    const endIndex = startIndex + this.itemsPerPageModal;
    return this.filteredModalTeamList.slice(startIndex, endIndex);
  },
  totalPagesModal() {
    return Math.ceil(this.filteredModalTeamList.length / this.itemsPerPageModal);
  },
  ageGroupTextMap() {
    // Define a mapping of age group values to their corresponding text
    const ageGroupMap = {
      'OC': 'Open Category: 8-15 years old',
      'JC': 'Junior Category: 8-12 years old',
      // Add more entries as needed for other age groups
    };
    return ageGroupMap;
  },
  },
  async mounted() {
    this.fetchTeams();

    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${Vue.$keycloak.token}`
    };
    try {
      this.groupsData = await axios.get(`${GET_ALL_GAME_GROUP_BASE_URL}`, { headers });
      this.groups = this.groupsData.data.data;

    } catch (error) {
      // Handle any errors that might occur during the request
      console.error("Error fetching users:", error);
    }
  },
  methods: {
    getRoundMatches(stage) {
      return this.rounds.filter(round => round.stage === stage);
    },

    async submitScore(hostId,oppoId,groupObj,groupIndex) {

      const confirmation = await Swal.fire({
        title: 'Are you sure?',
        text: 'You won\'t be able to revert this!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, Submit Score!'
      });
      if (confirmation.isConfirmed) {
      let requestBody = {
          id: groupObj.id,
          gameTeamIdHost: hostId,
          gameTeamIdOppo: oppoId,
          gameScoreHost: groupObj.enteredHostScore,
          gameScoreOppo : groupObj.enteredOppoScore,
        };

      const headers = {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Vue.$keycloak.token}`
        };

      try {
      const response = await axios.put(`${UPDATE_GAME_SCORE_BASE_URL}`,requestBody, { headers });

      this.loadGroup();
      if(groupIndex!=='na'){
        this.toggleRow(groupIndex)
      }else{
        await delay(1000);
        this.loadElimination();
        await delay(1000);
        this.checkAdvanceRound();
      }
      }
      catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }}
  },

    async startGame(gameId,groupIndex) {
      console.log('startGame Called')

      const requestBody = {
        id:gameId
      };
      const headers = {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Vue.$keycloak.token}`
        };
      try {
       const response = await axios.put(`${UPDATE_GAME_ONGOING_STATUS_BASE_URL}`,requestBody, { headers });
       if(groupIndex!=='na'){
        this.loadGroup();
        this.activeRow = this.activeRow === groupIndex ? null : groupIndex;
        }
      }

      catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }
  },

   async qualifyTeam(group) {

      let counter = false;

      for (let i = 0; i < group.gameTeamResponses.length; i++) {

        if(group.gameTeamResponses[i].qualificationRoundNumMatchesPlayed < 3){
          counter = false
        }
      }

      if(counter){
        Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Please score all the teams.',
            });
      }
      else{
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      }
      const requestBody = {
          id: group.id,
      };
      try {
          // If the group has an ID, update the existing record using a PUT request
          const response = await axios.post(`${CHECK_GAME_QUALIFICATION_STATUS_BASE_URL}`, requestBody, { headers });

          if(response.data.data='Group ',group.id,' is ready for qualification'){

            const response = await axios.post(`${QUALIFY_GAME_TEAM_BASE_URL}`, requestBody, { headers });
             Swal.fire({
              title: 'Success!',
              text: 'Top 2 teams of the group are now qualified for elimination round !',
              icon: 'success'
            });
          }
          await delay(1000);
          this.loadGroup();
        }
        catch (error) {
        // Handle errors, if any
          console.error('Error saving group:', error);
          if (error.response) {
      // Access and read the response data
            const responseData = error.response.data;
            console.error('Response data from 400 error:', responseData);

            // Show a pop-up notification with the error message and response data using SweetAlert2
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'An error occurred while saving the group.',
              html: `${JSON.stringify(responseData.data, null, 2)}`,
            });
          } else {
            // Handle other types of errors (e.g., network errors)
            console.error('Network error or other type of error:', error.message);
          }
        }
      }
    },

    async createQualiGame(index) {

      const confirmation = await Swal.fire({
        title: 'Are you sure?',
        text: 'Matchups will be created for all teams!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, create matchups!'
      });

      if (confirmation.isConfirmed) {
      const group = this.filteredGroups[index];
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      const teams = group.gameTeamResponses; // Assuming your group object has a 'teams' property containing an array of teams
      const currentDate = new Date();
      const isoDateTime = currentDate.toISOString(); // Generates date-time in "YYYY-MM-DDTHH:mm:ss.sssZ" format

      // Now, format it to "YYYY-MM-DDTHH:mm:ssZ" format
      const formattedDateTime = isoDateTime.slice(0, 19) + "Z";
    // Loop through each team in the group
    for (let i = 0; i < teams.length; i++) {
      for (let j = i + 1; j < teams.length; j++) {
        // Create a matchup object with two teams
        const requestBody = {
          gameTeamIdHost: teams[i].id,
          gameTeamIdOppo: teams[j].id,
          datetime:formattedDateTime,
          stage:'Quali'
        };

        try {
          // If the group has an ID, update the existing record using a PUT request
          const response = await axios.post(`${CREATE_GAME_BASE_URL}`, requestBody, { headers });

           Swal.fire({
            title: 'Game Set!',
            text: 'Matchups for all teams are created successfully.',
            icon: 'success'
          });
        }
        catch (error) {
        // Handle errors, if any
          console.error('Error saving group:', error);
        }}
        }
      }
    },
    async startElimination() {
          let firstPlaceArray = [];
          let secondPlaceArray = [];
          //loop through all game group, to get its 1st and 2nd place teams.
         for (let index = 0; index < this.groups.length; index++) {
          const grp =  this.groups[index];
          for(let index = 0; index < grp.gameTeamResponses.length; index++){
            if(grp.gameTeamIdFirst === grp.gameTeamResponses[index].id && grp.gameTeamResponses[index].isQualifiedForElimination){
              const details = {
                firstTeamID: grp.gameTeamIdFirst,
                groupId:grp.id,
                teamName:grp.gameTeamResponses[index].teamName
              }
              firstPlaceArray.push(details);
           }
           if(grp.gameTeamIdSecond === grp.gameTeamResponses[index].id && grp.gameTeamResponses[index].isQualifiedForElimination){
            const details = {
              secondTeamID: grp.gameTeamIdSecond,
              groupId:grp.id,
              teamName:grp.gameTeamResponses[index].teamName
            }
            secondPlaceArray.push(details);
           }
          }
        }
         const headers = {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Vue.$keycloak.token}`
        };
        let i = firstPlaceArray.length - 1
         for (let index = 0; index < firstPlaceArray.length; index++) {
             try {
              const requestBody = {
                  gameTeamIdHost: firstPlaceArray[index].firstTeamID,
                  gameTeamIdOppo: secondPlaceArray[i].secondTeamID,
                  stage:'Elim-01'
              };

             const response = await axios.post(`${CREATE_GAME_BASE_URL}`,requestBody, { headers });
              Swal.fire({
              title: 'Success!',
              text: 'Elimination Round Started!',
              icon: 'success'
            });
            this.loadGroup();

            } catch (error) {
              console.error("Error fetching data:", error);
            }
            i--
         }

    },

    async loadGroup() {
      this.groupsData = [];
      this.groups = [];
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      try {
          this.groupsData = await axios.get(`${GET_ALL_GAME_GROUP_BASE_URL}`, { headers });
          this.groups = this.groupsData.data.data;

        } catch (error) {
          console.error("Error fetching data:", error);
        }
    },
    async checkAdvanceRound() {

        const roundNumbersArray = this.filteredStages.map(item => {
          return parseInt(item.split('-')[1], 10);
        });
        //check what is the current round
        const largestNumber = Math.max(...roundNumbersArray);

        //check if all games in this round is completed
        const doneGameArr = this.filteredGames.filter((game) => game.stage===`Elim-0${largestNumber}` && game.gameStatus === "done");
        const totalGameArr = this.filteredGames.filter((game) => game.stage===`Elim-0${largestNumber}`);
        console.log('doneGameArr',doneGameArr)
        //get all winners in the team.
        let winnerArray = [];
         for (let index = 0; index < doneGameArr.length; index++) {

             const winner = doneGameArr[index]

             if(winner.gameOutcome ==='win'){
                winnerArray.push(winner.gameTeamIdHost)
             }else{
                winnerArray.push(winner.gameTeamIdOppo)
             }
          }
          console.log('winnerArray',winnerArray)

        //check if completed, is the game for next round created
        if(totalGameArr.length===doneGameArr.length && totalGameArr.length > 1 ){

            const currentDate = new Date();
            const isoDateTime = currentDate.toISOString(); // Generates date-time in "YYYY-MM-DDTHH:mm:ss.sssZ" format
            const formattedDateTime = isoDateTime.slice(0, 19) + "Z";// Now, format it to "YYYY-MM-DDTHH:mm:ssZ" format

            let index1 =0;
            let index2 =1;
            console.log('loop entered')

             const headers = {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${Vue.$keycloak.token}`
            };
            for (let index = 0; index < totalGameArr.length/2; index++) {

              let requestBody = {
                gameTeamIdHost: winnerArray[index1],
                gameTeamIdOppo: winnerArray[index2],
                datetime: formattedDateTime,
                stage : `Elim-0${largestNumber+1}`,
              };

               try {
                const response = await axios.post(`${CREATE_GAME_BASE_URL}`, requestBody, { headers });

                Swal.fire({
                  title: 'Round '+ largestNumber+ ' Completed!',
                  text: 'Matchups for Round '+ (largestNumber + 1)+ ' Created Successfully !',
                  icon: 'success'
                });
              }
              catch (error) {
              // Handle errors, if any
                console.error('Error saving group:', error);
              }
              index1 +=2;
              index2 +=2;

              console.log('index1 - ', index1 ,' index2 - ',index2)
              console.log('requestBody ',requestBody)

            }this.loadElimination();
        }else{
            Swal.fire({
                  title: 'Round '+ largestNumber+ ' Completed!',
                  text: 'Elimination Round Completed Successfully !',
                  icon: 'success'
                });
        }
    },
     async loadElimination() {
      this.allGameData = [];
      this.rounds = [];
      this.filteredStages=[];
      this.filteredGames=[];

      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      try {
          const response = await axios.get(`${GET_ALL_GAMES_BASE_URL}`, { headers });
          this.allGameData = response.data.data
        } catch (error) {
          console.error("Error fetching data:", error);
        }

        const uniqueStages = new Set();

        // Loop through allGameData and add distinct stage values to the Set
        this.allGameData.forEach(item => {
          uniqueStages.add(item.stage);
        });

         const distinctStagesArray = Array.from(uniqueStages);

      // Filter the array to show only values beginning with "Elim"
        this.filteredStages = distinctStagesArray.filter((stage) =>
          stage.startsWith("Elim")
        );

        this.filteredGames = this.allGameData.filter((game) => game.stage.startsWith('Elim'));

        for (let index = 0; index < this.filteredStages.length; index++) {
          for (let index = 0; index < this.filteredGames.length; index++) {
            const elimGame = this.filteredGames[index];
            const roundNumber = parseInt(elimGame.stage.split('-')[1], 10);

             if (elimGame.stage === `Elim-0${roundNumber}`) {

              const gameTeamIdHost = this.teamList.find(team => team.id === elimGame.gameTeamIdHost);

              const gameTeamIdOppo = this.teamList.find(team => team.id === elimGame.gameTeamIdOppo);
              const idExists = this.rounds.some(round => round.id === elimGame.id);

              if (!idExists) {
                const objectWithIdAndOppoArray = {
                  id: elimGame.id,
                  oppoTeamId: elimGame.gameTeamIdOppo,
                  oppoScore: elimGame.gameScoreOppo,
                  hostTeamId: elimGame.gameTeamIdHost,
                  hostScore: elimGame.gameScoreHost,
                  gameStatus : elimGame.gameStatus,
                  gameOutcome : elimGame.gameOutcome,
                  hostTeamName : gameTeamIdHost.teamName,
                  oppoTeamName : gameTeamIdOppo.teamName,
                  stage : `Elim-0${roundNumber}`
                };

                this.rounds.push(objectWithIdAndOppoArray);

              }
             }
          }
        }
    },

    async fetchTeams() {
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
    let response = '';
    try {
        response = await axios.get(`${GET_ALL_GAME_TEAM_BASE_URL}`, { headers });
        this.teamList = response.data.data

      }catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    gotoPageModal(page) {
    if (page >= 1 && page <= this.totalPagesModal) {
      this.currentPageModal = page;
    }
  },

    async toggleRow(index) {
    this.objectsWithIdAndTeamArray = []
    if (this.activeRow === index) {
      this.activeRow = null; // Collapse the row if it's already expanded
    } else {
      this.activeRow = index;
      const group = this.filteredGroups[index];
      const headers = {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Vue.$keycloak.token}`
        };
      try {
        const response = await axios.get(`${GET_ALL_GAMES_BASE_URL}`, { headers });

        let groupFilteredObj = this.groups.filter(team => team.id === group.id);

        const gameTeams = groupFilteredObj[0].gameTeamResponses;


        const tempArray = [];
        for (let index = 0; index < gameTeams.length; index++) {
          const item = gameTeams[index];
          tempArray.push(item.id);
        }


        this.groupMembers = response.data.data;

        // Iterate through the data objects
        for (const item of this.groupMembers) {
          // Create an object with "id" and associated teamArray

          if(item.stage.indexOf('Elim')){
            const gameTeamIdHost = this.teamList.find(team => team.id === item.gameTeamIdHost);

            const gameTeamIdOppo = this.teamList.find(team => team.id === item.gameTeamIdOppo);

          if (tempArray.includes(item.gameTeamIdOppo)) {
            const objectWithIdAndOppoArray = {
              id: item.id,
              oppoTeamId: item.gameTeamIdOppo,
              oppoScore: item.gameScoreOppo,
              hostTeamId: item.gameTeamIdHost,
              hostScore: item.gameScoreHost,
              gameStatus : item.gameStatus,
              gameOutcome : item.gameOutcome,
              hostTeamName : gameTeamIdHost.teamName,
              oppoTeamName : gameTeamIdOppo.teamName
            };
            this.objectsWithIdAndTeamArray.push(objectWithIdAndOppoArray);
          }}
          /*
          if (tempArray.includes(item.gameTeamIdHost)) {
            const objectWithIdAndHostArray = {
              id: item.id,
              teamId: item.gameTeamIdHost,
              gameStatus : item.gameStatus,
              gameOutcome : item.gameOutcome,
              hostScore: item.gameScoreHost,
              teamName : gameTeamIdHost.teamName
            };
            this.objectsWithIdAndTeamArray.push(objectWithIdAndHostArray);
          }*/
        }
        const jsonArray = JSON.stringify(this.objectsWithIdAndTeamArray, null, 2);
      }catch (error) {
        // Handle errors, if any
        console.error('Error calling API:', error);
      }
    }
  },
      gotoPage(page) {
        if (page >= 1 && page <= this.totalPages) {
          this.currentPage = page;
        }
      },

    async viewGAScore(groupObj) {

      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Vue.$keycloak.token}`
      };
      try {
          this.groupsData = await axios.get(`${GET_ALL_GAME_GROUP_BASE_URL}`, { headers });
          this.groups = this.groupsData.data.data;
        } catch (error) {
          console.error("Error fetching data:", error);
        }
        let groupFilteredObj = this.groups.filter(team => team.id === groupObj.id);
        this.gameTeamList = groupFilteredObj[0].gameTeamResponses

        this.showGAHistoryModal = true; // Show the modal after fetching the users
    },
  },
};
</script>

<style scoped>
/* Main Table Styles */
.main-table {

  width: 100%;
  border-collapse: collapse;
  font-size: 16px;
  text-align: center;
  margin-bottom: 20px;
}

.main-table th,
.main-table td {
padding: 8px;
  text-align: center;
  background-color: #f6f6f6;
}
.main-table th {
  background-color: #d7e7f2;
  font-weight: bold;
}


/* Hover effect for clickable rows */
.main-table tbody tr:hover {
  background-color: #f2f2f2;
  cursor: pointer;
}

.team-table {
  width: 100%;
  border-collapse: collapse;
}

.team-table th{
  padding: 8px 16px;
  border-bottom: 1px solid #ccc;
  text-align: center;
  background-color: #bad4e6;
}

.team-table td {
  padding: 8px 16px;
  border-bottom: 1px solid #ccc;
  text-align: center;
  background-color: white;
}

.nested-row {
  display: table-row;
  text-align: center; /* Center the text content horizontally */
}

.nested-row td {
  border: none;
  padding: 0;
  text-align: center; /* Center the text content horizontally */
  vertical-align: middle; /* Center the content vertically within the cell */
}

/* Hover effect for clickable rows */
.main-table tbody tr:hover {
  background-color: #f2f2f2;
  cursor: pointer;
}

.active-row {
  background-color: rgb(218, 234, 253)/* Light blue for active parent row */
}

.child-row {
  background-color: rgb(244, 249, 255);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px; /* Add some top margin for spacing */
}

.page-button {
  padding: 8px 16px;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin: 0 5px;
  background-color: transparent;
}

.search-container {
  display: flex;
  justify-content: flex-end; /* Aligns the search box to the right */
  margin-bottom: 20px;
  margin-right:65px;
}

/* Search Box Styles */
.search-box {
  padding: 8px 16px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  min-width: 200px; /* Adjust the width as needed */
}

.page-button .icon {
  background-color: transparent;
  border: none;
  padding: 0; /* Remove padding to make the icons fit better */
  font-size: 16px;
}

.parent-row .expand-icon {
  margin-right: 10px; /* Add some spacing between the icon and text */
  font-size: 16px;
  color: rgb(65, 127, 202); /* Set the color to blue (#3498db) */
}

.main-table th:first-child,
.main-table td:first-child {
  /* Set a fixed width for the new column */
  width: 30px;
}
.parent-row .expand-icon {
  /* Adjust the icon's size, margin, and color as needed */
  font-size: 16px;
  margin-right: 10px;
  color: rgb(65, 127, 202);
}

.delete-button {
  margin-left: 5px;
}

/* Styles for the text box in editing mode */
input.form-control.editing-textbox {
  font-size: 14px;
  min-width: 50px;
}

.modal-table {
  width: 100%; /* Set the table width to take full width of the modal */
  /* Optionally, you can set a max-width for the table if needed */
  /* max-width: 800px; */
  text-align: center;
}

.loader-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px; /* Adjust the height as needed */
}

.fa-spinner {
  font-size: 32px; /* Adjust the size as needed */
}

.modal-title-center {
  text-align: center;
  font-size: 30px; /* Adjust the font size as needed */
  margin-bottom: 10px; /* Add some margin at the bottom */
}

.editing-dropdown {
  font-size: 14px; /* Adjust the font size as needed */
  min-width: 200px; /* Set the minimum width of the dropdown */
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 8px 16px;
  background-color: #fff;
  color: #555;
}

/* Optionally, add styles for the dropdown arrow icon */
.editing-dropdown .dropdown-toggle::after {
  font-family: 'Font Awesome'; /* Assuming you're using Font Awesome for icons */
  content: '\f107'; /* Replace with the correct icon code */
  margin-left: 5px; /* Add some spacing between the text and the icon */
  color: #555; /* Set the color of the icon */
}

/* Style the button icon */
.add-button b-icon {
  font-size: 20px;
  margin-right: 5px; /* Add some space between the icon and the button */
}

.add-button {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
  margin-right: 27px;
}
.gold-star {
  color: gold; /* Apply gold color to the trophy icon */
}

.host-td {
  width: 140px; /* Adjust the width as needed */
  font-size: 20px;
  color: rgba(0, 0, 255, 0.744);
  font-weight: bold;
   font-family: 'Tourney', sans-serif;
}

.host-score-td {
  font-size: 20px;
  color: rgba(0, 0, 255, 0.744);
  font-weight: bold;
    font-family: 'Tourney', sans-serif;
}

.oppo-td {
  width: 140px; /* Adjust the width as needed */
  font-size: 20px;
  color: rgba(255, 0, 0, 0.807);
  font-weight: bold;
  font-family: 'Tourney', sans-serif;
}

.oppo-score-td {
  font-size: 20px;
  color: rgba(255, 0, 0, 0.807);
  font-weight: bold;
    font-family: 'Tourney', sans-serif;
}

.row-even {
  background-color: #f0f0f0; /* Even row color */
}

.row-odd {
  background-color: #ffffff; /* Odd row color */
}

.narrow-input {
  width: 60px;
}

.accordion-button {

  color: #100101;
}

.accordion-button:hover {
  background-color: #0056b3;
  color: #ffffff;
}

.tournament-bracket {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin: 20px; /* Add margin for spacing */
  justify-content: space-between; /* Evenly distribute space */
  width: 80%; /* Adjust as per your layout requirement */
  margin: 0 auto; /* Center the entire bracket */
  position: relative;
}

/* Styling for arrow lines between rounds*/
.arrow {
  width: 0;
  height: 0;
  border-top: 10px solid transparent;
  border-bottom: 10px solid transparent;
  border-left: 15px solid #333; /* Arrow color */
  right: 655px;
  position: absolute; /* Position the arrow absolutely within the container */
  top: 59%; /* Vertically center the arrow within the container */
  transform: translateY(-50%); /* Adjust for vertical centering */
}

.instruction-table {
  text-align: left;
  font-size: 14px;
  color: #120b51;
  margin-left: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
}

/* Create connecting lines using pseudo-elements */

</style>