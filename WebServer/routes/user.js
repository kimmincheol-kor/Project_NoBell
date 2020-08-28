var express = require('express');
var mysql = require('mysql');

var mysql = require('mysql');
var pool = mysql.createPool({
  connectionLimit: 10,
  host: 'localhost',
  user: 'root',
  database: 'nobell',
  password : 'asdasd',
 
});

var router = express.Router();

/* GET users listing. */
router.post('/', function(req, res, next) {
    res.send(200);
});
router.post('/hi', function(req, res) {
    res.send("LogOK");
});

router.post('/test', function(req, res) {
    res.send("LogOK");
});
// Register
router.post('/register', function(req, res, next) {

  var user_name = req.body.reg_name;
  var user_email = req.body.reg_email;
  var user_pwd = req.body.reg_pwd;
  var user_phone = req.body.reg_phone;
  var message = 'Exist';

  var check_data = null;
 
  var reg_data = [user_email, user_name,user_pwd, user_phone];

  console.log('-> Get Data = ', reg_data);
  pool.getConnection(function(err, connection){
      
    connection.query("INSERT INTO nobell.user_info(user_email, user_name, user_pwd, user_phone) values(?,?,?,?)", reg_data, function(err,data){
        if(err) {
            if(err.errno == 1062)
                res.send("Exist");
            else{
                res.send('Resiter error')
            }
        }
        else {
            res.send("RegOK");
        }
    });
  });
});


// Login
router.post('/login', function(req, res, next) {

  console.log('-> Get Email = ', req.body.login_email, '-> Get pwd = ' ,req.body.login_pwd);

  var login_email = req.body.login_email;
  var login_pwd = req.body.login_pwd;

  var login_data = [login_email, login_pwd];
  
  pool.getConnection(function(err, connection){

      connection.query("select * FROM nobell.user_info WHERE user_email=? AND user_pwd=?", login_data, function(err, data){

          if(err) {
              res.send('Error');
          }
          else {
              if(data == "") {
                  res.send('Incorrect');
              }

              else {
                  res.send("LogOK");
              }
          }
      });
  });
});





module.exports = router;
