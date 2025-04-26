import com.astar.common.library.utils.JWTUtility;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jwt.EncryptedJWT;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.lang.Object;

public class JWTUtilityTest {
    public static void main(String[] args) throws JOSEException, ParseException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decodePem(Files.readString(Paths.get("public.pem")))));
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(
                new PKCS8EncodedKeySpec(decodePem(Files.readString(Paths.get("private.pem")))));
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "Arif");
        String token = JWTUtility.generateJWTToken(claims, JWEAlgorithm.RSA_OAEP_512, EncryptionMethod.A192GCM, publicKey);
        System.out.println(token);
        EncryptedJWT jwt = JWTUtility.decryptJWTToken(token, privateKey);
        String email = JWTUtility.getClaimsValue(jwt, "email");
        System.out.print(email);
    }

    public static byte[] decodePem(String pem) {
        return Base64.getDecoder().decode(
                pem.replaceAll("-----\\w+ PRIVATE KEY-----", "")
                        .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                        .replaceAll("\\s", "")
        );
    }
}
