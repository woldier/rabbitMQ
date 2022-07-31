# springboot rabbit的使用
## 1.导入对应依赖
```xml
  <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>
```
## 2.在application.yml配置rabbit的信息

## 3.编写配置类
### 3.1声明交换机
```java
/**
     * 交换机
     */
    @Bean("bootExchange")
    public Exchange bootExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }
```
### 3.2声明队列
```java
    @Bean("bootQueue") //Bean没有指定名字 则默认为方法名
    public Queue bootQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }
    /**
     * 绑定
     */
```
### 3.3队列与交换机绑定
```java
 @Bean
    public Binding bindQueue2Exchange(
            @Qualifier("bootExchange") Exchange bootExchange, //若容器中有多个exchange可以使用变量名代表要注入哪一个
            @Qualifier("bootQueue") Queue queue //也可以使用@Qualifier指定
    ){
        /*绑定并设置路由信息*/
        return BindingBuilder.bind(queue).to(bootExchange).with(ROUTING_KEY).noargs();
    }
```
### 4.发送消息
```java

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitTest {
    @Autowired
    RabbitService rabbitService;

    @Test
    public void test1(){
       rabbitService.exc();
    }
}

```