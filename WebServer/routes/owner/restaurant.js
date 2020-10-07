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

// Get Restaurant Data
router.get('/:id', function (req, res, next) {
    console.log('[Get Restaurant Data Request]');
    console.log('-> Get Rs_id = ', req.params.id);

    var rs_id = req.params.id;

    pool.getConnection(function (err, connection) {

        var sqlForSelectBoard = "select * FROM nobell.restaurant_tbl WHERE rs_id=?";

        connection.query(sqlForSelectBoard, rs_id, function (err, data) {

            if (err) {
                // fail to get rs data
                console.log('-> Fail To Get Data : ', err);
                res.send('fail:500');
            }
            else {
                // success to get rs data
                console.log('-> Success To Get Data', data[0]);
                res.send(data[0]);
            }

        });
    });
});

// Change Restaurant State
router.post('/state', function (req, res, next) {
    console.log('[Change Restaurant State Request]');
    console.log('-> Get Rs_id = ', req.body.rs_id);
    console.log('-> Get state = ', req.body.state);

    // Change
    pool.getConnection(function (err, connection) {

        var sqlForInsertBoard = "update nobell.restaurant_tbl set rs_state=? WHERE rs_id=?";
        connection.query(sqlForInsertBoard, [req.body.state, req.body.rs_id], function (err, rows) {
            if (err) {
                console.log('-> Fail to Change : ', err);

                if (err.errno == 1062)
                    res.send("fail:500");
                else
                    res.send("fail:505");
            }
            else {
                console.log('-> Success to Change ! ');
                res.send("success");
            }
        });
    });
});

// Update Restaurant Data
router.post('/update', function (req, res, next) {
    console.log('[POST Restaurant Data Request]');
    console.log('-> Get Rs_id = ', req.body.rs_id);

    var owner_email = req.body.user_email;
    var rs_id = req.body.rs_id;
    var rs_name = req.body.name;
    var rs_phone = req.body.phone;
    var rs_address = req.body.address;
    var rs_intro = req.body.intro;
    var rs_open = req.body.open;
    var rs_close = req.body.close;

    var rs_data = [rs_name, rs_phone, rs_address, rs_intro, rs_open, rs_close, owner_email];

    if (rs_id > 0) {

        // Update
        pool.getConnection(function (err, connection) {
            rs_data.push(rs_id);
            var sqlForInsertBoard = "update nobell.restaurant_tbl set rs_name=?, rs_phone=?, rs_address=?, rs_intro=?, rs_open=?, rs_close=?, rs_owner=? WHERE rs_id=?";
            connection.query(sqlForInsertBoard, rs_data, function (err, rows) {
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
    } // end of Update
    else {

        // Register
        pool.getConnection(function (err, connection) {

            var sqlForInsertBoard = "insert into nobell.restaurant_tbl(rs_name, rs_phone, rs_address, rs_intro, rs_open, rs_close, rs_owner) values(?,?,?,?,?,?,?)";
            connection.query(sqlForInsertBoard, rs_data, function (err, rows) {
                if (err) {
                    console.log('-> Fail to Insert : ', err);

                    if (err.errno == 1062)
                        res.send("fail:500");
                    else
                        res.send("fail:505");
                }
                else {
                    console.log('-> Success to Register to ', rows.insertId);

                    // update owner's rs_id
                    var sqlForOwner = "update nobell.owner_tbl set owner_rs_id=? where owner_email=?";
                    connection.query(sqlForOwner, [rows.insertId, owner_email], function (err2, rows2) {
                        if (err) res.send("fail:500");
                        else res.send("success");
                    });
                }
            });
        });
    } // end of Register
});


module.exports = router;