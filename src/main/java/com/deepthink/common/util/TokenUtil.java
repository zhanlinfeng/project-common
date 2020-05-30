package com.deepthink.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.deepthink.common.web.RequestThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
/**
* @Description: 负责Token的签发和校验
* @Param:
* @return:
* @Author: td
* @Date: 2020/3/20
*/
public class TokenUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);
	
	/**
     * 过期时间,测试使用20分钟
     */
    final static long EXPIRE_TIME = 1000 * 60 * 60 * 24;

    private static String secret = "fajlgaermflksaeeklk";

    public static String assignToken(String userName, String password) {
    	Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(secret);
		return JWT.create()
				.withClaim("userName", userName)
				.withClaim("password", password)
				.withExpiresAt(date)
				.sign(algorithm);
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
    	try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    String userName = getUserName(token);
		    JWTVerifier verifier = JWT.require(algorithm)
		            .withClaim("userName", userName)
		            .build();
		    DecodedJWT jwt = verifier.verify(token);
		    return true;
    	 } catch (Exception exception) {
    		logger.error("token：{} 校验错误", token, exception);
             return false;
         }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUserName(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("userName").asString();
    }

    public static String getUserName() {
    	return getUserName(RequestThreadLocal.getToken());
	}

    public static String getPassword(String token) {
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getClaim("password").asString();
    }
}
