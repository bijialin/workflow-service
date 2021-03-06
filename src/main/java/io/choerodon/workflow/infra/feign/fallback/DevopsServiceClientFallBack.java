package io.choerodon.workflow.infra.feign.fallback;

import io.choerodon.workflow.infra.feign.DevopsServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Sheep on 2019/4/15.
 */
public class DevopsServiceClientFallBack implements DevopsServiceClient {
    @Override
    public ResponseEntity autoDeploy(Long stageRecordId, Long taskRecordId) {
        return new ResponseEntity("error.auto.deploy", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity setAutoDeployTaskStatus(Long pipelineRecordId, Long stageRecordId, Long taskRecordId, Boolean status) {
        return new ResponseEntity("error.set.auto.deploy.task.status", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> getAutoDeployTaskStatus(Long stageRecordId, Long taskRecordId) {
        return new ResponseEntity("error.get.auto.deploy.task.status", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity cdHostDeploy(Long pipelineRecordId, Long stageRecordId, Long jobRecordId) {
        return new ResponseEntity("error.get.cd.host.deploy", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Boolean> envAutoDeploy(Long pipelineRecordId, Long stageRecordId, Long jobRecordId) {
        return new ResponseEntity("error.get.env.auto.deploy", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity setAppDeployStatus(Long pipelineRecordId, Long stageRecordId, Long jobRecordId, Boolean status) {
        return new ResponseEntity("error.update.deploy.job.status", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> getJobStatus(Long pipelineRecordId, Long stageRecordId, Long jobRecordId) {
        return new ResponseEntity("error.get.deploy.job.status", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
