package test;


import com.deepthink.common.mqtt.AutoReconnectMqttClient;
import com.deepthink.common.mqtt.MqttMsg;
import com.deepthink.common.mqtt.MqttTopicEnum;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import util.ReflectUtils;

/**
 * @program: admin_server
 * @description: AutoReconnectMqttClient测试类
 * @author: td
 * @create: 2019-11-28 16:44
 **/
public class AutoReconnectMqttClientTest extends AutoRollbackBaseDatabaseTest {
    @Autowired
    private AutoReconnectMqttClient mqttClient;


    /**
     * 基础功能测试
    * @Description:
    * @Param:
    * @return:
    * @Author: td
    * @Date: 2019/11/28
    */
    @Test
    public void pulishTest() {
        MqttMsg msg = getMsg();
        mqttClient.publish(MqttTopicEnum.test_topic, msg);
    }

    @Test
    public void sharedTopicTest() {

    }

    private MqttMsg getMsg() {
        return new MqttMsg(){
            String str = "repeat_sub_test";
            public String getStr(){
                return str;
            }
        };
    }


    @Test
    public void reconectTest() throws Exception {
        mqttClient.reconectIfNessary();
        MqttClient client = ReflectUtils.getFieldValueForce(MqttClient.class, "mqttClient", mqttClient);
        client.disconnect();
        this.mqttClient.reconectIfNessary();
        Assert.assertTrue(client.isConnected());
    }



}
