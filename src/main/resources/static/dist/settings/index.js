/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
/******/ (function() { // webpackBootstrap
/*!********************************************************!*\
  !*** ./src/main/resources/static/js/settings/index.js ***!
  \********************************************************/
eval("console.log('from settings');\r\n\r\n// type=password <=> type=text の切り替え\r\nconst inputTypeToggle = (elementClass) => {\r\n    const nodeList = document.querySelectorAll(elementClass);\r\n    for (const node of nodeList) {\r\n        node.addEventListener('click', () => {\r\n            const target = node.previousElementSibling;\r\n            target.type == 'password' ? target.type = 'text' : target.type = 'password';\r\n        })\r\n    }\r\n}\r\ninputTypeToggle('.buttonCTA-show');\n\n//# sourceURL=webpack://imagepot/./src/main/resources/static/js/settings/index.js?");
/******/ })()
;