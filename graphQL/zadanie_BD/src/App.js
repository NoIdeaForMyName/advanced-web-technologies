import { createServer } from 'node:http'
import { createSchema, createYoga } from 'graphql-yoga'
import fs from "fs";
import path from "path";
import resolvers from './resolvers'
import db from './models/index.js';

const __dirname = path.resolve();

// const resolvers = {
    // Query: {
    //     users: getRestUsersList,
    //     user: (parent, args, context, info) => getRestUsersList().then(usersList => usersList.find(u => u.id == args.id)),
    //     todos: getRestTodosList,
    //     todo: (parent, args, context, info) => getRestTodosList().then(todosList => todosList.find(t => t.id == args.id))
    // },
    // User: {
    //     todos: (parent, args, context, info) => getRestTodosList().then(todosList => todosList.filter(t => t.userId == parent.id))
    // },
    // ToDoItem: {
    //     user: (parent, args, context, info) => getRestUsersList().then(usersList => usersList.find(u => u.id == parent.userId))
    // }
// }

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

db.sequelize.sync().then(req => {
    server.listen(4000, () => console.log(`Server is running on http://localhost:4000`));
});
