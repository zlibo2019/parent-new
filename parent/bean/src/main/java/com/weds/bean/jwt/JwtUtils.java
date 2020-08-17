package com.weds.bean.jwt;

import com.alibaba.fastjson.JSONObject;
import com.weds.core.utils.StringUtils;
import com.weds.core.utils.coder.AESCoder;
import com.weds.core.utils.coder.Coder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JwtUtils {

    private Logger log = LogManager.getLogger();

    public static JSONObject getJwtData(HttpServletRequest request) {
        Object jwtData = request.getAttribute("jwtData");
        JSONObject jsonObject = new JSONObject();
        if (jwtData != null) {
            jsonObject = JSONObject.parseObject(jwtData.toString());
        }
        return jsonObject;
    }

    /**
     * 生成jwt
     *
     * @param jwtEntity
     * @return
     */
    public static String createJWT(JwtEntity jwtEntity) throws Exception {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] signKeyBytes = jwtEntity.getJwtParams().getSignKey().getBytes(StandardCharsets.UTF_8);
        Key signingKey = new SecretKeySpec(signKeyBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(jwtEntity.getJwtParams().getSub())
                .setIssuer(jwtEntity.getJwtParams().getIss())
                .signWith(signatureAlgorithm, signingKey);
        //if it has been specified, let's add the expiration
        if (jwtEntity.getJwtParams().getExpMillis() >= 0) {
            long expMillis = nowMillis + jwtEntity.getJwtParams().getExpMillis();
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        // set header
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        // 设置加密算法
        builder.claim("palg", "aes-256-cbc");
        builder.claim("jti", StringUtils.getUUID());
        // 接收者
        builder.claim("aud", jwtEntity.getJwtParams().getAud());
        builder.setHeader(header);
        // 重新解析pdata，首位存入随机数
        JSONObject pDataRam = new JSONObject();
        pDataRam.put("random", getRandom());
        pDataRam.putAll(jwtEntity.getPdata());
        // 对内容进行加密
        String encrypted = AESCoder.encrypt(pDataRam.toJSONString(), jwtEntity.getJwtParams().getCoderKey(),
                jwtEntity.getJwtParams().getCoderIv());
        // 写入
        builder.claim("pdata", encrypted);
        // 加密算法id
        String tokenPlain = builder.compact();

        return Coder.encryptBASE64(tokenPlain.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param jwt
     * @return
     * @throws Exception
     */
    public static String parseJWT(String jwt, JwtParams jwtParams) throws Exception {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(jwtParams.getSignKey().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(jwt).getBody();
        // Date expDate = claims.getExpiration();

        // if (expDate != null) {
        //     long nowMillis = System.currentTimeMillis();
        //     long expMillis = claims.getExpiration().getTime();
        //     if (nowMillis > expMillis) {
        //         throw new JwtException("date is lose");
        //     }
        // }

        String pdata = claims.get("pdata").toString();
        String pdataStr = AESCoder.decrypt(pdata, jwtParams.getCoderKey(),
                jwtParams.getCoderIv());
        // JSONObject pdataJson = JSONObject.parseObject(pdataStr);
        return pdataStr;
        // System.out.println("ID: " + claims.getId());
        // System.out.println("Subject: " + claims.getSubject());
        // System.out.println("Issuer: " + claims.getIssuer());
        // System.out.println("Expiration: " + claims.getExpiration());
    }

    public static String refreshJWT(String jwt, JwtParams jwtParams) throws Exception {
        String pdata = parseJWT(jwt, jwtParams);
        JSONObject jsonObject = JSONObject.parseObject(pdata);
        JwtEntity jwtEntity = new JwtEntity();
        jwtEntity.setPdata(jsonObject);
        jwtEntity.setJwtParams(jwtParams);
        return createJWT(jwtEntity);
    }

    /**
     * 随机生成指定精确度的小数
     *
     * @return
     */
    private static String getRandom() {
        return String.valueOf(new Random().nextDouble());
    }
}
