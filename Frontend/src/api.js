// console.log("NODE_ENV is " + process.env.NODE_ENV);

// Base URL declarations
const BASE_URL = process.env.NODE_ENV === 'production' ? 'https://register.ric-robotics.xyz/' : 'http://localhost:8080/';
const GAME_SERVICE_URL = process.env.NODE_ENV === 'production' ? 'https://game.ric-robotics.xyz' : 'http://localhost:8081';
const IDC_SERVICE_URL = process.env.NODE_ENV === 'production' ? 'https://idc.ric-robotics.xyz' : 'http://localhost:8082';
const INFO_SERVICE_URL = process.env.NODE_ENV === 'production' ? 'https://info.ric-robotics.xyz' : 'http://localhost:8003';

// API object
const apiObject = Object.freeze({
    // NOTE this is used for the frontend to know where to redirect to after Keycloak login
    BASE_URL: BASE_URL,
    //CONTESTANTS
    GET_ALL_USER_INFO_BASE_URL: `${INFO_SERVICE_URL}/userinfo/view-all-users`,
    VIEW_USER_INFO_BASE_URL:    `${INFO_SERVICE_URL}/userinfo/view-user`,
    CREATE_USER_INFO_BASE_URL:  `${INFO_SERVICE_URL}/userinfo/create-user`,
    UPDATE_USER_INFO_BASE_URL:  `${INFO_SERVICE_URL}/userinfo/update-user`,
    DELETE_USER_INFO_BASE_URL:  `${INFO_SERVICE_URL}/userinfo/delete-user`,
    //IDC TEAM
    GET_ALL_IDC_TEAM_BASE_URL:      `${IDC_SERVICE_URL}/idcteam/view-all-teams`,
    VIEW_IDC_TEAM_BASE_URL:         `${IDC_SERVICE_URL}/idcteam/view-team`,
    CREATE_IDC_TEAM_BASE_URL:       `${IDC_SERVICE_URL}/idcteam/create-team`,
    UPDATE_IDC_TEAM_BASE_URL:       `${IDC_SERVICE_URL}/idcteam/update-team`,
    DELETE_IDC_TEAM_BASE_URL:       `${IDC_SERVICE_URL}/idcteam/delete-team`,
    QUALIFY_IDC_TEAM_BASE_URL:      `${IDC_SERVICE_URL}/idcteam/qualify-team`,
    ADD_MEMBER_IDC_TEAM_BASE_URL:   `${IDC_SERVICE_URL}/idcteam/assign-user`,
    //UPLOAD/DOWNLOAD
    UPLOAD_PRIM_FILE_IDC_BASE_URL:      `${IDC_SERVICE_URL}/idcteam/upload-prim-file`,
    UPLOAD_PROMO_FILE_IDC_BASE_URL:     `${IDC_SERVICE_URL}/idcteam/upload-promo-file`,
    DOWNLOAD_FILE_IDC_BASE_URL:         `${IDC_SERVICE_URL}/idcteam/download-file?file=participants`,
    DOWNLOAD_ADMIN_FILE_IDC_BASE_URL:   `${IDC_SERVICE_URL}/idcteam/download-file?file=admin`,
    VIEW_ALL_FILES_BASE_URL:            `${IDC_SERVICE_URL}/idcteam/view-all-participants-files`,
    VIEW_ALL_ADMIN_FILES_BASE_URL:      `${IDC_SERVICE_URL}/idcteam/view-all-admin-files`,
    UPLOAD_ADMIN_FILES_BASE_URL:        `${IDC_SERVICE_URL}/idcteam/upload-file`,
    //IDC METRICS
    GET_ALL_IDC_METRIC_BASE_URL:    `${IDC_SERVICE_URL}/idcmetrics/view-all-metrics`,
    CREATE_IDC_METRIC_BASE_URL:     `${IDC_SERVICE_URL}/idcmetrics/create-metric`,
    UPDATE_IDC_METRIC_BASE_URL:     `${IDC_SERVICE_URL}/idcmetrics/update-metric`,
    DELETE_IDC_METRIC_BASE_URL:     `${IDC_SERVICE_URL}/idcmetrics/delete-metrics`,
    CALCULATE_IDC_SCORE_BASE_URL:   `${IDC_SERVICE_URL}/idcmetrics/calculate`,
    //IDC Group
    GET_ALL_IDC_GROUP_BASE_URL:     `${IDC_SERVICE_URL}/idcgroup/view-all-groups`,
    VIEW_IDC_GROUP_BASE_URL:        `${IDC_SERVICE_URL}/idcgroup/view-group`,
    CREATE_IDC_GROUP_BASE_URL:      `${IDC_SERVICE_URL}/idcgroup/create-group`,
    UPDATE_IDC_GROUP_BASE_URL:      `${IDC_SERVICE_URL}/idcgroup/update-group`,
    DELETE_IDC_GROUP_BASE_URL:      `${IDC_SERVICE_URL}/idcgroup/delete-group`,
    ADD_TEAM_IDC_GROUP_BASE_URL:    `${IDC_SERVICE_URL}/idcgroup/assign-team`,
    //GAME TEAM
    GET_ALL_GAME_TEAM_BASE_URL:     `${GAME_SERVICE_URL}/games/view-all-teams`,
    VIEW_GAME_TEAM_BASE_URL:        '',
    CREATE_GAME_TEAM_BASE_URL:      `${GAME_SERVICE_URL}/games/create-team`,
    UPDATE_GAME_TEAM_BASE_URL:      `${GAME_SERVICE_URL}/games/update-team`,
    DELETE_GAME_TEAM_BASE_URL:      '',
    ADD_MEMBER_GAME_TEAM_BASE_URL:  `${GAME_SERVICE_URL}/games/assign-user`,
    //GAME METRICS
    GET_ALL_GAME_METRIC_BASE_URL:   '',
    CREATE_GAME_METRIC_BASE_URL:    '',
    UPDATE_GAME_METRIC_BASE_URL:    '',
    //GAME Group
    GET_ALL_GAME_GROUP_BASE_URL:    `${GAME_SERVICE_URL}/games/view-all-groups`,
    VIEW_GAME_GROUP_BASE_URL:       '',
    CREATE_GAME_GROUP_BASE_URL:     `${GAME_SERVICE_URL}/games/create-group`,
    UPDATE_GAME_GROUP_BASE_URL:     '',
    ADD_TEAM_GAME_GROUP_BASE_URL:   `${GAME_SERVICE_URL}/games/assign-team`,
    //GAMES
    GET_ALL_GAMES_BASE_URL:                     `${GAME_SERVICE_URL}/games/view-all-games`,
    CREATE_GAME_BASE_URL:                       `${GAME_SERVICE_URL}/games/create-game`,
    UPDATE_GAME_ONGOING_STATUS_BASE_URL:        `${GAME_SERVICE_URL}/games/update-ongoing-status`,
    UPDATE_GAME_SCORE_BASE_URL:                 `${GAME_SERVICE_URL}/games/update-scores`,
    CHECK_GAME_QUALIFICATION_STATUS_BASE_URL:   `${GAME_SERVICE_URL}/games/check-qualification-status`,
    QUALIFY_GAME_TEAM_BASE_URL:                 `${GAME_SERVICE_URL}/games/qualify-group`,
    //TECHNICAL COMP
    VIEW_ALL_TC_BASE_URL:       `${GAME_SERVICE_URL}/tech-comp/view-all-tech-comps`,
    CREATE_TC_BASE_URL:         `${GAME_SERVICE_URL}/tech-comp/create-tech-comp`,
    UPDATE_TC_STATUS_BASE_URL:  `${GAME_SERVICE_URL}/tech-comp/update-ongoing-status`,
    UPDATE_TC_OUTCOME_BASE_URL: `${GAME_SERVICE_URL}/tech-comp/update-outcome`
});

export const api = apiObject;
