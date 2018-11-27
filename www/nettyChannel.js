var exec = require('cordova/exec');

exports.write = function (arg0, success, error) {
    exec(success, error, 'nettyChannel', 'write', [arg0]);
};
