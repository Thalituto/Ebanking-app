package com.example.customerservice.sec;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities,jwt.getClaim("preferred_username"));
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String , Object> realmAccess;
        Collection<String> roles;
        if(jwt.getClaim("realm_access")==null){
            return Set.of();
        }
        realmAccess = jwt.getClaim("realm_access");
        roles = (Collection<String>) realmAccess.get("roles");
        return roles.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
    }

}

/**
 * {
 *   "exp": 1705226926,
 *   "iat": 1705226626,
 *   "jti": "7c390e98-6d6b-46ca-a325-21e563f3a0f7",
 *   "iss": "http://localhost:8080/realms/ebank-realm",
 *   "aud": "account",
 *   "sub": "874bbb2c-4da4-4cae-aba5-e83b347becdb",
 *   "typ": "Bearer",
 *   "azp": "ebank-client",
 *   "session_state": "3e1dcd35-7a67-43ab-95cd-93e116285251",
 *   "acr": "1",
 *   "allowed-origins": [
 *     "/*"
 *   ],
 *   "realm_access": {
 *     "roles": [
 *       "offline_access",
 *       "default-roles-ebank-realm",
 *       "uma_authorization",
 *       "USER"
 *     ]
 *   },
 *   "resource_access": {
 *     "account": {
 *       "roles": [
 *         "manage-account",
 *         "manage-account-links",
 *         "view-profile"
 *       ]
 *     }
 *   },
 *   "scope": "profile email",
 *   "sid": "3e1dcd35-7a67-43ab-95cd-93e116285251",
 *   "email_verified": false,
 *   "name": "Thali Tutondele",
 *   "preferred_username": "user1",
 *   "given_name": "Thali",
 *   "family_name": "Tutondele",
 *   "email": "user1@isga.ma"
 * }
 *
 *
 */