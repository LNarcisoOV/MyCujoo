package com.mycujoo.controller;

import java.time.Instant;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

@RestController(value = "/test")
public class MyController {
	

	@RequestMapping(value="/jwt", method=RequestMethod.GET)
	public String test(){
		String jwt = "";
		jwt = Jwts.builder()
		.setIssuer("Issuer")
		.setSubject("Subject")
		.claim("name", "Micah Silverman")
	    .claim("scope", "admins")
	    .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
	    .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
	    .signWith(
	      SignatureAlgorithm.HS256,
	      TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
	    )
	    .compact();
		return jwt;
	}

}
