# MyDiaryApp-portfolio

## 創作動機
每個人或多或少都有難以說出口的心事，不妨把它寫下來。無論開心還是難過，都可以藉由瘋狂的點擊表情符號按鈕來展示情緒程度。

## 資料庫介紹
使用Firebase做資料庫，儲存所有使用者資訊

## APP技術
- 自己繪製的icon
- 等月曆從資料庫獲取資訊前，會有loading動畫
- 多運用adapter的方式，靈活顯示資料
- 將頭像裁切成圓形
- 分享截圖是整個layout而非受限手機畫面
- 利用LocalBroadcastManager＋BroadcastReceiver在不切換activity的情況下更換左側顯示之使用者資訊
- 利用GlobalScope處理異步問題

## 其他
Screenshot資料夾裡有作品部分截圖
