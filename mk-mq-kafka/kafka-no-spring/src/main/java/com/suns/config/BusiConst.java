/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.config <br>
 *
 * @author mk <br>
 * Date:2018-12-17 11:02 <br>
 */

package com.suns.config;

/**
 * ClassName: BusiConst <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-17 11:02 <br>
 * @version
 */
public class BusiConst {
    public static final String HELLO_TOPIC  = "kafka-hello-topic-001";
    public static final int CONCURRENT_PARTITIONS_COUNT = 2;
    public static final String CONCURRENT_USER_INFO_TOPIC = "concurrenttest";

    public static final String SELF_SERIAL_TOPIC = "self-serial";

    public static final String SELF_PARTITION_TOPIC = "self-partition";

    public static final String CONSUMER_GROUP_TOPIC  = "consumer-group-002";
    public static final String CONSUMER_GROUP_A  = "groupA";
    public static final String CONSUMER_GROUP_B  = "groupB";
    public static final String CONSUMER_GROUP_C  = "groupC";

    public static final String CONSUMER_COMMIT_TOPIC  = "simple";

    public static final String INDEPENDENCE_CONSUMER_TOPIC = "independconsumer-topic-001";
    public static final int INDEPENDENCE_CONSUMER_TOPIC_PARTITIONS_COUNT = 1;
}
