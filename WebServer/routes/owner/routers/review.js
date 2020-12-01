// Import external Modules
const router = require('express').Router();

// Import internal Modules
const mysqlPool = require('../../../mysql-pool');

/* ---------------------------------------------------- */

// GET All Answer
router.get('/', async (req, res) => {
    const getAllReviewSql = `SELECT review_tbl.*, answer_tbl.answer_content, answer_tbl.answer_time
                                    FROM review_tbl
                                    JOIN answer_tbl ON review_tbl.review_aid=answer_tbl.answer_id
                                    WHERE review_rs_id=${req.user.owner_rs_id};`;

    const conn = await mysqlPool.getConnection();

    try {
        const rows = await conn.query(getAllReviewSql);
        res.status(200).send(rows[0]);
    } catch (err) {
        console.log(err);
        res.status(404).send();
    } finally {
        conn.release();
    }
});

// POST New Answer
router.post('/', async (req, res) => {
    const postAnswerSql = `INSERT INTO nobell.answer_tbl (answer_rs_id, answer_rid, answer_content, answer_time)
                                                        VALUES (${req.user.owner_rs_id}, ${req.body.reviewID}, "${req.body.content}", Date(Now()))`;
    
    const conn = await mysqlPool.getConnection();

    try {
        await conn.beginTransaction();
        const rows = await conn.query(postAnswerSql);
        await conn.query(`UPDATE nobell.review_tbl SET review_aid=${rows[0].insertId} WHERE review_id=${req.body.reviewID}`);
        await conn.commit();

        res.status(200).send();
    } catch (err) {
        console.log(err);
        res.status(404).send();
    } finally {
        conn.release();
    }
});

// EDIT Exist Answer
router.put('/', async (req, res) => {

    const conn = await mysqlPool.getConnection();

    try {
        await conn.beginTransaction();
        const rows = await conn.query(`SELECT review_aid FROM nobell.review_tbl WHERE review_id=${req.body.reviewID}`);
        await conn.query(`UPDATE nobell.answer_tbl SET answer_content="${req.body.content}", answer_time=Date(Now()) WHERE answer_id=${rows[0][0].review_aid}`);
        await conn.commit();

        res.status(200).send();
    } catch (err) {
        console.log(err);
        res.status(404).send();
    } finally {
        conn.release();
    }
});

// DELETE Exist Answer
router.delete('/', async (req, res) => {
    const conn = await mysqlPool.getConnection();

    try {
        await conn.beginTransaction();
        const rows = await conn.query(`SELECT review_aid FROM nobell.review_tbl WHERE review_id=${req.body.reviewID}`);
        await conn.query(`DELETE FROM nobell.answer_tbl WHERE answer_id=${rows[0][0].review_aid}`);
        await conn.query(`UPDATE nobell.review_tbl SET review_aid=1 WHERE review_id=${req.body.reviewID}`);
        await conn.commit();

        res.status(200).send();
    } catch (err) {
        console.log(err);
        res.status(404).send();
    } finally {
        conn.release();
    }
});

module.exports = router;