package com.jumkid.activity.security.jwt;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public class TokenUser {

    private String authorizationToken;

    private String username;
    private String displayName;
    private String givenName;
    private String familyName;
    private String email;
    private List<Integer> companyIds;
    private List<String> roles;

    public TokenUser(String authorizationToken) {
        this.authorizationToken = authorizationToken;

        JwtToken jwtToken = new JwtToken(authorizationToken);
        if (jwtToken.getTokenData() != null) {
            JwtTokenData tokenData = jwtToken.getTokenData();

            username = tokenData.getUsername();
            displayName = tokenData.getDisplayName();
            givenName = tokenData.getGivenName();
            familyName = tokenData.getFamilyName();
            email = tokenData.getEmailAddress();
            companyIds = tokenData.getCompanyIds();
            if (tokenData.getRealmAccess() != null) {
                roles = tokenData.getRealmAccess().getOrDefault("roles", Lists.newArrayList());
            } else {
                roles = Lists.newArrayList();
            }
        }
    }

    public boolean isSuperAdmin() {
        return roles.contains("Super Admin");
    }
}