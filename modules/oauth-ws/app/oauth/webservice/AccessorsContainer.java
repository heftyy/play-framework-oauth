package oauth.webservice;

public interface AccessorsContainer {
    ValidAccessor createNewAccessor(String accessorId, String accessToken, String remoteAddress, String domain, String scope, String userAgent);
    ValidAccessor validateAccessor(String accessorId, String accessToken, String scope, String remoteAddress, String domain, String userAgent);
    ValidAccessor findAccessor(String accessorId, byte[] digest);
}
