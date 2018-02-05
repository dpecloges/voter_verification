package com.ots.dpel.android.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.ots.dpel.android.Constants;
import com.ots.dpel.android.rest.dto.AttachmentType;
import com.ots.dpel.android.rest.dto.CandidateDto;
import com.ots.dpel.android.rest.dto.ElectionDepartmentTimerDto;
import com.ots.dpel.android.rest.dto.ResultsDto;
import com.ots.dpel.android.rest.dto.Token;
import com.ots.dpel.android.rest.dto.VerificationArgs;
import com.ots.dpel.android.rest.dto.VerificationDto;
import com.ots.dpel.android.rest.dto.VoterDto;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by tasos on 10/10/2017.
 */

public interface DpelApiService {

    @FormUrlEncoded
    @Headers("App-Version: " + Constants.VERSION)
    @POST("oauth/token")
    Call<Token> login(@Header("Authorization") String key,
                      @Field("grant_type") String grantType,
                      @Field("username") String username,
                      @Field("password") String password);


    /**
     * Returns verification data for the voter
     * Requires header Authorization: Bearer token
     *
     * @param auth
     * @param verificationArgs
     * @return
     */
    @Headers("App-Version: " + Constants.VERSION)
    @POST("ep/verification/verify")
    Call<VerificationDto> verify(@Header("Authorization") String auth,
                                 @Body VerificationArgs verificationArgs);

    /**
     * Verifies that a voter has indeed voted
     * Requires header Authorization: Bearer token
     *
     * @param auth
     * @param voterDto
     * @return
     */
    @Headers("App-Version: " + Constants.VERSION)
    @POST("ep/verification/savevoter")
    Call<VoterDto> saveVoter(@Header("Authorization") String auth,
                             @Body VoterDto voterDto);

    @Headers("App-Version: " + Constants.VERSION)
    @POST("ep/verification/undovote")
    Call<Boolean> undoVote(@Header("Authorization") String auth,
                            @Query("voterId") Long voterId);

    @Headers("App-Version: " + Constants.VERSION)
    @GET("ep/verification/votercount")
    Call<JsonNode> getVoterCountByElectionDepartmentId(@Header("Authorization") String auth,
                                                       @Query("electionDepartmentId") Long electionDepartmentId);

    /**
     * Sends the electoral results at the end of the voting process
     *
     * @param auth
     * @param resultsDto
     * @return
     */
    @Headers("App-Version: " + Constants.VERSION)
    @POST("rs/submission/save/results")
    Call<ResultsDto> saveResults(@Header("Authorization") String auth,
                                 @Body ResultsDto resultsDto);

    @Headers("App-Version: " + Constants.VERSION)
    @GET("rs/candidates/findcurrent")
    Call<List<CandidateDto>> getCandidatesForElectionProcedure(@Header("Authorization") String auth);

    @Multipart
    @Headers("App-Version: " + Constants.VERSION)
    @POST("rs/submission/upload/{electionDepartmentId}/{attachmentType}")
    Call<JsonNode> uploadFile(@Header("Authorization") String auth,
                              @Path("electionDepartmentId") Long departmentId,
                              @Path("attachmentType") AttachmentType attachmentType,
                              @Part MultipartBody.Part filePart);

    @GET("update.txt")
    Call<ResponseBody> checkVersion();


    @POST("mg/electiondepartment/timerstart")
    Call<Boolean> timerStart(@Header("Authorization") String auth);

    @POST("mg/electiondepartment/timerend")
    Call<Boolean> timerEnd(@Header("Authorization") String auth);

    @POST("mg/electiondepartment/gettimer")
    Call<ElectionDepartmentTimerDto> getUserElectionDepartmentTimer(@Header("Authorization") String auth);
}
