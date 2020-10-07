// Import external Modules
var express = require('express');
var mysql = require('mysql');

// Import internal Modules
// ...

// Import Owner's Routers
const ownerRouter = require('./owner');
const restaurantRouter = require('./restaurant');
const menuRouter = require('./menu');
const tableRouter = require('./table');

// Set Connetion with MySQL
var pool = mysql.createPool({
  connectionLimit: 10,
  host: 'localhost',
  user: 'root',
  database: 'nobell',
  password: '123456'
});

var router = express.Router();

// Default for Test
router.get('/', function(req, res, next) {
    console.log('[Test Request]');
    res.send('success to connection');
});

// SignUp Request
router.post('/signup', function(req, res, next) {

  var reg_name = req.body.reg_name;
  var reg_email = req.body.reg_email;
  var reg_pwd = req.body.reg_pwd;
  var reg_phone = req.body.reg_phone;

  var check_data = [reg_email, reg_phone];
  var reg_data = [reg_email, reg_pwd, reg_name, reg_phone];

  console.log('[Register Request]');
  console.log('-> Get Data = ', reg_data);
  
  pool.getConnection(function(err, connection){
      
    var sqlForInsertBoard = "insert into nobell.owner_tbl(owner_email, owner_pwd, owner_name, owner_phone) values(?,?,?,?)";
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

// Signin Request
router.post('/signin', function(req, res, next) {
  console.log('[Login Request]');
  console.log('-> Get Email = ', req.body.login_email);

  var login_email = req.body.login_email;
  var login_pwd = req.body.login_pwd;

  var login_data = [login_email, login_pwd];
  
  pool.getConnection(function(err, connection){
      var sqlForSelectBoard = "select * FROM nobell.owner_tbl WHERE owner_email=? AND owner_pwd=?";

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
                  res.send(data[0]);
              }
          }
      });
  });
});

// Routings
router.use('/owner', ownerRouter);
router.use('/restaurant', restaurantRouter);
router.use('/menu', menuRouter);
router.use('/table', tableRouter);

module.exports = router;