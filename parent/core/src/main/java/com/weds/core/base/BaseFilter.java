package com.weds.core.base;

import javax.servlet.http.HttpServletResponse;

public class BaseFilter {

    public void initResponse(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers",
                "User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,Authorization,Channel,CompanyId,OpenId");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        res.setHeader("Access-Control-Max-Age", "1209600");
        res.setHeader("Access-Control-Expose-Headers", "Authorization");
        res.setHeader("Access-Control-Request-Headers", "Authorization");
        res.setHeader("Expires", "-1");
        res.setHeader("Cache-Control", "no-cache");
        res.setHeader("pragma", "no-cache");
    }
}
