/*var path = require('path');

module.exports = {
    entry: './src/main/js/app.js',
    devtool: 'sourcemaps',
    cache: false,
    debug: true,
    output: {
        path: __dirname,
        filename: 'src/main/resources/static/built/bundle.js'
    },
    module: {
        loaders: [
            {test: path.join(__dirname, 'src/main/js'), loader: 'babel-loader'},
        ]
    }
};*/
const path = require('path');

module.exports = {
    entry: "./src/main/js/app.js",
    cache: true,
    mode: 'development',
    output: {
        path:__dirname,
        filename: "'src/main/resources/static/built/bundle.js",
        publicPath: '/'
    },
    resolve: {
        extensions: ['.js', '.jsx']
      },
    devServer: {
        inline: false,
        contentBase: "./dist",
    },
    module: {
        rules: [
            {
                test: /\.jsx?$/,
                exclude:/(node_modules|bower_components)/,
                loader: 'babel-loader',
                query: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                }
            }
        ]
    }

};