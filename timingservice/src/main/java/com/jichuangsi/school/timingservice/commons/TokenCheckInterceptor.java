package com.jichuangsi.school.timingservice.commons;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenCheckInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${app.token.headerName}")
    private String headerName;
    @Value("${app.token.ingoreTokenUrls}")
    private String[] ingoreTokenUrls;
    @Autowired
    private Algorithm tokenAlgorithm;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String url=request.getRequestURI();//地址
        if(null!=ingoreTokenUrls && ingoreTokenUrls.length>0){
            for (String ingoreUrl : ingoreTokenUrls) {
                if (ingoreUrl.equals(url) || url.startsWith(ingoreUrl)) {// 对免检查token的url放行
                    return true;
                }
            }
        }
        try {
            String accessToken=request.getHeader("accessToken");
            if(!StringUtils.isEmpty(accessToken)){
                final JWTVerifier verifier=JWT.require(tokenAlgorithm).build();
                verifier.verify(accessToken);
                return true;
            }
        }catch (JWTVerificationException e){
            logger.error("token检验不通过：" + e.getMessage());
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        return false;
    }
}
