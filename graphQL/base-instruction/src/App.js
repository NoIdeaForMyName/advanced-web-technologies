import { createServer } from 'node:http'
import { createSchema, createYoga } from 'graphql-yoga'
import fs from "fs";
import path from "path";

const __dirname = path.resolve();

const usersList = [
    { id: 1, name: "Jan Konieczny", email: "jan.konieczny@wonet.pl", login: "jkonieczny" },
    { id: 2, name: "Anna Wesołowska", email: "anna.w@sad.gov.pl", login: "anna.wesolowska" },
    { id: 3, name: "Piotr Waleczny", email: "piotr.waleczny@gp.pl", login: "p.waleczny" }
];

const todosList = [
{ id: 1, title: "Naprawić samochód", completed: false, user_id: 3 },
{ id: 2, title: "Posprzątać garaż", completed: true, user_id: 3 },
{ id: 3, title: "Napisać e-mail", completed: false, user_id: 3 },
{ id: 4, title: "Odebrać buty", completed: false, user_id: 2 },
{ id: 5, title: "Wysłać paczkę", completed: true, user_id: 2 },
{ id: 6, title: "Zamówic kuriera", completed: false, user_id: 3 },
];

const resolvers = {
    Query: {
        users: () => usersList,
        user: (parent, args, context, info) => usersList.find(u => u.id == args.id),
        todos: () => todosList,
        todo: (parent, args, context, info) => todosList.find(t => t.id == args.id)
    },
    User: {
        todos: (parent, args, context, info) => todosList.filter(t => t.user_id == parent.id)
    },
    ToDoItem: {
        user: (parent, args, context, info) => usersList.find(u => u.id == parent.user_id)
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
