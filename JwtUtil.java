
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/11.
 */
@ConfigurationProperties("jwt.config")
public class JwtUtil {

    private String key ; //关键字（盐）
	
    private long ttl ;//一个小时（过期时间）

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 生成JWT
     *
     * @param id
     * @param subject
     * @return
     */
    public String createJWT(String id, String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
		.setId(id)           //用户id
                .setSubject(subject) //用户名
                .setIssuedAt(now)   //用于设置签发时间
                .signWith(SignatureAlgorithm.HS256, key).claim("roles", roles);  //用于设置签名秘钥
        if (ttl > 0) {
            builder.setExpiration( new Date( nowMillis + ttl));  //设置过期时间
        }
        return builder.compact(); //转化成字符串返回
    }

    /**
     * 解析JWT
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr){
        return  Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}

/*
		jwt的pom.xml坐标
		<!-- jjwt -->
		<dependency>
		    <groupId>io.jsonwebtoken</groupId>
		    <artifactId>jjwt</artifactId>
		    <version>0.9.0</version>
		</dependency>
		  	
*/







