package com.mycujoo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.util.IOUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

@RestController
@RequestMapping(value="/jwt")
public class MyController {
	
	private static final int HTTP_CODE_SUCCESS = 200;
	
	@GetMapping
	public String test(){
		String jwt = "";
		jwt = Jwts.builder()
		.setHeaderParam("typ", "JWT")
		.setIssuer("MyCujoo Service")
		.setSubject("Subject Example")
		.claim("name", "Leonardo Narciso")
	    .claim("scope", "Admin")
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
			URL url = new URL("http://localhost:8080/MyCujoo/jwt");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			if(conn.getResponseCode() != HTTP_CODE_SUCCESS){
				throw new RuntimeException("HTTP error code : "+ conn.getResponseCode());
			}
			
			String[] splitJwt = jwtToken.split("\\.");
			String headerJwtToken = new String(TextCodec.BASE64.decode(splitJwt[0]));
			String bodyJwtToken = new String(TextCodec.BASE64.decode(splitJwt[1]));
			String signatureJwtToken = new String(TextCodec.BASE64.decode(splitJwt[2]));
			
			jsonResponse =  headerJwtToken + bodyJwtToken + signatureJwtToken;
			response.getWriter().write(jsonResponse);
		} catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
	
		return response.getContentType();
	}
	
	@PostMapping(value="/decodeJWTWithoutBody", produces="application/json")
	public String decodeJWT(HttpServletResponse response){		
		
		response.setCharacterEncoding("UTF-8");		
		String jsonResponse = "";	
		
		try{		
			
			InputStream is = new URL("http://localhost:8080/MyCujoo/jwt").openStream();
			
			String stringRetorno = IOUtils.toString(is);
			
			String[] splitJwt = stringRetorno.toString().split("\\.");
			String headerJwtToken = new String(TextCodec.BASE64.decode(splitJwt[0]));
			String bodyJwtToken = new String(TextCodec.BASE64.decode(splitJwt[1]));
			String signatureJwtToken = new String(TextCodec.BASE64.decode(splitJwt[2]));
			
			jsonResponse =  headerJwtToken + bodyJwtToken + signatureJwtToken;
			response.getWriter().write(jsonResponse);
		} catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
	
		return response.getContentType();
	}

}
