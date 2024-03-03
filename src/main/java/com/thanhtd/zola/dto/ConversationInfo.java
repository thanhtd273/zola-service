package com.thanhtd.zola.dto;

import com.thanhtd.zola.model.Message;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ConversationInfo {

    private Long conversationId;

    private Boolean isGroup;

    private String name;

    private String avatar;

    private Message lastMessage;

    private String lastMessageSender;

    private Timestamp lastSeenAt;


    private Boolean star;

    private Integer muteCode;

    private Long labelId;

    private String memberIdsStr; // 12-4-321

//    private List<User> members;

//    private List<Resource> resources;

}
