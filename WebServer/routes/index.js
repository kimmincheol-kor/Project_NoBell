var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/dodo', function(req, res, next) {
  res.render('index', {title : 'DODO!'});
});

/* GET home page. */
router.get('/tak', function(req, res, next) {
  res.render('tak', {title : 'tak!'});
});

/* GET home page. */
router.get('/', function(req, res, next) {
  res.send('yun');
});




module.exports = router;
