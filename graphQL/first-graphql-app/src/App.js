import { createServer } from 'node:http'
import { createSchema, createYoga } from 'graphql-yoga'
import fs from "fs";
import path from "path";

const __dirname = path.resolve();

const resolvers = {
    Query: {
        demo: () => "Witaj, GraphQL działa!",
    },
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
