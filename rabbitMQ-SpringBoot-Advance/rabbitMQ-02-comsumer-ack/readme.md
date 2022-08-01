Consumer Ack

ack指Acknowledge，确认。 表示消费端收到消息后的确认方式。

有三种确认方式：

• 自动确认：acknowledge="none" \
• 手动确认：acknowledge="manual" \
• 根据异常情况确认：acknowledge="auto"，（这种方式使用麻烦，不作讲解） \
其中自动确认是指，当消息一旦被Consumer接收到，则自动确认收到，并将相应 message 从 RabbitMQ 的
消息缓存中移除。但是在实际业务处理中，很可能消息接收到，业务处理出现异常，那么该消息就会丢失。如
果设置了手动确认方式，则需要在业务处理成功后，调用channel.basicAck()，手动签收，如果出现异常，则
调用channel.basicNack()方法，让其自动重新发送消息。


