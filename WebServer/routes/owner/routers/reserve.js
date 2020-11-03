// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../utils/mysqlAPI');

/* ---------------------------------------------------- */

router.get('/:id', (req, res) => {
    const getAllReserveSql = `SELECT * FROM nobell.reservation_tbl WHERE rsv_rs_id=${req.params.id} ORDER BY rsv_time`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch (err) {
            res.status(err).send();
        }
    })(getAllReserveSql)
});

router.post('/confirm', (req, res) => {
    const getReserveSql = `SELECT * FROM nobell.reservation_tbl WHERE rsv_id=${req.body.rsv_id}`;
    (async (sql) => {
        try {
            const rsv = await mysqlAPI(sql);
            
            const setReserveListSql = `INSERT INTO nobell.accept_rsv_tbl
                                                (arsv_rs_id, arsv_table, arsv_customer, arsv_headcount, arsv_target)
                                                VALUES (${rsv[0].rsv_rs_id}, ${rsv[0].rsv_table}, "${rsv[0].rsv_customer}", ${rsv[0].rsv_headcount}, "${rsv[0].rsv_target}")`;
            await mysqlAPI(setReserveListSql);
                
            const delReserveSql = `DELETE FROM nobell.reservation_tbl WHERE rsv_id=${req.body.rsv_id}`
            await mysqlAPI(delReserveSql)
            
            res.status(200).send(rsv);
        } catch (err) {
            res.status(err).send();
        }
    })(getReserveSql);
});

router.post('/reject', (req, res) => {
    const delReserveSql = `DELETE FROM nobell.reservation_tbl WHERE rsv_id=${req.body.rsv_id}`;
    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch(err) {
            res.status(err).send();
        }
    })(delReserveSql); 
});

////////// ACCEPTED RESERVATION

router.get('/accepted/:id', (req, res) => {
    const getAllaReserveSql = `SELECT * FROM nobell.accept_rsv_tbl WHERE arsv_rs_id=${req.params.id} ORDER BY arsv_target`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch (err) {
            res.status(err).send();
        }
    })(getAllaReserveSql)
});

router.post('/accepted/confirm', (req, res) => {
    const getARsvSql = `SELECT * FROM nobell.accept_rsv_tbl WHERE arsv_id=${req.body.arsv_id}`;
    (async (sql) => {
        try {
            const rows1 = await mysqlAPI(sql);
            
            const setTableSql = `UPDATE nobell.table_tbl
                                    SET table_state=1, table_customer="${rows1[0].arsv_customer}", table_time=NOW(), table_headCount=${rows1[0].arsv_headcount}
                                    WHERE  table_rs_id=${rows1[0].arsv_rs_id} AND table_no=${rows1[0].arsv_table} AND table_state=0
                                    `
            await mysqlAPI(setTableSql);
                
            const delVisitSql = `DELETE FROM nobell.accept_rsv_tbl WHERE arsv_id=${req.body.arsv_id}`
            await mysqlAPI(delVisitSql)
            
            res.status(200).send(rows1);
        } catch (err) {
    res.status(err).send();
        }
    })(getARsvSql);
});

router.post('/accepted/cancel', (req, res) => {
    const delaReserveSql = `DELETE FROM nobell.accept_rsv_tbl WHERE arsv_id=${req.body.arsv_id}`;
    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch(err) {
            res.status(err).send();
        }
    })(delaReserveSql); 
});

module.exports = router;