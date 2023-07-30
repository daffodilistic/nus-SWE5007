const axios = require('axios');

export const getUsers = async () => {
    const response = await axios.get('http://localhost:8080/userinfo/users')
    return response.data;
}

export const updateUser = async (user) => {
    const response = await fetch(`https://jsonplaceholder.typicode.com/users/${user.id}`, {
        method: 'PATCH',
        body: JSON.stringify(user)
    });
    const result = await response.json();
    return result;
}

export const addUser = async (user) => {
    const response = await fetch('http://localhost:3000/data', {
        method: 'POST',
        body: JSON.stringify(user)
    });
    const result = await response.json();
    return result;
}

export const deleteUser = async (id) => {
    const response = await axios.delete(`https://jsonplaceholder.typicode.com/users/${id}`);
    return response.data;
}

