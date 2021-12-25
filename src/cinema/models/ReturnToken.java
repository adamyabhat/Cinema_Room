package cinema.models;

import java.util.UUID;

public class ReturnToken {
    private UUID Token;
    public UUID getToken() {
        return Token;
    }

    public void setToken(UUID token) {
        Token = token;
    }
}
