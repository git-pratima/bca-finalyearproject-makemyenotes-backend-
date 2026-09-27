package com.pratima.makemyenotes.security;

import com.pratima.makemyenotes.entity.User;
import com.pratima.makemyenotes.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    // ✅ Add @Lazy on UserService to break the cycle
    public OAuth2SuccessHandler(JwtUtils jwtUtils, @Lazy UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();

        String email    = oAuth2User.getAttribute("email");
        String name     = oAuth2User.getAttribute("name");
        String provider = token.getAuthorizedClientRegistrationId();

        System.out.println("OAuth2 login success: email, provider"+email+" "+provider);
        log.info("OAuth2 login success: email={}, provider={}", email, provider);

        // Find or create user
        User user1 = (User) userService.findOrCreateSocialUser(email, name, provider);

        // Generate JWT
        String jwt = jwtUtils.generateToken(email);

       // UserDetails user = userService.findOrCreateSocialUser(email, name, provider);
       // String jwt = jwtUtils.generateToken(email);

        String redirectUrl = frontendUrl + "/auth/social-callback?token=" + jwt +"&role="+user1.getRole().name()+"&username="+user1.getName();
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
