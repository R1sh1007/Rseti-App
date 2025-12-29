
package com.rsetiapp.common

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsetiapp.core.uidai.ekyc.UidaiKycRequest
import com.rsetiapp.core.uidai.ekyc.UidaiResp
import com.rsetiapp.common.model.response.StateDataResponse
import com.rsetiapp.common.model.response.BlockResponse
import com.rsetiapp.common.model.response.DistrictResponse
import com.rsetiapp.common.model.response.VillageResponse
import com.rsetiapp.common.model.response.grampanchayatResponse
import com.rsetiapp.BuildConfig
import com.rsetiapp.common.model.request.AttendanceCheckReq
import com.rsetiapp.common.model.request.AttendanceInsertReq
import com.rsetiapp.common.model.request.BankIFSCSearchReq
import com.rsetiapp.common.model.request.CandidateDetailsReq
import com.rsetiapp.common.model.request.CandidateSearchReq
import com.rsetiapp.common.model.request.CourseRequest
import com.rsetiapp.common.model.request.DistrictListReq
import com.rsetiapp.common.model.request.EAPInsertRequest
import com.rsetiapp.common.model.request.EapListReq
import com.rsetiapp.common.model.request.FaceCheckReq
import com.rsetiapp.common.model.request.FacutlyDataReq
import com.rsetiapp.common.model.request.FogotPaasReq
import com.rsetiapp.common.model.request.FollowUpInsertReq
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
import com.rsetiapp.common.model.request.ValidateOtpReq
import com.rsetiapp.common.model.response.AttendanceBatchRes
import com.rsetiapp.common.model.response.AttendanceCandidateRes
import com.rsetiapp.common.model.response.AttendanceCheckRes
import com.rsetiapp.common.model.response.AttendanceInsertRes
import com.rsetiapp.common.model.response.BankIFSCSearchRes
import com.rsetiapp.common.model.response.Batch
import com.rsetiapp.common.model.response.BatchListResponse
import com.rsetiapp.common.model.response.CandidateListResponse
import com.rsetiapp.common.model.response.CandidateDetailsRes
import com.rsetiapp.common.model.response.CandidateSearchResp
import com.rsetiapp.common.model.response.CourseResponse
import com.rsetiapp.common.model.response.DistrictListResponse
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
import com.rsetiapp.common.model.response.TokenRes
import com.rsetiapp.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Header
import javax.inject.Inject


@HiltViewModel
class CommonViewModel @Inject constructor(private val commonRepository: CommonRepository) :
    ViewModel() {

    private var _getToken = MutableSharedFlow<Resource<out TokenRes>>()
    val getToken = _getToken.asSharedFlow()


    fun getToken(imeiNo:String , appVersion:String){
        viewModelScope.launch {
            commonRepository.getToken(imeiNo,appVersion).collectLatest {
                _getToken.emit(it)
            }
        }


    }


    private  var _getLoginAPI =  MutableStateFlow<Resource<out LoginRes>>(Resource.Loading())
    val getLoginAPI = _getLoginAPI.asStateFlow()


    fun getLoginAPI(loginReq: LoginReq) {
        viewModelScope.launch {
            commonRepository.getLoginAPI(loginReq).collectLatest {
                _getLoginAPI.emit(it)
            }
        }
    }


    private  var _settlementVeryfication =  MutableStateFlow<Resource<out SettlementVeryficationListResponse>>(Resource.Loading())
    val getsettlementVeryfication = _settlementVeryfication.asStateFlow()

    fun getSettlementsLoginAPI(settlementVeryficationReq: SettlementVeryficationReq) {
        viewModelScope.launch {
            commonRepository.getSettlementsLoginAPI(settlementVeryficationReq).collectLatest {
                _settlementVeryfication.emit(it)
            }
        }
    }


    private  var _districtListReq =  MutableStateFlow<Resource<out DistrictListResponse>>(Resource.Loading())
    val districtList = _districtListReq.asStateFlow()


    fun getdistrictListAPI(districtreq: DistrictListReq) {
        viewModelScope.launch {
            commonRepository.getdistrictListAPI(districtreq).collectLatest {
                _districtListReq.emit(it)
            }
        }
    }










/*
    private var _getDemAPI = MutableStateFlow<Resource<out DemRes>>(Resource.Loading())
    val getDemAPI = _getDemAPI.asStateFlow()

    fun getDemAPI(demReq : DemReq){
        viewModelScope.launch {
            commonRepository.getDemAPI(demReq).collectLatest{
                _getDemAPI.emit(it)
            }
        }
    }
*/

    private var _getFormAPI = MutableStateFlow<Resource<out FormResponse>>(Resource.Loading())
    val getFormAPI = _getFormAPI.asStateFlow()


    fun getFormAPI(header :String,appVersion:String , login:String, imeiNo :String){
        viewModelScope.launch {
            commonRepository.getFormAPI(header,appVersion,login,imeiNo).collectLatest {
                _getFormAPI.emit(it)
            }
        }
    }

    private var _stateList =  MutableStateFlow<Resource<out StateDataResponse>>(Resource.Loading())
    val getStateList = _stateList.asStateFlow()


    fun getStateListApi(header :String, login :String, imei :String){
        viewModelScope.launch {
            commonRepository.getStateListApi(header,BuildConfig.VERSION_NAME,login,imei).collectLatest {
                _stateList.emit(it)
            }
        }
    }


    private var _districtList =  MutableStateFlow<Resource<out DistrictResponse>>(Resource.Loading())
    val getDistrictList = _districtList.asStateFlow()


    fun getDistrictListApi(header :String,state :String, login :String, imei :String){
        viewModelScope.launch {
            commonRepository.getDistrictListApi(header,state,BuildConfig.VERSION_NAME,login,imei).collectLatest {
                _districtList.emit(it)
            }
        }
    }


    private var _blockList =  MutableStateFlow<Resource<out BlockResponse>>(Resource.Loading())
    val getBlockList = _blockList.asStateFlow()


    fun getBlockListApi(header :String,district :String, login :String, imeiNo :String){
        viewModelScope.launch {
            commonRepository.getBlockListApi(header,district,BuildConfig.VERSION_NAME,login,imeiNo).collectLatest {
                _blockList.emit(it)
            }
        }}
    private  var _gpList =  MutableStateFlow<Resource<out grampanchayatResponse>>(Resource.Loading())
    val getGpList = _gpList.asStateFlow()


    fun getGpListApi(header :String,block :String, login :String, imeiNo :String) {
        viewModelScope.launch {
            commonRepository.getGPListApi(header,block, BuildConfig.VERSION_NAME,login,imeiNo).collectLatest {
                _gpList.emit(it)
            }
        }


    }

    private  var _villageList =  MutableStateFlow<Resource<out VillageResponse>>(Resource.Loading())
    val getVillageList = _villageList.asStateFlow()


    fun getVillageListApi(header :String,gp :String, login :String, imeiNo :String) {
        viewModelScope.launch {
            commonRepository.getVillageListApi(header,gp, BuildConfig.VERSION_NAME,login,imeiNo).collectLatest {
                _villageList.emit(it)
            }
        }


    }




    private  var _getEapAutoFetchListAPI =  MutableStateFlow<Resource<out EapAutoFetchRes>>(Resource.Loading())
    val getEapAutoFetchListAPI = _getEapAutoFetchListAPI.asStateFlow()


    fun getEapAutoFetchListAPI(header :String,login :String, appVersion: String, imeiNo :String) {
        viewModelScope.launch {
            commonRepository.getEapAutoFetchListAPI( header,login,appVersion,imeiNo).collectLatest {
                _getEapAutoFetchListAPI.emit(it)
            }
        }


    }

    private  var _insertEAPAPI =  MutableStateFlow<Resource<out EAPInsertResponse>>(Resource.Loading())
    val insertEAPAPI = _insertEAPAPI.asStateFlow()


    fun insertEAPAPI(header :String,eapInsertRequest: EAPInsertRequest) {
        viewModelScope.launch {
            commonRepository.insertEAPAPI(header,eapInsertRequest).collectLatest {
                _insertEAPAPI.emit(it)
            }
        }


    }


    private  var _generateOtpAPI =  MutableStateFlow<Resource<out OtpGenerateResponse>>(Resource.Loading())
    val generateOtpAPI = _generateOtpAPI.asStateFlow()

    fun generateOtpAPI(otpGenerateRequest: OtpGenerateRequest) {
        viewModelScope.launch {
            commonRepository.generateOtpAPI(otpGenerateRequest).collectLatest {
                _generateOtpAPI.emit(it)
            }
        }
    }


    private  var _forgetPasswordAPI =  MutableStateFlow<Resource<out ForgotPassresponse>>(Resource.Loading())
    val forgetPasswordAPI = _forgetPasswordAPI.asStateFlow()

    fun forgetPasswordAPI(fogotPaasReq : FogotPaasReq) {
        viewModelScope.launch {
            commonRepository.forgetPasswordAPI(fogotPaasReq).collectLatest {
                _forgetPasswordAPI.emit(it)
            }
        }


    }

    private var _getBatchAPI = MutableStateFlow<Resource<out BatchListResponse>>(Resource.Loading())
    val getBatchAPI = _getBatchAPI.asStateFlow()

    fun getBatchAPI(header :String,appVersion:String , login:String,imeiNo: String,entityCode:String){
        viewModelScope.launch {
            commonRepository.getBatchAPI(header,appVersion,login,imeiNo,entityCode).collectLatest {
                _getBatchAPI.emit(it)
            }
        }
    }

    private var _getCandidateAPI = MutableStateFlow<Resource<out CandidateListResponse>>(Resource.Loading())
    val getCandidateAPI = _getCandidateAPI.asStateFlow()

    fun getCandidateAPI(header :String,appVersion:String , batchId: String,imeiNo: String,login: String){
        viewModelScope.launch {
            commonRepository.getCandidateAPI(header,appVersion, batchId,imeiNo,login).collectLatest {
                _getCandidateAPI.emit(it)
            }
        }
    }


    private  var _candidateSearchListAPI =  MutableStateFlow<Resource<out CandidateSearchResp>>(Resource.Loading())
    val candidateSearchListAPI = _candidateSearchListAPI.asStateFlow()

    fun candidateSearchListAPI(header :String,candidateSearchReq: CandidateSearchReq) {
        viewModelScope.launch {
            commonRepository.candidateSearchListAPI(header,candidateSearchReq).collectLatest {
                _candidateSearchListAPI.emit(it)
            }
        }


    }


    private  var _candidateDetailsAPI =  MutableStateFlow<Resource<out CandidateDetailsRes>>(Resource.Loading())
    val candidateDetailsAPI = _candidateDetailsAPI.asStateFlow()

    fun candidateDetailsAPI(header :String,candidateDetailsReq: CandidateDetailsReq) {
        viewModelScope.launch {
            commonRepository.candidateDetailsAPI(header,candidateDetailsReq).collectLatest {
                _candidateDetailsAPI.emit(it)
            }
        }


    }


    private  var _eapDetailsAPI =  MutableStateFlow<Resource<out EapListResponse>>(Resource.Loading())
    val eapDetailsAPI = _eapDetailsAPI.asStateFlow()

    fun eapDetailsAPI(header :String,eapListReq: EapListReq) {
        viewModelScope.launch {
            commonRepository.eapDetailsAPI(header,eapListReq).collectLatest {
                _eapDetailsAPI.emit(it)
            }
        }
    }

    private  var _getFollowTypeList =  MutableStateFlow<Resource<out FollowUpTypeResp>>(Resource.Loading())
    val getFollowTypeList = _getFollowTypeList.asStateFlow()

    private  var _getAttendanceBatchAPI =  MutableStateFlow<Resource<out AttendanceBatchRes>>(Resource.Loading())
    val getAttendanceBatchAPI = _getAttendanceBatchAPI.asStateFlow()

    fun getAttendanceBatchAPI(header :String,appVersion: String,imeiNo: String,login: String) {
        viewModelScope.launch {
            commonRepository.getAttendanceBatchAPI(header,appVersion,imeiNo,login).collectLatest {
                _getAttendanceBatchAPI.emit(it)
            }
        }


    }



    private  var _getInsertAttendance =  MutableStateFlow<Resource<out AttendanceInsertRes>>(Resource.Loading())
    val getInsertAttendance = _getInsertAttendance.asStateFlow()

    fun getInsertAttendance(header :String,attendanceInsertReq: AttendanceInsertReq) {
        viewModelScope.launch {
            commonRepository.getInsertAttendance(header,attendanceInsertReq).collectLatest {
                _getInsertAttendance.emit(it)
            }
        }
    }



    private  var _getAttendanceCandidate =  MutableStateFlow<Resource<out AttendanceCandidateRes>>(Resource.Loading())
    val getAttendanceCandidate = _getAttendanceCandidate.asStateFlow()

    fun getAttendanceCandidate(header :String,appVersion: String,batchId :String,imeiNo: String,login: String) {
        viewModelScope.launch {
            commonRepository.getAttendanceCandidate(header,appVersion,batchId,imeiNo,login).collectLatest {
                _getAttendanceCandidate.emit(it)
            }
        }
    }


    private  var _getAttendanceCheckStatus =  MutableStateFlow<Resource<out AttendanceCheckRes>>(Resource.Loading())
    val getAttendanceCheckStatus = _getAttendanceCheckStatus.asStateFlow()

    fun getAttendanceCheckStatus(header :String,attendanceCheckReq: AttendanceCheckReq
    ) {
        viewModelScope.launch {
            commonRepository.getAttendanceCheckStatus(header,attendanceCheckReq).collectLatest {
                _getAttendanceCheckStatus.emit(it)
            }
        }
    }


    fun getFollowTypeListAPI(header :String,imeiNo: String,login: String) {
        viewModelScope.launch {
            commonRepository.getFollowTypeListAPI(header,BuildConfig.VERSION_NAME,imeiNo,login).collectLatest {
                _getFollowTypeList.emit(it)
            }
        }
    }

    private  var _getFollowStatusList =  MutableStateFlow<Resource<out FollowUpStatusResp>>(Resource.Loading())
    val getFollowStatusList = _getFollowStatusList.asStateFlow()

    fun getFollowStatusListAPI(header :String,imeiNo: String,login: String) {
        viewModelScope.launch {
            commonRepository.getFollowStatusListAPI(header,BuildConfig.VERSION_NAME,imeiNo,login).collectLatest {
                _getFollowStatusList.emit(it)
            }
        }
    }

    private var _insertFollowUpAPI =  MutableStateFlow<Resource<out FollowUpInsertRes>>(Resource.Loading())
    val insertFollowUpAPI = _insertFollowUpAPI.asStateFlow()

    fun insertFollowUpAPI(header :String,followUpInsertReq: FollowUpInsertReq) {
        viewModelScope.launch {
            commonRepository.insertFollowUpAPI(header,followUpInsertReq).collectLatest {
                _insertFollowUpAPI.emit(it)
            }
        }
    }


    private var _postOnAUAFaceAuthNREGA = MutableSharedFlow<Resource<out Response<UidaiResp>>>()
    val postOnAUAFaceAuthNREGA = _postOnAUAFaceAuthNREGA.asSharedFlow()

    fun postOnAUAFaceAuthNREGA(url:String, uidaiKycRequest: UidaiKycRequest){
        viewModelScope.launch {
            commonRepository.postOnAUAFaceAuthNREGA(url, uidaiKycRequest).collectLatest {
                _postOnAUAFaceAuthNREGA.emit(it)
            }
        }
    }



    private  var _getbankIFSCAPI =  MutableStateFlow<Resource<out BankIFSCSearchRes>>(Resource.Loading())
    val getbankIFSCAPI = _getbankIFSCAPI.asStateFlow()

    fun getbankIFSCAPI(header :String,bankIFSCSearchReq: BankIFSCSearchReq) {
        viewModelScope.launch {
            commonRepository.getbankIFSCAPI(header,bankIFSCSearchReq).collectLatest {
                _getbankIFSCAPI.emit(it)
            }
        }


    }

private var _salaryDetailsState = MutableStateFlow<Resource<out SalaryRangeRes>>(Resource.Loading())
    val salaryDetailsState = _salaryDetailsState.asStateFlow()

    fun getSalaryRange(header :String,salaryRangeReq: SalaryRangeReq) {
        viewModelScope.launch {
            commonRepository.getSalaryDetailsAPI(header,salaryRangeReq).collectLatest {
                _salaryDetailsState.emit(it)
            }
        }
    }
    private var _insertSdrApi = MutableStateFlow<Resource<out SdrInsertResp>>(Resource.Loading())
    val insertSdrApi = _insertSdrApi.asStateFlow()

    fun insertSdrApi(header :String,insertSdrVisitReq: InsertSdrVisitReq) {
        viewModelScope.launch {
            commonRepository.insertSdrApi(header,insertSdrVisitReq).collectLatest {
                _insertSdrApi.emit(it)
            }
        }
    }



    private var _getSdrListApi = MutableStateFlow<Resource<out SdrListResp>>(Resource.Loading())
    val getSdrListApi = _getSdrListApi.asStateFlow()

    fun getSdrListApi(header :String,sdrListReq: SdrListReq) {
        viewModelScope.launch {
            commonRepository.getSdrListAPI(header,sdrListReq).collectLatest {
                _getSdrListApi.emit(it)
            }
        }
    }

    private var _courseEapApi = MutableStateFlow<Resource<out CourseResponse>>(Resource.Loading())
    val courseEapApi = _courseEapApi.asStateFlow()

    fun courseEapApi(header :String,courseRequest: CourseRequest) {
        viewModelScope.launch {
            commonRepository.courseEapApi(header,courseRequest).collectLatest {
                _courseEapApi.emit(it)
            }
        }
    }


    private  var _updateFaceApi =  MutableStateFlow<Resource<out FaceResponse>>(Resource.Loading())
    val updateFaceApi = _updateFaceApi.asSharedFlow()

    fun updateFaceApi(faceCheckReq: FaceCheckReq){
        viewModelScope.launch {
            commonRepository.updateFaceApi(faceCheckReq).collectLatest {
                _updateFaceApi.emit(it)
            }
        }


    }


    private  var _getOtpValidateApi =  MutableStateFlow<Resource<out OtpGenerateResponse>>(Resource.Loading())
    val getOtpValidateApi = _getOtpValidateApi.asSharedFlow()


    fun getOtpValidateApi(validateOtpReq: ValidateOtpReq){
        viewModelScope.launch {
            commonRepository.getOtpValidateApi(validateOtpReq).collectLatest {
                _getOtpValidateApi.emit(it)
            }
        }


    }
    private  var _getFacultyDataApi =  MutableStateFlow<Resource<out FacultyDetailsRes>>(Resource.Loading())
    val getFacultyDataApi = _getFacultyDataApi.asSharedFlow()


    fun getFacultyDataApi(token: String, facultyDataReq: FacutlyDataReq){
        viewModelScope.launch {
            commonRepository.getFacultyDataApi(token,facultyDataReq).collectLatest {
                _getFacultyDataApi.emit(it)
            }
        }
    }
    private  var _insertFacultyAttendanceApi =  MutableStateFlow<Resource<out InsertFacultyRes>>(Resource.Loading())
    val insertFacultyAttendanceApi = _insertFacultyAttendanceApi.asSharedFlow()


    fun insertFacultyAttendanceApi(token: String, insertFacultyReq: InsertFacultyReq){
        viewModelScope.launch {
            commonRepository.insertFacultyAttendanceApi(token,insertFacultyReq).collectLatest {
                _insertFacultyAttendanceApi.emit(it)
            }
        }
    }





    private var _getSettleStatusApi = MutableStateFlow<Resource<out  SettleStatusResponse>>(Resource.Loading())
    val getSettleStatusApi = _getSettleStatusApi.asSharedFlow()

    fun getSettleStatusApi( token: String, settleStatusRequest: SettleStatusRequest){
        viewModelScope.launch {
            commonRepository.getSettleStatusApi(token,settleStatusRequest).collectLatest {
                _getSettleStatusApi.emit(it)
            }
        }
    }


    val settlementData = MutableLiveData<Bundle>()








    //    instituteList
    private val _instituteListAPI = MutableSharedFlow<Resource<InstituteResponse>>()
    val instituteListAPI = _instituteListAPI.asSharedFlow()

    fun instituteListAPI(
        token: String,
        request: InstituteListReq
    ) {
        viewModelScope.launch {
            commonRepository
                .instituteListAPI(token, request)
                .collectLatest {
                    _instituteListAPI.emit(it)
                }
        }
    }



//    settled-batch





    private var _getsettledbatchAPI = MutableStateFlow<Resource<out SettlementPercentageListResponse>>(Resource.Loading())
    val getsettledbatchAPI = _getsettledbatchAPI.asStateFlow()




    fun getsettledbatchAPI(settlementReq: SettlementVeryficationBatchReq){
        viewModelScope.launch {
            commonRepository.getsettledbatchAPI(settlementReq).collectLatest {
                _getsettledbatchAPI.emit(it)
            }
        }
    }






//    fun getsettledbatchAPI(appVersion:String , instituteId:String){
//        viewModelScope.launch {
//            commonRepository.getsettledbatchAPI(appVersion,instituteId).collectLatest {
//                _getBatchAPI.emit(it)
//            }
//        }
//    }





}