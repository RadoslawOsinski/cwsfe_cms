package eu.com.cwsfe.cms.web.login;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Radosław Osiński
 */
public class CmsUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -8942611273704893882L;

    public CmsUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public CmsUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
