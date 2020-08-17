package com.weds.bean.license;

import com.weds.core.base.BaseFilter;
import com.weds.core.license.LicenseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter("/*")
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class LicenseFilter extends BaseFilter implements Filter {

    private Logger log = LogManager.getLogger();

    @Resource
    private LicenseService licenseService;

    @Resource
    private LicenseParams licenseParams;

    private static List<Pattern> patterns = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            LicenseEntity licenseEntity = licenseService.parseLicenseEntity();
            if (licenseEntity != null) {
                String[] modules = licenseEntity.getInfo().getModule();
                for (String module : modules) {
                    patterns.add(Pattern.compile(module));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        initResponse(res);

        if (req.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            res.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        licenseParams.setActive(true);
        if (licenseParams.isActive()) {
            String url = req.getRequestURI().substring(req.getContextPath().length());
            if (url.startsWith("/") && url.length() > 1) {
                url = url.substring(1);
            }
//            if (isInclude(url) && licenseService.checkRight()) {
            if (licenseService.checkRight(licenseParams.getPollCode())) {
                chain.doFilter(req, res);
            } else {
                unauthorized(res, "Authorization invalid license");
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"Protected\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }

    /**
     * 是否需要过滤
     *
     * @param url
     * @return
     */
    private boolean isInclude(String url) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }
}
