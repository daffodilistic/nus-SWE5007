export const MOCK_VIEW_IDC_TEAM = {
   userResponses: [
    {
      id:'123',
      userName: 'user1',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      phone: '123-456-7890',
      dateOfBirth: '1990-01-01',
      schoolName: 'Example School',
      yearsOfExp: 5,
    },
    {
      id:'124',
      userName: 'user2',
      firstName: 'Roy',
      lastName: 'Doe',
      email: 'Roy@example.com',
      phone: '123-456-7890',
      dateOfBirth: '1989-01-01',
      schoolName: 'Example School',
      yearsOfExp: 4,
    },
  ],
  presentationResponses: [
    {
      stage: 'Final 1st Stage',
      venue: 'NUS',
      score: '78',
    },
    {
      stage: 'Final 2nd Stage',
      venue: 'NUS',
      score: '98',
    },
    // Add more response objects as needed
  ],
};

export const MOCK_GET_ALL_IDC_TEAM = {
    "data": [
        {
            "id": "14a2413b-ff72-41ec-bd6c-c9273d9041fe",
            "teamName": "Team ABC",
            "ageGroup": "OC",
            "isQualifiedPromo": false,
            "isQualifiedFinal": false,
            "isQualifiedFinalSecondStage": false,
            "presentationResponses": []
        },
        {
            "id": "60ac712e-75a0-4f33-b1c9-4a6e0e5a7920",
            "teamName": "Team BCD",
            "ageGroup": "OC",
            "isQualifiedPromo": false,
            "isQualifiedFinal": false,
            "isQualifiedFinalSecondStage": false,
            "presentationResponses": []
        }
    ]
}

export const MOCK_GET_ALL_USER_INFO = {
  "data": [
  {
    id: '1',
    userName: 'user1',
    firstName: 'John',
    lastName: 'Doe',
    email: 'JD@gmail.com',
    phoneNumber: '12312321321321',
    country: 'Singapore',
    state: 'Singapore',
    schoolName: 'NUS',
    yearsOfExp: 5,
    dateOfBirth: '12/05/1988',
  },
  {
    id: '2',
    userName: 'user2',
    firstName: 'John',
    lastName: 'Goh',
    email: 'JG@gmail.com',
    phoneNumber: '12312321321321',
    country: 'Singapore',
    state: 'Singapore',
    schoolName: 'NUS',
    yearsOfExp: 5,
    dateOfBirth: '12/05/1988',
  }
  ]
};

export const MOCK_GET_ALL_IDC_METRIC = {
    "data": [
        {
            "id": "76ddb8df-7475-46d2-a81c-d723ad164ef5",
            "stageName": "Promotional Round",
            "metricName": "Implementation of the robotics solution",
            "metricWeight": 0.3
        },
        {
            "id": "9c2dde35-7199-46c0-b346-9ca1afb2d837",
            "stageName": "Promotional Round",
            "metricName": "Design of the robotics solution",
            "metricWeight": 0.4
        },
        {
            "id": "0860dc80-8e8b-4ccc-8de6-4a0dc4515e53",
            "stageName": "Promotional Round",
            "metricName": "Video",
            "metricWeight": 0.3
        },
        {
            "id": "cbd8c9cb-5b40-443d-bd2e-0cc380ad9cd8",
            "stageName": "Final 1st Stage",
            "metricName": "Overall plan",
            "metricWeight": 0.4
        },
        {
            "id": "f2914e0a-c2f2-4e86-9ffb-987fae62c4b0",
            "stageName": "Final 1st Stage",
            "metricName": "Robotics solution",
            "metricWeight": 0.3
        },
        {
            "id": "f98bbf11-7f8f-4390-aae4-eb62596d0cee",
            "stageName": "Final 1st Stage",
            "metricName": "Presentation",
            "metricWeight": 0.3
        },
        {
            "id": "195e6988-a8cc-4df9-80e5-54a3513ed3b0",
            "stageName": "Final 2nd Stage",
            "metricName": "Overall plan",
            "metricWeight": 0.4
        },
        {
            "id": "b2ca46af-8845-4f8a-85a7-dcdcf0d83622",
            "stageName": "Final 2nd Stage",
            "metricName": "Robotics solution",
            "metricWeight": 0.3
        },
        {
            "id": "d5c17a43-eab4-4300-ad45-bcb4100e12bc",
            "stageName": "Final 2nd Stage",
            "metricName": "Presentation",
            "metricWeight": 0.3
        }
    ]
}
