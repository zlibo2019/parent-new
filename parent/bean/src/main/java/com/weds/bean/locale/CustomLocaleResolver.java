package com.weds.bean.locale;

import com.weds.core.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class CustomLocaleResolver implements LocaleResolver {

    private Logger log = LogManager.getLogger();

    //分隔符
    private static final String LOCALE_REGX = "_";

    private static final String HTTP_HEADER_LANG_ID = "LANG_ID";

    // @Resource
    // private MessageSource messageSource;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(HTTP_HEADER_LANG_ID);
        Locale locale = Locale.getDefault();
        if (!StringUtils.isBlank(lang)) {
            try {
                String[] lans = lang.split(LOCALE_REGX);
                if (lans.length == 2) {
                    locale = new Locale(lans[0], lans[1]);
                } else {
                    locale = new Locale(lans[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("错误信息:", e);
            }
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }

    // public String getMessage(String msgKey) {
    //     return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
    // }
}