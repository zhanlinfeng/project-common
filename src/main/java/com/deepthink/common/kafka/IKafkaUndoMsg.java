package com.deepthink.common.kafka;

import java.util.Date;

public interface IKafkaUndoMsg {

     String getMsg();

     Long getId();

     String getTopic();

     Date getCreateTime();
}
