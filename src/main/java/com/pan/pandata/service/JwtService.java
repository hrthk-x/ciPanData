package com.pan.pandata.service;

import com.pan.pandata.entity.ResponsePanDetails;
import com.pan.pandata.repository.BlackListedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtService {
	
	private static final String SECRET="31c7d90208669ebab0f295efe744d2c8894e407269d1f1537b36098f2d5c9fa9";

	private BlackListedTokenRepository bP;
	
	public String extractPanNumber(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	public Integer extractCustomerId(String token) {
//		Claims.
		return extractClaim(token,claims->(Integer) claims.get("customerId"));
	}
	public String extractFirstName(String token) {
	    return extractClaim(token, claims -> (String) claims.get("firstName"));
	}
	public String extractLastName(String token) {
	    return extractClaim(token, claims -> (String) claims.get("lastName"));
	}
	public List<Integer> extractAccounts(String token){
		return extractClaim(token,claims ->(List<Integer>) claims.get("accounts"));
	}

	public <T> T extractClaim(String token,Function<Claims,T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(ResponsePanDetails panData) {
		Map<String,Object> extraClaims=new HashMap<>();
		extraClaims.put("firstName",panData.getFirstName());
		extraClaims.put("lastName",panData.getLastName());
		extraClaims.put("customerId",panData.getCustomerId());
		extraClaims.put("accounts",panData.getAccounts());
		return generateToken(extraClaims,panData);
	}
	public String generateToken(Map<String,Object> extraClaims,ResponsePanDetails panData) {
		return Jwts
			.builder()
			.setClaims(extraClaims)
			.setSubject(panData.getPanNumber())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
			.signWith(getSignKey(),SignatureAlgorithm.HS256)
			.compact();
	}
	
	public boolean isTokenValid(String token,ResponsePanDetails panData) {
		final String panNumber=extractPanNumber(token);
		if(bP.findById(token).isPresent()) return false;
		return (panNumber.equals(panData.getPanNumber())&&!isTokenExpired(token));
	}
	
	private boolean isTokenExpired(String token) {
	    Date expirationDate = extractExpiration(token);
	    return expirationDate != null && expirationDate.before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}
	private Claims extractAllClaims(String token) {
		return Jwts
			  .parserBuilder()
			  .setSigningKey(getSignKey())
			  .build()
			  .parseClaimsJws(token)
			  .getBody();
	}
	private Key getSignKey() {
		byte[] keyBytes=Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	

}
