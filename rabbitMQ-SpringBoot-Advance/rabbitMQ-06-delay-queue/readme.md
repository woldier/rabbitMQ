延迟队列

很可惜，在RabbitMQ中并未提供延迟队列功能。 \
但是可以使用：TTL+死信队列 组合实现延迟队列的效果。

![img.png](img.png)

1. 延迟队列 指消息进入队列后，可以被延迟一定时间，再进行消费。
2. RabbitMQ没有提供延迟队列功能，但是可以使用 ： TTL + DLX 来实现延迟队列效果。

`其实现就是与dle中第三种进入死信交换机的方式 ,这里不在赘述`