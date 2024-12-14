package api.giybat.uz.api.giybat.uz.util;

import api.giybat.uz.api.giybat.uz.dto.JwtDTO;
import api.giybat.uz.api.giybat.uz.enums.GeneralStatus;
import api.giybat.uz.api.giybat.uz.enums.ProfileRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

public class JwtUtil {

    private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day
    private static final String secretKey = "veryLongSecretmazgillattayevlasharaaxmojonjinnijonsurbetbekkiydirhonuxlatdibekloxovdangasabekochkozjonduxovmashaynikmaydagapchishularnioqiganbolsangizgapyoqaniqsizmazgi";

    public static String encode(Integer id, List<ProfileRole> roleList) {

        String strRoleList = roleList.stream()
                .map(item -> item.name())
                .collect(Collectors.joining(","));  // "ROLE_USER,ROLE_ADMIN"

        Map<String,String> claims = new HashMap<>();
        claims.put("role", strRoleList);

        return Jwts
                .builder()
                .subject(String.valueOf(id))
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .signWith(getSignInKey())
                .compact();
    }

    public static JwtDTO decode(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Integer id = Integer.valueOf(claims.getSubject());
        String strRole = (String) claims.get("role");  // "ROLE_USER,ROLE_ADMIN"
        String[] roleArray = strRole.split(",");
        // 1-oddiyroq usul
//        List<ProfileRole> roleList_1 = new ArrayList<>();
//        for (String role : roleArray) {
//            roleList_1.add(ProfileRole.valueOf(role));
//        }
        // 2-qisqaroq usul
        List<ProfileRole> roleList_2 = Arrays.stream(strRole.split(","))
                .map(ProfileRole::valueOf)
                .toList();
        return new JwtDTO(id, roleList_2);
    }

    public static String encode(Integer id) {

        return Jwts
                .builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)))
                .signWith(getSignInKey())
                .compact();
    }

    public static Integer decodeRegVerToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Integer id = Integer.valueOf(claims.getSubject());
        return id;
    }

    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

