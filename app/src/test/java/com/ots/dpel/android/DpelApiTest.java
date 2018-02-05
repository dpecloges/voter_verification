package com.ots.dpel.android;

import com.ots.dpel.android.rest.DpelApiService;
import com.ots.dpel.android.rest.DpelApiServiceFactory;
import com.ots.dpel.android.rest.dto.Token;
import com.ots.dpel.android.rest.dto.VerificationArgs;
import com.ots.dpel.android.rest.dto.VerificationDto;
import com.ots.dpel.android.utils.FileUtils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tasos on 10/10/2017.
 */

public class DpelApiTest {

    private DpelApiService dpelApiService = null;

    @Before
    public void setUp() throws Exception {
         dpelApiService = DpelApiServiceFactory.createDpelApiService("http://prselec.ots.gr:8080/dpelapi/").create(DpelApiService.class);
    }

    @Test
    public void testLogin() {

        String auth = Credentials.basic("dpel_cl", "c5xP$Uf5#[v)");

        Call<Token> call = dpelApiService.login(auth, "password", "admin", "otsdba");
        Token token = null;
        try {
            Response<Token> response = call.execute();
            if (response.isSuccessful()) {
                token = response.body();
            } else {
                System.out.println(response.errorBody().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull("Token was null", token);
        System.out.println(token.getAccessToken());

    }

    @Test
    public void testVoterVerification() {
        Token token = getToken();
        assertNotNull(token);

        VerificationDto verificationDto = null;
        if (token != null) {

            VerificationArgs args = new VerificationArgs();
            args.setEklSpecialNo("11111111");
            args.setFirstName("John");
            args.setLastName("Doe");

            Call<VerificationDto> call = dpelApiService.verify("Bearer " + token.getAccessToken(), args);
            try {
                Response<VerificationDto> response = call.execute();
                if (response.isSuccessful()) {
                    verificationDto = response.body();
                } else {
                    System.out.println(response.errorBody().string());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assertNotNull(verificationDto);
    }

    public Token getToken() {
        //DpelApiService dpelApiService = DpelApiServiceFactory.createDpelApiService().create(DpelApiService.class);
        String auth = Credentials.basic("dpel_cl", "c5xP$Uf5#[v)");

        Call<Token> call = dpelApiService.login(auth, "password", "admin", "otsdba");
        Token token = null;
        try {
            Response<Token> response = call.execute();
            if (response.isSuccessful()) {
                token = response.body();
            } else {
                System.out.println(response.errorBody().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    @Test
    public void testFileNameVersion() {
        assertEquals("1.11", FileUtils.getUpdateFileVersion("dpekloges-1.11-release.apk"));
        assertEquals("1.10", FileUtils.getUpdateFileVersion("dpekloges-1.10-release.apk"));
        assertEquals("3.123", FileUtils.getUpdateFileVersion("dpekloges-3.123-release.apk"));
        assertEquals("1.17", FileUtils.getUpdateFileVersion("dpekloges-1.17-prselec.apk"));
        assertEquals("1.17", FileUtils.getUpdateFileVersion("1.17-prselec"));
        assertEquals("1.17", FileUtils.getUpdateFileVersion("1.17"));


        assertEquals(1, FileUtils.checkVersionEquality(FileUtils.getUpdateFileVersion("dpekloges-3.123-release.apk"),
                FileUtils.getUpdateFileVersion("dpekloges-3.122-release.apk")));

        assertEquals(0, FileUtils.checkVersionEquality(FileUtils.getUpdateFileVersion("dpekloges-1.11-release.apk"),
                FileUtils.getUpdateFileVersion("dpekloges-1.110-release.apk")));

        assertEquals(-1, FileUtils.checkVersionEquality(FileUtils.getUpdateFileVersion("dpekloges-1.16-prselec.apk"),
                FileUtils.getUpdateFileVersion("dpekloges-1.17-prselec.apk")));
    }





}
