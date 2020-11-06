// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../../../mysql');

/* ---------------------------------------------------- */

// Get ALL Tables of Restaurant Request
router.get('/', (req, res) => {
  const getTableSql = `SELECT * FROM nobell.table_tbl WHERE table_rs_id="${req.user.owner_rs_id}" ORDER BY table_position_y, table_position_x`;

  (async (sql) => {
    try {
      const rows = await mysqlAPI(sql);
      res.status(200).send(rows);
    } catch (err) {
      res.status(err).send();
    }
  })(getTableSql)
});

router.post('/', (req, res, next) => {
  const createTableSql = `INSERT INTO nobell.table_tbl(table_rs_id, table_no, table_position_x, table_position_y, table_maxCount) values(${req.user.owner_rs_id}, ${req.body.table_no}, ${req.body.table_x}, ${req.body.table_y}, ${req.body.table_headcount})`;

  (async (sql) => {
      try {
          const rows = await mysqlAPI(sql);
          res.status(200).send();
      } catch (err) {
          res.status(err).send();
      }
  })(createTableSql)
});

router.put('/', (req, res, next) => {
  const updateTableSql = `UPDATE nobell.table_tbl SET table_maxCount=${req.body.table_headcount} WHERE table_rs_id=${req.user.owner_rs_id} AND table_no=${req.body.table_no}`;

  (async (sql) => {
      try {
          const rows = await mysqlAPI(sql);
          res.status(200).send();
      } catch (err) {
          res.status(err).send();
      }
  })(updateTableSql)
});

router.delete('/', (req, res, next) => {
  const deleteTableSql = `DELETE FROM nobell.table_tbl WHERE table_rs_id=${req.user.owner_rs_id} AND table_no=${req.body.table_no}`;

  (async (sql) => {
      try {
          const rows = await mysqlAPI(sql);
          res.status(200).send(rows[0]);
      } catch (err) {
          res.status(err).send();
      }
  })(deleteTableSql)
});

module.exports = router;