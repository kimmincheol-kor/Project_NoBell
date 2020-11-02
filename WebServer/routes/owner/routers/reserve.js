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
    // req.body.rsv_id
    const getReserveSql = `SELECT * FROM nobell.reservation_tbl WHERE rsv_id=${req.body.rsv_id}`;
    (async (sql) => { // Get Visit Data
        try {
            const rsv = await mysqlAPI(sql);

            // MySQL DATETIME
            const targetT = new Date(rsv[0].rsv_target);
            const targetTime = targetT.toISOString();

            const rsvT = new Date(rsv[0].rsv_time);
            const rsvTime = rsvT.toISOString();

            target = targetTime.substring(0, 10) + "&" + targetTime.substring(11, 19);
            reserve = rsvTime.substring(0, 10) + "&" + rsvTime.substring(11, 19);
            
            const setReserveListSql = `INSERT INTO nobell.accept_rsv_tbl
                                                (arsv_rs_id, arsv_table, arsv_customer, arsv_headcount, arsv_target, arsv_time)
                                                VALUES (${rsv[0].rsv_rs_id}, ${rsv[0].rsv_table}, "${rsv[0].rsv_customer}", ${rsv[0].rsv_headcount}, "${target}", "${reserve}")`;
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

router.get('/accepted/:id', (req, res) => {
    const getAllaReserveSql = `SELECT * FROM nobell.accept_rsv_tbl WHERE arsv_rs_id=${req.params.id} ORDER BY arsv_time`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch (err) {
            res.status(err).send();
        }
    })(getAllaReserveSql)
});

router.post('/accepted', (req, res) => {
    const delaReserveSql = `DELETE FROM nobell.accept_rsv_tbl WHERE arsv_id=${req.body.rsv_id}`;
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