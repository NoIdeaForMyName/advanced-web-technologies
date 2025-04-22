import { createServer } from 'node:http'
import { createSchema, createYoga } from 'graphql-yoga'
import fs from "fs";
import path from "path";
import resolvers from './resolvers.js'
import db from './models/index.js';

const __dirname = path.resolve();

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
