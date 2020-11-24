const mysql = require('mysql2/promise');

// Create Pool with MySQL
const pool = mysql.createPool({
    connectionLimit: 50,
    host: 'localhost',
    user: 'root',
    database: 'nobell',
    password: '123456'
});

module.exports = pool;