// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../utils/mysqlAPI');

/* ---------------------------------------------------- */

// Edit Owner Information Request
router.post('/', (req, res, next) => {
    const editOwnerSql = `UPDATE nobell.owner_tbl 
                            SET owner_pw="${req.body.owner_pw}", owner_pin="${req.body.owner_pin}" 
                            WHERE owner_email="${req.body.owner_email}"`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows[0]);
        } catch (err) {
            res.status(err).send();
        }
    })(editOwnerSql)
});

module.exports = router;