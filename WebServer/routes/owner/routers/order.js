// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../../../mysql');

/* ---------------------------------------------------- */

router.get('/', (req, res) => {
    const getAllOrderSql = `SELECT * FROM nobell.order_tbl WHERE order_rs_id=${req.user.owner_rs_id} ORDER BY order_state, order_time`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch (err) {
            res.status(err).send();
        }
    })(getAllOrderSql) 
});

router.put('/receive', (req, res) => {
    const receiveOrderSql = `UPDATE nobell.order_tbl SET order_state=1 WHERE order_id=${req.body.order_id} AND order_state=0`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send();
        } catch (err) {
            res.status(err).send();
        }
    })(receiveOrderSql)
});

router.put('/complete', (req, res) => {
    const completeOrderSql = `UPDATE nobell.order_tbl SET order_state=2 WHERE order_id=${req.body.order_id} AND order_state=1`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send();
        } catch (err) {
            res.status(err).send();
        }
    })(completeOrderSql)
});

module.exports = router;