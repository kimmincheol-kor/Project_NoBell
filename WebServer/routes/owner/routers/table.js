// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../utils/mysqlAPI');

/* ---------------------------------------------------- */

// Get ALL Tables of Restaurant Request
router.get('/:id', (req, res) => {
  const getTableSql = `SELECT * FROM nobell.table_tbl WHERE table_rs_id="${req.params.id}" ORDER BY table_position_y, table_position_x`;

  (async (sql) => {
    try {
      const rows = await mysqlAPI(sql);
      res.status(200).send(rows);
    } catch (err) {
      res.status(err).send();
    }
  })(getTableSql)
});

// Edit Table of Restaurant Request
router.post('/', function (req, res, next) {
  let postTableSql = "";

  // Classify by operation
  if (req.body.operation == "create") {
    postTableSql = `INSERT INTO nobell.table_tbl(table_rs_id, table_no, table_position_x, table_position_y, table_headcount) values(${req.body.rs_id}, ${req.body.table_no}, ${req.body.table_x}, ${req.body.table_y}, ${req.body.table_headcount})`;
  }
  else if (req.body.operation == "update") {
    postTableSql = `UPDATE nobell.table_tbl SET table_headcount=${req.body.table_headcount} WHERE table_rs_id=${req.body.rs_id} AND table_no=${req.body.table_no}`;
  }
  else if (req.body.operation == "delete") {
    postTableSql = `DELETE FROM nobell.table_tbl WHERE table_rs_id=${req.body.rs_id} AND table_no=${req.body.table_no}`;
  }

  (async (sql) => {
    try {
      const rows = await mysqlAPI(sql);
      res.status(200).send(rows[0]);
    } catch (err) {
      res.status(err).send();
    }
  })(postTableSql)
});

module.exports = router;