ActiveMq实战，用户注册的异步处理

用户注册：包含保存用户信息，发送邮件，发送短信（验证码）
业务实现
1.正常的业务按顺序调用1.用户信息，2.发送邮件，3.发送短信==>SerailProcess
2.通过多线程的方式==>ParallelProcess
3.使用Mq==>MqProcess
    3.1使用了queue:队列名称为asyncApp.queue.email 和asyncApp.queue.sms      对应的消息消费者为ProcessEmail,ProcessSms
    3.2使用了topic：队列名称为asyncApp.topic    对应的消息消费者为CustomerService，DataCenter
    3.3使用了request-response，请求相应模式。队列名称为asyncApp.queue.sms，相应队列为临时的一个队列或创建一个queue
        SendSms-----ProcessSms ------ ReplayToProducer----GetConsumerResp

访问：http://localhost:8080/index