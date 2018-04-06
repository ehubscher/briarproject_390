const wdio = require('webdriverio');
const config = require('./config');

let client = wdio.remote(config);

client.init()
.click('~edit_password')
.end();