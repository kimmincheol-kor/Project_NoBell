// Import external Modules
var express = require('express');

// Import internal Modules
var pool = require('./utils/mysql-pool');
var mysqlQuery = require('./utils/mysql-query');

// Import Owner's Routers
const ownerRouter = require('./owner');
const restaurantRouter = require('./restaurant');
const menuRouter = require('./menu');
const tableRouter = require('./table');

/* ---------------------------------------------------- */

var router = express.Router();

// Default URL for Test
router.get('/', function (req, res, next) {
    console.log('[Test Request]');
    res.send('success to connection');
});

// SignUp Request
router.post('/signup', (req, res, next) => {
    console.log('')
    console.log('[POST REQUEST : SIGNUP]');
    console.log('-> RECV DATA = ', reg_data);

    var reg_data = [req.body.reg_email, req.body.reg_pwd, req.body.reg_name, req.body.reg_phone];

    pool.getConnection(function (err, connection) {
        var sqlForInsertBoard = "INSERT INTO nobell.owner_tbl(owner_email, owner_pwd, owner_name, owner_phone) VALUES(?,?,?,?)";
        connection.query(sqlForInsertBoard, reg_data, function (err, rows) {
            connection.release();
            
            if (err) {
                console.log('-> FAIL : ', err.errno);
                res.header(500);
                if (err.errno == 1062)
                    res.send("fail:duplicated");
                else
                    res.send(`fail:${err.errno}`);
            }
            else {
                console.log('-> SUCCESS');
                res.header(200);
                res.send("success");
            }
        });
    });
});

// Signin Request
router.post('/signin', (req, res, next) => {
    console.log('')
    console.log('[POST REQUEST : SIGNIN]');
    console.log('-> RECV DATA = ', req.body.login_email);

    var sqlForSelectBoard = "select * FROM nobell.owner_tbl WHERE owner_email=? AND owner_pwd=?";
    var login_data = [req.body.login_email, req.body.login_pwd];

    mysqlQuery(sqlForSelectBoard, login_data)
        .then((data) => {
            // Incorrect Email or Password
            if (data == "") {
                console.log('-> FAIL : Incorrect Data')
                res.header(500);
                res.send('fail:incorrect');
            }
            // Correct Email and Password
            else {
                console.log('-> SUCCESS')
                res.header(200);
                res.send(data[0]);
            }
        })
            .catch((err) => {
            console.log('-> FAIL : ', err.errno);
            res.header(500);
            res.send(`fail:${err.errno}`);
        });
});

// Routings
router.use('/owner', ownerRouter);
router.use('/restaurant', restaurantRouter);
router.use('/menu', menuRouter);
router.use('/table', tableRouter);

module.exports = router;