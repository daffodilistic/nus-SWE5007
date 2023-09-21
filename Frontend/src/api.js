//CONTESTANTS
export const GET_ALL_USER_INFO_BASE_URL ='http://localhost:8081/userinfo/view-all-users'
export const UPDATE_USER_INFO_BASE_URL ='http://localhost:8081/userinfo/update-user'
export const CREATE_USER_INFO_BASE_URL ='http://localhost:8081/userinfo/create-user'
export const DELETE_USER_INFO_BASE_URL ='http://localhost:8081/userinfo/delete-user'

//IDC TEAM
export const GET_ALL_IDC_TEAM_BASE_URL ='http://localhost:8082/idcteam/view-all-teams'
export const VIEW_IDC_TEAM_BASE_URL = 'http://localhost:8082/idcteam/view-team'
export const UPDATE_IDC_TEAM_BASE_URL = 'http://localhost:8082/idcteam/update-team'
export const ADD_MEMBER_IDC_TEAM_BASE_URL = 'http://localhost:8082/idcteam/assign-user'
export const CREATE_IDC_TEAM_BASE_URL = 'http://localhost:8082/idcteam/create-team'
export const QUALIFY_IDC_TEAM_BASE_URL = 'http://localhost:8082/idcteam/qualify-team'
export const DELETE_IDC_TEAM_BASE_URL = 'http://localhost:8082/idcteam/delete-team'

//GAME TEAM
export const GET_ALL_GAME_TEAM_BASE_URL = 'http://localhost:8083/games/view-all-teams'
export const VIEW_GAME_TEAM_BASE_URL = ''
export const UPDATE_GAME_TEAM_BASE_URL = ''
export const ADD_MEMBER_GAME_TEAM_BASE_URL = ''
export const CREATE_GAME_TEAM_BASE_URL = 'http://localhost:8083/games/create-team'
export const DELETE_GAME_TEAM_BASE_URL =''

//IDC METRICS
export const GET_ALL_IDC_METRIC_BASE_URL = 'http://localhost:8082/idcmetrics/view-all-metrics'
export const CREATE_IDC_METRIC_BASE_URL = 'http://localhost:8082/idcmetrics/create-metric'
export const UPDATE_IDC_METRIC_BASE_URL = 'http://localhost:8082/idcmetrics/update-metric'
export const CALCULATE_IDC_SCORE_BASE_URL = 'http://localhost:8082/idcmetrics/calculate'
export const DELETE_IDC_METRIC_BASE_URL = 'http://localhost:8082/idcmetrics/delete-metrics'

//GAME METRICS
export const GET_ALL_GAME_METRIC_BASE_URL = ''
export const CREATE_GAME_METRIC_BASE_URL = ''
export const UPDATE_GAME_METRIC_BASE_URL = ''

//IDC Group
export const GET_ALL_IDC_GROUP_BASE_URL = 'http://localhost:8082/idcgroup/view-all-groups'
export const VIEW_IDC_GROUP_BASE_URL = 'http://localhost:8082/idcgroup/view-group'
export const CREATE_IDC_GROUP_BASE_URL = 'http://localhost:8082/idcgroup/create-group'
export const ADD_TEAM_IDC_GROUP_BASE_URL = 'http://localhost:8082/idcgroup/assign-team'
export const UPDATE_IDC_GROUP_BASE_URL = 'http://localhost:8082/idcgroup/update-group'
export const DELETE_IDC_GROUP_BASE_URL = 'http://localhost:8082/idcgroup/delete-group'

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

//UPLOAD/DOWNLOAD
export const UPLOAD_PRIM_FILE_IDC_BASE_URL = 'http://localhost:8082/idcteam/upload-prim-file'
export const UPLOAD_PROMO_FILE_IDC_BASE_URL = 'http://localhost:8082/idcteam/upload-promo-file'
export const DOWNLOAD_FILE_IDC_BASE_URL = 'http://localhost:8082/idcteam/download-file?file=participants'
export const VIEW_ALL_FILES_BASE_URL = 'http://localhost:8082/idcteam/view-all-participants-files'