package com.suns.usemq;


/**
 *   使用原生ActiveMQ的API编程展示
 *   ActiveMq生产者:
 *   1.引入pom
 *          <dependency>
 *             <groupId>org.apache.activemq</groupId>
 *             <artifactId>activemq-all</artifactId>
 *             <version>5.9.0</version>
 *         </dependency>
 *   2.创建连接工厂，创建连接，启动连接，创建会话，创建消息目的地，创建生产者，组装好消息，然后发送
 *   3.消息有两种模式，点对点p2p和发布订阅(广播)pub/sub模式，在创建消息目的地时指定
 *   点对点p2p模式：一个消息只有一个消费者，默认会持久化，就是说重启activeMq，消息会自动保存
 *   发布订阅(广播)pub/sub模式：一个消息有多个消费者消费，默认不会持久化，就是说重启activeMq，消息会丢失，而且如果是生产者先发送消息之后，消费者才启动，那么消费者是获取不到消息的
 *
 *
 *
 *  ActiveMq消费者:同步阻塞获取消息  或者使用异步监听器的方式获取消息
 *   1.引入pom
 *          <dependency>
 *             <groupId>org.apache.activemq</groupId>
 *             <artifactId>activemq-all</artifactId>
 *            <version>5.9.0</version>
 *         </dependency>
 *   2.创建连接工厂，创建连接，启动连接，创建会话，创建消息目的地，创建消费者，循环同步阻塞获取消息
 *   3.消息有两种模式，点对点p2p和发布订阅(广播)pub/sub模式，在创建消息目的地时指定
 *   点对点p2p模式：一个消息只有一个消费者，默认会持久化，就是说重启activeMq，消息会自动保存
 *   发布订阅(广播)pub/sub模式：一个消息有多个消费者消费，默认不会持久化，就是说重启activeMq，消息会丢失，而且如果是生产者先发送消息之后，消费者才启动，那么消费者是获取不到消息的
 *
 */

