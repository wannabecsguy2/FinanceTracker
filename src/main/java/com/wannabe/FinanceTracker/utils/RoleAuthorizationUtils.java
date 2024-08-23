package com.wannabe.FinanceTracker.utils;

import com.wannabe.FinanceTracker.model.Role;
import com.wannabe.FinanceTracker.repository.RoleRepository;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Slf4j
@Service
public class RoleAuthorizationUtils {
    @Autowired
    private RoleRepository roleRepository;
    public boolean isUserAuthorized(UserPrincipal userPrincipal, String lowestRoleName) {
        // Get the highest in auth role of user
         Role highestUserRoleLevel = userPrincipal.getRoles().stream().min(Comparator.comparing(Role::getLevel)).orElseThrow(() -> new RuntimeException("User has no roles"));
         Role lowestRoleAllowed = roleRepository.findByName(lowestRoleName).orElseThrow(() -> new RuntimeException("Role not found"));

         return highestUserRoleLevel.getLevel() <= lowestRoleAllowed.getLevel();
    }
}
