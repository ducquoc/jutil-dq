package vn.ducquoc.activiti;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import vn.ducquoc.activiti.config.ConfigManager;
import vn.ducquoc.activiti.config.TenantConfig;

public class SimpleProcessTest {

    @Test
    public void testStartingSimpleProcess() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("diagrams/MailProcess.bpmn20.xml").deploy();

        Map<String, Object> variableMap = new HashMap<String, Object>();
        TenantConfig tenantConfig = ConfigManager.getInstance().getTenantConfig("GOOGLE");
        variableMap.put("mailNotification", tenantConfig.getMailNotification());
        variableMap.put("mailConfig", tenantConfig.getMailConfig());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("mail_process1", variableMap);

        assertNotNull(processInstance.getId());
        System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
    }

}
