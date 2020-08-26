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

// SignUp Request
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

// Login Request
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
                  res.send(data[0]);
              }
          }
      });
  });
});

// Get Restaurant Data
router.get('/restaurant/:id', function(req, res, next){
  console.log('[Get Restaurant Data Request]');
  console.log('-> Get Rs_id = ', req.params.id);

  var rs_id = req.params.id;
  
  pool.getConnection(function(err, connection){

      var sqlForSelectBoard = "select * FROM nobell.restaurant WHERE rs_id=?";

      connection.query(sqlForSelectBoard, rs_id, function(err, data){

          if(err) {
              // fail to get rs data
              console.log('-> Fail To Get Data : ', err);
              res.send('fail:500');
          }
          else {
              // success to get rs data
              console.log('-> Success To Get Data', data[0]);
              res.send(data[0]);
          }

      });
  });
});

// Change Restaurant State
router.post('/change_rs', function(req, res, next){
    console.log('[Change Restaurant State Request]');
    console.log('-> Get Rs_id = ', req.body.rs_id);
    console.log('-> Get state = ', req.body.state);

    // Change
    pool.getConnection(function(err, connection){
        
        var sqlForInsertBoard = "update nobell.restaurant set rs_state=? WHERE rs_id=?";
        connection.query(sqlForInsertBoard, [req.body.state ,req.body.rs_id], function(err, rows){
            if(err) {
                console.log('-> Fail to Change : ', err);
    
                if(err.errno == 1062)
                    res.send("fail:500");
                else
                    res.send("fail:505");
            }
            else {
                console.log('-> Success to Change ! ');
                res.send("success");
            }
        });
      });
});

// Update Restaurant Data
router.post('/restaurant', function(req, res, next){
  console.log('[POST Restaurant Data Request]');
  console.log('-> Get Rs_id = ', req.body.rs_id);

  var owner_email = req.body.user_email;
  var rs_id = req.body.rs_id;
  var rs_name = req.body.name;
  var rs_phone = req.body.phone;
  var rs_address = req.body.address;
  var rs_intro = req.body.intro;
  var rs_open = req.body.open;
  var rs_close = req.body.close;

  var rs_data = [rs_name, rs_phone, rs_address, rs_intro, rs_open, rs_close, owner_email];

  if(rs_id > 0){
    
    // Update
    pool.getConnection(function(err, connection){
        rs_data.push(rs_id);
        var sqlForInsertBoard = "update nobell.restaurant set rs_name=?, rs_phone=?, rs_address=?, rs_intro=?, rs_open=?, rs_close=?, rs_owner=? WHERE rs_id=?";
        connection.query(sqlForInsertBoard, rs_data, function(err, rows){
            if(err) {
                console.log('-> Fail to Update : ', err);
    
                if(err.errno == 1062)
                    res.send("fail:500");
                else
                    res.send("fail:505");
            }
            else {
                console.log('-> Success to Update ! ');
                res.send("success");
            }
        });
      });
  } // end of Update
  else{
    
    // Register
    pool.getConnection(function(err, connection){
      
        var sqlForInsertBoard = "insert into nobell.restaurant(rs_name, rs_phone, rs_address, rs_intro, rs_open, rs_close, rs_owner) values(?,?,?,?,?,?,?)";
        connection.query(sqlForInsertBoard, rs_data, function(err, rows){
            if(err) {
                console.log('-> Fail to Insert : ', err);
    
                if(err.errno == 1062)
                    res.send("fail:500");
                else
                    res.send("fail:505");
            }
            else {
                console.log('-> Success to Register to ', rows.insertId);

                // update owner's rs_id
                var sqlForOwner = "update nobell.owner_info set owner_rs_id=? where owner_email=?";
                connection.query(sqlForOwner, [rows.insertId, owner_email], function(err2, rows2){
                    if(err) res.send("fail:500");
                    else res.send("success");
                });
            }
        });
      });
  } // end of Register
});

// Edit Owner Information Request
router.post('/edit_owner', function(req, res, next){

  console.log('[POST Edit Owner Request]');
  console.log('-> Get owner_email = ', req.body.user_email);

  var owner_email = req.body.user_email;
  var new_pwd = req.body.password;
  var new_phone = req.body.phone;

  var datas = [new_phone, new_pwd, owner_email];

  // Update
  pool.getConnection(function(err, connection){
    
    var sqlForInsertBoard = "update nobell.owner_info set owner_phone=?, owner_pwd=? WHERE owner_email=?";
    connection.query(sqlForInsertBoard, datas, function(err, rows){
        if(err) {
            console.log('-> Fail to Update : ', err);

            if(err.errno == 1062)
                res.send("fail:500");
            else
                res.send("fail:505");
        }
        else {
            console.log('-> Success to Update ! ');
            res.send("success");
        }
    });
  });
});



module.exports = router;