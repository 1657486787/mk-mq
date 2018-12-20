/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.selfserial <br>
 *
 * @author mk <br>
 * Date:2018-12-20 11:01 <br>
 */

package com.suns.selfserial;

import com.suns.vo.DemoUser;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * ClassName: SelfDeserializer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-20 11:01 <br>
 * @version
 */
public class SelfDeserializer implements Deserializer<DemoUser> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public DemoUser deserialize(String s, byte[] data) {
        try {
            if(data==null){
                return null;
            }
            if(data.length<8){
                throw new SerializationException("Error data size.");
            }
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int id;
            String name;
            int nameSize;
            id = buffer.getInt();
            nameSize = buffer.getInt();
            byte[] nameByte = new byte[nameSize];
            buffer.get(nameByte);
            name = new String(nameByte,"UTF-8");
            return new DemoUser(id,name);
        } catch (Exception e) {
            throw new SerializationException("Error Deserializer DemoUser."+e);
        }
    }

    @Override
    public void close() {

    }
}
