// External Modules
const express = require('express');
const session = require('express-session');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const os = require('os');
const passport = require('./passport');

// Internal Modules

// Internal Routers
const customerRouter = require('./routes/customer');
const ownerRouter = require('./routes/owner');

////////////////////////////////////////////////////////

const mySignature = "12345";

// Set Middleware
const app = express();
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser(mySignature));
app.use(session({
  resave: false,
  saveUninitialized: true,
  secret: mySignature,
  cookie: {
    httpOnly: true,
    secure: false,
  }
}));
app.use(passport.initialize());
app.use(passport.session());

// Custom Middleware
app.use('*', (req, res, next) => {
  console.log('');
  console.log(`=> [Method : ${req.method}] [URL : ${req.originalUrl}]`);
  console.log('=> SESSION :', req.session);
  console.log('=> RECV DATA :', req.body); 
  next();
});

app.use('/user', customerRouter);
app.use('/owner', ownerRouter);

app.use((err, req, res, next) => {
  console.error(err);
  res.send('ERROR');
});

// Create HTTP Server Object
const server = require('http').Server(app);
const port = process.env.PORT || 3000;

// Start Listening
server.listen(port, err => {
  if (err) console.log('ERROR : ', err);
  else {
    console.log('IP :', os.networkInterfaces()['Wi-Fi'][1].cidr);
    console.log('----------------------------------------------------')
    console.log(`    [ START SERVER ]   NOBELL   [ PORT = ${port} ]`);
    console.log('----------------------------------------------------')
  }
  console.log('')
});