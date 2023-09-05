import { shallowMount, createLocalVue,mount } from '@vue/test-utils';
import manageMetric from '@/views/manageMetric.vue'; // Import your Vue component
import BootstrapVue from 'bootstrap-vue';
import { MOCK_GET_ALL_IDC_METRIC} from '../../mock-data';
import mockAxios from '../../axios-mocks';
import { GET_ALL_IDC_METRIC_BASE_URL,CREATE_IDC_METRIC_BASE_URL,UPDATE_IDC_METRIC_BASE_URL,DELETE_IDC_METRIC_BASE_URL} from '@/api';
import Swal from 'sweetalert2';
import axios from 'axios';

jest.mock('sweetalert2');
jest.mock('axios');
const swalMock = jest.spyOn(Swal, 'fire');
console.error = jest.fn();
const localVue = createLocalVue();
localVue.use(BootstrapVue);

//START - renders correct number of tabs based on filteredCompetitionChoices
const filteredCompetitionChoices = [
  { value: 'IDC', text: 'Innovation Design Challenge', id: '1' },
  { value: 'GA', text: 'Game Arena', id: '2' },
];

describe(`manageMetric.vue`, () => {
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
      const Component = require(`@/views/manageMetric`).default;

      const wrapper = mount(manageMetric, {
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
describe('manageMetric.vue', () => {

  it('displays the correct number of Metrics', async () => {
    mockAxios.onGet(`${GET_ALL_IDC_METRIC_BASE_URL}`).reply(200, MOCK_GET_ALL_IDC_METRIC);

    const wrapper = mount(manageMetric, {
      data() {
        return {
          metrics: MOCK_GET_ALL_IDC_METRIC.data, // Let the mock response handle the data
          filteredCompetitionChoices: filteredCompetitionChoices,
        };
      },
    });

    await wrapper.vm.$nextTick();

    const displayedRows = wrapper.findAll('tr').filter(row => row.classes('parent-row')).length;

    const metricsCount = MOCK_GET_ALL_IDC_METRIC.data.length;

    expect(displayedRows/filteredCompetitionChoices.length).toBe(metricsCount);

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

//START - IDC delete metric when delete team button is clicked
describe('manageMetric.vue', () => {
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
  it('IDC metric is deleted when delete button is clicked', async () => {

    axios.delete.mockResolvedValue({});

    const wrapper = mount(manageMetric, {
      data() {
        return {
          metrics: MOCK_GET_ALL_IDC_METRIC.data, // Use the copied array
          selectedCompetition: 'Innovation Design Challenge',
        };
      },
    });
    const toggleRow = wrapper.find('#delete-button');

    // Trigger the click event to delete the team
    await toggleRow.trigger('click');

    swalMock.mockResolvedValue({ isConfirmed: true });

    await wrapper.vm.deleteMetric(1); // Pass any necessary arguments

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
  DELETE_IDC_METRIC_BASE_URL,
  {
    data: { id: expect.anything() },
    headers: { Authorization: "Bearer mockedToken", "Content-Type": "application/json" },
  }
);

    // Clean up mocks
    swalMock.mockRestore();
  });
});
//END - IDC delete metric when delete button is clicked

//START - update IDC metric when update button is clicked
describe('manageMetric.vue', () => {
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
  it('IDC metric is updated when update button is clicked', async () => {

    axios.put.mockResolvedValue({});

    const wrapper = mount(manageMetric, {
      data() {
        return {
          metrics: MOCK_GET_ALL_IDC_METRIC.data,
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
      expect.stringContaining(UPDATE_IDC_METRIC_BASE_URL),
      expect.objectContaining({
        id: expect.anything(),
        stageName : expect.anything(),
        metricName : expect.anything(),
        metricWeight: expect.anything(),
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
//END - update IDC metric when update button is clicked


//START - create IDC metric when create button is clicked
describe('manageMetric.vue', () => {
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
  it('IDC metric is created when create button is clicked', async () => {

    const wrapper = mount(manageMetric, {
      data() {
        return {
          metrics: MOCK_GET_ALL_IDC_METRIC.data,
          selectedCompetition: 'Innovation Design Challenge',
          filteredCompetitionChoices:filteredCompetitionChoices,
          metric: {
            id : ''
          }
        };
      },
    });
    const metricCountBefore = MOCK_GET_ALL_IDC_METRIC.data.length
    // Find the button with the id "edit-button"
    const addButton = wrapper.find('#addNewMetric');

    // Trigger a click event on the button
    await addButton.trigger('click');

    await wrapper.vm.$nextTick();

    const displayedRows = wrapper.findAll('tr').filter(row => row.classes('parent-row')).length;

    expect((displayedRows/filteredCompetitionChoices.length)-metricCountBefore).toBe(1);

    // Find the button with the id "edit-button"
    const editButton = wrapper.find('#edit-button');

    // Trigger a click event on the button
    await editButton.trigger('click');

    await wrapper.vm.$nextTick();

    expect(axios.post).toHaveBeenCalledWith(
      expect.stringContaining(CREATE_IDC_METRIC_BASE_URL),
      expect.objectContaining({
        stageName : expect.anything(),
        metricName : expect.anything(),
        metricWeight: expect.anything(),
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
//END - create IDC metric when create button is clicked

