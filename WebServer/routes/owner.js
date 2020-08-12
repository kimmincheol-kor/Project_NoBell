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

// Register
router.post('/register', function(req, res, next) {

  var reg_name = req.body.reg_name;
  var reg_email = req.body.reg_email;
  var reg_pwd = req.body.reg_pwd;
  var reg_phone = req.body.reg_phone;

  var check_data = [reg_email, reg_phone];
  var reg_data = [reg_name, reg_email, reg_pwd, reg_phone];

  console.log('[Register Request]');
  console.log('-> Get Data = ', reg_data);
  
  pool.getConnection(function(err, connection){
      
      var sqlForSelectBoard = "select * FROM nobell.owner WHERE owner_email=? OR owner_phone=?";
      
      connection.query(sqlForSelectBoard, check_data, function(err, data){
          
          // New Member -> Insert
          if(data == ""){
              sqlForInsertBoard = "insert into nobell.owner(owner_name, owner_email, owner_pwd, owner_phone) values(?,?,?,?)";
              connection.query(sqlForInsertBoard, reg_data, function(err, rows){
                  if(err) {
                      console.log('-> Fail to Insert : ', err);
                      res.send("fail:505");
                  }
                  else {
                      console.log('-> Success to Register ! ');
                      res.send("success");
                  }
              });
          }
          
          // Exist Member : Fail
          else{
              console.log('-> Fail to Register : Exist Member');
              res.send("fail:500");
          }

      });
  });
});





module.exports = router;
