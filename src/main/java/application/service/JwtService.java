package application.service;

import config.common.CurrentDateTime;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import model.SecurityKey;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;

@Service
public class JwtService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final String COMPANY_CLAIM_KEY = "Lab Company Dot Com";
    private static final String PRIVATE_KEY_CLAIM_KEY = "PRIVATE_KEY";
    private static final String SHIP_ID_CLAIM_KEY = "SHIP_ID";
    private static final String CREATED_CLAIM_KEY = "iat";
    private static final String EXPIRE_CLAIM_KEY = "exp";

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public SecurityKey decode(String jwt) {
        String BEARER = "Bearer ";
        Key key = getSignInKey();
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt.replace(BEARER, ""))
                    .getBody();
            //map keys from claims
            String company = (String) claims.get(COMPANY_CLAIM_KEY);
            String privateKey = (String) claims.get(PRIVATE_KEY_CLAIM_KEY);
            Integer shipId = (Integer) claims.get(SHIP_ID_CLAIM_KEY);
            LocalDateTime created = CurrentDateTime.from((Integer) claims.get(CREATED_CLAIM_KEY));
            LocalDateTime expire = CurrentDateTime.from((Integer) claims.get(EXPIRE_CLAIM_KEY));
            return new SecurityKey(company, privateKey, shipId, created, expire);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
