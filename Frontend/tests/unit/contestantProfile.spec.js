import { shallowMount, createLocalVue,mount } from '@vue/test-utils';
import contestantProfile from '@/views/contestantProfile.vue'; // Import your Vue component
import BootstrapVue from 'bootstrap-vue';
import { MOCK_VIEW_IDC_TEAM} from '../../mock-data';
import mockAxios from '../../axios-mocks';
import { VIEW_IDC_TEAM_BASE_URL} from '@/api';
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

//START - displays the correct number of parent rows
describe('contestantProfile.vue', () => {

  it('displays the correct number of Team', async () => {
    mockAxios.onGet(`${VIEW_IDC_TEAM_BASE_URL}`).reply(200, MOCK_VIEW_IDC_TEAM);

    const wrapper = mount(contestantProfile, {
      data() {
        return {
          team: MOCK_VIEW_IDC_TEAM.data, // Let the mock response handle the data
          selectedCompetition: "Innovation Design Challenge",
        };
      },
    });

    const table = wrapper.find('.user-table')
    const rows = table.findAll('tr');

    const teamCount = MOCK_VIEW_IDC_TEAM.data.userResponses.length;

    expect(rows.length-1).toBe(teamCount);

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

//START - displays the correct number of parent rows
describe('contestantProfile.vue', () => {

  it('test click view team score', async () => {
    mockAxios.onGet(`${VIEW_IDC_TEAM_BASE_URL}`).reply(200, MOCK_VIEW_IDC_TEAM);

    const wrapper = mount(contestantProfile, {
      data() {
        return {
          team: MOCK_VIEW_IDC_TEAM.data, // Let the mock response handle the data
          selectedCompetition: "Innovation Design Challenge",
        };
      },
    });

    const table = wrapper.find('.user-table')
    const rows = table.findAll('tr');

    const teamCount = MOCK_VIEW_IDC_TEAM.data.userResponses.length;

    expect(rows.length-1).toBe(teamCount);

     const saveScore = wrapper.find('#viewScore');

  await saveScore.trigger('click');

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
  });
});
//END - displays the correct number of parent rows

