import { shallowMount, createLocalVue,mount } from '@vue/test-utils';
import manageContestant from '@/views/manageContestant.vue'; // Import your Vue component
import BootstrapVue from 'bootstrap-vue';
import { MOCK_GET_ALL_USER_INFO} from '../../mock-data';
import mockAxios from '../../axios-mocks';
import {GET_ALL_USER_INFO_BASE_URL,UPDATE_USER_INFO_BASE_URL,CREATE_USER_INFO_BASE_URL,DELETE_USER_INFO_BASE_URL} from '@/api';
import Swal from 'sweetalert2';
import axios from 'axios';

jest.mock('sweetalert2');
jest.mock('axios');
const swalMock = jest.spyOn(Swal, 'fire');
console.error = jest.fn();
const localVue = createLocalVue();
localVue.use(BootstrapVue);

mockAxios.onGet(`${GET_ALL_USER_INFO_BASE_URL}`).reply(200, MOCK_GET_ALL_USER_INFO);

//START - renders correct number of tabs based on filteredCompetitionChoices
const filteredCompetitionChoices = [
  { value: 'IDC', text: 'Innovation Design Challenge', id: '1' },
  { value: 'GA', text: 'Game Arena', id: '2' },
];

describe(`manageContestant.vue`, () => {
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
      const Component = require(`@/views/manageContestant`).default;

      const wrapper = mount(manageContestant, {
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
describe('manageContestant.vue', () => {

  it('displays the correct number of users', async () => {

    const wrapper = mount(manageContestant, {
      data() {
        return {
          users: MOCK_GET_ALL_USER_INFO.data, // Let the mock response handle the data
          filteredCompetitionChoices: filteredCompetitionChoices,
          itemsPerPage: 10,
        };
      },
    });

    expect(axios.get).toHaveBeenCalledWith(
  GET_ALL_USER_INFO_BASE_URL,
    {
      headers: {Authorization: "Bearer mockedToken", "Content-Type": "application/json"},
    }
  );
    const displayedRows = wrapper.findAll('tr').filter(row => row.classes('parent-row')).length;

    const usersCount = MOCK_GET_ALL_USER_INFO.data.length;

    expect(displayedRows/filteredCompetitionChoices.length).toBe(usersCount);

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

//START - IDC delete user when delete team button is clicked
describe('manageContestant.vue', () => {
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
  it('IDC user is deleted when delete button is clicked', async () => {

    axios.delete.mockResolvedValue({});

    const wrapper = mount(manageContestant, {
      data() {
        return {
          users: MOCK_GET_ALL_USER_INFO.data, // Use the copied array
          selectedCompetition: 'Innovation Design Challenge',
        };
      },
    });
    const toggleRow = wrapper.find('#deleteUser');

    // Trigger the click event to delete the team
    await toggleRow.trigger('click');

    swalMock.mockResolvedValue({ isConfirmed: true });

    await wrapper.vm.deleteUser(1); // Pass any necessary arguments

    expect(swalMock).toHaveBeenCalledWith({
      title: 'Are you sure?',
      text: 'You won\'t be able to revert this!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!'
    });

         expect(axios.delete).toHaveBeenCalledWith(
  DELETE_USER_INFO_BASE_URL,
  {
    data: { id: expect.anything() },
    headers: { Authorization: "Bearer mockedToken", "Content-Type": "application/json" },
  }
);

    // Clean up mocks
    swalMock.mockRestore();
  });
});
//END - IDC delete user when delete button is clicked

//START - update IDC user when update button is clicked
describe('manageContestant.vue', () => {
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
  it('IDC user is updated when update button is clicked', async () => {

    axios.put.mockResolvedValue({});

    const wrapper = mount(manageContestant, {
      data() {
        return {
          users: MOCK_GET_ALL_USER_INFO.data,
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
      expect.stringContaining(UPDATE_USER_INFO_BASE_URL),
      expect.objectContaining({
        id: expect.anything(),
        userName: undefined,
        firstName: expect.anything(),
        lastName: expect.anything(),
        email: expect.anything(),
        phoneNumber: expect.anything(),
        country: expect.anything(),
        state: expect.anything(),
        schoolName: expect.anything(),
        yearsOfExp: expect.anything(),
        dateOfBirth: expect.anything(),
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
//END - update IDC user when update button is clicked

//START - create IDC user when create button is clicked
describe('manageContestant.vue', () => {
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
  it('IDC user is created when create button is clicked', async () => {

    const wrapper = mount(manageContestant, {
      data() {
        return {
          users: MOCK_GET_ALL_USER_INFO.data,
          selectedCompetition: 'Innovation Design Challenge',
          filteredCompetitionChoices:filteredCompetitionChoices,
          user: {
            id : ''
          }
        };
      },
    });
    const userCountBefore = MOCK_GET_ALL_USER_INFO.data.length //29
    // Find the button with the id "edit-button"
    const addButton = wrapper.find('#addNewuser');

    // Trigger a click event on the button
    await addButton.trigger('click');

    await wrapper.vm.$nextTick();

    const displayedRows = wrapper.findAll('tr').filter(row => row.classes('parent-row')).length; //10

    expect((displayedRows/filteredCompetitionChoices.length)-userCountBefore).toBe(1);

    // Find the button with the id "edit-button"
    const editButton = wrapper.find('#edit-button');

    // Trigger a click event on the button
    await editButton.trigger('click');

    await wrapper.vm.$nextTick();

    expect(axios.post).toHaveBeenCalledWith(
      expect.stringContaining(CREATE_USER_INFO_BASE_URL),
      expect.objectContaining({
        userName: expect.anything(),
        firstName: expect.anything(),
        lastName: expect.anything(),
        email: expect.anything(),
        phoneNumber: expect.anything(),
        country: expect.anything(),
        state: expect.anything(),
        schoolName: expect.anything(),
        yearsOfExp: expect.anything(),
        dateOfBirth: expect.anything(),
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
//END - create IDC user when create button is clicked



