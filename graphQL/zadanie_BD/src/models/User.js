module.exports = (sequelize, DataTypes) => {
    const User = sequelize.define("User", {
        name: {
            type: DataTypes.STRING,
            allowNull: false
        },
        email: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        login: {
            type: DataTypes.STRING,
            allowNull: false,
        },
    },
    {
        timestamps: false
    });

    return User;
}
