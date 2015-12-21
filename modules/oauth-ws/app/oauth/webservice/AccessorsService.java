package oauth.webservice;

import oauth.messages.WebServiceValidateResponse;

import java.util.stream.Collectors;

public interface AccessorsService {
    Accessor validateToken(String token, String requestedScope);

    default Accessor getAccessor(WebServiceValidateResponse response) {
        return new Accessor(
                response.getAccessorId(),
                response.getAccessToken(),
                response.getAllowedLevels().stream().map(SecurityRole::new).collect(Collectors.toList())
        );
    }
}
