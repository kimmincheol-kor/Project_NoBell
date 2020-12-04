// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlAPI = require('../../mysql');
const passport = require('../../passport');

// Import Owner's Routers
const infoRouter = require('./routers/info');
const restaurantRouter = require('./routers/restaurant');
const menuRouter = require('./routers/menu');
const tableRouter = require('./routers/table');
const visitRouter = require('./routers/visit');
const reserveRouter = require('./routers/reserve');
const orderRouter = require('./routers/order');
const reviewRouter = require('./routers/review');
const statRouter = require('./routers/stat');

/* ---------------------------------------------------- */

// Get User Data
router.get('/', (req, res) => {
    if(req.user === undefined) res.status(404).send();
    else res.status(200).send(req.user);
});

router.post('/signup', (req, res, next) => {
    const signupSql = `INSERT 
                        INTO nobell.owner_tbl(owner_email, owner_pw, owner_name, owner_phone, owner_pin) 
                        VALUES("${req.body.signup_email}", "${req.body.signup_pw}", "${req.body.signup_name}", "${req.body.signup_phone}", "${req.body.signup_pin}")`;

    (async (sql) => {
        try {
            const rows = await mysqlAPI(sql);
            res.status(200).send();
        } catch (err) {
            res.status(err).send();
        }
    })(signupSql)
});

router.post('/signin', (req, res) => {
    // Login By Passport
    passport.authenticate('local', (err, user, info) => {
        if (err) res.status(500).send();
        else if (user) {
            req.logIn(user, (err) => {
                if (err) res.status(500).send();
                else res.status(200).send();
            });
        }
        else if (info) res.status(404).send(`${info.message}`);
    })(req, res);
});

// Routings
router.use('/info', infoRouter);
router.use('/restaurant', restaurantRouter);
router.use('/menu', menuRouter);
router.use('/table', tableRouter);
router.use('/visit', visitRouter); reserveRouter
router.use('/reserve', reserveRouter);
router.use('/order', orderRouter);
router.use('/review', reviewRouter);
router.use('/stat', statRouter);

module.exports = router;