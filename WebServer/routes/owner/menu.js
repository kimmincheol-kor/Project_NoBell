// Import external Modules
var express = require('express');

// Import internal Modules
var pool = require('./utils/mysql-pool');

/* ---------------------------------------------------- */

var router = express.Router();

// Get ALL Menus of Restaurant Request
router.get('/read/:id', (req, res) => {
    console.log('')
    console.log('[GET REQUEST : MENU]');
    console.log('-> RECV DATA = ', req.params.id);

    pool.getConnection((err, connection) => {
        var sqlForMenu = "SELECT * FROM nobell.menu_tbl WHERE menu_rs_id=?";

        connection.query(sqlForMenu, req.params.id, (err, rows) => {
            connection.release();
            if (err) {
                console.log('FAIL : ', err.errno);
                res.header(500);
                res.send("fail:500");
            }
            else {
                console.log('SUCCESS');
                res.header(200);
                res.send(rows);
            }
        });
    });
});

// Edit Menu of Restaurant Request
router.post('/update', (req, res, next) => {
    console.log('')
    console.log('[POST REQUEST : UPDATE MENU]');
    console.log('-> RECV DATA = ', req.body.rs_id, req.body.operation);

    // Classify by operation
    if (req.body.operation == "create") {
        menu_data = [req.body.rs_id, req.body.menu_name, req.body.menu_price];
        var sqlForMenu = "INSERT INTO nobell.menu_tbl(menu_rs_id, menu_name, menu_price) values(?,?,?)";
    }
    else if (req.body.operation == "update") {
        menu_data = [req.body.menu_price, req.body.rs_id, req.body.menu_name];
        var sqlForMenu = "UPDATE nobell.menu_tbl SET menu_price=? WHERE menu_rs_id=? AND menu_name=?";
    }
    else if (req.body.operation == "delete") {
        menu_data = [req.body.rs_id, req.body.menu_name];
        var sqlForMenu = "DELETE FROM nobell.menu_tbl WHERE menu_rs_id=? AND menu_name=?";
    }

    // Connect DB
    pool.getConnection((err, connection) => {
        connection.query(sqlForMenu, menu_data, (err, rows) => {
            connection.release();
            if (err) {
                console.log('FAIL : ', err.errno);
                res.header(500);
                res.send("fail:500");
            }
            else {
                console.log('SUCCESS');
                res.header(200);
                res.send('success');
            }
        });
    });
});

module.exports = router;