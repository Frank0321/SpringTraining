# SpringTraining

## Ch1 

## Ch2
### Maria DB 補充
- 需要先安裝並啟動
- 建立 trainingdb

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
  