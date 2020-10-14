var mysql = require('mysql');

// Create Pool with MySQL
var pool = mysql.createPool({
    connectionLimit: 50,
    host: 'localhost',
    user: 'root',
    database: 'nobell',
    password: '123456'
});

module.exports = pool;