package oauth.webservice;

import com.fasterxml.jackson.databind.JsonNode;
import oauth.messages.WebServiceValidateRequest;
import oauth.messages.WebServiceValidateResponse;
import oauth.webservice.scopes.ScopesContainer;
import play.Configuration;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class AccessorsContainer {
	private Map<String, ValidAccessor> accessors = new HashMap<>();
	private static Configuration config = Configuration.root().getConfig("oauth-ws");

    private final ScopesContainer scopesContainer;

    @Inject
    public AccessorsContainer(ScopesContainer scopesContainer) {
        this.scopesContainer = scopesContainer;
    }

    /**
	 * Create a MD5 hash from client's accessorId + client's IP + client's USER-AGENT.
	 *
	 * @param accessorId
	 *            String: Client's accessorId.
	 * @param ip
	 *            String: Client's IP.
	 * @param userAgent
	 *            String: Client's USER-AGENT.
	 * @return MessageDigest: MD5 hash of the above.
	 */
	static public MessageDigest createMD5Hash(String accessorId, String ip, String userAgent) {
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("MD5");
			md.update(accessorId.getBytes("UTF-8"));
			md.update(ip.getBytes("UTF-8"));
			md.update(userAgent.getBytes("UTF-8"));
		} catch (Exception ignored) {

		}

		return md;
	}

	/**
	 * Creates new accessor in the memory if everything is OK.
     */
	private boolean createNewAccessor(String accessorId, String accessToken, String remoteAddress, String domain, String scope, String userAgent) {
        WebServiceValidateResponse response = askOauthIfAccessTokenValid(accessorId, accessToken, remoteAddress, domain, scope);
        if(response == null) return false;

        ValidAccessor newAccessor = new ValidAccessor(
                response.getAccessorId(),
                response.getAccessToken(),
                response.getClientRemoteAddress(),
                response.getExpiresAt(),
                response.getAllowedScopes().stream().map(SecurityRole::new).collect(Collectors.toList()),
                createMD5Hash(
                        response.getAccessorId(),
                        response.getClientRemoteAddress(), userAgent
                )
        );

        if (scopesContainer.checkIfClientAllowed(newAccessor, scope)) {
            accessors.put(response.getAccessorId(), newAccessor);
            return true;
        } else
            return false;
	}

	/**
	 * Find the right accessor using the MD5 hash. Called by deadbolt to find
	 * the accessor object or called by onRequest from Global class.
	 * 
	 * @param hashMD5
	 *            MessageDigest: MD5 hash of client's USER-AGENT header +
	 *            client's IP + client's accessorId.
	 * @return ValidAccessor: Object of the found accessor or null if not found.
	 */
	public ValidAccessor findAccessor(String accessorId, MessageDigest hashMD5) {
		ValidAccessor accessor = accessors.get(accessorId);
        if(accessor.getRequestHash().equals(hashMD5)) return accessor;

		return null;
	}

	/**
	 * Send a POST request to the oauth authorization server to validate the
	 * data sent by a client. It uses play framework's F.Promise. Sends a JSON
	 * with data about the client and receives a JSON including
	 * token_valid:true/false and level client is allowed to.
	 * 
	 * @param accessorId
	 *            String: Client's accessorId.
	 * @param accessToken
	 *            String: Access token granted by oauth authorization server.
	 * @param remoteAddress
	 *            String: Client's IP.
	 * @param domain
	 *            String: Domain of the webservice registered in oauth.
	 * @param scope
	 *            String: Scope client asked for.
	 * @return JsonNode: JSON with all the data received from oauth.
	 */
	private WebServiceValidateResponse askOauthIfAccessTokenValid(String accessorId, String accessToken, String remoteAddress, String domain, String scope) {
        WebServiceValidateRequest request = new WebServiceValidateRequest(
                accessorId,
                accessToken,
                remoteAddress,
                scope,
                domain
        );

        Promise<WSResponse> promise = WS.url(config.getString("validate-token")).setHeader("Content-Type", "application/json").post(request.getJson());

        String body = promise.get(10000L).getBody();

        JsonNode responseJson = Json.parse(body);
        WebServiceValidateResponse response = Json.fromJson(responseJson, WebServiceValidateResponse.class);
        if(response != null && response.isTokenValid()) return response;
        return null;
	}

    /**
	 * Validates the request sent by client. Checks if an accessor with client's
	 * data already exists. If not then method attempts to create the accessor.
	 *
     * @param accessorId from authorize Json
     * @param accessToken from authorize Json
     * @param scope from authorize Json
	 * @param remoteAddress
	 *            String: Client's IP.
	 * @param domain
	 *            String: Domain of this webservice.
	 * @param userAgent
	 *            String: Client's USER-AGENT.
	 * @return Boolean: True if the accessor has been validated, false if not.
	 */
	public boolean validateAccessor(String accessorId, String accessToken, String scope, String remoteAddress, String domain, String userAgent) {
		ValidAccessor accessor = accessors.get(accessorId);

        MessageDigest md = createMD5Hash(accessorId, remoteAddress, userAgent);

        if(accessor == null) { // no accessor present yet
            return createNewAccessor(accessorId, accessToken, remoteAddress, domain, scope, userAgent);
        } else if(accessor.getTokenExpiresAt() > System.currentTimeMillis()) { // accessor expired
            accessors.remove(accessorId);
            return createNewAccessor(accessorId, accessToken, remoteAddress, domain, scope, userAgent);
        } else if (accessor.getAccessToken().equals(accessToken) &&
                accessor.getRemoteAddress().equals(remoteAddress) &&
                accessor.getRequestHash().equals(md)) { // accessor is in memory and is valid
            if (scopesContainer.checkIfClientAllowed(accessor, scope)) return true;
        }

		return false;
	}
}
