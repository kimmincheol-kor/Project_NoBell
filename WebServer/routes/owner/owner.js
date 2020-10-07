var express = require('express');

var mysql = require('mysql');
var pool = mysql.createPool({
  connectionLimit: 10,
  host: 'localhost',
  user: 'root',
  database: 'nobell',
  password: '123456'
});

var router = express.Router();

// Edit Owner Information Request
router.post('/edit', function (req, res, next) {

    console.log('[POST Edit Owner Request]');
    console.log('-> Get owner_email = ', req.body.user_email);

    var owner_email = req.body.user_email;
    var new_pwd = req.body.password;
    var new_phone = req.body.phone;

    var datas = [new_phone, new_pwd, owner_email];

    // Update
    pool.getConnection(function (err, connection) {

        var sqlForInsertBoard = "update nobell.owner_tbl set owner_phone=?, owner_pwd=? WHERE owner_email=?";
        connection.query(sqlForInsertBoard, datas, function (err, rows) {
            if (err) {
                console.log('-> Fail to Update : ', err);

                if (err.errno == 1062)
                    res.send("fail:500");
                else
                    res.send("fail:505");
            }
            else {
                console.log('-> Success to Update ! ');
                res.send("success");
            }
        });
    });
});

module.exports = router;