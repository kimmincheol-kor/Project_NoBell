// Import external Modules
var express = require('express');

// Import internal Modules
var pool = require('./utils/mysql-pool');

/* ---------------------------------------------------- */

var router = express.Router();

// Edit Owner Information Request
router.post('/edit', function (req, res, next) {
    console.log('')
    console.log('[POST REQUEST : EDIT OWNER]');
    console.log('-> RECV DATA = ', req.body.user_email);

    var owner_email = req.body.user_email;
    var new_pwd = req.body.password;

    var datas = [new_pwd, owner_email];

    // Update
    pool.getConnection(function (err, connection) {

        var sqlForInsertBoard = "UPDATE nobell.owner_tbl SET owner_pwd=? WHERE owner_email=?";
        connection.query(sqlForInsertBoard, datas, function (err, rows) {
            connection.release();
            if (err) {
                console.log('-> Fail : ', err.errno);

                if (err.errno == 1062)
                    res.send("fail:duplicated");
                else
                    res.send(`fail:${err.errno}`);
            }
            else {
                console.log('-> SUCCESS');
                res.send("success");
            }
        });
    });
});

module.exports = router;