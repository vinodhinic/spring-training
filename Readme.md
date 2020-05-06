# Spring Training
This is recipe based training on Spring framework - which is VAST. We won't be covering them all but enough to get you 
started. 

Every recipe has a focus point that trains you to analyze the template when you work on a specific problem statement at Spring framework level - just like how you instinctively exclaim "It's a sliding window problem!" or "It is a DP problem!"

Follow these recipes in order. Each recipe has solutions committed in this repo under a particular module. The name of this module is at **Modules** section in each recipe. Needless to say, you should also know how to write tests for each recipe.

## Prerequisite
Basic knowledge on Java is must. With each recipe, I have also added focus point on some java features for beginners. If you already know a little
of spring and java, you can easily finish 10 recipes in just hours. Don't let the number scare you :) 

## Setup
* IDE - We prefer that you work on Intellij
* Gradle
* Java 8 or above
* Spring 4 or above
* Postgres DB

<details>
<summary>Postgres windows installation</summary>

* initdb
```
C:\Users\vino\Downloads\pgsql\bin>initdb -D "C:\Users\vino\Downloads\pgsql\datadir"
The files belonging to this database system will be owned by user "vino".
This user must also own the server process.

The database cluster will be initialized with locale "English_United States.1252".
The default database encoding has accordingly been set to "WIN1252".
The default text search configuration will be set to "english".

Data page checksums are disabled.

creating directory C:/Users/vino/Downloads/pgsql/datadir ... ok
creating subdirectories ... ok
selecting dynamic shared memory implementation ... windows
selecting default max_connections ... 100
selecting default shared_buffers ... 128MB
selecting default time zone ... Asia/Calcutta
creating configuration files ... ok
running bootstrap script ... ok
performing post-bootstrap initialization ... ok
syncing data to disk ... ok

initdb: warning: enabling "trust" authentication for local connections
You can change this by editing pg_hba.conf or using the option -A, or
--auth-local and --auth-host, the next time you run initdb.

Success. You can now start the database server using:

    pg_ctl -D ^"C^:^\Users^\vino^\Downloads^\pgsql^\datadir^" -l logfile start
```
* Start postgres server	
```
C:\Users\vino\Downloads\pgsql\bin>pg_ctl -D "C:\Users\vino\Downloads\pgsql\datadir" start
```

* Use client (or use dbeaver) and give permissions to user "postgres" and assign password too.
```
C:\Users\vino\Downloads\pgsql\bin>psql -d postgres
psql (12.2)
WARNING: Console code page (437) differs from Windows code page (1252)
         8-bit characters might not work correctly. See psql reference
         page "Notes for Windows users" for details.
Type "help" for help.

postgres=# CREATE USER postgres SUPERUSER;
CREATE ROLE
postgres=# CREATE DATABASE postgres WITH OWNER postgres;
ERROR:  database "postgres" already exists
postgres=# ALTER USER postgres WITH PASSWORD 'admin';
ALTER ROLE
```
</details>

---
## Recipes

1. [Dependency Injection](#recipe1)
1. [PropertySource](#recipe2)
1. [Bean Scopes](#recipe3)
1. [Profiles](#recipe4)
1. [Java JDBC](#jdbc)
1. [Spring JDBC](#recipe5)
1. [JDBC Transaction](#jdbcTransaction)
1. [Spring Transaction Management](#recipe7)
1. [Transaction Propagation](#recipe8)
1. [Transaction Isolation](#recipe9)
1. [JPA - Mybatis](#recipe10)
1. [Spring Batch Configuration](#recipe11)
1. [Spring Scheduler](#recipe12)
1. [Spring Batch - Flows and Decider](#recipe13)
1. [Spring Batch - Chunk Processing and listeners](#recipe14)

-------------------
<a name="recipe1"></a>
### Dependency Injection

```
interface GreetingService {
    String greet();
}
```

GreetingService has 2 implementations 
- one that returns "Hello" 
- another that returns "Hola"

MessageService has GreetingService :

```
class MessageService {
  String getMessage() {
     return greetingService.greet();
  }
}
```
**Exercise** 
1. Inject "Hello" GreetingService into MessageService. Add a Junit test to assert the output for `MessageService.getMessage()`
1. Repeat the above for "Hola" GreetingService
1. Understand the buzzwords - _Dependency Injection_ and _Inversion of Control_  

You can try out xml config if you are interested but annotation based config solution is what I am looking for.

**Focus Points**
1. Understand Bill of Materials (BOM), dependencyManagement and dependencies in gradle
1. You will learn the following annotations :
```
@Component
@Primary
@ComponentScan
@Autowired
@Configuration
@Bean
@Qualifier
@ContextConfiguration
```
**Modules** 
 
 `recipe1` has 4 submodules - `recipe1a` (xml based context), `recipe1b, recipe1c, recipe1d` - annotation based

---
<a name="recipe2"></a>
### PropertySource

Extend the above recipe by reading name from property file and displaying the same at both GreetingService Implementations.

**Exercise**

**_Set 1 :_**
1. Add a property file - `app.properties` - under src/main/resources
1. This property file should have an entry `name=Robert`
1. Read this name at both greeting service implementations
1. When the property is not found, it should default to "world"
1. Assert in test cases accordingly.

**_Set 2 :_**
1. Add a property file similar `app-test.properties` - under src/test/resources. This should have `name=Michael`
1. Add another MessageServiceTest which reads property from `app-test.properties`
1. Can you see Michael is assigned as name in GreetingService? Now comment out that entry at `app-test.properties`. 
you will notice that it falls back to `Robert` defined at `app.properties` 

**Focus Points**

With this recipe, you will be introduced to 
```
@PropertySource
PropertySourcesPlaceholderConfigurer 
@Value
@TestPropertySource
```
**Module** 

recipe2

---
<a name="recipe3"></a>
### Bean Scopes

**Exercise**

**_Set 1:_**
1. Create `Product` class that only has `name` field
1. Create `ShoppingCart` class that contains `List<Product>`
1. ShoppingCart has `addProduct(Product product)`
1. When I print ShoppingCart, I should see the products I have added so far
Till here - try and understand toString(), [equals() and hashCode()](https://www.journaldev.com/21095/java-equals-hashcode) - These are just Java concepts. 

**_Set 2:_**

1. Coming to the spring recipe - Every time I request for ShoppingCart bean, I should get a new one.
1. Enhance this recipe further by adding unique Id for ShoppingCart bean.

```
ShoppingCart shoppingCart1 = context.getBean(ShoppingCart.class);
shoppingCart1.addProduct(new Product("biscuit"));
shoppingCart1.addProduct(new Product("chocolate"));

ShoppingCart shoppingCart2 = context.getBean(ShoppingCart.class);
shoppingCart2.addProduct(new Product("vegetables"));
shoppingCart2.addProduct(new Product("fruits"));

System.out.println(shoppingCart1); //should print biscuit and chocolate
System.out.println(shoppingCart2); //should print vegetables and fruits
```

**Focus points**

1. You will understand `@Scope` with this exercise. Understand default scope of a bean. Analyze the output when bean is singleton/prototype
1. `@PreDestroy, @PostConstruct` - Read about Bean life cycle

**Module**

recipe3

-------------------
<a name="recipe4"></a>
### Profiles

Enhance the shopping cart recipe to introduce profile based Discount Service

**Exercises**

**_Set 1_**
1. Introduce `price` field to `Product` class
1. Add a method called `checkout()` in `ShoppingCart`
1. Once an instance of shopping cart is checkedout, you should not add new product to that instance
1. Add a junit test case for the above

**_Set 2_**
1. Introduce `DiscountService` - an interface with method,
`Double applyDiscount(Product product);`
1. Add 3 implementations to this DiscountService
    - NullDiscountService - no discounts
    - FlashSaleDiscountService - 20% discount
    - EndOfSeasonSaleDiscountService - 50% discount
1. DiscountService should be injected to ShoppingCart based on the active profile the application is running for :
    - default - NullDiscountService
    - eos - EndOfSeasonSaleDiscountService
    - flash - FlashSaleDiscountService
1. Add test case for each profile and assert that discounts are applied correctly

**_Set 3_**
1. Understand Java lambdas and try some basic exercises - https://code-exercises.com/programming/tags/java8-lambdas-streams/ms/
1. Understand Exceptions in Java - checked and unchecked exceptions

**Focus points**
1. Asserting expected exceptions in Junit test case
1. You will be introduced to `@Profile` and `@ActiveProfiles`
1. Try and set activeProfile to `AnnotationConfigApplicationContext` in `Application.java`
Understand the basics of inheritance in Java. 

For eg : in Application.java
```
ApplicationContext applicationContext = new AnnotationConfigApplicationContext();
AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
```
both assignments are correct. So what is the difference?

**Module**

recipe4

---------------------

<a name="jdbc"></a>
### Java JDBC

**Exercises**
1. Setup postgres
1. Create a table,...wait for it....`Employee`
    - columns -> `first name, last name, id, level`
    ```
   create table employee (id int, first_name varchar(50), last_name varchar(50), level int);
    ```
1. With just Java - not springs - write an EmployeeDao that does CRUD
    ```
    create(employee)
    Employee select(id)
    List<Employee> select(level)
    updateLevel(id, level)
    deleteById(id)
    ```
JDBC basics : http://tutorials.jenkov.com/jdbc/index.html

This recipe is not on springs but in upcoming recipes you would see how much heavy
 lifting Spring's data access does for you.

-------------------------

<a name="recipe5"></a>
### Spring JDBC Template

Repeat the previous recipe - which was in plain Java - using Spring now.

**Exercise**

**_Set 1:_**
1. Read the database properties such as url, password, etc from `application.properties` - We have already done this in recipe2 using property source.
1. You will be introduced to `JdbcTemplate`. Datasource can be `DriverManagerDataSource` for now.
1. Add a primary constraint on id column. Try inserting the same Employee record multiple times.
1. Understand why postgres is added as a Runtime dependency in gradle.

**_Set 2:_**
1. Enhance further directly extending EmployeeDaoImpl with `JdbcDaoSupport` 
1. Enhance further by extending EmployeeDaoImpl with `NamedParameterJdbcDaoSupport` and try out the following :
    - MapSqlParameterSource
    - BeanPropertySqlParameterSource
1. If you look at the update() method of JdbcTemplate, there are so many flavors of it
    `org.springframework.jdbc.core.JdbcTemplate.update()`
    This is called method overloading.
1. Understand overloading and overriding in Java
1. Finally, try `batchUpdate()`. You should generate random employee objects and batch insert and batch delete them.
    ```
    boolean create(List<Employee> employees);
    boolean delete(List<Employee> employees);
    ```
    Use this library for random object creation :  `org.jeasy:easy-random-core:4.0.0`
    Of course in production you would never generate random Employee objects. But this library is extensively used in test cases to mock data.
1. Go over the coding guidelines related to Java 8 [here](http://wiki.ia55.net/display/TECHDOCS/Pricing+Code+Review+Checklist#PricingCodeReviewChecklist-CodingguidelinesrelatedtoJava8). Especially dos and donts of stream()

<a name="set3_recipe5"></a>
**_Set 3:_**
1. Add a test case for the EmployeeDao
1. Each test should clear the test data after it is done running. No, do not call deleteAllEmployees in afterTest. Approach using transaction rollbacks
1. You will be using :
    ```
    @Transactional
    TransactionalManager
    @TransactionalConfiguration -> understand defaultRollback here
    @Import
    ```
1. I have committed a failing test case `EmployeeDaoFailingTest1` : Understand why it does not fail the first time and fails only when the tests are repeated. Remove primary key constraint, if you have any, before running this test
1. What would you do if you have to connect to another test db for test cases? You should already know the answer for this. If not revise PropertySource recipe.
1. Don't worry if you don't understand much about transactions here. We will have multiple recipes on Transaction Management. This recipe is to give you an idea about `TestExecutionListener` -> one such listener is
 `TransactionalTestExecutionListener` which automatically rollbacks the transaction when you annotate Test class with `@Transactional`. It is all made available by one magic annotation `@RunWith(SpringJUnit4ClassRunner.class)` which is a Junit extension for Springs.

**Focus Points**

1. JdbcTemplate
1. JdbcDaoSupport
1. NamedParameterJdbcDaoSupport
1. `@Import`
1. `@Transactional`
1. `TestExecutionListener`
1. `@TransactionalConfiguration`
1. `TransactionalManager`

**Module**

recipe5

-----------------------

<a name="jdbcTransaction"></a>
### JDBC Transaction

We are going to extend the [Java JDBC](#jdbc) recipe. 

**Exercise**
1. Create the following tables in Postgres
    ```
    CREATE TABLE book (
        isbn         VARCHAR(50)    NOT NULL,
        book_name    VARCHAR(100)   NOT NULL,
        price        INT,
        PRIMARY KEY (isbn)
    );
    
    CREATE TABLE book_stock (
        isbn     VARCHAR(50)    NOT NULL,
        stock    INT            NOT NULL,
        PRIMARY KEY (isbn),
        CONSTRAINT positive_stock CHECK (stock >= 0)
    );
    
    CREATE TABLE amazon_pay (
        username    VARCHAR(50)    NOT NULL,
        balance     INT            NOT NULL,
        PRIMARY KEY (username),
        CONSTRAINT positive_balance CHECK (balance >= 0)
    );
    ```
1. Add an interface `KindleStore`
    ```
    public interface KindleStore {
        void purchase(String isbn, String username);
    }
    ```
1. While purchasing, we need to reduce the `stock` at `book_stock` and reduce the `balance` at `amazon_pay`
1. Simulate data such that either book stock is not available or there is not enough balance in amazonPay
1. How do you ensure book stock is restored to its old value when there is not enough balance in amazonPay? Transactions
1. Add transaction support to purchase() only using Java JDBC.

**Focus Point**

By default, autocommit is turned on to commit each SQL statement immediately after its execution. 
To enable transaction management, you must turn off this default behavior and commit the connection only when all the SQL statements 
have been executed successfully. If any of the statements go wrong, you must roll back all changes made by this connection.

**Module**

recipe6

------------
<a name="recipe7"></a>
### Spring Transaction Management

You would have guessed by now. If not, take the previous recipe and use spring transaction to achieve it.

**Focus points**
1. There are 3 ways to achieve this.
    - PlatformTransactionManager API
    - TransactionTemplate
    - @Transactional annotations
1. I have all these approaches injected at different profiles. If you want to revisit profiles, check [recipe4](#recipe4)
1. Bonus : Understand default methods in Java interfaces. Do you know interfaces can also have fields?
1. Revisit [Spring JDBC Template test case](set3_recipe5) you have written at recipe5.
1. If you are light-hearted just stick to testing your setup at a main method. Refer `com.kindle.AmazonApp` under `src/main/java/`
1. But for the curious brave hearted fellas, check out the `KindleStoreTest`. Don't worry if you do not understand much of it, 
as we have more and more recipes on Transaction Management, things will get clearer.

**Module**

recipe7

----------

<a name="recipe8"></a>
### Transaction Propagation

1. Enhance the previous recipe by adding a KindleCart that can checkout multiple books for an account
    ```
    public void checkout(List<String> isbns, String username) {
        for(String isbn : isbns) {
            kindleStore.purchase(isbn, username);
        }
    }
    ```
1. Implement 2 behaviours of this Cart
    * One that allows partial checkout - i.e. If the account balance for the user is only sufficient for few books in the cart, 
    check out only those
    * Another that does not checkout any books if the account balance for the user is not sufficient for all of them
1. Do not change the KindleStore implementations. i.e. the purchase logic remains the same. Try and approach this problem using Transaction Propagation.

**Focus Points**
1. When a transactional method is called by another method, it is necessary to specify how the transaction should be propagated. For example, the method may continue to run within the existing transaction, or it may start a new transaction and run within its
  own transaction. This propagation behavior is defined by `org.springframework.transaction.annotation.Propagation`
1. Understand all the propagation types and find the suitable one for this use case.
1. Check out the diagrams at Spring doc for [Propagation.REQUIRED](https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/images/tx_prop_required.png)
 and [Propagation.REQUIRES_NEW](https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/images/tx_prop_requires_new.png)
1. Bonus : Refer to this [usecase](http://wiki.ia55.net/pages/viewpage.action?spaceKey=arctechx&postingDay=2017%2F7%2F12&title=Integration+tests+involving+Moss+StageUtils)

**Module**

recipe 8

-------------------
<a name="recipe9"></a>
### Transaction Isolation

**Exercises**
1. Introduce 2 new transactional methods to `KindleStore`
    ```
    public void increaseStock(String isbn, int stock);
    public int checkStock(String isbn);
   ```
1. Simulate the following :
    * increaseStock() - increases stock by 5, sleeps for 10 seconds, rollsback the transaction
    * checkStock() - reads the stock, sleeps for 10 seconds, reads it again
    * create 2 threads - one that calls increaseStock() and another calls checkStock()
    * increaseStockThread should be started first. 5 seconds later trigger checkStockThread
    
    We are trying to read the stock value (checkStock), when it is getting updated at another transaction (increaseStock).
    checkStock should not read the updated value unless it is committed. What isolation level will you use?
    
1. Same scenario as above but increaseStock() commits the transaction. Observe what values checkStock() print before and after its sleep.
 
1. Simulate the following 
    * increaseStock() - increases stock by 5, sleeps for 2 seconds, commits the transaction
    * checkStock() - reads the stock, sleeps for 15 seconds, reads it again
    * create 2 threads - one that calls increaseStock() and another calls checkStock()
    * checkStockThread should be started first. 5 seconds later trigger increaseStockThread
    
    Between 2 reads within the same transaction at checkStock, the stock is updated at other transaction. i.e. 
    checkStock has read the stock, say 10. Meanwhile increaseStock is updating the stock to 15 in another transaction and also 
    successfully commits the transaction as well. Now the same checkStock reads the stock again. Instead of reading the updated value 15, 
    I want checkStock to read the same value it read initially, which is 10. What isolation level would you use here?

**Focus Points**
1. You will be introduced to Threads in Java
1. Understand the problems caused by concurrent transactions - dirty reads, nonrepeatable reads, phantom reads, lost updates 
1. Understand the isolation levels in Transactions - read committed, read uncommitted, repeatable read, serializable
1. Understand the scenario by running `ReadCommittedApp` and `RepeatableReadApp`
1. Rerun `RepeatableApp` with Isolation set to READ_COMMITTED and observe what changed

**Module**

recipe9

----------------- 
<a name="recipe10"></a>
### JPA - Mybatis

Rewrite EmployeeDAO at [JDBC recipe](#jdbc) using spring-mybatis

**Focus Points**
1. You will be introduced to `SqlSessionFactory` and `SqlSession` in mybatis
1. You can add mappers via annotations or xml. Stick to xml mappers.
1. Understand mapper namespace. Try and log the statements executed by mybatis. Refer 
[this](http://wiki.ia55.net/display/arctechx/2017/10/10/Log+SQL+query+used+in+MyBatis)
         
**Module**

recipe10

---------------------- 

<a name="recipe11"></a>
### Spring Batch Configuration

**Exercise**

**__Set : 1__**
1. Add spring batch dependency (update bom to the latest version)
1. Configure Spring batch - Refer `@EnableBatchProcessing` and initialize database with batch tables (hint : `DataSourceInitializer`)
1. Create a job called `simple-job` which has a single step `simple-step`
1. Step should be a `Tasklet` which just logs (not sysout. use Logger) - jobName, stepName, stepExecutionId and jobExecutionId
1. main() method should launch this job. Use `JobLauncher`
1. If you got it working till this far, try running the main() method again. You should not be able to run the job again. Understand why.

**__Set : 2__**
1. Understand the [domain language of batch](https://docs.spring.io/spring-batch/docs/current/reference/html/domain.html#domainLanguageOfBatch) - for now understand the correlation between `Job, JobInstance, JobExecution`
1. Look into the batch tables. Understand how they have been updated for the run at set 1.
1. Understand what `@EnableBatchProcessing` does for you. It abstracts - `JobLauncher, JobRepositoryFactoryBean, JobRegistryBeanPostProcessor, JobRegistry`. 
Understand what each of these components are for.

**__Set : 3__**
1. Add a jobParameter to the job you are launching at main().
1. Make sure this parameter is unique across runs (hint: currentTimeInMillis is a good thing to ensure uniqueness across runs)
1. Log this jobParameter as well, at Tasklet.
1. Try running main() twice. It should work now

**__Set : 4__**
1. Modify the Tasklet to run twice with the help of an instance variable `count`. Hint : Pay attention to the return type `RepeatStatus`
1. Add this count to step execution context
1. at main() launch the job twice. i.e we are launching the job twice within the same run.
1. Why does the second job has tasklet executed only once? Hint : `@StepScope`

**__Set : 5__**
1. Add a asynchronous task executor for Job Launcher. By default JobLauncher does not come with task executor. How will you customize that? Hint : extending `DefaultBatchConfigurer` 
1. Observe the logs from Tasklet. you should see the thread name

```
[2020-04-07 03:39:36,139 [SimpleAsyncTaskExecutor-2] c.b.SimpleTasklet.execute():22  INFO ]: Executing step : simple-step for job : simple-job with stepExecutionId: 13,  jobExecutionId : 13 & JobParameter : {currentTime=1586210969653}. Count : 1
```

**Focus Points**
1. Domain language of spring batch
1. Being able to query various batch tables and understand a job instance, job execution, step execution, how to find the execution status,
job parameter, etc.
1. Understand the components of spring batch : 
    ```
    @EnableBatchProcessing
    DefaultBatchConfigurer
   JobLauncher
   JobParameter
   Job
   Step
   Tasklet
   @StepScope
   RepeatStatus.CONTINUE/FINISHED
   TaskExecutor
    ``` 

**Module**

spring-batch-recipes:recipe11

-----------------------

<a name="recipe12"></a>
### Spring Scheduler
**Exercise**

1. Extend the above recipe to run the job every 2 seconds - Use Spring scheduler for this.
    ```
   @EnableScheduling
   @Scheduled
   ```
1. Use a `Job Operator` to launch job. jobOperator should just invoke `startNextInstance(job)` in the scheduler.
1. Job should be aware of what an `instance` is. Introduce an incrementer to the Job - `RunIdIncrementer` should do.
1. Extend above by using your own incrementer - that just adds `System.currentTimeMillis` as job parameter.
1. Understand the difference between fixedDelay and fixedRate. Analyse the following snippet in case of fixedRate and fixedDelay
    ```
       AtomicInteger count = new AtomicInteger(0);
       @Scheduled(fixedDelay = 2_000L)
       public void run() throws InterruptedException {
           int runId = count.getAndIncrement();
           System.out.println("Starting "+runId);
           if(runId < 2) {
               Thread.sleep(10_000L);
           }
           System.out.println("Ending : "+runId);
       }
    ```

**Focus Points**
1. Spring scheduler 
   ```
   @EnableScheduling
   @Scheduled
   ```
1. JobOperator
1. JobParameterIncrementer
1. Difference b/w fixedDelay and fixedRate

**Module**

spring-batch-recipes:recipe12

-----------------------

<a name="recipe13"></a>
### Spring Batch - Flows and Decider

**Exercise**
**_Set : 1_**
1. Create a step called `odd-step` that just prints odd
1. Create a step called `even-step` that just prints even
1. Create a job with `RunIdIncrementer` that executes `odd-step` if the `run.id` is odd and executes 'even-step' if the `run.id` is even
1. Invoke this job using `JobOperator` at main() atleast twice
1. Study the table `BATCH_STEP_EXECUTION` for the execution Ids

**_Set : 2_**
1. Create a step called `flaky-step` that throws `RuntimeException` randomly.
1. Create a step called `backup-step` that just prints 'backup'
1. Create a job that calls `flaky-step` -> if it succeeds, the job ends. if the flaky step failed, then `backup-step` should kick in
1. Invoke this job using `JobOperator` at main() for cases where flaky-step succeeds and fails
1. Study the table `BATCH_STEP_EXECUTION` for the execution Ids

**Focus Points**
1. Understand [Flows](https://docs.spring.io/spring-batch/docs/current/reference/html/step.html#controllingStepFlow)
1. Set 1 will require you to understand [JobExecutionDecider](https://docs.spring.io/spring-batch/docs/current/reference/html/step.html#programmaticFlowDecisions)
1. Set 2 will require you to understand [Conditional Flow](https://docs.spring.io/spring-batch/docs/current/reference/html/step.html#conditionalFlow)

**Modules**

spring-batch-recipes:recipe13

-----------------------

<a name="recipe14"></a>
### Spring Batch - Chunk Processing and listeners

**Exercise**

This recipe is not strictly a hands-on. It serves as a reference for you to run the job and understand how chunk processing in spring batch works.

**_Set : 1_**
1. Create class `ComicCharacter`. Copy it from `spring-batch-recipes:recipe14`
1. Create a reader that iterators over `DataUtil.villainCharacters` if jobParameter 'isVillain' is set to true and
 over `DataUtil.heroCharacters` otherwise. `DataUtil` class is in `spring-batch-recipes:recipe14`
    1. Remember `@StepScope`
    1. How to acquire job parameter within a step scope? Hint : `@BeforeStep`
    1. Use `ItemReader`
1. Create processor that converts `ComicCharacter` to `String` which is just the name of the character in uppercase
    1. These processors should also filter by universe they are initialized with
    1. Use `ItemProcessor`
1. Create a writer that just writes the string to console. Use `ItemWriter`
1. Combine the reader-processor-writer for step `guardian-step` which in turn is used by `guardian-job`
1. Repeat the same for step `avenger-step` which in turn is used by `avenger-job`
    ```
    @Bean
    public Step avengerStep() {
    return stepBuilderFactory.get("avenger-step")
           .<ComicCharacter, String>chunk(3)
           .reader(comicCharacterReader)
           .processor(avengerComicCharacterProcessor())
           .writer(consoleWriter())
           .build();
    }
   	
    @Bean
    public Job avengerJob() {
    return jobBuilderFactory.get("avenger-job")
           .start(avengerStep())
           .build();
   }
   ```
1. Understand the advantage of processing in chunks. Refer [this](https://docs.spring.io/spring-batch/docs/current/reference/html/step.html#chunkOrientedProcessing)
1. Did you notice that at read() and process() is on single item and write() is for list of items?
    ItemReader : `ComicCharacter read()`
    
    ItemProcessor:  `String process(ComicCharacter item)`
    
    ItemWriter : `void write(List<String> items)` 

    If you have chunk size 100, items are read and processed one by one but all 100 items are written at once.
    
**_Set : 2_**
1. Add `StepExecutionListener` with log statements in beforeStep() and afterStep()
1. Similarly add `ChunkListener` and `JobExecutionListener` as well

**_Set : 3_**
1. Use `JobOperator` to start a job with parameter `isVillain=true`
1. You would have to add `ParameterConverter` to JobOperator. Refer `JobParametersConverter`
1. Understand how the listeners work.

**_Set : 4_**
1. Add Test case to verify if `avenger-step` with parameter `isVillain=true` is giving only villain names 
1. Another test with parameter isVillain=false
1. Use `JobLauncherTestUtils`
1. Enhance [recipe13](#recipe13) by adding test for set 1 and set 2

**Focus Point**
1. Chunk processing - reader, processor, writer
1. Chunk, step execution and job execution listeners
1. ParameterConverter
1. @BeforeStep
1. JobLauncherTestUtils

**Module**

spring-batch-recipes:recipe14

----------------------------