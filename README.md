# SpringTraining

## Ch1 

## Ch2
### Maria DB 補充
- 需要先安裝並啟動
- 建立 trainingdb

### GeneratedValue 用法
- @GeneratedValue(strategy = GenerationType.IDENTITY) : 自增量的意思
- @GeneratedValue(strategy = GenerationType.SEQUENCE) : 可知數據庫的序列來生成主鍵，條件是數據庫支持序列。
- @GeneratedValue(strategy = GenerationType.TABLE) : 透過另外一個表格來定義 ID
- [理解JPA注解@GeneratedValue](https://blog.csdn.net/canot/article/details/51455967)
- [JPA 中 GeneratedValue 的三種策略](https://medium.com/@BalicantaYao/jpa-%E4%B8%AD-generatedvalue-%E7%9A%84%E4%B8%89%E7%A8%AE%E7%AD%96%E7%95%A5-bedebf1c076d)

### 問題排除
- Q1 : Test ignored.
  - Error creating bean with name 'policyRepository'
  1. 檢查名稱有沒有打錯
  2. 中文問題
     - 看 log ，有跳出 java.sql.SQLException: Incorrect string value: '\xE0\xA4\xA7\xE0\xA4\xBE...
     - 在 Maria db (MySQL) 輸入
    ```xml
    ALTER DATABASE database_name CHARACTER SET utf8 COLLATE utf8_general_ci;
     ```
    - [error 原因](https://stackoverflow.com/questions/44588055/spring-boot-cant-save-unicode-string-in-mysql-using-spring-data-jpa)
    - [參考](https://www.jinnsblog.com/2018/01/mariadb-garbled.html)
  