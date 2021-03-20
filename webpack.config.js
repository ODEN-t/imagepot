const glob = require('glob');
const path = require('path');
const srcDir = './src/main/resources/static/js';
const entries = glob
    // 検索
    .sync('**/*.js', {
        cwd: srcDir,
    })
    // mapping return [[directory/file.js, fullpath],[directory/file.js, fullpath]]
    .map(function (key) {
        return [key, path.resolve(srcDir, key)]
    });

// 配列→{key:value}の連想配列へ変換
const entryObj = Object.fromEntries(entries);

module.exports = {
    mode: 'development',
    watch: true,
    module: {
        rules: [
            {
                test: /\.css/,
                use: [
                    "style-loader",
                    {
                        loader: "css-loader",
                        options: { url: false }
                    }
                ]
            }
        ]
    },
    entry: entryObj,
    output: {
        path: path.join(__dirname, './src/main/resources/static', "dist"),
        filename: "[name]",
    },
};