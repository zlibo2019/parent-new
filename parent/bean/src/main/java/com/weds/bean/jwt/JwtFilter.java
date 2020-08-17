package com.weds.bean.jwt;

import com.weds.core.base.BaseFilter;
import com.weds.core.constants.CoreConstants;
import com.weds.core.resp.JsonResult;
import com.weds.core.utils.StringUtils;
import com.weds.core.utils.coder.Coder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.http.HttpStatus;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter("/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtFilter extends BaseFilter implements Filter {

    private Logger log = LogManager.getLogger();

    //封装，不需要过滤的list列表
    private static List<Pattern> patterns = new ArrayList<>();

    @Resource
    private JwtParams jwtParams;

    @Resource
    private JwtHandlerService jwtHandlerService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        patterns.add(Pattern.compile("v2/api-docs.*"));
        patterns.add(Pattern.compile("configuration/ui.*"));
        patterns.add(Pattern.compile("swagger-resources.*"));
        patterns.add(Pattern.compile("configuration/security.*"));
        patterns.add(Pattern.compile("swagger-ui.html"));
        patterns.add(Pattern.compile("spec"));
        patterns.add(Pattern.compile("healthy"));
        patterns.add(Pattern.compile("webjars.*"));

        if (jwtParams.isActive() && !StringUtils.isBlank(jwtParams.getFilter())) {
            String[] filters = jwtParams.getFilter().split(",");
            // 添加自定义filter
            for (String filter : filters) {
                patterns.add(Pattern.compile(filter));
            }
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
        if (jwtParams.isActive()) {
            String url = req.getRequestURI().substring(req.getContextPath().length());
            if (url.startsWith("/") && url.length() > 1) {
                url = url.substring(1);
            }
            if (isInclude(url)) {
                chain.doFilter(req, res);
            } else {
                authorizationToken(req, res, chain);
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }

    private void authorizationToken(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain chain) throws IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null) {
            try {
                StringTokenizer st = new StringTokenizer(authHeader);
                if (st.hasMoreTokens()) {
                    String basic = st.nextToken();
                    String credentials = new String(Coder.decryptBASE64(basic), StandardCharsets.UTF_8);
                    String jwtData = JwtUtils.parseJWT(credentials, jwtParams);
                    JsonResult jsonResult = jwtHandlerService.dataHandler(jwtData);
                    if (CoreConstants.SUCCESSED_FLAG.equals(jsonResult.getCode())) {
                        req.setAttribute("jwtData", jwtData);
                        chain.doFilter(req, res);
                    } else {
                        unauthorized(res, jsonResult.getMsg());
                    }
                }
            } catch (SignatureException e) {
                e.printStackTrace();
                unauthorized(res, "Authorization invalid token");
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                unauthorized(res, "Authorization expired token");
            } catch (Exception e) {
                e.printStackTrace();
                unauthorized(res, "Authorization error token");
            }
        } else {
            unauthorized(res, "Authorization header not found");
        }
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
