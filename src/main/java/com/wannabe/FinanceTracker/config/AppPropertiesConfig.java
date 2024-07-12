package com.wannabe.FinanceTracker.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@Data
@ConfigurationPropertiesScan
public class AppPropertiesConfig {
    private final Auth auth = new Auth();

    @Data
    public static class Auth {
        @Value("${app.auth.jwtSecret}")
        private String secretKey;

        @Value("${app.auth.jwtValidityMsec}")
        private long jwtValidityMsec;

        public String getTokenSecret() {
            return secretKey;
        }

        public void setTokenSecret(String tokenSecret) {
            this.secretKey = tokenSecret;
        }

        public long getTokenExpirationMsec() {
            return jwtValidityMsec;
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.jwtValidityMsec = tokenExpirationMsec;
        }
    }
}
