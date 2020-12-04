// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlPool = require('../../../mysql-pool');

/* ---------------------------------------------------- */

router.get('/', async (req, res) => {
    const getAllOrderSql = `SELECT * FROM nobell.order_tbl WHERE order_rs_id=${req.user.owner_rs_id} ORDER BY order_state, order_time`;

    const conn = await mysqlPool.getConnection();

    try {
        const rows = await conn.query(getAllOrderSql);
        res.status(200).send(rows[0]);
    } catch(err) {
        res.status(404).send();
    } finally {
        conn.release();
    }
});

router.put('/receive', async (req, res) => {
    const receiveOrderSql = `UPDATE nobell.order_tbl SET order_state=1 WHERE order_id=${req.body.order_id} AND order_state=0`;

    const conn = await mysqlPool.getConnection();

    try {
        await conn.beginTransaction();
        const rows = await conn.query(receiveOrderSql);
        await conn.commit();
        res.status(200).send();
    } catch(err) {
        res.status(404).send();
    } finally {
        conn.release();
    }
});

router.put('/complete',  async (req, res) => {
    const completeOrderSql = `UPDATE nobell.order_tbl SET order_state=2 WHERE order_id=${req.body.order_id} AND order_state=1`;

    const conn = await mysqlPool.getConnection();

    try {
        await conn.beginTransaction();
        const rows = await conn.query(completeOrderSql);
        await conn.commit();
        res.status(200).send();
    } catch(err) {
        res.status(404).send();
    } finally {
        conn.release();
    }
});

router.get('/:table', async (req, res) => {
    const getOrderSql = `SELECT * FROM nobell.order_tbl WHERE order_rs_id=${req.user.owner_rs_id} AND order_table=${req.params.table} ORDER BY order_state, order_time`;

    const conn = await mysqlPool.getConnection();

    try {
        const rows = await conn.query(getOrderSql);
        res.status(200).send(rows[0]);
    } catch(err) {
        res.status(404).send();
    } finally {
        conn.release();
    }
});

router.post('/pay', async (req, res) => {
    const getTableSql = `SELECT * FROM nobell.table_tbl WHERE table_rs_id=${req.user.owner_rs_id} AND table_no=${req.body.table}`;
    const outTableSql = `UPDATE nobell.table_tbl SET table_state=0, table_customer=NULL, table_time=NULL, table_headCount=NULL WHERE table_rs_id=${req.user.owner_rs_id} AND table_no=${req.body.table}`;
    const delOrderSql = `DELETE FROM nobell.order_tbl WHERE order_rs_id=${req.user.owner_rs_id} AND order_table=${req.body.table}`;

    const conn = await mysqlPool.getConnection();

    try {
        await conn.beginTransaction();
        ////////////////////////
        const rows = await conn.query(getTableSql);
        const tblTime = new Date(rows[0][0].table_time);
        const curTime = new Date();
        const rotate = new Date(curTime - tblTime);
        
        const addHistorySql = `INSERT INTO nobell.history_tbl(history_rs_id, history_table, history_customer, history_headcount, history_total, history_time, history_rotate) 
                                                        VALUES (${req.user.owner_rs_id}, ${req.body.table}, "${rows[0][0].table_customer}", ${rows[0][0].table_headCount}, ${req.body.total}, ${tblTime.getHours()}, ${parseInt(rotate.getTime()/1000)})`;
        const outUserSql = `UPDATE nobell.customer_tbl SET customer_state=0 WHERE customer_email="${rows[0][0].table_customer}"`;

        await conn.query(outTableSql);
        await conn.query(delOrderSql);
        await conn.query(addHistorySql);
        await conn.query(outUserSql);
        ////////////////////////
        await conn.commit();

        res.status(200).send(rows);
    } catch (err) {
        res.status(err).send();
    } finally {
        conn.release();
    }
});

module.exports = router;