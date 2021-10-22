package com.nfceanalysis.api.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Document(collection = "triggerLogs")
public class TriggerLog {

    @Id
    private String id;

    @NotNull
    private ObjectId userId;

    @NotNull
    private LogType logType;

    @NotNull
    private LocalDateTime updateAt;


    public String getUserId() { return userId.toHexString(); }
}
