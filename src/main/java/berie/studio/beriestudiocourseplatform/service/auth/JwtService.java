package berie.studio.beriestudiocourseplatform.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
    Для работы нужны следующие зависимости
     1) JJWT-API
     2) JJWT-IMPL
     3) JJWT-JACKSON
 */
@Service
public class JwtService {

    /*** SECRET-KEY ***/
    @Value("${app.secret-key}")
    private String SECRET_KEY;

    private Key getSigningKey() {
        byte [] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*** Получение информации из токена ***/


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // Применит переданный метод все пары ключ:значение и тем самым извлечет тот что нужен
    private  <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Извлекает все Claims (пары ключ:значение) из токена
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }




    /*** Создание токена ***/


    // Если не нужно добавлять новые ассоциации
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    // Если нужно добавлять новые ассоциации
    private String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Установка срока годности токена, в данном случае будет годен +- 10 часов
                .setExpiration(new Date(System.currentTimeMillis()  + 60 * 1000 * 24))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }


    /*** Проверка токена ***/

    // Кроме самого токена передаем UserDetails для проверки принадлежности токена данному пользователю
    public boolean isTokenValid(String token , UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Проверяем годен ли еще токен
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }




}