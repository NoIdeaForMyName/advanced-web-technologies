import { createServer } from 'node:http'
import { createSchema, createYoga } from 'graphql-yoga'
import fs from "fs";
import path from "path";
import axios from 'axios'

const __dirname = path.resolve();

const USERS_LIST_URL = "https://jsonplaceholder.typicode.com/users";
const TODOS_LIST_URL = "https://jsonplaceholder.typicode.com/todos";

async function getRestUsersList() {
    return axios.get(USERS_LIST_URL)
        .then(users => {
            return users.data.map(({id, name, email, username}) => ({
                id: id,
                name: name,
                email: email,
                login: username,
            }));
        })
        .catch(error => {throw error})
}

async function getRestTodosList() {
    return axios.get(TODOS_LIST_URL)
        .then(todos => todos.data)
        .catch(error => {throw error})
}

const resolvers = {
    Query: {
        users: getRestUsersList,
        user: (parent, args, context, info) => getRestUsersList().then(usersList => usersList.find(u => u.id == args.id)),
        todos: getRestTodosList,
        todo: (parent, args, context, info) => getRestTodosList().then(todosList => todosList.find(t => t.id == args.id))
    },
    User: {
        todos: (parent, args, context, info) => getRestTodosList().then(todosList => todosList.filter(t => t.userId == parent.id))
    },
    ToDoItem: {
        user: (parent, args, context, info) => getRestUsersList().then(usersList => usersList.find(u => u.id == parent.userId))
    }
}

const yoga = createYoga({
    schema: createSchema({
        typeDefs: fs.readFileSync(
            path.join(__dirname, "./", "src", "schema.graphql"),
            "utf8"
          ),
        resolvers,
    })
});

const server = createServer(yoga);

server.listen(4000, () => console.log(`Server is running on http://localhost:4000`));
