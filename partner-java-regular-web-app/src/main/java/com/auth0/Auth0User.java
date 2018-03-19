package com.auth0;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Auth0 User Profile information. Implements Principal interface -
 * represents the abstract notion of a principal, which can be used
 * to represent any entity, such as an individual or login id.
 */
public class Auth0User implements Principal, Serializable {

    private static final long serialVersionUID = 2371882820082543721L;

    final Map<String, Object> attributes;

    public Auth0User(final Map<String, Object> attributes) {
      this.attributes = attributes;
    }

    public String getUserId() {
      final String sub = (String) attributes.get("sub");
      return sub;
    }

    public boolean isEmailVerified() {
      final boolean emailVerified = (Boolean) attributes.get("email_verified");
      return emailVerified;
    }

    @Override
    public String getName() {
      final String name = (String) attributes.get("name");
      return name;
    }

    public String getNickname() {
      final String nickname = (String) attributes.get("nickname");
      return nickname;
    }

    public String getPicture() {
      final String picture = (String) attributes.get("picture");
      return picture;
    }

    public String getEmail() {
      final String email = (String) attributes.get("email");
      return email;
    }

    public Date getCreatedAt() {
      Date createdAt = null;
      final String createdAtStr = (String) attributes.get("created_at");
      if (createdAtStr != null) {
        final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
          createdAt = format.parse(createdAtStr);
        } catch (Exception e) {
            // ignore
        }
      }
      return createdAt;
    }

    public List<LinkedHashMap> getIdentities() {
       final List<LinkedHashMap> identities = (List<LinkedHashMap>) attributes.get("identities");
       return Collections.unmodifiableList(identities);
    }

    public Map<String, Object> getUserMetadata() {
      final Map<String, Object> userMetadata = (Map<String, Object>) attributes.get("user_metadata");
      return userMetadata;
    }

    public Map<String, Object> getAppMetadata() {
      final Map<String, Object> appMetadata = (Map<String, Object>) attributes.get("app_metadata");
      return appMetadata;
    }

    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final Auth0User rhs = (Auth0User) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(getUserId(), rhs.getUserId())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(getUserId()).
                toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).
                append("userId", getUserId()).
                append("name", getName()).
                append("email", getEmail()).
                toString();
    }
 }
