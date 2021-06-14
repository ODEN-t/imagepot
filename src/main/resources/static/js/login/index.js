import * as module from '../module/index';


// input=text input=password のトグル
module.inputTypeToggle('.c-buttonCTA-show');

// 実行結果メッセージの表示
module.showResultMessageModal('.c-message', '.c-message-success', '.c-message-error', true);