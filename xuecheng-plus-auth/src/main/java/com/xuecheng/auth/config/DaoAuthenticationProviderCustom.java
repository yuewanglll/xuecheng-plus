package com.xuecheng.auth.config;

import com.xuecheng.ucenter.service.UserDetailsService;
import com.xuecheng.ucenter.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DaoAuthenticationProviderCustom extends DaoAuthenticationProvider {


    @Autowired
    public void setUserDetailsService(UserServiceImpl userServiceImpl) {
        super.setUserDetailsService(userServiceImpl);
    }


    //屏蔽密码对比
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {


    }

}