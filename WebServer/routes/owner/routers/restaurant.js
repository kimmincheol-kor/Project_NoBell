// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../utils/mysqlAPI');

/* ---------------------------------------------------- */

// Get Restaurant Data
router.get('/:id', function (req, res, next) {
    const getRsSql = `SELECT * FROM nobell.restaurant_tbl WHERE rs_id=${req.params.id}`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows[0]);
        } catch (err) {
            res.status(err).send();
        }
    })(getRsSql)
});

// Update Restaurant Data
router.post('/', function (req, res, next) {
    // Edit Exist Restaurant
    if (req.body.rs_id > 0) {
        const editRsSql = `UPDATE nobell.restaurant_tbl 
                                    SET rs_name="${req.body.rs_name}", rs_phone="${req.body.rs_phone}", rs_address="${req.body.rs_address}", 
                                        rs_intro="${req.body.rs_intro}", rs_open="${req.body.rs_open}", rs_close="${req.body.rs_close}" 
                                    WHERE rs_id=${req.body.rs_id}`;

        (async (sql) => {
            try {
                const rows = await mysqlAPI(sql);
                res.status(200).send(`${req.body.rs_id}`);
            } catch (err) {
                res.status(err).send();
            }
        })(editRsSql)
    }
    // Register New Restaurant
    else {
        const regRsSql = `INSERT INTO nobell.restaurant_tbl(rs_name, rs_phone, rs_address, rs_intro, rs_open, rs_close, rs_owner) 
                                    VALUES("${req.body.rs_name}","${req.body.rs_phone}","${req.body.rs_address}","${req.body.rs_intro}",
                                            "${req.body.rs_open}","${req.body.rs_close}","${req.body.owner_email}")`;

        (async (sql) => {
            try {
                const rows1 = await mysqlAPI(sql); // Register

                const setRsIdSql = `UPDATE nobell.owner_tbl SET owner_rs_id=${rows1.insertId} WHERE owner_email="${req.body.owner_email}"`;
                const rows2 = await mysqlAPI(setRsIdSql); // Set New Rs_Id

                res.status(200).send(`${rows1.insertId}`);
            } catch (err) {
                res.status(err).send();
            }
        })(regRsSql)
    }
});

// Change Restaurant State
router.post('/switch', function (req, res, next) {
    const switchRsSql = `UPDATE nobell.restaurant_tbl SET rs_state=${req.body.state} WHERE rs_id=${req.body.rs_id}`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows[0]);
        } catch (err) {
            res.status(err).send();
        }
    })(switchRsSql)
});

module.exports = router;