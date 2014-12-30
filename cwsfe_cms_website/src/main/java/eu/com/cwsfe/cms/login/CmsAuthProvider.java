package eu.com.cwsfe.cms.login;

import eu.com.cwsfe.cms.dao.CmsUserAllowedNetAddressDAO;
import eu.com.cwsfe.cms.model.CmsUserAllowedNetAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Radoslaw Osinski
 */
public class CmsAuthProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LogManager.getLogger(CmsAuthProvider.class);

    @Autowired
    private CmsUsersDAO cmsUsersDAO;
    @Autowired
    private CmsRolesDAO cmsRolesDAO;
    @Autowired
    private CmsUserAllowedNetAddressDAO cmsUserAllowedNetAddressDAO;

    @Override
    public boolean supports(Class<?> authentication) {
        return (CmsUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    public Authentication authenticate(Authentication auth) {
        final Object login = auth.getPrincipal();
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) auth.getDetails();
        String userIPAddress = webAuthenticationDetails.getRemoteAddress();
        LOGGER.debug("User login: " + login + ", IP Address: " + userIPAddress);

        if (cmsUsersDAO.isActiveUsernameInDatabase(String.valueOf(login))) {
            final Object password = auth.getCredentials();
            CmsUser cmsUser = cmsUsersDAO.getByUsername((String) login);
            boolean userIsUsingIPFiltering = userIsUsingIPFiltering(cmsUser.getId());
            if (!userIsUsingIPFiltering || (userIsUsingAllowedAddress(userIPAddress, cmsUser.getId()))) {
                if (BCrypt.checkpw(String.valueOf(password), cmsUser.getPasswordHash())) {
                    final List<CmsRole> cmsRoles = cmsRolesDAO.listUserRoles(cmsUser.getId());
                    List<GrantedAuthority> authorities = new ArrayList<>(cmsRoles.size());
                    for (CmsRole cmsRole : cmsRoles) {
                        authorities.add(new SimpleGrantedAuthority(cmsRole.getRoleCode()));
                    }
                    LOGGER.info("User " + login + " has granted access with roles: " + cmsRoles.stream().map(CmsRole::getRoleName).collect(Collectors.toList()));
                    return new CmsUsernamePasswordAuthenticationToken(auth.getName(), password, authorities);
                } else {
                    LOGGER.error("User " + login + " password is incorrect");
                }
            } else {
                LOGGER.error("User " + login + " was trying to login from not allowed address " + userIPAddress);
            }
        } else {
            LOGGER.error("User " + login + " is not active");
        }
        throw new BadCredentialsException("Username/Password does not match for " + login);
    }

    /**
     * @param userIPAddress user IP address
     * @return true if user is allowed to enter from this IP. Otherwise false.
     */
    private boolean userIsUsingAllowedAddress(String userIPAddress, long userId) {
        List<CmsUserAllowedNetAddress> cmsUserAllowedNetAddresses = cmsUserAllowedNetAddressDAO.listForUser(userId);
        return cmsUserAllowedNetAddresses.stream().
                map(CmsUserAllowedNetAddress::getInetAddress).collect(Collectors.toList()).
                contains(userIPAddress);
    }

    /**
     * @return true if user is using IP filtering during logging in. Otherwise false.
     */
    private boolean userIsUsingIPFiltering(long userId) {
        return cmsUserAllowedNetAddressDAO.countAddressesForUser(userId) > 0;
    }

}