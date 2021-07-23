# SpringTraining

## Ch1 

## Ch2
### Maria DB 補充
- 確認是否有資料庫的存在 (trainingdb)

### GeneratedValue 用法
- @GeneratedValue(strategy = GenerationType.IDENTITY) : 自增量的意思
- @GeneratedValue(strategy = GenerationType.SEQUENCE) : 可知數據庫的序列來生成主鍵，條件是數據庫支持序列。
- @GeneratedValue(strategy = GenerationType.TABLE) : 透過另外一個表格來定義 ID
- [理解JPA注解@GeneratedValue](https://blog.csdn.net/canot/article/details/51455967)
- [JPA 中 GeneratedValue 的三種策略](https://medium.com/@BalicantaYao/jpa-%E4%B8%AD-generatedvalue-%E7%9A%84%E4%B8%89%E7%A8%AE%E7%AD%96%E7%95%A5-bedebf1c076d)
- [What use is @TestInstance annotation in JUnit 5?](https://stackoverflow.com/questions/52551718/what-use-is-testinstance-annotation-in-junit-5)

### 測試方法
- Junit 5 提供了一個 class 只初始化僅僅一次的方式 : @TestInstance(TestInstance.Lifecycle.PER_CLASS)
- 有 @TestInstance(TestInstance.Lifecycle.PER_CLASS) 才可以執行 @BeforeAll、@AfterAll

- [Spring Boot 第一個 JUnit 測試程式](https://ithelp.ithome.com.tw/articles/10250569?sc=rss.iron)

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
  

## CH 3
### log 層級
- 在 application.properties 中設定
  - logging.level.tw.com.softleader=DEBUG
- 層級主要分 :
  1. TRACE
  2. DEBUG
  3. INFO
  4. WARN
  5. ERROR
  6. FATAL  
- [Spring Boot-Log日誌管理篇](https://ithelp.ithome.com.tw/articles/10195479)    

### WebMvcConfigurer 介紹
- 是 Spring 內部的一種配置方式
- 採用 JavaBean 的形式取代傳統的 xml 配置
- addArgumentResolvers : 添加自定義方法參數處理器  
- [spring的WebMvc配置](https://zhuanlan.zhihu.com/p/146167716)

### ConversionService 類型轉換
- Object 轉換成 Object
- 進入轉換系統的入口點
- [Spring ConversionService 类型转换](https://www.cnblogs.com/binarylei/p/10263581.html)
- [聊聊Spring中的数据转换](https://blog.csdn.net/f641385712/article/details/90702928)

### annotation
- @Configuration : 為用 spring 的時候 xml 裡面的 <beans> 標籤
-[@Configuration和@Bean的用法和理解](https://blog.csdn.net/u012260707/article/details/52021265)