const passport = require('passport');
const LocalStrategy = require('passport-local').Strategy;
const kakaoStrategy = require('passport-kakao').Strategy;
const mysqlAPI = require('../routes/owner/utils/mysqlAPI');

passport.serializeUser((user, done) => {
    console.log("serialize");
    done(null, user);
});

passport.deserializeUser((user, done) => {
    console.log("deserialize");
    done(null, user);
});

passport.use(new LocalStrategy({
    usernameField: 'signin_email',
    passwordField: 'signin_pw'
},
    async function (username, password, done) {
        const loginSql = `SELECT * FROM nobell.owner_tbl WHERE owner_email="${username}"`;
        try {
            const rows = await mysqlAPI(loginSql);

            if (rows[0].owner_pw !== password) return done(null, false, { message: 'Incorrect Password' });
            else return done(null, rows[0]); // Success
        } catch (err) {
            if (err === 404) return done(null, false, { message: 'Incorrect Email' });
            else return done(err);
        }
    }
));

module.exports = passport;