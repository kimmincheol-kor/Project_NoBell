// Import external Modules
var express = require('express');

// Import internal Modules
var pool = require('./utils/mysql-pool');

/* ---------------------------------------------------- */

var router = express.Router();

// Get Restaurant Data
router.get('/:id', function (req, res, next) {
    console.log('')
    console.log('[GET REQUEST : RESTAURANT DATA]');
    console.log('-> RECV DATA = ', req.params.id);

    var rs_id = req.params.id;

    pool.getConnection(function (err, connection) {

        var sqlForSelectBoard = "SELECT * FROM nobell.restaurant_tbl WHERE rs_id=?";

        connection.query(sqlForSelectBoard, rs_id, function (err, data) {
            connection.release();
            if (err) {
                // fail to get rs data
                console.log('-> FAIL : ', err.errno);
                res.send(`fail:${err.errno}`);
            }
            else {
                // success to get rs data
                console.log('-> SUCCESS');
                res.send(data[0]);
            }
        });
    });
});

// Change Restaurant State
router.post('/change_state', function (req, res, next) {
    console.log('')
    console.log('[POST REQUEST : CHANGE RESTAURANT STATE]');
    console.log('-> RECV DATA = ', req.body.rs_id, req.body.state);

    // Change
    pool.getConnection(function (err, connection) {
        var sqlForInsertBoard = "UPDATE nobell.restaurant_tbl SET rs_state=? WHERE rs_id=?";
        connection.query(sqlForInsertBoard, [req.body.state, req.body.rs_id], function (err, rows) {
            connection.release();
            if (err) {
                console.log('-> FAIL : ', err.errno);

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

// Update Restaurant Data
router.post('/update', function (req, res, next) {
    console.log('')
    console.log('[POST REQUEST : EDIT RESTAURANT DATA]');
    console.log('-> RECV DATA = ', req.body.rs_id);

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
            var sqlForInsertBoard = "UPDATE nobell.restaurant_tbl SET rs_name=?, rs_phone=?, rs_address=?, rs_intro=?, rs_open=?, rs_close=?, rs_owner=? WHERE rs_id=?";
            connection.query(sqlForInsertBoard, rs_data, function (err, rows) {
                connection.release();
                if (err) {
                    console.log('-> FAIL : ', err.errno);

                    if (err.errno == 1062)
                        res.send("fail:duplicated");
                    else
                        res.send(`fail:${err.errno}`);
                }
                else {
                    console.log('-> SUCCESS');
                    res.send(`${rs_id}`);
                }
                
            });
        });
    } // end of Update
    else {

        // Register
        pool.getConnection(function (err, connection) {

            var sqlForInsertBoard = "INSERT INTO nobell.restaurant_tbl(rs_name, rs_phone, rs_address, rs_intro, rs_open, rs_close, rs_owner) VALUES(?,?,?,?,?,?,?)";
            connection.query(sqlForInsertBoard, rs_data, function (err, rows) {
                if (err) {
                    console.log('-> FAIL : ', err.errno);

                    if (err.errno == 1062)
                        res.send("fail:duplicated");
                    else
                        res.send(`fail:${err.errno}`);
                }
                else {
                    console.log('-> SUCCESS TO : ', rows.insertId);

                    // update owner's rs_id
                    var sqlForOwner = "UPDATE nobell.owner_tbl SET owner_rs_id=? WHERE owner_email=?";
                    connection.query(sqlForOwner, [rows.insertId, owner_email], function (err2, rows2) {
                        connection.release();
                        if (err) res.send(`fail:${err.errno}`);
                        else res.send(`${rows.insertId}`);
                    });
                }
            });
        });
    } // end of Register
});

module.exports = router;