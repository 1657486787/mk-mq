/**
 * Project Name:mk-mq <br>
 * Package Name:com.suns.selfserial <br>
 *
 * @author mk <br>
 * Date:2018-12-20 10:58 <br>
 */

package com.suns.selfserial;

import com.suns.vo.DemoUser;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * ClassName: SelfSerializer <br>
 * Description:  <br>
 * @author mk
 * @Date 2018-12-20 10:58 <br>
 * @version
 */
public class SelfSerializer implements Serializer<DemoUser> {

    @Override
    public void configure(Map map, boolean b) {
        //配置
    }

    @Override
    public byte[] serialize(String s, DemoUser data) {
        try {
            byte[] name;
            int nameSize;
            if(data==null){
                return null;
            }
            if(data.getName()!=null){
                name = data.getName().getBytes("UTF-8");
                //字符串的长度
//                nameSize = data.getName().length();
                nameSize = data.getName().getBytes("UTF-8").length;
            }else{
                name = new byte[0];
                nameSize = 0;
            }
            /*id的长度4个字节，字符串的长度描述4个字节，
            字符串本身的长度nameSize个字节*/
            ByteBuffer buffer = ByteBuffer.allocate(4+4+nameSize);
            buffer.putInt(data.getId());//4
            buffer.putInt(nameSize);//4
            buffer.put(name);//nameSize
            return buffer.array();
        } catch (Exception e) {
            throw new SerializationException("Error serialize DemoUser:"+e);
        }
    }

    @Override
    public void close() {
        //关闭资源
    }
}
