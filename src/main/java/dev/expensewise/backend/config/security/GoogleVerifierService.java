package dev.expensewise.backend.config.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author arpan
 * @since 10/9/25
 */
@Service
public class GoogleVerifierService {
    @Value("${app.google.android.client-id}")
    private String googleClientId;

    public GoogleIdToken.Payload verifyGoogleIdToken(String idTokenString) {
        try {
            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google Id accessToken, Verification Failed.");
            } else {
                return idToken.getPayload();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Google ID accessToken: " + e.getMessage(), e);
        }
    }
}
