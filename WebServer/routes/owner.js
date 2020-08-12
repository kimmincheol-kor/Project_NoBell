var express = require('express');
var mysql = require('mysql');

var mysql = require('mysql');
var pool = mysql.createPool({
  connectionLimit: 10,
  host: 'localhost',
  user: 'root',
  database: 'nobell',
  password: 'alscjf45'
});

var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.send('success');
});





module.exports = router;
