package com.rsetiapp.core.data.remote

import com.rsetiapp.common.model.request.AttendanceBatchReq
import com.rsetiapp.common.model.request.AttendanceCandidateReq
import com.rsetiapp.common.model.request.AttendanceCheckReq
import com.rsetiapp.common.model.request.AttendanceInsertReq
import com.rsetiapp.common.model.request.BankIFSCSearchReq
import com.rsetiapp.common.model.request.BatchListReq
import com.rsetiapp.common.model.request.BlockReq
import com.rsetiapp.common.model.request.CandidateDetailsReq
import com.rsetiapp.common.model.request.CandidateListReq
import com.rsetiapp.common.model.request.CandidateSearchReq
import com.rsetiapp.common.model.request.CourseRequest
import com.rsetiapp.common.model.request.DistrictListReq
import com.rsetiapp.common.model.request.DistrictReq
import com.rsetiapp.common.model.request.EAPInsertRequest
import com.rsetiapp.common.model.request.EapAutofetchReq
import com.rsetiapp.common.model.request.EapListReq
import com.rsetiapp.common.model.request.FaceCheckReq
import com.rsetiapp.common.model.request.FacutlyDataReq
import com.rsetiapp.common.model.request.FogotPaasReq
import com.rsetiapp.common.model.request.FollowUpInsertReq
import com.rsetiapp.common.model.request.FollowUpTypeReq
import com.rsetiapp.common.model.request.FormRequest
import com.rsetiapp.common.model.request.GramPanchayatReq
import com.rsetiapp.common.model.request.InsertFacultyReq
import com.rsetiapp.common.model.request.InsertSdrVisitReq
import com.rsetiapp.common.model.request.InstituteListReq
import com.rsetiapp.common.model.request.LoginReq
import com.rsetiapp.common.model.request.OtpGenerateRequest
import com.rsetiapp.common.model.request.SalaryRangeReq
import com.rsetiapp.common.model.request.SdrListReq
import com.rsetiapp.common.model.request.SettleStatusRequest
import com.rsetiapp.common.model.request.SettlementVeryficationBatchReq
import com.rsetiapp.common.model.request.SettlementVeryficationReq
import com.rsetiapp.common.model.request.SettlementVeryficationUploadReq
import com.rsetiapp.common.model.request.StateListReq
import com.rsetiapp.common.model.request.TokenReq
import com.rsetiapp.common.model.request.ValidateOtpReq
import com.rsetiapp.common.model.request.VillageReq
import com.rsetiapp.common.model.response.BankIFSCSearchRes
import com.rsetiapp.common.model.response.AttendanceBatchRes
import com.rsetiapp.common.model.response.AttendanceCandidateRes
import com.rsetiapp.common.model.response.AttendanceCheckRes
import com.rsetiapp.common.model.response.AttendanceInsertRes
import com.rsetiapp.common.model.response.BatchListResponse
import com.rsetiapp.common.model.response.BlockResponse
import com.rsetiapp.common.model.response.CandidateDetailsRes
import com.rsetiapp.common.model.response.CandidateListResponse
import com.rsetiapp.common.model.response.CandidateSearchResp
import com.rsetiapp.common.model.response.CourseResponse
import com.rsetiapp.common.model.response.DistrictListResponse
import com.rsetiapp.common.model.response.DistrictResponse
import com.rsetiapp.common.model.response.EAPInsertResponse
import com.rsetiapp.common.model.response.EapAutoFetchRes
import com.rsetiapp.common.model.response.EapListResponse
import com.rsetiapp.common.model.response.FaceResponse
import com.rsetiapp.common.model.response.FacultyDetailsRes
import com.rsetiapp.common.model.response.FollowUpInsertRes
import com.rsetiapp.common.model.response.FollowUpStatusResp
import com.rsetiapp.common.model.response.FollowUpTypeResp
import com.rsetiapp.common.model.response.ForgotPassresponse
import com.rsetiapp.common.model.response.FormResponse
import com.rsetiapp.common.model.response.InsertFacultyRes
import com.rsetiapp.common.model.response.InstituteResponse
import com.rsetiapp.common.model.response.LoginRes
import com.rsetiapp.common.model.response.OtpGenerateResponse
import com.rsetiapp.common.model.response.ProgramResponse
import com.rsetiapp.common.model.response.SalaryRangeRes
import com.rsetiapp.common.model.response.SdrInsertResp
import com.rsetiapp.common.model.response.SdrListResp
import com.rsetiapp.common.model.response.SettleStatusResponse
import com.rsetiapp.common.model.response.SettlementPercentageListResponse
import com.rsetiapp.common.model.response.SettlementVeryficationListResponse
import com.rsetiapp.common.model.response.SettlementVeryficationUploadInsertRes
import com.rsetiapp.common.model.response.StateDataResponse
import com.rsetiapp.common.model.response.TokenRes
import com.rsetiapp.common.model.response.VillageResponse
import com.rsetiapp.common.model.response.grampanchayatResponse
import com.rsetiapp.core.uidai.ekyc.UidaiKycRequest
import com.rsetiapp.core.uidai.ekyc.UidaiResp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface AppLevelApi {

    @POST("generateToken")
    suspend fun getToken(@Body tokenReq: TokenReq): TokenRes

    @POST("login")
    suspend fun getLoginAPI(@Body loginReq: LoginReq): LoginRes

    @POST("forms")
    suspend fun getFormAPI(
        @Header("rsetiappauth") token: String,
        @Body formRequest: FormRequest
    ): FormResponse


    @POST("stateList")
    suspend fun getStateListAPI(
        @Header("rsetiappauth") token: String,
        @Body stateListReq: StateListReq
    ): StateDataResponse

    @POST("districtList")
    suspend fun getDistrictListAPI(
        @Header("rsetiappauth") token: String,
        @Body districtReq: DistrictReq
    ): DistrictResponse

    @POST("blockList")
    suspend fun getBlockListAPI(
        @Header("rsetiappauth") token: String,
        @Body blockReq: BlockReq
    ): BlockResponse


    @POST("gramPanchayatList")
    suspend fun getGpListAPI(
        @Header("rsetiappauth") token: String,
        @Body gramPanchayatReq: GramPanchayatReq
    ): grampanchayatResponse


    @POST("villageList")
    suspend fun getVillageListAPI(
        @Header("rsetiappauth") token: String,
        @Body villageReq: VillageReq
    ): VillageResponse


    @POST("eapautofetch")
    suspend fun getEapAutoFetchListAPI(
        @Header("rsetiappauth") token: String,
        @Body eapAutofetchReq: EapAutofetchReq
    ): EapAutoFetchRes

    @POST("insertEap")
    suspend fun insertEAPAPI(
        @Header("rsetiappauth") token: String,
        @Body eapInsertRequest: EAPInsertRequest
    ): EAPInsertResponse

    @POST("verifiyMobile")
    suspend fun generateOtpAPI(@Body otpGenerateRequest: OtpGenerateRequest): OtpGenerateResponse

    @POST("forgetPassword")
    suspend fun forgetPasswordAPI(@Body fogotPaasReq: FogotPaasReq): ForgotPassresponse

    @POST("batchList")
    suspend fun getFollowUpBatchListAPI(
        @Header("rsetiappauth") token: String,
        @Body batchListReq: BatchListReq
    ): BatchListResponse

    @POST("batchCandidateList")
    suspend fun getFollowUpCandidateListAPI(
        @Header("rsetiappauth") token: String,
        @Body candidateListReq: CandidateListReq
    ): CandidateListResponse

    @POST("candidateList")
    suspend fun candidateSearchListAPI(
        @Header("rsetiappauth") token: String,
        @Body candidateSearchReq: CandidateSearchReq
    ): CandidateSearchResp


    @POST("candidateDetails")
    suspend fun candidateDetailsAPI(
        @Header("rsetiappauth") token: String,
        @Body candidateDetailsReq: CandidateDetailsReq
    ): CandidateDetailsRes

    @POST("eapDetails")
    suspend fun eapDetailsAPI(
        @Header("rsetiappauth") token: String,
        @Body eapListReq: EapListReq
    ): EapListResponse

    @POST("followUpType")
    suspend fun getFollowTypeListAPI(
        @Header("rsetiappauth") token: String,
        @Body followUpTypeReq: FollowUpTypeReq
    ): FollowUpTypeResp

    @POST("followUpStatus")
    suspend fun getFollowStatusListAPI(
        @Header("rsetiappauth") token: String,
        @Body followUpTypeReq: FollowUpTypeReq
    ): FollowUpStatusResp


    @POST("bankDetailsByIfsc")
    suspend fun bankIFSCAPI(
        @Header("rsetiappauth") token: String,
        @Body bankIFSCSearchReq: BankIFSCSearchReq
    ): BankIFSCSearchRes


    @POST("onGoingBatchList")
    suspend fun getAttendanceBatch(
        @Header("rsetiappauth") token: String,
        @Body attendanceBatchReq: AttendanceBatchReq
    ): AttendanceBatchRes

    @POST("onGoingBatchCandidateList")
    suspend fun getAttendanceCandidate(
        @Header("rsetiappauth") token: String,
        @Body attendanceCandidateReq: AttendanceCandidateReq
    ): AttendanceCandidateRes

    @POST("insertFollowUp")
    suspend fun insertFollowUpAPI(
        @Header("rsetiappauth") token: String,
        @Body followUpInsertReq: FollowUpInsertReq
    ): FollowUpInsertRes




//    instituteListAPI




//    @POST("instituteList")
//    suspend fun instituteListAPI(
//        @Header("rsetiappauth") token: String,
//        @Body appVersion: String,login: String,imeiNo: String,districtCode: String
//    ): InstituteResponse

    @POST("instituteList")
    suspend fun instituteListAPI(
        @Header("rsetiappauth") token: String,
        @Body request: InstituteListReq
    ): InstituteResponse



//    interface AppLevelApi {

//        @POST("demobackend/rsetiapp/instituteList")
//        suspend fun instituteListAPI(
//            @Header("rsetiappauth") token: String,
//            @Header("Content-Type") contentType: String,
//            @Body request: InstituteListReq
//        ): InstituteResponse
//    }

//    @POST("instituteList")
//    suspend fun instituteListAPI(
//        @Header("rsetiappauth") token: String,
//        @Body insti: InstituteListReq
//    ): InstituteResponse











    @POST("attandanceCheck")
    suspend fun getAttendanceCheckStatus(
        @Header("rsetiappauth") token: String,
        @Body attendanceCheckReq: AttendanceCheckReq
    ): AttendanceCheckRes


    @POST("insertAttandance")
    suspend fun getInsertAttendance(
        @Header("rsetiappauth") token: String,
        @Body attendanceInsertReq: AttendanceInsertReq
    ): AttendanceInsertRes

    @POST("salaryRange")
    suspend fun salaryRangeDetails(
        @Header("rsetiappauth") token: String,
        @Body salaryRangeReq: SalaryRangeReq
    ): SalaryRangeRes

    @POST("insertsdr")
    suspend fun insertSdrApi(
        @Header("rsetiappauth") token: String,
        @Body sdrVisitReq: InsertSdrVisitReq
    ): SdrInsertResp


    @POST("sdrList")
    suspend fun sdrListApi(
        @Header("rsetiappauth") token: String,
        @Body sdrListReq: SdrListReq
    ): SdrListResp

    @POST("updateFaceRegistered")
    suspend fun updateFaceApi(@Body faceCheckReq: FaceCheckReq): FaceResponse

    @POST("eapCourse")
    suspend fun courseEapApi(
        @Header("rsetiappauth") token: String,
        @Body courseRequest: CourseRequest
    ): CourseResponse


    @POST("validateOtp")
    suspend fun getOtpValidateApi(@Body validateOtpReq: ValidateOtpReq): OtpGenerateResponse

    @POST
    suspend fun postOnAUAFaceAuthNREGA(
        @Url url: String, @Body request: UidaiKycRequest
    ): Response<UidaiResp>


    @POST("candidateSettleStatus")
    suspend fun getSettleStatusApi(
        @Header("rsetiappauth") token: String,
        @Body settleStatusRequest: SettleStatusRequest
    ): SettleStatusResponse


    @POST("onGoingBatchFaculty")
    suspend fun getFacultyDataApi(
        @Header("rsetiappauth") token: String,
        @Body facultyDataReq: FacutlyDataReq
    ): FacultyDetailsRes

    @POST("insertFacultyAttandance")
    suspend fun insertFacultyAttendanceApi(
        @Header("rsetiappauth") token: String,
        @Body insertFacultyReq: InsertFacultyReq
    ): InsertFacultyRes

    @POST("settlements")
    suspend fun getSettlementsLoginAPI(@Body settlementVeryficationReq: SettlementVeryficationReq): SettlementVeryficationListResponse




    @POST("districtList")
    suspend fun getdistrictListAPI(@Body districtListReq: DistrictListReq): DistrictListResponse





    @POST("settled-batch")
    suspend fun getgetsettledbatchAPIListAPI(@Body batchListReq: SettlementVeryficationBatchReq): SettlementPercentageListResponse



    @POST("reverificationSettlement")
    suspend fun reverificationSettlementAPI(@Body settlementVeryficationReq: SettlementVeryficationUploadReq): SettlementVeryficationUploadInsertRes



}



