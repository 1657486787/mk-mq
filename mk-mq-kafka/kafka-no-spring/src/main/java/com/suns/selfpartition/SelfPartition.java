/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.selfpartition <br>
 *
 * @author mk <br>
 * Date:2018-12-20 14:32 <br>
 */

package com.suns.selfpartition;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * ClassName: SelfPartition <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-20 14:32 <br>
 * @version
 */
public class SelfPartition implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.availablePartitionsForTopic(topic);
        for(PartitionInfo p : partitionInfos){
            System.out.println("分区信息："+p);
        }
        int pId = key.hashCode() % partitionInfos.size();
        System.out.println("将分配到分区pId:"+pId);
        return pId;
    }

    @Override
    public void close() {

        //do something

    }

    @Override
    public void configure(Map<String, ?> configs) {

        //do something

    }
}
