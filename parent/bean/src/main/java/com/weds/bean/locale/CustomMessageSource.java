package com.weds.bean.locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;

public class CustomMessageSource {

    private Logger log = LogManager.getLogger();

    @Resource
    private MessageSource messageSource;

    public String getMessage(String msgCode) {
        try {
            return messageSource.getMessage(msgCode, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            log.error("Message Code [" + msgCode + "] Not Exist");
            return "Message Code [" + msgCode + "] Not Exist";
        }
    }
}
