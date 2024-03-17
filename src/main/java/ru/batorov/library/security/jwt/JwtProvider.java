package ru.batorov.library.security.jwt;

import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtProvider {
	@Value("${jwt_secret}") // внедряем из файля
	private String secret;
	private final int ttl = 60;

	public String generateToken(String username) {
		Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(ttl).toInstant());
		return JWT.create()
				.withSubject("User details")// обозначает что вообще хранится в jwt
				.withClaim("username", username)// добавления пары ключ-значение
				.withIssuedAt(new Date())// время создания
				.withIssuer("Batorov")// кто выдал
				.withExpiresAt(expirationDate)// когда перестанет работать
				.sign(Algorithm.HMAC256(secret))// секретный ключ
		;
	}

	public String ValidateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
				.withSubject("User details")// должен быть такой смысл
				.withIssuer("Batorov")// должен быть определенный автор
				.build();

		DecodedJWT jwt = verifier.verify(token);
		return jwt.getClaim("username").asString();
	}
}
