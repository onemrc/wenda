package com.demo.wenda.domain;

import lombok.Data;

import java.util.Date;

/**
 * 消息表
 *
 * 私信
 */
@Data
public class Message {

    private Integer messageId;

    //谁发给我的
    private Integer fromId;

    //我发给谁的
    private Integer toId;

    //读没读
    private Integer hasRead;

    private String content;

    private Date createDate;


    private String conversationId;


    public void setConversationId() {
        if (fromId < toId){
            this.conversationId =  String.format("%d_%d",fromId,toId);
        }
        this.conversationId = String.format("%d_%d",toId,fromId);
    }

}
