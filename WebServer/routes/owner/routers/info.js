// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../../../mysql');

/* ---------------------------------------------------- */

// Edit Owner Information Request
router.post('/', (req, res, next) => {
    const editOwnerSql = `UPDATE nobell.owner_tbl 
                            SET owner_pw="${req.body.new_pw}", owner_pin="${req.body.new_pin}" 
                            WHERE owner_email="${req.user.owner_email}"`;

    (async (sql) => {
        try {
            await mysqlAPI(sql);

            req.user.owner_pw = req.body.new_pw;
            req.user.owner_pin = req.body.new_pin;

            res.status(200).send();
        } catch (err) {
            res.status(err).send();
        }
    })(editOwnerSql)
});

module.exports = router;