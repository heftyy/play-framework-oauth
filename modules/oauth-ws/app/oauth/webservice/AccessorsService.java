package oauth.webservice;

import oauth.messages.WSValidateResponse;

import java.util.stream.Collectors;

public interface AccessorsService {
    Accessor validateToken(String token, String requestedScope);

    default Accessor getAccessor(WSValidateResponse response) {
        return new Accessor(
                response.getAccessorId(),
                response.getAccessToken(),
                response.getAllowedScopes() == null ? null :
                        response.getAllowedScopes().stream().map(SecurityRole::new).collect(Collectors.toList())
        );
    }
}
