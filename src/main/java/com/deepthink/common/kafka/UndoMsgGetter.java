package com.deepthink.common.kafka;

import java.util.List;

public interface UndoMsgGetter {

    /**
     * 获取为处理的记录
     * @param limit 返回记录大小限制
     * @return 未处理记录列表
     */
    List<? extends IKafkaUndoMsg> getUndoMsg(int limit);

    /**
     * 完成未处理记录
     * @param id 记录id
     */
    void done(IKafkaUndoMsg msg);

    /**
     * 添加未处理的记录
     * @param undo 为处理的记录
     */
    void addUndoMsg(String topic, String message, String undoReason);
}
