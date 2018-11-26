/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.embed <br>
 *
 * @author mk <br>
 * Date:2018-11-26 18:16 <br>
 */

package com.suns.embed;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;

/**
 * ClassName: EmbedMQ <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-11-26 18:16 <br>
 * @version
 */
public class EmbedMQ {

    public static void main(String[] args) throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("embedMq");
        brokerService.addConnector("tcp://localhost:61000");
        brokerService.setManagementContext(new ManagementContext());
        brokerService.start();
        System.out.println("EmbedMQ start......");
    }
}
