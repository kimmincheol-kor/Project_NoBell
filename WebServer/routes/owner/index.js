// Import external Modules
const express = require('express');

// Import internal Modules
const pool = require('./utils/mysql-pool');
const mysqlQuery = require('./utils/mysql-query');

// Import Owner's Routers
const ownerRouter = require('./owner');
const restaurantRouter = require('./restaurant');
const menuRouter = require('./menu');
const tableRouter = require('./table');

/* ---------------------------------------------------- */

const router = express.Router();

// Default URL for Test
router.get('/', function (req, res, next) {
    console.log('[Test Request]');
    res.send('success to connection');
});

// SignUp Request
router.post('/signup', (req, res, next) => {
    console.log('')
    console.log('[POST REQUEST : SIGNUP]');
    console.log('-> RECV DATA = ', req.body.reg_email);

    const signupSql = "INSERT INTO nobell.owner_tbl(owner_email, owner_pwd, owner_name, owner_phone) VALUES(?,?,?,?)";
    const signupData = [req.body.reg_email, req.body.reg_pwd, req.body.reg_name, req.body.reg_phone];

    (async (sql, data) => {
        try {
            const rows = await mysqlQuery(sql, data);

            console.log('-> SUCCESS');
            res.send("success");
        } catch (err) {
            console.log('-> FAIL :', err.errno);
            if (err.errno == 1062)
                res.send("fail:duplicated");
            else
                res.send(`fail:${err.errno}`);
        }
    })(signupSql, signupData)
});

// Signin Request
router.post('/signin', (req, res, next) => {
    console.log('')
    console.log('[POST REQUEST : SIGNIN]');
    console.log('-> RECV DATA = ', req.body.login_email);

    const signinSql = "select * FROM nobell.owner_tbl WHERE owner_email=? AND owner_pwd=?";
    const signinData = [req.body.login_email, req.body.login_pwd];

    (async (sql, data) => {
        try {
            const rows = await mysqlQuery(sql, data);

            if (rows == "") {
                console.log('-> FAIL : Incorrect Data')
                res.send('fail:incorrect');
            }
            else {
                console.log('-> SUCCESS')
                res.send(rows[0]);
            }
        } catch {
            console.log('-> FAIL :', err.errno);
            res.send(`fail:${err.errno}`);
        }
    })(signinSql, signinData)
});

// Routings
router.use('/owner', ownerRouter);
router.use('/restaurant', restaurantRouter);
router.use('/menu', menuRouter);
router.use('/table', tableRouter);

module.exports = router;