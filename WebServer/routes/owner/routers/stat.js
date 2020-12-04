// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlPool = require('../../../mysql-pool');

/* ---------------------------------------------------- */

router.get('/', async (req, res) => {
    // let result = [
    //     { // age
    //         "20": 12, // number of age 20-29
    //     },
    //     { // time
    //         "0": 12, // number of users at time 0
    //     },
    //     { // rotate
    //         "avg": 3000, // average seconds
    //     },
    //     { // again
    //         "total": 5, // (again / total) % (visit user)
    //     }
    // ];

    const sqlForAge = `SELECT customer_age AS age, count(customer_age) AS cnt FROM customer_tbl 
                            WHERE customer_email in (SELECT history_customer FROM history_tbl WHERE history_rs_id=${req.user.owner_rs_id}) 
                            GROUP BY customer_age
                            ORDER BY customer_age`;

    const sqlForTime = `SELECT history_time AS time, count(history_time) AS cnt FROM history_tbl 
                            WHERE history_rs_id=${req.user.owner_rs_id}
                            GROUP BY history_time
                            ORDER BY time`;

    const sqlForRotate = `SELECT ROUND(AVG(history_rotate), 0) AS rotate FROM history_tbl
                            WHERE history_rs_id=${req.user.owner_rs_id}`;

    const sqlForAgain = `SELECT history_customer AS user, COUNT(history_customer) AS cnt FROM history_tbl
                            WHERE history_rs_id=1
                            GROUP BY history_customer`;

    let result = new Object();
    const conn = await mysqlPool.getConnection();

    try {
        const age = await conn.query(sqlForAge);
        const time = await conn.query(sqlForTime);
        const rotate = await conn.query(sqlForRotate);
        const again = await conn.query(sqlForAgain);
        
        result.age = age[0];
        result.time = time[0];
        result.rotate = rotate[0];
        result.again = again[0];

        console.log(result);

        res.status(200).send(result);
    } catch (err) {
        console.log(err);
        res.status(404).send();
    } finally {
        conn.release();
    }
});

module.exports = router;