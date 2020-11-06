// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../../../mysql');

/* ---------------------------------------------------- */

// Get ALL Menus of Restaurant Request
router.get('/', (req, res) => {
    const getMenuSql = `SELECT * FROM nobell.menu_tbl WHERE menu_rs_id="${req.user.owner_rs_id}"`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows);
        } catch (err) {
            res.status(err).send();
        }
    })(getMenuSql)
});

router.post('/', (req, res, next) => {
    const createMenuSql = `INSERT INTO nobell.menu_tbl(menu_rs_id, menu_name, menu_price) values("${req.user.owner_rs_id}","${req.body.menu_name}","${req.body.menu_price}")`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send();
        } catch (err) {
            res.status(err).send();
        }
    })(createMenuSql)
});

router.put('/', (req, res, next) => {
    const updateMenuSql = `UPDATE nobell.menu_tbl SET menu_price="${req.body.menu_price}" WHERE menu_rs_id="${req.user.owner_rs_id}" AND menu_name="${req.body.menu_name}"`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send();
        } catch (err) {
            res.status(err).send();
        }
    })(updateMenuSql)
});

router.delete('/', (req, res, next) => {
    const deleteMenuSql = `DELETE FROM nobell.menu_tbl WHERE menu_rs_id="${req.user.owner_rs_id}" AND menu_name="${req.body.menu_name}"`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send();
        } catch (err) {
            res.status(err).send();
        }
    })(deleteMenuSql)
});

module.exports = router;