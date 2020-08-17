package com.weds.drools.service.impl;

import com.weds.drools.service.RuleManager;
import com.weds.drools.util.DroolsResource;
import com.weds.drools.util.DroolsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;


/**
 * 规则对外封装类
 */
public class RuleManagerImpl implements RuleManager {

    private Logger log = LogManager.getLogger();

    // 存放test session对象，避免重复编译 KieContainer
    private Map<String, KieContainer> kieContainerMap = new ConcurrentHashMap<>();


    // jdk8的线程锁
    private StampedLock lock = new StampedLock();

    /**
     * 定时处理器
     */
    private Timer timer;

    /**
     * 开始定时清理规则
     */
    private synchronized void startClearRuleTimer() {
        if (timer != null) {
            return;
        }
        log.info("startClearRuleTimer");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //清除规则
                resetAllRule();
            }
        }, 0L, 600000L);
    }

    public RuleManagerImpl() {
        super();
    }

    /**
     * 重置指定的group
     *
     * @param group
     */
    public void resetRuleByGroup(String group) {
        Map<String, KieContainer> tempKieContainerMap = new HashMap<>();
        synchronized (kieContainerMap) {
            // 复制下来，以保证随后去释放不再使用的
            tempKieContainerMap.putAll(kieContainerMap);
            kieContainerMap = new ConcurrentHashMap<>();
        }

        String key;
        // 将上一次不再引用的，进行清理
        for (Entry<String, KieContainer> entry : tempKieContainerMap.entrySet()) {
            key = entry.getKey();
            //如果key是null,说明清除所有,或者指定了对应的group
            if (StringUtils.isEmpty(key) || key.equals(group)) {
                entry.getValue().dispose();
                log.info("clearRule ruleGroupName {}", key);
            }
        }
    }

    /**
     * 重置所有规则
     */
    public void resetAllRule() {
        resetRuleByGroup(null);
    }

    /**
     * 根据
     *
     * @param group
     * @param content
     * @return
     */
    public String getDrlContent(String group, String content) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("package com.weds.drools." + group + ";\n");
        strBuilder.append(content);
        return strBuilder.toString();
    }

    /**
     * 获取kieSession,以保证不同的工作区
     *
     * @param group
     * @return
     * @throws IOException
     */
    private KieSession getKieSession(String group, String content) throws IOException {
        //如果timer为null时
        if (timer == null) {
            //开始定时清理内存中的规则
            startClearRuleTimer();
        }

        KieSession kieSession;
        KieContainer kieContainer = kieContainerMap.get(group);

        // 不存在，才去尝试重新获取，并上锁
        if (kieContainer == null) {
            long stamp = lock.writeLock();
            log.info("StampedWriteLock group {} lockValid {}", group, lock.validate(stamp));
            try {
                // double check
                kieContainer = kieContainerMap.get(group);
                if (kieContainer == null) {
                    // 找出所有实现RuleGetDataManager 接口的实现类，并将它设置到全局
                    String drlStr = getDrlContent(group, content);
                    KieServices kieServices = KieServices.Factory.get();
                    String fileName = "drools-" + group + "-rules";
                    //指定kjar包
                    final ReleaseId releaseId = kieServices.newReleaseId("com.weds.drools", fileName, "1.0.0");
                    log.info("DroolsGetKieSession fileName:{}", fileName);
                    log.info("DroolsGetKieSession drlStr:{}", drlStr);
                    // 创建初始化的kjar
                    InternalKieModule kJar = DroolsUtils.createKieJar(kieServices, releaseId,
                            new DroolsResource(ResourceFactory.newByteArrayResource(drlStr.getBytes()), fileName + ".drl"));
                    KieRepository repository = kieServices.getRepository();
                    repository.addKieModule(kJar);
                    kieContainer = kieServices.newKieContainer(releaseId);
                    kieContainerMap.put(group, kieContainer);
                } else {
                    log.info("在doubleCheck时发现已不需要执行 group:{}", group);
                }
            } finally {
                lock.unlock(stamp);
            }
        }

        kieSession = kieContainer.newKieSession();
        return kieSession;
    }

    /**
     * 根据group ,content
     *
     * @param group
     * @param content
     * @param objects
     * @throws Exception
     */
    public void executeRule(String group, String content, Object... objects) throws Exception {
        if (StringUtils.isEmpty(group)) {
            throw new RuntimeException("group不能为空");
        }

        KieSession kieSession = getKieSession(group, content);
        for (Object obj : objects) {
            kieSession.insert(obj);
        }

        try {
            int count = kieSession.fireAllRules();
            if (log.isDebugEnabled()) {
                log.debug("droolsFireAllRules count:{}", count);
            }
        } finally {
            kieSession.destroy();
        }
    }
}
