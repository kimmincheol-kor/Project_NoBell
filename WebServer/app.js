// External Modules
var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

// Internal Modules
// ...

// Internal Routers
var indexRouter = require('./routes/index');
var userRouter = require('./routes/user');
var ownerRouter = require('./routes/owner/index');

// Main App
var app = express();
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// Routings
app.use('/', indexRouter);
app.use('/user', userRouter);
app.use('/owner', ownerRouter);

// Create HTTP Server Object
const server = require('http').Server(app);
const port = process.env.PORT || 3000;

// Start Listening
server.listen(port, err => {
    if(err) console.log('ERROR : ', err);
    else    console.log(`*** [ START NOBELL SERVER ] [ port = ${port} ] ***`);
});