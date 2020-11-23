// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../../../mysql');

/* ---------------------------------------------------- */

router.get('/', (req, res) => {
    const getAllVisitSql = `SELECT * FROM nobell.visit_tbl WHERE visit_rs_id=${req.user.owner_rs_id} ORDER BY visit_time`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch (err) {
            res.status(err).send();
        }
    })(getAllVisitSql)
});

router.post('/confirm', (req, res) => {
    const getVisitSql = `SELECT * FROM nobell.visit_tbl WHERE visit_id=${req.body.visit_id}`;
    (async (sql) => {
        try {
            const rows1 = await mysqlAPI(sql);
            
            const setTableSql = `UPDATE nobell.table_tbl
                                    SET table_state=1, table_customer="${rows1[0].visit_customer}", table_time=NOW(), table_headCount=${rows1[0].visit_headcount}
                                    WHERE  table_rs_id=${rows1[0].visit_rs_id} AND table_no=${rows1[0].visit_table} AND table_state=0
                                    `;
            await mysqlAPI(setTableSql);

            const setUserSql = `UPDATE nobell.customer_tbl
                                    SET customer_state=2
                                    WHERE customer_email="${rows1[0].visit_customer}"
                                    `;
            await mysqlAPI(setUserSql);
                
            const delVisitSql = `DELETE FROM nobell.visit_tbl WHERE visit_id=${req.body.visit_id}`;
            await mysqlAPI(delVisitSql);
            
            res.status(200).send(rows1);
        } catch (err) {
            res.status(err).send();
        }
    })(getVisitSql);
});

router.post('/reject', (req, res) => {
    const delVisitSql = `DELETE FROM nobell.visit_tbl WHERE visit_id=${req.body.visit_id}`;
    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch(err) {
            res.status(err).send();
        }
    })(delVisitSql);
});

module.exports = router;