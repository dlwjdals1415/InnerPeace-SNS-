package com.social.innerPeace.handler;

import com.social.innerPeace.detail.MemberDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        setDefaultTargetUrl("/board/post/list");
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        HttpSession session = request.getSession();
        session.setAttribute("loginedMember", memberDetails.getNickName());
        super.onAuthenticationSuccess(request,response,authentication);
    }

}
