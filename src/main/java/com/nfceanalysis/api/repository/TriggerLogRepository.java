package com.nfceanalysis.api.repository;

import com.nfceanalysis.api.model.LogType;
import com.nfceanalysis.api.model.TriggerLog;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@SpringBootApplication
public interface TriggerLogRepository extends MongoRepository<TriggerLog, String> {

    TriggerLog findTop1ByUserIdAndLogTypeOrderByUpdateAtDesc(Object userId, LogType logType);
}
