package com.wannabe.FinanceTracker.security.authentication;

import com.wannabe.FinanceTracker.model.OTPType;
import com.wannabe.FinanceTracker.model.User;
import com.wannabe.FinanceTracker.security.CustomUserDetailsService;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import com.wannabe.FinanceTracker.utils.CommonFunctionsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class OTPAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CommonFunctionsUtils commonFunctionsUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userIdentifier = authentication.getName();
        String otp = authentication.getCredentials().toString();

        UserPrincipal userPrincipal = customUserDetailsService.loadUserByUsername(userIdentifier);
        try {
            if (commonFunctionsUtils.verifyOtp(otp, userPrincipal.getId(), OTPType.LOGIN)) {
                return new UsernamePasswordAuthenticationToken(userPrincipal, otp, userPrincipal.getRoles());
            } else {
                throw new BadCredentialsException("Incorrect User identifier or OTP");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Incorrect User identifier or OTP");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
