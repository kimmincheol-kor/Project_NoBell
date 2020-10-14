var pool = require('./mysql-pool');

var mysqlAPI = function(sql, datas) {
    return new Promise((resolve, reject) => {
        pool.getConnection(function (err, connection) {
            connection.query(sql, datas, function (err, data) {
                connection.release();
                if (err) reject(err);
                else resolve(data);
            });
        });
    });
}

module.exports = mysqlAPI;