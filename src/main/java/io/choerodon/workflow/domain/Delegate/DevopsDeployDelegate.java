package io.choerodon.workflow.domain.Delegate;


import java.util.concurrent.CountDownLatch;

import io.choerodon.workflow.domain.repository.DevopsServiceRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Sheep on 2019/4/3.
 */
@Component
public class DevopsDeployDelegate implements JavaDelegate {

    @Autowired
    DevopsServiceRepository devopsServiceRepository;

    private Logger logger = LoggerFactory.getLogger(DevopsDeployDelegate.class);

    private final String SUCCRESS = "success";
    private final String RUNNING = "running";
    private final String FAILED = "failed";

    @Override
    public void execute(DelegateExecution delegateExecution) {

        logger.info(String.format("ServiceTask:%s 开始", delegateExecution.getCurrentActivityId()));

        String[] ids = delegateExecution.getCurrentActivityId().split("\\.");
        Long pipelineId = Long.parseLong(ids[1]);
        Long stageId = Long.parseLong(ids[2]);
        Long taskId = Long.parseLong(ids[3]);
        delegateExecution.getProcessInstanceId();

        devopsServiceRepository.autoDeploy(stageId, taskId);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        int[] count = {0};
        Runnable runnable = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                count[0] = count[0] + 1;
                String deployResult = devopsServiceRepository.getAutoDeployTaskStatus(stageId, taskId);
                System.out.println(deployResult);
                if (SUCCRESS.equals(deployResult)) {
                    Thread.currentThread().interrupt();
                    countDownLatch.countDown();

                }
                //自动部署失败或者执行20s以上没反应也重置为失败
                if (FAILED.equals(deployResult)||count[0]==10) {
                    devopsServiceRepository.setAutoDeployTaskStatus(pipelineId, stageId, taskId, false);
                    Thread.currentThread().interrupt();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        devopsServiceRepository.setAutoDeployTaskStatus(pipelineId, stageId, taskId, true);
        logger.info(String.format("ServiceTask:%s  结束", delegateExecution.getCurrentActivityId()));
    }
}
