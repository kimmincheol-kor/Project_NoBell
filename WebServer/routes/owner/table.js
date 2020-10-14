// Import external Modules
var express = require('express');

// Import internal Modules
var pool = require('./utils/mysql-pool');

/* ---------------------------------------------------- */

var router = express.Router();

// Get ALL Tables of Restaurant Request
router.get('/read/:id', (req, res) => {
  console.log('')
  console.log('[GET REQUEST : ALL TABLES]');
  console.log('-> RECV DATA = ', req.params.id);

  pool.getConnection(function (err, connection) {
    var sqlForTable = "SELECT * FROM nobell.table_tbl WHERE table_rs_id=? ORDER BY table_position_y, table_position_x";

    connection.query(sqlForTable, req.params.id, function (err, rows) {
      connection.release();
      if (err) res.send("fail:500");
      else res.send(rows);
    });
  });
});

// Edit Table of Restaurant Request
router.post('/update', function (req, res, next) {
  console.log('')
  console.log('[POST REQUEST : EDIT TABLE]');
  console.log('-> RECV DATA = ', req.body.rs_id, req.body.operation);
  
  var rs_id = req.body.rs_id;
  var table_no = req.body.table_no;
  
  var operation = req.body.operation;

  // Classify by operation
  var sqlForTable
  if (operation == "create") {
    var table_x = req.body.table_x;
    var table_y = req.body.table_y;
    var table_people = req.body.table_people;
    sqlForTable = `INSERT INTO nobell.table_tbl(table_rs_id, table_no, table_position_x, table_position_y, table_people) values(${rs_id}, ${table_no}, ${table_x}, ${table_y}, ${table_people})`;
  }
  else if (operation == "update") {
    var table_people = req.body.table_people;
    sqlForTable = `UPDATE nobell.table_tbl SET table_people=${table_people} WHERE table_rs_id=${rs_id} AND table_no=${table_no}`;
  }
  else if (operation == "delete") {
    sqlForTable = `DELETE FROM nobell.table_tbl WHERE table_rs_id=${rs_id} AND table_no=${table_no}`;
  }

  // Connect DB
  pool.getConnection(function (err, connection) {
    connection.query(sqlForTable, function (err, rows) {
      connection.release();
      if (err) res.send("fail:500");
      else res.send('success');
    });
  });
});

module.exports = router;