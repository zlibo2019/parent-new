package com.weds.drools.service;

/**
 */
public interface RuleManager {

    /**
     * 根据group ,content
     *
     * @param group
     * @param content
     * @param objects
     * @throws Exception
     */
    public void executeRule(String group, String content, Object... objects) throws Exception;
}
