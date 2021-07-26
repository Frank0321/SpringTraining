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
- [@Configuration和@Bean的用法和理解](https://blog.csdn.net/u012260707/article/details/52021265)
  
### bug 處理
- IntelliJ Idea 解決 Could not autowire. No beans of 'xxxx' type found 的錯誤提示
  - 先確定是否能正常執行，可以就可以不用管他了 ?!
    - 加上註解 : @Autowired(required = false)
    - 交給 Spring 管理 : 標註 @Service
    - 調整 interlliJ 的危險等級
  - [解決方法 1](https://www.itread01.com/content/1547060884.html)
  - [解決方法 2](https://blog.csdn.net/Hello_World_QWP/article/details/114923905)
  - [@Autowired 其他參數設定](http://blog.appx.tw/2017/08/21/spring-%E8%A8%BB%E8%A7%A3-%E4%B9%8B-autowired/)

# CH4 
### interlliJ 建立資料庫
- 以 local 端為例，僅需要輸入帳密，並測試連線
- 在資料庫右鍵 -> Database Tools -> Manage Shown Schemas ，點選要顯示的 DB

### 4.1 
- 在 policy 內新增欄位
- 將 policy 內中的Insureds欄位前的 @JoinColumn(name = "POLICY_ID")拿掉，可以發現會多一個 Insured Entity 的產生
  - 試玩後，記得先把 table drop 掉，以免會有其他問題發生
- @OneToMany 的參數
  - cascade : 聯級操作
    - CascadeType.PERSIST : 在儲存時一併儲存 被參考的物件。
    - CascadeType.MERGE : 在合併修改時一併 合併修改被參考的物件。
    - CascadeType.REMOVE : 在移除時一併移除 被參考的物件。
    - CascadeType.REFRESH : 在更新時一併更新 被參考的物件。
    - CascadeType.ALL : 無論儲存、合併、 更新或移除，一併對被參考物件作出對應動作。
 - fetch :
   - FetchType.LAZY : 只在用到時才載入關聯的物件。
     在查詢時，不會立刻將物件(insureds)載入
   - FetchType.EAGER : 在查詢時立刻載入關聯的物件
   - 如果設定成 FetchType.EAGER 則可以不用 @Transactional 但不會 Rolled back
 - orphanRemoval
   - 預設值為 false
   - orphanRemoval=true : 當物件 (insured) 從 Set 裡面移除，資料庫就會移除該資料

- [CascadeType 與 FetchType](https://openhome.cc/Gossip/EJB3Gossip/CascadeTypeFetchType.html)  
- [cascade設為CascadeType.REFRESH的作用](https://matthung0807.blogspot.com/2019/05/hibernate-jpa-onetomany.html)
- [參數FetchType.LAZY和FetchType.EAGER的差別](https://matthung0807.blogspot.com/2018/06/jpa-onetomanyfetchtypelazyfetchtypeeager.html)
- [@OneToMany的orphanRemoval屬性](https://matthung0807.blogspot.com/2018/06/jpa-onetoone-onetomanyorphanremoval.html)
  
### 4.2
- @Transactional
  - 沒有加的時候，無法找到下一層的資料
  - Began transaction (1)
  -  Rolled back transaction for test (回歸到測試前的資料，可以新增一筆資料測試)
  
  - [Spring @Transactional註解淺談](https://iter01.com/61414.html)
  
### 4.3 
- n+1 selection : A 底下有 B，查詢 A 之後，要顯示 B，則也會查詢 B (多查詢一次)
- 範例程式 : 將 Policy 這一個 Entity 中的 @OneToMany 裡面的 fetch 進行修改
  - fetch = FetchType.EAGER : 查詢時就會找關聯的資料，因此就會找下一層的資料 => n+1 selection
  - fetch = FetchType.LAZY : 需要用到的時候才會找，因此沒有要用到下一層的資料就沒有要尋找 => 沒有 n+1 selection 的問題
  
- 改善 N +1 Selection
  - policy :OneToMany : fetch = FetchType.LAZY
  - 在 policy 的 class 加上 @NamedEntityGraph(name = "policy.insureds", attributeNodes = @NamedAttributeNode("insureds"))
  - 在會使用到的 DAO 加上 :  @EntityGraph(value = "policy.insureds", type = EntityGraph.EntityGraphType.LOAD)
  - 可以先把這兩個註解拿掉還原  
  - [參考](https://www.baeldung.com/spring-data-jpa-named-entity-graphs)
 
### 4.4
- Policy -> insured -> Item ，至少中間的 insured 不能使用 @EqualsAndHashCode ，否則會發生 
  - org.springframework.orm.jpa.JpaSystemException: collection was evicted; nested exception is org.hibernate.HibernateException: collection was evicted
    - 解法 1 : Inusred 那一層只使用 @Getter、@Setter
    - 解法 2 : Inusred 那一層使用 @Data，並加上 @EqualsAndHashCode(exclude = {"items", "payment"})
  
  - [Hibernate OneToMany java.lang.StackOverflowError](https://stackoverflow.com/questions/17445657/hibernate-onetomany-java-lang-stackoverflowerror)
  - [使用Hibernate、JPA、Lombok遇到的有趣問題](https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/745475/)
  - [Lombok & Hibernate: How to Avoid Common Pitfalls](https://thorben-janssen.com/lombok-hibernate-how-to-avoid-common-pitfalls/)
  
 