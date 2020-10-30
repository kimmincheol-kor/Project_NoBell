const mysql = require('mysql');

// Create Pool with MySQL
const pool = mysql.createPool({
    connectionLimit: 50,
    host: 'localhost',
    user: 'root',
    database: 'nobell',
    password: '123456'
});

const mysqlAPI = function(sql) {
    return new Promise((resolve, reject) => {
        pool.getConnection(function (err, connection) { // get Connection.
            connection.query(sql, function (err, data) { // Query Operation
                // release Connection.
                connection.release();

                if (err) { // error
                    console.log("-> FAIL :", err.errno);
                    if(err.errno == 1062) reject(409);
                    else reject(500);
                }
                else {
                    if (data == "") { // no data
                        console.log("-> FAIL : No Data");
                        reject(404);
                    }
                    else { // exist data
                        console.log("-> SUCCESS");
                        resolve(data);
                    }
                }
            });
        });
    });
}

module.exports = mysqlAPI;