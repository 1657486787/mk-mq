/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.service <br>
 *
 * @author mk <br>
 * Date:2018-12-24 10:50 <br>
 */

package com.suns.service;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

/**
 * ClassName: SendListener <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 10:50 <br>
 * @version
 */
public class SendListener implements ProducerListener {
    @Override
    public void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {
        System.out.println(String.format("生产者发送消息成功:topic:%s,partition:%d,key:%s,value:%s,recordMetadata:%s",topic,partition,key,value,recordMetadata.partition()));
    }

    @Override
    public void onError(String topic, Integer partition, Object key, Object value, Exception exception) {
        System.out.println("onerror.....");
    }

    @Override
    public boolean isInterestedInSuccess() {
        System.out.println("生产者关注成功事件isInterestedInSuccess。。。。。。。。。。。。。");
        return true;
    }
}
