// console.log("NODE_ENV is " + process.env.NODE_ENV);

// Base URL declarations
const BASE_URL = process.env.NODE_ENV === 'production' ? 'https://ric-robotics.xyz/' : 'http://localhost:8080/';
const IDC_SERVICE_URL = process.env.NODE_ENV === 'production' ? 'https://idc.ric-robotics.xyz' : '${IDC_SERVICE_URL}';
const INFO_SERVICE_URL = process.env.NODE_ENV === 'production' ? 'https://info.ric-robotics.xyz' : 'http://localhost:8081';

// API object
const apiObject = Object.freeze({
    // NOTE this is used for the frontend to know where to redirect to after Keycloak login
    BASE_URL: BASE_URL,
    //CONTESTANTS
    GET_ALL_USER_INFO_BASE_URL: `${INFO_SERVICE_URL}/userinfo/view-all-users`,
    UPDATE_USER_INFO_BASE_URL: `${INFO_SERVICE_URL}/userinfo/update-user`,
    CREATE_USER_INFO_BASE_URL: `${INFO_SERVICE_URL}/userinfo/create-user`,
    DELETE_USER_INFO_BASE_URL: `${INFO_SERVICE_URL}/userinfo/delete-user`,
    VIEW_USER_INFO_BASE_URL: `${INFO_SERVICE_URL}/userinfo/view-user`,
    //IDC TEAM
    GET_ALL_IDC_TEAM_BASE_URL:      `${IDC_SERVICE_URL}/idcteam/view-all-teams`,
    VIEW_IDC_TEAM_BASE_URL:         `${IDC_SERVICE_URL}/idcteam/view-team`,
    UPDATE_IDC_TEAM_BASE_URL:       `${IDC_SERVICE_URL}/idcteam/update-team`,
    ADD_MEMBER_IDC_TEAM_BASE_URL:   `${IDC_SERVICE_URL}/idcteam/assign-user`,
    CREATE_IDC_TEAM_BASE_URL:       `${IDC_SERVICE_URL}/idcteam/create-team`,
    QUALIFY_IDC_TEAM_BASE_URL:      `${IDC_SERVICE_URL}/idcteam/qualify-team`,
    DELETE_IDC_TEAM_BASE_URL:       `${IDC_SERVICE_URL}/idcteam/delete-team`,
    //UPLOAD/DOWNLOAD
    UPLOAD_PRIM_FILE_IDC_BASE_URL:      `${IDC_SERVICE_URL}/idcteam/upload-prim-file`,
    UPLOAD_PROMO_FILE_IDC_BASE_URL:     `${IDC_SERVICE_URL}/idcteam/upload-promo-file`,
    DOWNLOAD_FILE_IDC_BASE_URL:         `${IDC_SERVICE_URL}/idcteam/download-file?file=participants`,
    DOWNLOAD_ADMIN_FILE_IDC_BASE_URL:   `${IDC_SERVICE_URL}/idcteam/download-file?file=admin`,
    VIEW_ALL_FILES_BASE_URL:            `${IDC_SERVICE_URL}/idcteam/view-all-participants-files`,
    VIEW_ALL_ADMIN_FILES_BASE_URL:      `${IDC_SERVICE_URL}/idcteam/view-all-admin-files`,
    UPLOAD_ADMIN_FILES_BASE_URL:        `${IDC_SERVICE_URL}/idcteam/upload-file`,
    //IDC METRICS
    GET_ALL_IDC_METRIC_BASE_URL:        `${IDC_SERVICE_URL}/idcmetrics/view-all-metrics`,
    CREATE_IDC_METRIC_BASE_URL:         `${IDC_SERVICE_URL}/idcmetrics/create-metric`,
    UPDATE_IDC_METRIC_BASE_URL:         `${IDC_SERVICE_URL}/idcmetrics/update-metric`,
    CALCULATE_IDC_SCORE_BASE_URL:       `${IDC_SERVICE_URL}/idcmetrics/calculate`,
    DELETE_IDC_METRIC_BASE_URL:         `${IDC_SERVICE_URL}/idcmetrics/delete-metrics`,
    //IDC Group
    GET_ALL_IDC_GROUP_BASE_URL:         `${IDC_SERVICE_URL}/idcgroup/view-all-groups`,
    VIEW_IDC_GROUP_BASE_URL:            `${IDC_SERVICE_URL}/idcgroup/view-group`,
    CREATE_IDC_GROUP_BASE_URL:          `${IDC_SERVICE_URL}/idcgroup/create-group`,
    ADD_TEAM_IDC_GROUP_BASE_URL:        `${IDC_SERVICE_URL}/idcgroup/assign-team`,
    UPDATE_IDC_GROUP_BASE_URL:          `${IDC_SERVICE_URL}/idcgroup/update-group`,
    DELETE_IDC_GROUP_BASE_URL:          `${IDC_SERVICE_URL}/idcgroup/delete-group`
});

export const api = apiObject;


//GAME TEAM
export const GET_ALL_GAME_TEAM_BASE_URL = 'http://localhost:8083/games/view-all-teams'
export const VIEW_GAME_TEAM_BASE_URL = ''
export const UPDATE_GAME_TEAM_BASE_URL = 'http://localhost:8083/games/update-team'
export const ADD_MEMBER_GAME_TEAM_BASE_URL = 'http://localhost:8083/games/assign-user'
export const CREATE_GAME_TEAM_BASE_URL = 'http://localhost:8083/games/create-team'
export const DELETE_GAME_TEAM_BASE_URL =''

//GAME METRICS
export const GET_ALL_GAME_METRIC_BASE_URL = ''
export const CREATE_GAME_METRIC_BASE_URL = ''
export const UPDATE_GAME_METRIC_BASE_URL = ''

//GAME Group
export const GET_ALL_GAME_GROUP_BASE_URL = 'http://localhost:8083/games/view-all-groups'
export const VIEW_GAME_GROUP_BASE_URL = ''
export const CREATE_GAME_GROUP_BASE_URL = 'http://localhost:8083/games/create-group'
export const ADD_TEAM_GAME_GROUP_BASE_URL = 'http://localhost:8083/games/assign-team'
export const UPDATE_GAME_GROUP_BASE_URL = ''

//GAMES
export const GET_ALL_GAMES_BASE_URL = 'http://localhost:8083/games/view-all-games'
export const CREATE_GAME_BASE_URL = 'http://localhost:8083/games/create-game'
export const UPDATE_GAME_ONGOING_STATUS_BASE_URL = 'http://localhost:8083/games/update-ongoing-status'
export const UPDATE_GAME_SCORE_BASE_URL = 'http://localhost:8083/games/update-scores'
export const CHECK_GAME_QUALIFICATION_STATUS_BASE_URL = 'http://localhost:8083/games/check-qualification-status'//
export const QUALIFY_GAME_TEAM_BASE_URL = 'http://localhost:8083/games/qualify-group'//

//TECHNICAL COMP
export const VIEW_ALL_TC_BASE_URL = 'http://localhost:8083/tech-comp/view-all-tech-comps'
export const CREATE_TC_BASE_URL = 'http://localhost:8083/tech-comp/create-tech-comp'
export const UPDATE_TC_STATUS_BASE_URL = 'http://localhost:8083/tech-comp/update-ongoing-status'
export const UPDATE_TC_OUTCOME_BASE_URL = 'http://localhost:8083/tech-comp/update-outcome'
