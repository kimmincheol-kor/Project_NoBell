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

// Get ALL Menus of Restaurant Request
router.get('/get/:id', function (req, res) {
    console.log('[GET All Menus Request]');
    console.log('-> Get rs_id = ', req.params.id);

    pool.getConnection(function (err, connection) {
        var sqlForMenu = "select * FROM nobell.menu_tbl WHERE menu_rs_id=?";

        connection.query(sqlForMenu, req.params.id, function (err, rows) {
            if (err) res.send("fail:500");
            else res.send(rows);
        });
    });
});

// Edit Menu of Restaurant Request
router.post('/update', function (req, res, next) {
    console.log('[POST Edit Menu Request]');
    console.log('-> Get rs_id = ', req.body.rs_id);
    console.log('-> Get operation = ', req.body.operation);

    var rs_id = req.body.rs_id;
    var menu_name = req.body.menu_name;
    var menu_price = req.body.menu_price;

    var operation = req.body.operation;

    // Classify by operation
    if (operation == "new") {
        menu_data = [rs_id, menu_name, menu_price];
        var sqlForMenu = "INSERT INTO nobell.menu_tbl values(?,?,?)";
    }
    else if (operation == "update") {
        menu_data = [menu_price, rs_id, menu_name];
        var sqlForMenu = "UPDATE nobell.menu_tbl SET menu_price=? WHERE menu_rs_id=? AND menu_name=?";
    }
    else if (operation == "delete") {
        menu_data = [rs_id, menu_name];
        var sqlForMenu = "DELETE FROM nobell.menu_tbl WHERE menu_rs_id=? AND menu_name=?";
    }

    // Connect DB
    pool.getConnection(function (err, connection) {
        connection.query(sqlForMenu, menu_data, function (err, rows) {
            if (err) res.send("fail:500");
            else res.send('success');
        });
    });
});

module.exports = router;