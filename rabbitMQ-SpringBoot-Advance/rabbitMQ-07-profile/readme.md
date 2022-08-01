# 1.日志管理



# 2.应用

## 2.1 消息可靠性保障--消息补偿

![image-20220801195118035](https://woldier-pic-repo-1309997478.cos.ap-chengdu.myqcloud.com/image-20220801195118035.png)

## 2.2 消息幂等性保障

幂等性指一次和多次请求某一个资源，对于资源本身应该具有同样的结果。也就是说，其任 意多次执行对资源本身所产生的影响均与一次执行的影响相同。 在MQ中指，消费多条相同的消息，得到与消费该消息一次相同的结果。



![image-20220801195206158](https://woldier-pic-repo-1309997478.cos.ap-chengdu.myqcloud.com/image-20220801195206158.png)

视频讲解

 https://www.bilibili.com/video/BV15k4y1k7Ep?p=35&vd_source=b592fd0fd3bd041bab6398e89668385d