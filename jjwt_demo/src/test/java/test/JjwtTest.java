package test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

public class JjwtTest {

    //创建jwt
    @Test
    public void test1() {
        //一般情况,不要发布没有失效时间的jwt(token)
        //因为一旦泄露,其他人就可以用这个token进行操作
        //所以最好要设置失效时间

        //获取当前时间
        long now = System.currentTimeMillis();
        //把当前时间加1分钟
        Date exp = new Date(now + 60000l);


        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("123456")//jwt的唯一身份标识"jti":"123456"
                .setIssuedAt(new Date())//jwt的签发时间"iat":1555298226
                .claim("username", "zhangsan")//存放自定义信息,不要存放敏感
                .claim("roles", "admin")
                .setExpiration(exp)//设置失效时间
                .signWith(SignatureAlgorithm.HS256, "itcast");//签名的算法和secret

        //创建jwt,其实就是一个字符串,由头部,载荷,签名三部分组成,中间用.分隔
        String jwt = jwtBuilder.compact();

        System.out.println(jwt);
    }


    //解析jwt
    @Test
    public void test2() {
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1NTUyOTgyMjZ9.ngGUo6pa3gbH67ag9QiTBdVVhx36LF33DcdLzdm_7Wg";
        //String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1NTUyOTg2MzQsInVzZXJuYW1lIjoiemhhbmdzYW4iLCJyb2xlcyI6ImFkbWluIn0.p1vkUGEYiUUTQpsBuW5S00e1881iETeI724SaU5aRHE";

        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1NTUyOTg5MjYsInVzZXJuYW1lIjoiemhhbmdzYW4iLCJyb2xlcyI6ImFkbWluIiwiZXhwIjoxNTU1Mjk4OTg2fQ.ONnFWHTWntzVEHH1n3RuNeiTVsjyN77FBhOpzldkN08";

        Claims claims = Jwts.parser().setSigningKey("itcast").parseClaimsJws(jwt).getBody();

        System.out.println("jti:" + claims.getId());
        System.out.println("iat:" + claims.getIssuedAt());
        System.out.println("username:" + claims.get("username"));
        System.out.println("roles:" + claims.get("roles"));
    }

}
