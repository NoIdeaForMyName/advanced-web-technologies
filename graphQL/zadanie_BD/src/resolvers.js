import db from './models/index.js';
import { GraphQLError } from 'graphql';

const User = db.User;
const TodoItem = db.TodoItem;

const resolvers = {
    User: {
        todos: (parent, args, context, info) => parent.TodoItems
    },
    TodoItem: {
        user: (parent, args, context, info) => User.findByPk(parent.user_id)
    },
    Query: {
        users: () => User.findAll({ include: [TodoItem] }),
        user: (parent, args, context, info) => User.findByPk(args.id, { include: [TodoItem] }),
        todos: () => TodoItem.findAll({ include: [User] }),
        todo: (parent, args, context, info) => TodoItem.findByPk(args.id, { include: [User] })
    },
    Mutation: {
        createUser: async (_, { userCreate }) => {
            try {
                const user = new User(userCreate);
                await user.save();
                return user;
            } catch (err) {
                throw new Error("Error creating user");
            }
        },
        updateUser: async (_, { userUpdate }) => {
            const user = await User.findByPk(userUpdate.id);
            if (!user) {
                throw new GraphQLError('User not found', {
                    extensions: { code: 'BAD_USER_INPUT', field: 'id' }
                });
            }
            for (const [key, value] of Object.entries(userUpdate)) {
                console.log(key, value);
                user[key] = value;
            }
            await user.save();
            return user;
        },
        deleteUser: async (_, { id }) => {
            const user = await User.findByPk(id, { include: [TodoItem] });
            if (!user) {
                throw new GraphQLError('User not found', {
                    extensions: { code: 'BAD_USER_INPUT', field: 'id' }
                });
            }
            if (user.TodoItems.length !== 0) return false;
            await user.destroy();
            return true;
        },
        createTodo: async (_, { todoCreate }) => {
            try {
                const todo = new TodoItem(todoCreate);
                await todo.save();
                return todo;
            } catch (err) {
                if (err.name === 'SequelizeForeignKeyConstraintError') {
                    throw new GraphQLError('Incorrect user id', {
                      extensions: { code: 'BAD_USER_INPUT', field: 'user_id' }
                    });
                } else {
                    throw new Error("Error creating user");
                }
            }
        },
        updateTodo: async (_, { todoUpdate }) => {
            const todo = await TodoItem.findByPk(todoUpdate.id);
            if (!todo) {
                throw new GraphQLError('TodoItem not found', {
                    extensions: { code: 'BAD_USER_INPUT', field: 'id' }
                });
            }
            try {
                for (const [key, value] of Object.entries(todoUpdate)) {
                    console.log(key, value);
                    todo[key] = value;
                }
                await todo.save();
            } catch (err) {
                if (err.name === 'SequelizeForeignKeyConstraintError') {
                    throw new GraphQLError('User id not found', {
                        extensions: { code: 'BAD_USER_INPUT', field: 'user_id' }
                    });
                } else {
                    throw new Error("Error updating todo item")
                }
            }
            return todo;
        },
        deleteTodo: async (_, { id }) => {
            const todo = await TodoItem.findByPk(id);
            if (!todo) {
                throw new GraphQLError('TodoItem not found', {
                    extensions: { code: 'BAD_USER_INPUT', field: 'id' }
                });
            }
            await todo.destroy();
            return true;
        }
    },
}

export default resolvers;
