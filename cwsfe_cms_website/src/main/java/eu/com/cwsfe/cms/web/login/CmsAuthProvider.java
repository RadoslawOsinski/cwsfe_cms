package eu.com.cwsfe.cms.web.login;

import eu.com.cwsfe.cms.db.users.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Radoslaw Osinski
 */
@Component
public class CmsAuthProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsAuthProvider.class);

    private CmsUsersRepository cmsUsersRepository;
    private CmsRolesRepository cmsRolesRepository;
    private CmsUserAllowedNetAddressRepository cmsUserAllowedNetAddressRepository;

    @Autowired
    public void setCmsUsersRepository(CmsUsersRepository cmsUsersRepository) {
        this.cmsUsersRepository = cmsUsersRepository;
    }

    @Autowired
    public void setCmsRolesRepository(CmsRolesRepository cmsRolesRepository) {
        this.cmsRolesRepository = cmsRolesRepository;
    }

    @Autowired
    public void setCmsUserAllowedNetAddressRepository(CmsUserAllowedNetAddressRepository cmsUserAllowedNetAddressRepository) {
        this.cmsUserAllowedNetAddressRepository = cmsUserAllowedNetAddressRepository;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CmsUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication auth) {
        final Object login = auth.getPrincipal();
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) auth.getDetails();
        String userIPAddress = webAuthenticationDetails.getRemoteAddress();
        LOGGER.debug("User login: {}, IP Address: {}", login, userIPAddress);

        if (cmsUsersRepository.isActiveUsernameInDatabase(String.valueOf(login))) {
            final Object password = auth.getCredentials();
            CmsUsersEntity cmsUser = cmsUsersRepository.getByUsername((String) login);
            boolean userIsUsingIPFiltering = userIsUsingIPFiltering(cmsUser.getId());
            if (!userIsUsingIPFiltering || (userIsUsingAllowedAddress(userIPAddress, cmsUser.getId()))) {
                if (BCrypt.checkpw(String.valueOf(password), cmsUser.getPasswordHash())) {
                    final List<CmsRolesEntity> cmsRoles = cmsRolesRepository.listUserRoles(cmsUser.getId());
                    List<GrantedAuthority> authorities = new ArrayList<>(cmsRoles.size());
                    for (CmsRolesEntity cmsRole : cmsRoles) {
                        authorities.add(new SimpleGrantedAuthority(cmsRole.getRoleCode()));
                    }
                    LOGGER.info("User {} has granted access with roles: {}", login, cmsRoles.stream().map(CmsRolesEntity::getRoleName).collect(Collectors.toList()));
                    return new CmsUsernamePasswordAuthenticationToken(auth.getName(), password, authorities);
                } else {
                    LOGGER.error("User {} password is incorrect", login);
                }
            } else {
                LOGGER.error("User {} was trying to login from not allowed address {}", login, userIPAddress);
            }
        } else {
            LOGGER.error("User {} is not active", login);
        }
        throw new BadCredentialsException("Username/Password does not match for " + login);
    }

    /**
     * @param userIPAddress user IP address
     * @return true if user is allowed to enter from this IP. Otherwise false.
     */
    private boolean userIsUsingAllowedAddress(String userIPAddress, long userId) {
        List<CmsUserAllowedNetAddressEntity> cmsUserAllowedNetAddresses = cmsUserAllowedNetAddressRepository.listForUser(userId);
        return cmsUserAllowedNetAddresses.stream().
            map(CmsUserAllowedNetAddressEntity::getInetAddress).collect(Collectors.toList()).
            contains(userIPAddress);
    }

    /**
     * @return true if user is using IP filtering during logging in. Otherwise false.
     */
    private boolean userIsUsingIPFiltering(long userId) {
        return cmsUserAllowedNetAddressRepository.countAddressesForUser(userId) > 0;
    }

}
