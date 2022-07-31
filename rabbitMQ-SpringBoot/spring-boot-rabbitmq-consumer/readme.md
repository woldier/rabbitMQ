# springboot rabbit的使用(consumer)
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
### 注册监听器
```java
/**
 * 消费者的配置类
 */
@Configuration
public class ConsumerConfig {
    /**
     * 监听器配置
     * @param message
     */
    @RabbitListener(queues = "boot-topic-queue") // queues参数设置队列名称
    public void Listen(Message message){
        System.out.println(message.toString());
    }
}

```

#### 启动应用即可
