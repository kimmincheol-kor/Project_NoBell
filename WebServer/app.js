// External Modules
const express = require('express');
const session = require('express-session');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const os = require('os');

// Internal Modules
// ...

// Internal Routers
const customerRouter = require('./routes/customer');
const ownerRouter = require('./routes/owner');3

// Set Middleware
const app = express();
app.use(logger('dev'));
app.use(cookieParser('mySignature'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

// Custom Middleware
app.use('*', (req, res, next) => {
  console.log('');
  console.log(`=> [Method : ${req.method}] [URL : ${req.originalUrl}]`);
  console.log('-> RECV DATA :', req.body);
  next();
});

app.use('/customer', customerRouter);
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