package com.mycujoo.controller;

import java.time.Instant;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

@RestController
@RequestMapping(value="/jwt")
public class MyController {
	
	@GetMapping
	public String test(){
		String jwt = "";
		jwt = Jwts.builder()
		.setHeaderParam("typ", "JWT")
		.setIssuer("MyCujoo Service")
		.setSubject("Subject Example")
		.claim("name", "Leonardo Narciso")
	    .claim("scope", "Developer")
	    .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
	    .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
	    .signWith(
	      SignatureAlgorithm.HS256,
	      TextCodec.BASE64.encode("secret key example")
	    )
	    .compact();
		return jwt;
	}
	
	@PostMapping(value="/decode", produces="application/json", consumes="application/base64")
	public String decodeJWT(@RequestBody String jwtToken, HttpServletResponse response){
		
		response.setCharacterEncoding("UTF-8");
		
		String jsonResponse = "";	
		try{
			Base64 base64 = new Base64(true);
			String[] splitJwt = jwtToken.split("\\.");
			String headerJwtToken = new String(base64.decode(splitJwt[0]));
			String bodyJwtToken = new String(base64.decode(splitJwt[1]));
			String signatureJwtToken = new String(base64.decode(splitJwt[2]));
			
			jsonResponse =  headerJwtToken + bodyJwtToken + signatureJwtToken;
			response.getWriter().write(jsonResponse);
		}catch(Exception e){
			
		}
	
		return response.getContentType();
	}

}
