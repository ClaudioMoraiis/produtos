package com.example.demo.jwt;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService fTokenService;

    @Autowired
    UserRepository fUsuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest mRequest, HttpServletResponse mResponse, FilterChain mFilterChain) throws ServletException, IOException {
        var mToken = this.recoverToken(mRequest);
        if (mToken != null){
            var mEmail = fTokenService.validateToken(mToken);
            UserDetails mUser = fUsuarioRepository.findByEmail(mEmail);

            var mAuthentication = new UsernamePasswordAuthenticationToken(mUser, null, mUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(mAuthentication);
        }
        mFilterChain.doFilter(mRequest, mResponse);

    }

    private String recoverToken(HttpServletRequest mRequest){
        var mAuthHeader = mRequest.getHeader("Authorization");
        if (mAuthHeader == null) return null;

        return mAuthHeader.replace("Bearer ", "");
    }
}
