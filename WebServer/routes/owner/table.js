var express = require('express');

var mysql = require('mysql');
var pool = mysql.createPool({
  connectionLimit: 10,
  host: 'localhost',
  user: 'root',
  database: 'nobell',
  password: '123456'
});

var router = express.Router();

module.exports = router;