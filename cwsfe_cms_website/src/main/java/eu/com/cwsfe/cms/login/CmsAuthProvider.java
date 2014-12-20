package eu.com.cwsfe.cms.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import eu.com.cwsfe.cms.dao.CmsRolesDAO;
import eu.com.cwsfe.cms.dao.CmsUsersDAO;
import eu.com.cwsfe.cms.model.CmsRole;
import eu.com.cwsfe.cms.model.CmsUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Radoslaw Osinski
 */
public class CmsAuthProvider implements AuthenticationProvider {

    @Autowired
    private CmsUsersDAO cmsUsersDAO;
    @Autowired
    private CmsRolesDAO cmsRolesDAO;

    @Override
    public boolean supports(Class<?> authentication) {
        return (CmsUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    public Authentication authenticate(Authentication auth) {
        final Object login = auth.getPrincipal();
        if (cmsUsersDAO.isActiveUsernameInDatabase(String.valueOf(login))) {
            final Object password = auth.getCredentials();
            CmsUser cmsUser = cmsUsersDAO.getByUsername((String) login);
            if (BCrypt.checkpw(String.valueOf(password), cmsUser.getPasswordHash())) {
                final List<CmsRole> cmsRoles = cmsRolesDAO.listUserRoles(cmsUser.getId());
                List<GrantedAuthority> authorities = new ArrayList<>(cmsRoles.size());
                for (CmsRole cmsRole : cmsRoles) {
                    authorities.add(new SimpleGrantedAuthority(cmsRole.getRoleCode()));
                }
                return new CmsUsernamePasswordAuthenticationToken(auth.getName(), password, authorities);
            }
        }
        throw new BadCredentialsException("Username/Password does not match for " + login);
    }

}