import db from './models/index.js';

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
            if (!user) throw new Error("User not found");
            for (const [key, value] of Object.entries(userUpdate)) {
                console.log(key, value);
                user[key] = value;
            }
            await user.save();
            return user;
        },
        deleteUser: async (_, { id }) => {
            const user = await User.findByPk(id, { include: [TodoItem] });
            if (!user) throw new Error('User not found');
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
                throw new Error("Error creating todo item");
            }
        },
        updateTodo: async (_, { todoUpdate }) => {
            const todo = await TodoItem.findByPk(todoUpdate.id);
            if (!todo) throw new Error("Todo item not found");
            for (const [key, value] of Object.entries(todoUpdate)) {
                console.log(key, value);
                todo[key] = value;
            }
            await todo.save();
            return todo;
        },
        deleteTodo: async (_, { id }) => {
            const todo = await TodoItem.findByPk(id);
            if (!todo) throw new Error('Todo not found');
            await todo.destroy();
            return true;
        }
    },
}

export default resolvers;
