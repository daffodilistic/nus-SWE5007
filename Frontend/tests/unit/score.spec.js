import { shallowMount, createLocalVue,mount } from '@vue/test-utils';
import Score from '@/views/Score.vue'; // Import your Vue component
import BootstrapVue from 'bootstrap-vue';
import { MOCK_VIEW_IDC_TEAM,MOCK_GET_ALL_IDC_TEAM,MOCK_GET_ALL_IDC_METRIC} from '../../mock-data';
import mockAxios from '../../axios-mocks';
import { GET_ALL_IDC_TEAM_BASE_URL,VIEW_IDC_TEAM_BASE_URL,QUALIFY_IDC_TEAM_BASE_URL,GET_ALL_IDC_METRIC_BASE_URL,CALCULATE_IDC_SCORE_BASE_URL,UPDATE_IDC_TEAM_BASE_URL}from '@/api';
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
mockAxios.onGet(`${GET_ALL_IDC_METRIC_BASE_URL}`).reply(200, MOCK_GET_ALL_IDC_METRIC);


//START - renders correct number of tabs based on filteredCompetitionChoices
const filteredCompetitionChoices = [
  { value: 'IDC', text: 'Innovation Design Challenge', id: '1' },
  { value: 'GA', text: 'Game Arena', id: '2' },
];

describe(`Score.vue`, () => {
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
      const Component = require(`@/views/Score`).default;

      const wrapper = mount(Score, {
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

describe('Score.vue', () => {
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

    const wrapper = mount(Score, {
      data() {
        return {
          teams: MOCK_GET_ALL_IDC_TEAM.data, // Let the mock response handle the data
          filteredCompetitionChoices:filteredCompetitionChoices
        };
      },
    });

     expect(axios.get).toHaveBeenCalledWith(
  GET_ALL_IDC_TEAM_BASE_URL,
  {
    headers: { Authorization: "Bearer mockedToken", "Content-Type": "application/json" },
  }
);
    const displayedRows = wrapper.findAll('tr').filter(row => row.classes('parent-row')).length;

    const teamsCount = MOCK_GET_ALL_IDC_TEAM.data.filter(record => !record.isQualifiedPromo).length;

    expect(displayedRows/filteredCompetitionChoices.length).toBe(teamsCount);

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

//START - displays Promotionalchild rows for promotional round when toggle row is clicked
describe('Score.vue', () => {
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

  it('displays Promotional Round child rows when toggle row is clicked', async () => {

    const wrapper = mount(Score,{
      data() {
        return {
          metrics: MOCK_GET_ALL_IDC_METRIC.data,
          teams: MOCK_GET_ALL_IDC_TEAM.data,
          selectedCompetition: 'Innovation Design Challenge',
        };
      },
    });

    const selectElement = wrapper.find('select[name="game-type"]');

    // Simulate changing the value by triggering a change event
    await selectElement.setValue('Promotional Round'); // Replace 'option1' with the desired option value

    // Check if the component's data has been updated
    expect(wrapper.vm.selectedQualification).toBe('Promotional Round'); // Replace 'option1' with the expected value

  //Find and trigger the click event on the "toggle row"
  const toggleRow = wrapper.find('.parent-row');

  await toggleRow.trigger('click');
 // await wrapper.vm.toggleRow(1); // Pass any necessary arguments

  // Wait for Vue to update the DOM
  await wrapper.vm.$nextTick();

  expect(axios.get).toHaveBeenCalledWith(
  GET_ALL_IDC_METRIC_BASE_URL,
  {
    headers: { Authorization: "Bearer mockedToken", "Content-Type": "application/json" },
  }
);
  // Calculate the expected number of child rows
  const expectedChildRows =  MOCK_GET_ALL_IDC_METRIC.data.filter(record => record.stageName === 'Promotional Round').length;

  const displayedChildRows = ((wrapper.findAll('.child-row').length)/filteredCompetitionChoices.length)-1;

  // Assert that the displayed rows match the number of teams

  expect(displayedChildRows).toBe(expectedChildRows);


  const saveScore = wrapper.find('#saveScore');

  await saveScore.trigger('click');

   expect(axios.post).toHaveBeenCalledWith(
      expect.stringContaining(CALCULATE_IDC_SCORE_BASE_URL),
      expect.objectContaining({
        metricIds: expect.anything(),
        metricScores: expect.anything(),
      }),
      expect.objectContaining({
        headers: {
          Authorization: 'Bearer mockedToken',
          'Content-Type': 'application/json',
        },
      })
    );

    expect(axios.put).toHaveBeenCalledWith(
      expect.stringContaining(QUALIFY_IDC_TEAM_BASE_URL),
      expect.objectContaining({
        id: expect.anything(),
        isQualifiedPromo : expect.anything(),
        isQualifiedFinal : expect.anything(),
        isQualifiedFinalSecondStage : expect.anything(),
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
//END - displays child rows for promotional round when toggle row is clicked

//START - preview Score
describe('Score.vue', () => {
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

  it('Show score when preview score is clicked', async () => {

    const wrapper = mount(Score,{
      data() {
        return {
          metrics: MOCK_GET_ALL_IDC_METRIC.data,
          teams: MOCK_GET_ALL_IDC_TEAM.data,
          selectedCompetition: 'Innovation Design Challenge',
        };
      },
    });

    const selectElement = wrapper.find('select[name="game-type"]');

    // Simulate changing the value by triggering a change event
    await selectElement.setValue('Promotional Round'); // Replace 'option1' with the desired option value

    // Check if the component's data has been updated
    expect(wrapper.vm.selectedQualification).toBe('Promotional Round'); // Replace 'option1' with the expected value

  //Find and trigger the click event on the "toggle row"
  const toggleRow = wrapper.find('.parent-row');

  await toggleRow.trigger('click');
 // await wrapper.vm.toggleRow(1); // Pass any necessary arguments

  // Wait for Vue to update the DOM
  await wrapper.vm.$nextTick();

  expect(axios.get).toHaveBeenCalledWith(
  GET_ALL_IDC_METRIC_BASE_URL,
  {
    headers: { Authorization: "Bearer mockedToken", "Content-Type": "application/json" },
  }
);
  // Calculate the expected number of child rows
  const expectedChildRows =  MOCK_GET_ALL_IDC_METRIC.data.filter(record => record.stageName === 'Promotional Round').length;

  const displayedChildRows = ((wrapper.findAll('.child-row').length)/filteredCompetitionChoices.length)-1;

  // Assert that the displayed rows match the number of teams

  expect(displayedChildRows).toBe(expectedChildRows);

  const previewScore = wrapper.find('#previewScore');

  await previewScore.trigger('click');

   expect(axios.post).toHaveBeenCalledWith(
      expect.stringContaining(CALCULATE_IDC_SCORE_BASE_URL),
      expect.objectContaining({
        metricIds: expect.anything(),
        metricScores: expect.anything(),
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
//END - preview Score
