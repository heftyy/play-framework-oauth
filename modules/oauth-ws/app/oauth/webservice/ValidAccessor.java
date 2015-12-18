package oauth.webservice;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;

public class ValidAccessor implements Subject {

    protected String accessorId;
    protected String accessToken;
    protected String remoteAddress;
    protected byte[] requestHashBytes;
    protected Long tokenExpiresAt;
    protected List<SecurityRole> allowedLevels;
    protected MessageDigest requestHash;

    @Override
    public String getIdentifier() {
        return null;
    }

    public ValidAccessor(String accessorId, String accessToken, String remoteAddress, Long tokenExpiresAt, List<SecurityRole> allowedLevels, MessageDigest requestHash) {
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.remoteAddress = remoteAddress;
        this.tokenExpiresAt = tokenExpiresAt;
        this.allowedLevels = allowedLevels;
        this.requestHash = requestHash;
    }

    /**
     * Get a list of level names client is allowed to access.
     *
     * @return List<String>: List of level names.
     */
    public List<String> getAllowedLevels() {
        return allowedLevels.stream().map(allowedLevel -> allowedLevel.roleName).collect(Collectors.toList());
    }

    @Override
    public List<? extends Role> getRoles() {
        return allowedLevels;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    public String getAccessorId() {
        return accessorId;
    }

    public void setAccessorId(String accessorId) {
        this.accessorId = accessorId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public byte[] getRequestHashBytes() {
        return requestHashBytes;
    }

    public void setRequestHashBytes(byte[] requestHashBytes) {
        this.requestHashBytes = requestHashBytes;
    }

    public Long getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    public void setTokenExpiresAt(Long tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }

    public void setAllowedLevels(List<SecurityRole> allowedLevels) {
        this.allowedLevels = allowedLevels;
    }

    public MessageDigest getRequestHash() {
        return requestHash;
    }

    public void setRequestHash(MessageDigest requestHash) {
        this.requestHash = requestHash;
    }
}
