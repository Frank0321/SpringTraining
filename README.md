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
- 是 Spring 內部的一種配置方式 (或是說對於 SpringMVC 進行配置)
- 作法 : 
  1. 配置 @Configuration
  2. 實作 WebMvcConfigurer
- 採用 JavaBean 的形式取代傳統的 xml 配置
- addArgumentResolvers : 添加自定義方法參數處理器 (Add resolvers to support custom controller method argument types.)
- [spring的WebMvc配置](https://zhuanlan.zhihu.com/p/146167716)
- [使用WebMvcConfigurer配置SpringMVC](https://blog.csdn.net/qq877728715/article/details/110678656)

### ConversionService 類型轉換
- Object 轉換成 Object
- 進入轉換系統的入口點
- [Spring ConversionService 类型转换](https://www.cnblogs.com/binarylei/p/10263581.html)
- [聊聊Spring中的数据转换](https://blog.csdn.net/f641385712/article/details/90702928)

### @Spec annotation
#### Enabling spec annotations in your Spring app
- All you need to do is to wire SpecificationArgumentResolver into your application. 
  Then you can use @Spec and other annotations in your controllers. 
  SpecificationArgumentResolver implements Spring's HandlerMethodArgumentResolver and can be plugged in as follows:
  ```java
  @Configuration
  @EnableJpaRepositories
  public class MyConfig implements WebMvcConfigurer {
  
      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
          argumentResolvers.add(new SpecificationArgumentResolver());
      }
      ...
  }
  ```
- 用法 : 決定欄位耀怎麼比較 (Like、Equal、In 等)
  
- [ref](https://github.com/tkaczmarzyk/specification-arg-resolver)


### 補充 3.3 內容
- 新增資料方便進行測試
  ```java
    @PostConstruct
    void init(){
        Policy policy1 = Policy.builder().policyNo("AAA").applicantLocalName("aaa").build();
        policyRepository.save(policy1);

        Policy policy2 = Policy.builder().policyNo("AAA").applicantLocalName("bbb").build();
        policyRepository.save(policy2);

        Policy policy3 = Policy.builder().policyNo("BBB").applicantLocalName("aaacc").build();
        policyRepository.save(policy3);

        Policy policy4 = Policy.builder().policyNo("CCC").applicantLocalName("ccc").build();
        policyRepository.save(policy4);
    }
  
- 說明 : 
  - test0 : 參數皆需要有輸入，才可以做查詢。 (沒有彈性空間)
  - test1 : 否則需要使用到 if 判斷句，判斷是否有值，再決定使用哪一個方法查詢
  - test2 : 使用 @Spec 的方式
    - 參數可以動態新增，如果有此參數，就會用這個參數查詢。如果沒有，則不會用使用這個參數查詢
   
  ```
  - 利用網址進行測試，分別在 @GetMapping 加上 test1，test2，然後利用網址進行測試
    - http://localhost:8081/policy/test1?policyNo=AAA&applicantLocalName=aaa
    - http://localhost:8081/policy/test1?policyNo=AAA
    - http://localhost:8081/policy/test1?applicantLocalName=aaa
    - http://localhost:8081/policy/test1

- JpaSpecificationExecutor<T>
  - 使用 Specification
    ```java
    Specification<Policy> newSpec = 
        (root, criteriaQuery, criteriaBuilder)->{
        ...
        };
    ```
    - root：Root接口，代表查詢的根對象，可以通過root獲取實體中的屬性
    - query：代表一個頂層查詢對象，用來自定義查詢 
    - cb ：用來構建查詢，此對象里有很多條件方法
  - [ref1](https://kknews.cc/zh-tw/code/oeo6pp6.html)
  - [ref2](https://zh.codeprj.com/blog/b1ea451.html)
  - 程式對應的 SQL 如下
  ```sql
  select 
  policy0_.id as id1_0_, 
  policy0_.applicant_idno as applican2_0_, 
  policy0_.applicant_local_name as applican3_0_, 
  policy0_.endst_no as endst_no4_0_, 
  policy0_.policy_no as policy_n5_0_ 
  from 
  policy policy0_ 
  where 
  policy0_.policy_no=? 
  and (policy0_.applicant_local_name like ?) 
  and policy0_.endst_no=
      (select max(policy1_.endst_no) 
      from 
      policy policy1_ 
      where 
      policy0_.policy_no=policy1_.policy_no)
  ```
### 3.4 存取多資料庫
- 注意檔案目錄位置
- gary 有在 integration 這一個 package 裡面多創一個 entity
- [菜鳥工程師](https://matthung0807.blogspot.com/2019/09/spring-data-jpa-multiple-datasource.html)


### annotation
- @Configuration : 為用 spring 的時候 xml 裡面的 <beans> 標籤
-[@Configuration和@Bean的用法和理解](https://blog.csdn.net/u012260707/article/details/52021265)