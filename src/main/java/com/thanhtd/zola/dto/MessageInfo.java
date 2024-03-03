package com.thanhtd.zola.dto;

import com.thanhtd.zola.model.Reaction;
import com.thanhtd.zola.model.Resource;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class MessageInfo {
    private Long messageId;

    private Long conversationId;

    private String content;

    private Integer priority;

    private Timestamp createDate;

    private Timestamp modifiedDate;

    private Boolean deleted;

    private List<Reaction> reactions;

    private Resource resource;
}
