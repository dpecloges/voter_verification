package com.ots.dpel.android;



import com.auth0.android.jwt.JWT;

import org.junit.Test;

/**
 * Created by tasos on 11/10/2017.
 */

public class JwtTest {

    /*private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbGVjdGlvbkNlbnRlck5hbWUiO" +
            "iIyzr8gzpXOus67zr_Os865zrrPjCDOms6tzr3PhM-Bzr8gzpjOtc-Dz4POsc67zr_Ovc6vzrrOt8-CIiwiYXVk" +
            "IjpbImRwZWxfcmVzIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImVsZWN" +
            "0aW9uRGVwYXJ0bWVudElkIjo3LCJmdWxsTmFtZSI6Is6UzrnOsc-HzrXOuc-BzrnPg8-Ezq7PgiDOo8-Fz4PPhM" +
            "6uzrzOsc-Ezr_PgiIsImVsZWN0aW9uQ2VudGVySWQiOjMsImV4cCI6MTUwNzY4OTQ1OSwiYXV0aG9yaXRpZXMiO" +
            "lsibWcuZWxlY3Rpb25kZXBhcnRtZW50LnVwZGF0ZSIsInJzIiwibWcuZWxlY3Rpb25jZW50ZXIudXBkYXRlIiwi" +
            "bWcuZWxlY3Rpb25kZXBhcnRtZW50LmRlbGV0ZSIsIm1nLmVsZWN0aW9uZGVwYXJ0bWVudC5wcmludCIsIm1nLmV" +
            "sZWN0aW9uY2VudGVyLmRlbGV0ZSIsIm1nLmVsZWN0aW9uY2VudGVyLnByaW50IiwibWcuZWxlY3Rpb25kZXBhcn" +
            "RtZW50LnZpZXciLCJtZy5lbGVjdGlvbmRlcGFydG1lbnQiLCJtZy5lbGVjdGlvbmNlbnRlci52aWV3IiwibWcuZ" +
            "WxlY3Rpb25kZXBhcnRtZW50LmNob29zZSIsIm1nLmVsZWN0aW9uY2VudGVyLmNyZWF0ZSIsIm1nLmVsZWN0aW9u" +
            "ZGVwYXJ0bWVudC5jcmVhdGUiLCJtZy5lbGVjdGlvbmNlbnRlci5saXN0IiwibWcuZWxlY3Rpb25jZW50ZXIuY2h" +
            "vb3NlIiwibWcuZWxlY3Rpb25jZW50ZXIiLCJtZy5lbGVjdGlvbmRlcGFydG1lbnQubGlzdCIsIm1nIl0sImp0aS" +
            "I6IjY0MDQwYjMzLTc2YTctNGJjMy05ODRiLTZmYzQwZGZkMDIxYSIsImVsZWN0aW9uRGVwYXJ0bWVudE5hbWUiO" +
            "iLOkScgzqTOvM6uzrzOsSAyzr_PhSDOlc66zrvOv86zzrnOus6_z40gzprOrc69z4TPgc6_z4UgzpjOtc-Dz4PO" +
            "sc67zr_Ovc6vzrrOt8-CIiwiY2xpZW50X2lkIjoiZHBlbF9jbCJ9.RFO4mHd5HcwqbT6mV-g1q6RYOM55TIZ4-9" +
            "CVCcOeDYY";*/
    private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbGVjdGlvbkNlbnRlck5hbWUiOiIyzr8gzpXOus67zr_Os865zrrPjCDOms6tzr3PhM-Bzr8gzpjOtc-Dz4POsc67zr_Ovc6vzrrOt8-CIiwiYXVkIjpbImRwZWxfcmVzIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImVsZWN0aW9uRGVwYXJ0bWVudElkIjo3LCJmdWxsTmFtZSI6Is6UzrnOsc-HzrXOuc-BzrnPg8-Ezq7PgiDOo8-Fz4PPhM6uzrzOsc-Ezr_PgiIsImVsZWN0aW9uQ2VudGVySWQiOjMsImV4cCI6MTUwNzc1NTc3MSwiYXV0aG9yaXRpZXMiOlsibWcuZWxlY3Rpb25kZXBhcnRtZW50LnVwZGF0ZSIsInJzIiwibWcuZWxlY3Rpb25jZW50ZXIudXBkYXRlIiwibWcuZWxlY3Rpb25kZXBhcnRtZW50LmRlbGV0ZSIsIm1nLmVsZWN0aW9uZGVwYXJ0bWVudC5wcmludCIsIm1nLmVsZWN0aW9uY2VudGVyLmRlbGV0ZSIsIm1nLmVsZWN0aW9uY2VudGVyLnByaW50IiwibWcuZWxlY3Rpb25kZXBhcnRtZW50LnZpZXciLCJtZy5lbGVjdGlvbmRlcGFydG1lbnQiLCJtZy5lbGVjdGlvbmNlbnRlci52aWV3IiwibWcuZWxlY3Rpb25kZXBhcnRtZW50LmNob29zZSIsIm1nLmVsZWN0aW9uY2VudGVyLmNyZWF0ZSIsIm1nLmVsZWN0aW9uZGVwYXJ0bWVudC5jcmVhdGUiLCJtZy5lbGVjdGlvbmNlbnRlci5saXN0IiwibWcuZWxlY3Rpb25jZW50ZXIuY2hvb3NlIiwibWcuZWxlY3Rpb25jZW50ZXIiLCJtZy5lbGVjdGlvbmRlcGFydG1lbnQubGlzdCIsIm1nIl0sImp0aSI6IjljODQxNzEyLTRlMmEtNDE5NC1hNDFiLWM5ZGI3NjIwNDdhYiIsImVsZWN0aW9uRGVwYXJ0bWVudE5hbWUiOiLOkScgzqTOvM6uzrzOsSAyzr_PhSDOlc66zrvOv86zzrnOus6_z40gzprOrc69z4TPgc6_z4UgzpjOtc-Dz4POsc67zr_Ovc6vzrrOt8-CIiwiY2xpZW50X2lkIjoiZHBlbF9jbCJ9.qOB0vGywXEcaZ6vzfXTFILJEKmjnIhdulWSHlUfOHII";
    private final String SIGNING_KEY = "|g}vh7YdDqU5zmRN";

    @Test
    public void testVerifyNew() {



    }

    /*@Test
    public void testVerifyToken() {

        DecodedJWT jwt = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SIGNING_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("client_id", "dpel_cl")
                    .build(); //Reusable verifier instance
            jwt = verifier.verify(token);
        } catch (UnsupportedEncodingException | JWTVerificationException e){
            System.out.println(e.getMessage());
        }
        assertNotNull(jwt);
    }

    @Test
    public void testDecodeJwtToken() {
        String electionCenterName = null;
        String username = null;
        long electionDepartmentId = 0;
        String fullName = null;
        long electionCenterId = 0;
        long exp = 0;
        List<String> authorities = null;
        String electionDepartmentName = null;

        try {

            DecodedJWT jwt = JWT.decode(token);

            electionCenterName = jwt.getClaim("electionCenterName").asString();
            username = jwt.getClaim("user_name").asString();
            electionDepartmentId = jwt.getClaim("electionDepartmentId").asLong();
            fullName = jwt.getClaim("fullName").asString();
            electionCenterId = jwt.getClaim("electionCenterId").asLong();
            exp = jwt.getClaim("exp").asLong();
            electionDepartmentName = jwt.getClaim("electionDepartmentName").asString();
            authorities = jwt.getClaim("authorities").asList(String.class);

        } catch (JWTDecodeException exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }

        assertNotNull(electionCenterName);
        assertNotNull(username);
        assertNotNull(electionDepartmentId);
        assertNotNull(fullName);
        assertNotNull(electionCenterId);
        assertNotNull(exp);
        assertNotNull(electionDepartmentName);
        assertNotNull(authorities);

        System.out.println("electionCenterName: " + electionCenterName);
        System.out.println("username: " + username);
        System.out.println("electionDepartmentId: " + electionDepartmentId);
        System.out.println("fullName: " + fullName);
        System.out.println("electionCenterId: " + electionCenterId);
        System.out.println("exp: " + exp);
        System.out.println("electionDepartmentName: " + electionDepartmentName);
        System.out.println("Authorities { ");
        for (String authority : authorities) {
            System.out.println("    " + authority);
        }
        System.out.println("}");

    }*/

}
