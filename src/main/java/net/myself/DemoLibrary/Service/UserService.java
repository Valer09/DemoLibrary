package net.myself.DemoLibrary.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
	public String getUserIdFromToken() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null)
		{
			Object principal = authentication.getPrincipal();
			if(principal instanceof Jwt) return ((Jwt) principal).getSubject();
		}
		
		return null;
	}
}
