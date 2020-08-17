package com.weds.drools.test;

import com.weds.drools.service.RuleManager;
import com.weds.drools.service.impl.RuleManagerImpl;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static junit.framework.TestCase.fail;

public class DroolsService {
    @Test
    public void fireRule() {
        // load up the knowledge base
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kSession = kContainer.newKieSession("ksession-baseprice");

        // go !
        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);
        kSession.insert(message);//插入
        int rtn = kSession.fireAllRules();//执行规则
        System.out.println(rtn);
        kSession.dispose();
        System.out.println(message.getMessage());
    }

    private RuleManager ruleManager = new RuleManagerImpl();

    private String getRuleContent() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("import com.weds.drools.test.User;\n");
        stringBuilder.append("rule test1 when\n");
        stringBuilder.append("user : User(age==20)\n");
        stringBuilder.append("then\n");
        stringBuilder.append("user.setName(\"张三\");\n");
        stringBuilder.append("end\n");
        return stringBuilder.toString();
    }

    @Test
    public void executeRule1() throws Exception {

        User user = new User();
        user.setAge(20);

        //调用规则
        ruleManager.executeRule("test1", getRuleContent(), user);
        System.out.println("test result:" + user.toString());

        if (!"张三".equals(user.getName())) {
            fail("error rule");
        }
    }

    @Test
    public void executeRule2() throws Exception {

        User user = new User();
        user.setAge(21);

        //调用规则
        ruleManager.executeRule("test1", getRuleContent(), user);
        System.out.println("test result:" + user.toString());

        if ("张三".equals(user.getName())) {
            fail("error rule");
        }
    }
}