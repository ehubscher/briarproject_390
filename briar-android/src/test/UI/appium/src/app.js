const wdio = require('webdriverio');
const config = require('./config');

let client = wdio.remote(config);

console.log(client.isAndroid);