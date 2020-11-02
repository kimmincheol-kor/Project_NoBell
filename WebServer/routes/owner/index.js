// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('./utils/mysqlAPI');

// Import Owner's Routers
const infoRouter = require('./routers/info');
const restaurantRouter = require('./routers/restaurant');
const menuRouter = require('./routers/menu');
const tableRouter = require('./routers/table');
const visitRouter = require('./routers/visit');
const reserveRouter = require('./routers/reserve');

/* ---------------------------------------------------- */

// SignUp Request
router.post('/signup', (req, res, next) => {
    const signupSql = `INSERT 
                        INTO nobell.owner_tbl(owner_email, owner_pw, owner_name, owner_phone, owner_pin) 
                        VALUES("${req.body.signup_email}", "${req.body.signup_pw}", "${req.body.signup_name}", "${req.body.signup_phone}", "${req.body.signup_pin}")`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows[0]);
        } catch (err) {
            res.status(err).send();
        }
    })(signupSql)
});

// Signin Request
router.post('/signin', (req, res, next) => {
    const signinSql = `select * FROM nobell.owner_tbl 
                                WHERE owner_email="${req.body.signin_email}" AND owner_pw="${req.body.signin_pw}"`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send(rows[0]);
        } catch (err) {
            res.status(err).send();
        }
    })(signinSql)
});

// Routings
router.use('/info', infoRouter);
router.use('/restaurant', restaurantRouter);
router.use('/menu', menuRouter);
router.use('/table', tableRouter);
router.use('/visit', visitRouter);reserveRouter
router.use('/reserve', reserveRouter);

module.exports = router;