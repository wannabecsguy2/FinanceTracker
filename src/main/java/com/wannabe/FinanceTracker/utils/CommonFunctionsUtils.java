package com.wannabe.FinanceTracker.utils;

import com.wannabe.FinanceTracker.model.UserProfile;
import com.wannabe.FinanceTracker.payload.SignUpRequest;
import com.wannabe.FinanceTracker.payload.UpdateProfileRequest;
import org.springframework.stereotype.Service;

@Service
public class CommonFunctionsUtils {
    public void mapUserProfile(UserProfile userProfile, UpdateProfileRequest request) {
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setBirthDate(request.getBirthDate());
    }

    public void mapUserProfile(UserProfile userProfile, SignUpRequest request) {
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setBirthDate(request.getBirthDate());
    }
}
