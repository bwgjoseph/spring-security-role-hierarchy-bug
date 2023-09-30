package com.bwgjoseph.springsecurityrolehierarchybug;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import lombok.ToString;

@ToString
public class MyUserDetails implements UserDetails {
    private final String username;
    private final String email;
    /**
     * Roles represent coarse-grained access rights, prefixed by ROLE_
     */
    private final List<String> roles;
    /**
     * Authorities represent fine-grained access rights
     */
    private final Set<GrantedAuthority> authorities;

    public MyUserDetails(String username, String email, List<String> roles, List<String> authorities) {
        this.username = username;
        this.email = email;
        this.roles = roles.stream().map(role -> "ROLE_"+role).toList();
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities.stream().map(SimpleGrantedAuthority::new).toList()));
    }

    @Override
	public Collection<GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

    @Override
    public String getPassword() {
        return "N/A";
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		// Ensure array iteration order is predictable (as per
		// UserDetails.getAuthorities() contract and SEC-717)
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}
		return sortedAuthorities;
	}

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

		@Override
		public int compare(GrantedAuthority g1, GrantedAuthority g2) {
			// Neither should ever be null as each entry is checked before adding it to
			// the set. If the authority is null, it is a custom authority and should
			// precede others.
			if (g2.getAuthority() == null) {
				return -1;
			}
			if (g1.getAuthority() == null) {
				return 1;
			}
			return g1.getAuthority().compareTo(g2.getAuthority());
		}

	}

}
