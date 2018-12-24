/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.rebalance <br>
 *
 * @author mk <br>
 * Date:2018-12-24 8:40 <br>
 */

package com.suns.rebalance;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: HandlerRebalance <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-24 8:40 <br>
 * @version
 */
public class HandlerRebalance implements ConsumerRebalanceListener {

    /*模拟一个保存分区偏移量的数据库表*/
    public static ConcurrentHashMap<TopicPartition,Long> partitionOffsetMap = new ConcurrentHashMap<>();

    private Map<TopicPartition,OffsetAndMetadata> currOffsets;
    private KafkaConsumer consumer;

    public HandlerRebalance(Map<TopicPartition,OffsetAndMetadata> currOffsets, KafkaConsumer consumer){
        this.currOffsets = currOffsets;
        this.consumer = consumer;
    }

    //分区再均衡之前
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println(Thread.currentThread().getId()+">>>>>>>>>>>>>>>>>>>onPartitionsRevoked>>>>>>>>>>>>>>>>>>>>>>"+partitions);
        System.out.println(Thread.currentThread().getId()+"-服务器准备分区再均衡，提交偏移量。当前偏移量："+currOffsets);

        //我们可以不使用consumer.commitSync(currOffsets);
        //提交偏移量到kafka,由我们自己维护*/
        //开始事务
        //偏移量写入数据库
        System.out.println("分区偏移量表中："+partitionOffsetMap);
        for(TopicPartition topicPartition : partitions){
            partitionOffsetMap.put(topicPartition,currOffsets.get(topicPartition).offset());
        }
        consumer.commitSync(currOffsets);
        //提交业务数和偏移量入库  tr.commit
    }

    //分区再均衡之后
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        System.out.println(Thread.currentThread().getId()+"-再均衡完成，onPartitionsAssigned参数值为："+partitions);
        System.out.println("分区偏移量表中："+partitionOffsetMap);
        for(TopicPartition topicPartition : partitions){
            System.out.println(Thread.currentThread().getId()+"-topicPartition"+topicPartition);
            //模拟从数据库中取得上次的偏移量
            Long offset = partitionOffsetMap.get(topicPartition);
            if(null == offset) continue;
            consumer.seek(topicPartition,partitionOffsetMap.get(topicPartition));
        }
    }
}
