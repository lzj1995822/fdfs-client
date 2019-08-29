package com.jtzh.image.server.conf;


import com.jtzh.image.server.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token 拦截器
 * @author jerry
 */
@Component
public class AccessTokenInterceptor {

    /** redis 数据库操作模板类*/
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {

        //从header中得到token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        Claims claims;
        try {
            claims = TokenUtil.parseJWT(token);
        } catch (ExpiredJwtException expiredJwtException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String principalId = claims.getId();
        String appToken = redisTemplate.opsForValue().get("app:token:principalId:" + principalId);
        if (!token.equals(appToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
