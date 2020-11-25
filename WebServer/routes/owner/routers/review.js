// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlPool = require('../../../mysql-pool');

/* ---------------------------------------------------- */

router.get('/', async (req, res) => {
    const getAllReviewSql = `select review_tbl.*, answer_tbl.answer_content, answer_tbl.answer_time
    from review_tbl
    join answer_tbl
    on review_tbl.review_aid=answer_tbl.answer_id;`;

    const conn = await mysqlPool.getConnection();

    try {
        const rows = await conn.query(getAllReviewSql);
        console.log(rows[0]);
        res.status(200).send(rows[0]);
    } catch(err) {
        console.log(err);
        res.status(404).send(); 
    } finally {
        conn.release();
    }
});

module.exports = router;