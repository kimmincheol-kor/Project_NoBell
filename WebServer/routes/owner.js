var express = require('express');
var mysql = require('mysql');

var mysql = require('mysql');
var pool = mysql.createPool({
  connectionLimit: 10,
  host: 'localhost',
  user: 'root',
  database: 'nobell',
  password: '123456'
  
  
});

var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.send('success');
});

// Register
router.post('/register', function(req, res, next) {

  var reg_name = req.body.reg_name;
  var reg_email = req.body.reg_email;
  var reg_pwd = req.body.reg_pwd;
  var reg_phone = req.body.reg_phone;

  var check_data = [reg_email, reg_phone];
  var reg_data = [reg_email, reg_pwd, reg_name, reg_phone];

  console.log('[Register Request]');
  console.log('-> Get Data = ', reg_data);
  
  pool.getConnection(function(err, connection){
      
    var sqlForInsertBoard = "insert into nobell.owner_info(owner_email, owner_pwd, owner_name, owner_phone) values(?,?,?,?)";
    connection.query(sqlForInsertBoard, reg_data, function(err, rows){
        if(err) {
            console.log('-> Fail to Insert : ', err);

            if(err.errno == 1062)
                res.send("fail:500");
            else
                res.send("fail:505");
        }
        else {
            console.log('-> Success to Register ! ');
            res.send("success");
        }
    });
  });
});

// Login
router.post('/login', function(req, res, next) {
  console.log('[Login Request]');
  console.log('-> Get Email = ', req.body.login_email);

  var login_email = req.body.login_email;
  var login_pwd = req.body.login_pwd;

  var login_data = [login_email, login_pwd];
  
  pool.getConnection(function(err, connection){
      var sqlForSelectBoard = "select * FROM nobell.owner_info WHERE owner_email=? AND owner_pwd=?";

      connection.query(sqlForSelectBoard, login_data, function(err, data){

          if(err) {
              console.log('-> Fail to SELECT : ', err);
              res.send('fail:505');
          }
          else {
              // Incorrect Email or Password
              if(data == "") {
                  console.log('-> Fail to Login !')
                  res.send('fail:500');
              }

              // Correct Email and Password
              else {
                  console.log('-> Success to Login !')
                  res.send('success');
              }
          }
      });
  });
});

// Upload Restaurant Data
router.post('/reg_rs', function(){

});

module.exports = router;
