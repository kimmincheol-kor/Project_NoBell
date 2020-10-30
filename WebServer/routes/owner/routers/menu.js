// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../utils/mysqlAPI');

/* ---------------------------------------------------- */

// Get ALL Menus of Restaurant Request
router.get('/:id', (req, res) => {
    const getMenuSql = `SELECT * FROM nobell.menu_tbl WHERE menu_rs_id="${req.params.id}"`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch (err) {
            res.status(err).send();
        }
    })(getMenuSql)
});

// Edit Menu of Restaurant Request
router.post('/', (req, res, next) => {
    let updateMenuSql = "";

    // Classify by operation
    if (req.body.operation == "create") {
        updateMenuSql = `INSERT INTO nobell.menu_tbl(menu_rs_id, menu_name, menu_price) values("${req.body.rs_id}","${req.body.menu_name}","${req.body.menu_price}")`;
    }
    else if (req.body.operation == "update") {
        updateMenuSql = `UPDATE nobell.menu_tbl SET menu_price="${req.body.menu_price}" WHERE menu_rs_id="${req.body.rs_id}" AND menu_name="${req.body.menu_name}"`;
    }
    else if (req.body.operation == "delete") {
        updateMenuSql = `DELETE FROM nobell.menu_tbl WHERE menu_rs_id="${req.body.rs_id}" AND menu_name="${req.body.menu_name}"`;
    }

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows[0]);
        } catch (err) {
            res.status(err).send();
        }
    })(updateMenuSql)
});

module.exports = router;