package com.rsetiapp.common

import com.rsetiapp.core.uidai.ekyc.UidaiKycRequest
import com.rsetiapp.core.uidai.ekyc.UidaiResp
import com.rsetiapp.common.model.request.AttendanceBatchReq
import com.rsetiapp.common.model.request.AttendanceCandidateReq
import com.rsetiapp.common.model.request.AttendanceCheckReq
import com.rsetiapp.common.model.request.AttendanceInsertReq
import com.rsetiapp.common.model.request.BankIFSCSearchReq
import com.rsetiapp.common.model.request.BatchListReq
import com.rsetiapp.common.model.response.StateDataResponse
import com.rsetiapp.common.model.request.StateListReq
import com.rsetiapp.common.model.request.BlockReq
import com.rsetiapp.common.model.request.CandidateListReq
import com.rsetiapp.common.model.request.CandidateDetailsReq
import com.rsetiapp.common.model.request.CandidateSearchReq
import com.rsetiapp.common.model.request.CourseRequest
import com.rsetiapp.common.model.request.DistrictReq
import com.rsetiapp.common.model.request.EAPInsertRequest
import com.rsetiapp.common.model.request.EapAutofetchReq
import com.rsetiapp.common.model.request.EapListReq
import com.rsetiapp.common.model.request.FaceCheckReq
import com.rsetiapp.common.model.request.FacutlyDataReq
import com.rsetiapp.common.model.request.FogotPaasReq
import com.rsetiapp.common.model.request.FollowUpInsertReq
import com.rsetiapp.common.model.request.FollowUpTypeReq
import com.rsetiapp.common.model.request.GramPanchayatReq
import com.rsetiapp.common.model.request.VillageReq
import com.rsetiapp.common.model.response.BlockResponse
import com.rsetiapp.common.model.response.DistrictResponse
import com.rsetiapp.common.model.response.VillageResponse
import com.rsetiapp.common.model.response.grampanchayatResponse
import com.rsetiapp.common.model.request.FormRequest
import com.rsetiapp.common.model.request.InsertFacultyReq
import com.rsetiapp.common.model.request.InsertSdrVisitReq
import com.rsetiapp.common.model.request.LoginReq
import com.rsetiapp.common.model.request.OtpGenerateRequest
import com.rsetiapp.common.model.request.SalaryRangeReq
import com.rsetiapp.common.model.request.SdrListReq
import com.rsetiapp.common.model.request.SettleStatusRequest
import com.rsetiapp.common.model.request.TokenReq
import com.rsetiapp.common.model.request.ValidateOtpReq
import com.rsetiapp.common.model.response.AttendanceBatchRes
import com.rsetiapp.common.model.response.AttendanceCandidateRes
import com.rsetiapp.common.model.response.BankIFSCSearchRes
import com.rsetiapp.common.model.response.Batch
import com.rsetiapp.common.model.response.AttendanceCheckRes
import com.rsetiapp.common.model.response.AttendanceInsertRes
import com.rsetiapp.common.model.response.BatchListResponse
import com.rsetiapp.common.model.response.CandidateListResponse
import com.rsetiapp.common.model.response.EAPInsertResponse
import com.rsetiapp.common.model.response.EapAutoFetchRes
import com.rsetiapp.common.model.response.FollowUpStatus
import com.rsetiapp.common.model.response.CandidateDetailsRes
import com.rsetiapp.common.model.response.CandidateSearchResp
import com.rsetiapp.common.model.response.CourseResponse
import com.rsetiapp.common.model.response.EapListResponse
import com.rsetiapp.common.model.response.FaceResponse
import com.rsetiapp.common.model.response.FacultyDetailsRes
import com.rsetiapp.common.model.response.FollowUpInsertRes
import com.rsetiapp.common.model.response.FollowUpStatusResp
import com.rsetiapp.common.model.response.FollowUpTypeResp
import com.rsetiapp.common.model.response.ForgotPassresponse
import com.rsetiapp.common.model.response.FormResponse
import com.rsetiapp.common.model.response.InsertFacultyRes
import com.rsetiapp.common.model.response.LoginRes
import com.rsetiapp.common.model.response.OtpGenerateResponse
import com.rsetiapp.common.model.response.ProgramResponse
import com.rsetiapp.common.model.response.SalaryRangeRes
import com.rsetiapp.common.model.response.SdrInsertResp
import com.rsetiapp.common.model.response.SdrListResp
import com.rsetiapp.common.model.response.SettleStatusResponse
import com.rsetiapp.common.model.response.TokenRes
import com.rsetiapp.core.data.local.database.AppDatabase
import com.rsetiapp.core.data.remote.AppLevelApi
import com.rsetiapp.core.di.AppModule
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.networkBoundResourceWithoutDb
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Header
import javax.inject.Inject


class CommonRepository @Inject constructor(
    @AppModule.PreLoginAppLevelApi private val appLevelApi: AppLevelApi,
    private val database: AppDatabase
    ){


    suspend fun getToken(imeiNo : String, appVersion :String): Flow<Resource<out TokenRes>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.getToken(TokenReq(appVersion,imeiNo))
        }
    }

    suspend fun getLoginAPI(loginReq: LoginReq): Flow<Resource<out LoginRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getLoginAPI(loginReq)
        }
    }

    suspend fun getFormAPI(header :String,appVersion : String, login :String, imeiNo :String): Flow<Resource<out FormResponse>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.getFormAPI(header,FormRequest(appVersion,login,imeiNo))
        }
    }
    suspend fun getStateListApi(header :String,appVersion: String, login :String, imeiNo :String): Flow<Resource<out StateDataResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getStateListAPI(header,StateListReq(appVersion,imeiNo,login))
        }
    }
    suspend fun getDistrictListApi(header :String,stateCode: String,appVersion: String, login :String, imeiNo :String): Flow<Resource<out DistrictResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getDistrictListAPI(header,DistrictReq(stateCode, appVersion,imeiNo,login))
        }
    }

    suspend fun getBlockListApi(header :String,districtCode: String,appVersion: String, login :String, imeiNo :String): Flow<Resource<out BlockResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getBlockListAPI(header,BlockReq(districtCode, appVersion,imeiNo,login))
        }
    }
    suspend fun getGPListApi(header :String,blockCode: String,appVersion: String, login :String, imeiNo :String): Flow<Resource<out grampanchayatResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getGpListAPI(header,GramPanchayatReq(blockCode, appVersion,imeiNo,login))
        }
    }

    suspend fun getVillageListApi(header :String,gpCode: String,appVersion: String, login :String, imeiNo :String): Flow<Resource<out VillageResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getVillageListAPI(header,VillageReq(gpCode, appVersion,imeiNo,login))
        }
    }



    suspend fun getEapAutoFetchListAPI(header :String,login : String, appVersion: String, imeiNo :String): Flow<Resource<out EapAutoFetchRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getEapAutoFetchListAPI(header,EapAutofetchReq(appVersion,imeiNo,login))
        }
    }

    suspend fun insertEAPAPI(header :String,eapInsertRequest: EAPInsertRequest): Flow<Resource<out EAPInsertResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.insertEAPAPI(header,eapInsertRequest)
        }
    }


    suspend fun generateOtpAPI(otpGenerateRequest: OtpGenerateRequest): Flow<Resource<out OtpGenerateResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.generateOtpAPI(otpGenerateRequest)
        }
    }

    suspend fun forgetPasswordAPI(fogotPaasReq: FogotPaasReq): Flow<Resource<out ForgotPassresponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.forgetPasswordAPI(fogotPaasReq)
        }
    }

    suspend fun getBatchAPI(header :String,appVersion : String,login :String,imeiNo :String,entityCode:String): Flow<Resource<out BatchListResponse>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.getFollowUpBatchListAPI(header,BatchListReq(appVersion,imeiNo,login,entityCode))
        }
    }

    suspend fun candidateSearchListAPI(header :String,candidateSearchReq: CandidateSearchReq): Flow<Resource<out CandidateSearchResp>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.candidateSearchListAPI(header,candidateSearchReq)
        }
    }

    suspend fun candidateDetailsAPI(header :String,candidateDetailsReq: CandidateDetailsReq): Flow<Resource<out CandidateDetailsRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.candidateDetailsAPI(header,candidateDetailsReq)
        }
    }
    suspend fun eapDetailsAPI(header :String,eapListReq: EapListReq): Flow<Resource<out EapListResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.eapDetailsAPI(header,eapListReq)
        }
    }

    suspend fun getCandidateAPI(header :String,
        appVersion: String, batchId: String,imeiNo: String,login: String
    ): Flow<Resource<out CandidateListResponse>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.getFollowUpCandidateListAPI(header,CandidateListReq(appVersion, batchId,imeiNo,login))
        }
    }


    suspend fun getbankIFSCAPI(header :String,
       bankIFSCSearchReq: BankIFSCSearchReq
    ): Flow<Resource<out BankIFSCSearchRes>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.bankIFSCAPI(header,bankIFSCSearchReq)
        }
    }

    suspend fun getFollowTypeListAPI(header :String,appVersion: String,imeiNo: String,login: String): Flow<Resource<out FollowUpTypeResp>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getFollowTypeListAPI(header,FollowUpTypeReq( appVersion,imeiNo,login))
        }
    }

    suspend fun getFollowStatusListAPI(header :String,appVersion: String,imeiNo: String,login: String): Flow<Resource<out FollowUpStatusResp>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getFollowStatusListAPI(header,FollowUpTypeReq( appVersion,imeiNo,login))
        }
    }

    suspend fun getAttendanceBatchAPI(header :String,
        appVersion: String,imeiNo: String,login: String
    ): Flow<Resource<out AttendanceBatchRes>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.getAttendanceBatch(header,AttendanceBatchReq(appVersion,imeiNo,login))
        }
    }

    suspend fun getAttendanceCandidate(header :String,
        appVersion: String, batchId:String,imeiNo: String,login: String
    ): Flow<Resource<out AttendanceCandidateRes>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.getAttendanceCandidate(header,AttendanceCandidateReq(appVersion,batchId,imeiNo,login))
        }
    }

    suspend fun getAttendanceCheckStatus(header :String,
        attendanceCheckReq: AttendanceCheckReq
    ): Flow<Resource<out AttendanceCheckRes>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.getAttendanceCheckStatus(header,attendanceCheckReq)
        }
    }

    suspend fun getInsertAttendance(header :String,
       attendanceInsertReq: AttendanceInsertReq
    ): Flow<Resource<out AttendanceInsertRes>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.getInsertAttendance(
                header,attendanceInsertReq)
        }
    }

    suspend fun insertFollowUpAPI(header :String,followUpInsertReq: FollowUpInsertReq): Flow<Resource<out FollowUpInsertRes>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.insertFollowUpAPI(header,followUpInsertReq)
        }
    }

    suspend fun postOnAUAFaceAuthNREGA(url:String, uidaiKycRequest: UidaiKycRequest): Flow<Resource<out Response<UidaiResp>>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.postOnAUAFaceAuthNREGA(url,uidaiKycRequest)
        }
    }

    suspend fun getSalaryDetailsAPI(header :String,salaryRangeReq: SalaryRangeReq): Flow<Resource<out SalaryRangeRes>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.salaryRangeDetails(header,salaryRangeReq)
        }
    }



    suspend fun insertSdrApi(header :String,sdrInsertSdrVisitReq: InsertSdrVisitReq): Flow<Resource<out SdrInsertResp>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.insertSdrApi(header,sdrInsertSdrVisitReq)
        }
    }


    suspend fun getSdrListAPI(header :String,sdrListReq: SdrListReq): Flow<Resource<out SdrListResp>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.sdrListApi(header,sdrListReq)
        }
    }


    suspend fun updateFaceApi(faceCheckReq: FaceCheckReq): Flow<Resource<out FaceResponse>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.updateFaceApi(faceCheckReq)
        }
    }
    suspend fun courseEapApi(header :String,courseRequest: CourseRequest): Flow<Resource<out CourseResponse>> {
        return networkBoundResourceWithoutDb {
            appLevelApi.courseEapApi(header,courseRequest)
        }
    }



    suspend fun getOtpValidateApi(validateOtpReq: ValidateOtpReq): Flow<Resource<out OtpGenerateResponse>>{
        return networkBoundResourceWithoutDb {

            appLevelApi.getOtpValidateApi(validateOtpReq)
        }
    }



    suspend fun getSettleStatusApi(token: String, settleStatusRequest: SettleStatusRequest) : Flow<Resource<out SettleStatusResponse>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getSettleStatusApi(token,settleStatusRequest)
        }
    }


    suspend fun getFacultyDataApi(token: String,facultyDataReq: FacutlyDataReq) : Flow<Resource<out FacultyDetailsRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.getFacultyDataApi(token,facultyDataReq)
        }
    }
    suspend fun insertFacultyAttendanceApi(token: String,insertFacultyReq: InsertFacultyReq) : Flow<Resource<out InsertFacultyRes>>{
        return networkBoundResourceWithoutDb {
            appLevelApi.insertFacultyAttendanceApi(token,insertFacultyReq)
        }
    }


}