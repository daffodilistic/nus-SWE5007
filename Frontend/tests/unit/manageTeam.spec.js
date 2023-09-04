import { shallowMount, createLocalVue,mount } from '@vue/test-utils';
import manageTeam from '@/views/manageTeam.vue'; // Import your Vue component
import BootstrapVue from 'bootstrap-vue';
import { MOCK_VIEW_IDC_TEAM,MOCK_GET_ALL_IDC_TEAM,MOCK_GET_ALL_USER_INFO} from '../../mock-data';
import mockAxios from '../../axios-mocks';
import { GET_ALL_IDC_TEAM_BASE_URL,VIEW_IDC_TEAM_BASE_URL,GET_ALL_USER_INFO_BASE_URL,DELETE_IDC_TEAM_BASE_URL,ADD_MEMBER_IDC_TEAM_BASE_URL,UPDATE_IDC_TEAM_BASE_URL}from '@/api';
import Swal from 'sweetalert2';
import axios from 'axios';

jest.mock('sweetalert2');
jest.mock('axios');
const swalMock = jest.spyOn(Swal, 'fire');
console.error = jest.fn();
const localVue = createLocalVue();
localVue.use(BootstrapVue);

mockAxios.onGet(`${GET_ALL_IDC_TEAM_BASE_URL}`).reply(200, MOCK_GET_ALL_IDC_TEAM);
mockAxios.onGet(`${VIEW_IDC_TEAM_BASE_URL}`).reply(200, MOCK_VIEW_IDC_TEAM);
mockAxios.onGet(`${GET_ALL_USER_INFO_BASE_URL}`).reply(200, MOCK_GET_ALL_USER_INFO);

//START - renders correct number of tabs based on filteredCompetitionChoices
const filteredCompetitionChoices = [
  { value: 'IDC', text: 'Innovation Design Challenge', id: '1' },
  { value: 'GA', text: 'Game Arena', id: '2' },
];

describe(`manageTeam.vue`, () => {
    beforeEach(() => {
    jest.spyOn(axios, 'get');
    jest.spyOn(axios, 'put');
    jest.spyOn(axios, 'delete');
  });

  // Clear Axios mock after each test
  afterEach(() => {
    axios.get.mockClear();
    axios.put.mockClear();
    axios.delete.mockClear();
  });

  // Restore Axios mock after all tests
  afterAll(() => {
    jest.restoreAllMocks();
  });
    it('renders correct number of tabs based on filteredCompetitionChoices', async () => {
      const Component = require(`@/views/manageTeam`).default;

      const wrapper = mount(manageTeam, {
        localVue,
        data() {
          return {
            filteredCompetitionChoices: filteredCompetitionChoices,
            selectedCompetition: 'Innovation Design Challenge',
          };
        },
      });

      const tabElements = wrapper.findAllComponents({ name: 'b-tab' });

      // Check if the number of rendered tabs matches the length of filteredCompetitionChoices
      expect(tabElements.length).toBe(filteredCompetitionChoices.length);
    });
  });

//END - renders correct number of tabs based on filteredCompetitionChoices

//START - displays the correct number of parent rows


describe('manageTeam.vue', () => {
    beforeEach(() => {
    jest.spyOn(axios, 'get');
    jest.spyOn(axios, 'put');
    jest.spyOn(axios, 'delete');
  });

  // Clear Axios mock after each test
  afterEach(() => {
    axios.get.mockClear();
    axios.put.mockClear();
    axios.delete.mockClear();
  });

  // Restore Axios mock after all tests
  afterAll(() => {
    jest.restoreAllMocks();
  });
  it('displays the correct number of parent rows', async () => {

    const wrapper = mount(manageTeam, {
      data() {
        return {
          teams: MOCK_GET_ALL_IDC_TEAM.data, // Let the mock response handle the data
        };
      },
    });

    await wrapper.vm.$nextTick();

    const displayedRows = wrapper.findAll('tr').filter(row => row.classes('parent-row')).length;

    const teamsCount = MOCK_GET_ALL_IDC_TEAM.data.length;

    expect(displayedRows/2).toBe(teamsCount);

  });
});
//END - displays the correct number of parent rows

describe('Expected Fail cases', () => {
  // ... other test cases ...

  it('should not log the error message', () => {
    // Check if console.error was not called during this test
    expect(console.error).not.toHaveBeenCalled();
  });
});

//START - displays child rows when toggle row is clicked
describe('manageTeam.vue', () => {
    beforeEach(() => {
    jest.spyOn(axios, 'get');
    jest.spyOn(axios, 'put');
    jest.spyOn(axios, 'delete');
  });

  // Clear Axios mock after each test
  afterEach(() => {
    axios.get.mockClear();
    axios.put.mockClear();
    axios.delete.mockClear();
  });

  // Restore Axios mock after all tests
  afterAll(() => {
    jest.restoreAllMocks();
  });

  it('displays child rows when toggle row is clicked', async () => {

    const wrapper = mount(manageTeam,{
      data() {
        return {
          teamMembers: MOCK_VIEW_IDC_TEAM, // Provide sample data to the component
          teams: MOCK_GET_ALL_IDC_TEAM.data,
        };
      },
    });

//Find and trigger the click event on the "toggle row"
  const toggleRow = wrapper.find('.parent-row');

 await toggleRow.trigger('click');

  // Wait for Vue to update the DOM
  await wrapper.vm.$nextTick();

  expect(axios.post).toHaveBeenCalledWith(
      expect.stringContaining(VIEW_IDC_TEAM_BASE_URL),
      expect.objectContaining({
        id: expect.anything(),
      }),
      expect.objectContaining({
        headers: {
          Authorization: 'Bearer mockedToken',
          'Content-Type': 'application/json',
        },
      })
    );

  // Calculate the expected number of child rows
  const expectedChildRows = MOCK_VIEW_IDC_TEAM.userResponses.length;

  const displayedChildRows = wrapper.findAll('.child-row').length;

  // Assert that the displayed rows match the number of teams
  expect(displayedChildRows/3).toBe(expectedChildRows); // x Displayed 6: as 2-header, 2-IDC, 2-GA Expected 2
  });
});
//END - displays child rows when toggle row is clicked

//START - save users records when add user button is clicked
describe('manageTeam.vue', () => {
    beforeEach(() => {
    jest.spyOn(axios, 'get');
    jest.spyOn(axios, 'put');
    jest.spyOn(axios, 'delete');
  });

  // Clear Axios mock after each test
  afterEach(() => {
    axios.get.mockClear();
    axios.put.mockClear();
    axios.delete.mockClear();
  });

  // Restore Axios mock after all tests
  afterAll(() => {
    jest.restoreAllMocks();
  });
  it('displays users records when add user button is clicked', async () => {

    const wrapper = mount(manageTeam, {
      data() {
        return {
          teams: MOCK_GET_ALL_IDC_TEAM.data,
          paginatedModalUserList: MOCK_GET_ALL_USER_INFO,
          selectedUsers: ['123','124'],
        };
      },
    });

  //Find and trigger the click event on the "toggle row"
  const toggleRow = wrapper.find('#addUserButton');
  await toggleRow.trigger('click');

  // Find the modal by its id
  const modal = wrapper.find('#showUserModal');
  const allAxiosGetCalls = axios.get.mock.calls;
  expect(allAxiosGetCalls[0][0]).toEqual(expect.stringContaining(GET_ALL_USER_INFO_BASE_URL));

  // Within the modal, find all rows within the tbody
  const rows = modal.findAll('tbody tr');

  // Calculate the expected number of user rows
  const expectedUserRows = MOCK_GET_ALL_USER_INFO.length;
  // Assert that the displayed rows match the number of users
  expect(rows.length).toBe(expectedUserRows);

  const saveButton = wrapper.find('#addMembersToTeam');
  await saveButton.trigger('click');

  expect(axios.put).toHaveBeenCalledWith(
      expect.stringContaining(ADD_MEMBER_IDC_TEAM_BASE_URL),
      expect.objectContaining({
          id: expect.anything(),
          teamName: expect.anything(),
          userIds:expect.anything(),
      }),
      expect.objectContaining({
        headers: {
          Authorization: 'Bearer mockedToken',
          'Content-Type': 'application/json',
        },
      })
    );
  });
});
//END - save users records when add user button is clicked

//START - displays score history when show history button is clicked
describe('manageTeam.vue', () => {
    beforeEach(() => {
    jest.spyOn(axios, 'get');
    jest.spyOn(axios, 'put');
    jest.spyOn(axios, 'delete');
  });

  // Clear Axios mock after each test
  afterEach(() => {
    axios.get.mockClear();
    axios.put.mockClear();
    axios.delete.mockClear();
  });

  // Restore Axios mock after all tests
  afterAll(() => {
    jest.restoreAllMocks();
  });
  it('displays progression history when view score button is clicked', async () => {


    const wrapper = mount(manageTeam, {
      data() {
        return {
          teams: MOCK_GET_ALL_IDC_TEAM.data,
        };
      },
    });

  const toggleRow = wrapper.find('#viewScore');
  await toggleRow.trigger('click');

   expect(axios.post).toHaveBeenCalledWith(
      expect.stringContaining(VIEW_IDC_TEAM_BASE_URL),
      expect.objectContaining({ }),
      expect.objectContaining({
        headers: {
          Authorization: 'Bearer mockedToken',
          'Content-Type': 'application/json',
        },
      })
    );

  // Find the modal by its id
  const modal = wrapper.find('#showHistoryModal');

  // Within the modal, find all rows within the tbody
  const rows = modal.findAll('tbody tr');

  // Calculate the expected number of user rows
  const expectedHistoryRows = MOCK_VIEW_IDC_TEAM.presentationResponses.length;
  // Assert that the displayed rows match the number of users
  expect(rows.length).toBe(expectedHistoryRows);

  });
});
//END - displays score history when show history button is clicked

//START - IDC delete team when delete team button is clicked
describe('manageTeam.vue', () => {
    beforeEach(() => {
    jest.spyOn(axios, 'get');
    jest.spyOn(axios, 'put');
    jest.spyOn(axios, 'delete');
  });

  // Clear Axios mock after each test
  afterEach(() => {
    axios.get.mockClear();
    axios.put.mockClear();
    axios.delete.mockClear();
  });

  // Restore Axios mock after all tests
  afterAll(() => {
    jest.restoreAllMocks();
  });
  it('IDC team is deleted when delete team button is clicked', async () => {
    axios.put.mockResolvedValue({});
    axios.delete.mockResolvedValue({});

    const wrapper = mount(manageTeam, {
      data() {
        return {
          teams: MOCK_GET_ALL_IDC_TEAM.data, // Use the copied array
          selectedCompetition: 'Innovation Design Challenge',
        };
      },
    });
    const toggleRow = wrapper.find('#delete-button');

    // Trigger the click event to delete the team
    await toggleRow.trigger('click');

    swalMock.mockResolvedValue({ isConfirmed: true });

    await wrapper.vm.deleteTeam(1); // Pass any necessary arguments

    expect(swalMock).toHaveBeenCalledWith({
      title: 'Are you sure?',
      text: 'You won\'t be able to revert this!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!'
    });

    expect(axios.put).toHaveBeenCalledWith(
      expect.stringContaining(ADD_MEMBER_IDC_TEAM_BASE_URL),
      expect.objectContaining({
          id: expect.anything(),
          teamName: expect.anything(),
          userIds:[]
      }),
      expect.objectContaining({
        headers: {
          Authorization: 'Bearer mockedToken',
          'Content-Type': 'application/json',
        },
      })
    );

     expect(axios.delete).toHaveBeenCalledWith(
  DELETE_IDC_TEAM_BASE_URL,
  {
    data: { id: expect.anything() },
    headers: { Authorization: "Bearer mockedToken", "Content-Type": "application/json" },
  }
);

    expect(swalMock).toHaveBeenCalledWith({
      title: 'Deleted!',
      text: 'The Team has been deleted.',
      icon: 'success'
    });

    // Clean up mocks
    swalMock.mockRestore();
  });
});
//END - IDC delete team when delete team button is clicked

//START - update IDC team when update team button is clicked
describe('manageTeam.vue', () => {
    beforeEach(() => {
    jest.spyOn(axios, 'get');
    jest.spyOn(axios, 'put');
    jest.spyOn(axios, 'delete');
  });

  // Clear Axios mock after each test
  afterEach(() => {
    axios.get.mockClear();
    axios.put.mockClear();
    axios.delete.mockClear();
  });

  // Restore Axios mock after all tests
  afterAll(() => {
    jest.restoreAllMocks();
  });
  it('IDC team is updated when update team button is clicked', async () => {

    axios.put.mockResolvedValue({});

    const wrapper = mount(manageTeam, {
      data() {
        return {
          teams: MOCK_GET_ALL_IDC_TEAM.data,
          selectedCompetition: 'Innovation Design Challenge',
        };
      },
    });
    // Find the button with the id "edit-button"
    const editButton = wrapper.find('#edit-button');

    // Trigger a click event on the button
    await editButton.trigger('click');

    await wrapper.vm.$nextTick();

    // Trigger another click event on the button
    await editButton.trigger('click');

    await wrapper.vm.$nextTick()

    expect(axios.put).toHaveBeenCalledWith(
      expect.stringContaining(UPDATE_IDC_TEAM_BASE_URL),
      expect.objectContaining({
        id: expect.anything(),
        teamName : expect.anything(),
        ageGroup : expect.anything(),
      }),
      expect.objectContaining({
        headers: {
          Authorization: 'Bearer mockedToken',
          'Content-Type': 'application/json',
        },
      })
    );
  });
});
//END - update IDC team when update team button is clicked

