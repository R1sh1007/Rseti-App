
package com.rsetiapp.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karumi.dexter.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.common.model.request.BankIFSCSearchReq
import com.rsetiapp.common.model.request.SalaryRangeReq
import com.rsetiapp.common.model.request.SettleStatusRequest
import com.rsetiapp.common.model.response.BankDetailsList
import com.rsetiapp.common.model.response.SalaryRange
import com.rsetiapp.common.model.response.SettlementStatus
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.gone
import com.rsetiapp.core.util.log
import com.rsetiapp.core.util.toastLong
import com.rsetiapp.core.util.toastShort
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class VeryficationSattelementBottomSheet : BottomSheetDialogFragment() {
    private val commonViewModel: CommonViewModel by activityViewModels()
    private lateinit var progressBar: View
    private lateinit var bankName: TextView
    private lateinit var branchName: TextView
    private lateinit var accountNo: TextView
    private lateinit var etCity: TextView
    private lateinit var etSelfInvestment: TextView
    private lateinit var etReason: TextView
    private lateinit var etCreditFromBank: TextView
    private lateinit var btnSettledSubmit: TextView
    private lateinit var total: TextView
    private lateinit var etEmploymentGiven: TextView
    private lateinit var ivSettlementPhoto: ImageView
    private lateinit var ivPassbookCopy: ImageView
    private lateinit var ivAppointmentLetter: ImageView

    // private lateinit var spinnerStatus: AutoCompleteTextView
    private lateinit var spinnerAccount: AutoCompleteTextView
    private lateinit var spinnerEarning: AutoCompleteTextView
    private lateinit var spinnerFamimyMemberJob: AutoCompleteTextView
    private lateinit var llselfInvestment: LinearLayout
    private lateinit var llBankinvestment: LinearLayout
    private lateinit var llTotal: LinearLayout
    private lateinit var ifsc: EditText
    private lateinit var settleText: TextView
    private lateinit var selectedImageView: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var spinnerSettleStatus: AutoCompleteTextView

    // self service adapter
    /*   private lateinit var selfAndServiceAdapter: ArrayAdapter<String>
       private val selfAndServiceList = listOf("SelfSettled", "Settled In service")
   */
    private lateinit var settleStatusAdapter: ArrayAdapter<String>
    private var settleStatusList: List<SettlementStatus> = listOf()
    private var statusNameList = ArrayList<String>()
    private var statusId = ArrayList<String>()



    private lateinit var accountStatusAdapter: ArrayAdapter<String>
    private val accountStatusList = listOf("Active", "InActive")


    private lateinit var familyMemberPartTimeJobAdapter: ArrayAdapter<String>
    private val familyMemberPartTimeJobList = listOf("Yes", "No")

    private var ifscSearchList: List<BankDetailsList> = listOf()

    private lateinit var salaryRangeAdapter: ArrayAdapter<String>
    private var SalaryRangeList: List<SalaryRange> = listOf()
    private var SalaryRangeNameList = ArrayList<String>()
    private var SalaryRangeIdList = ArrayList<String>()

    //Selected all values
    var selectedBankCode = 0
    var SelectedBranchCode = 0
    var accLenghth = 0
    private var selectedStatusItem = ""
    private var selectedStatusId = ""
    private var selectedSelfInvestmentItem = ""
    private var SelectedCreditFromBankItem = ""
    private var selectedTotal = 0
    private var selectedUpperCaseIfscText = ""
    private var selectedLoanAcc = ""
    private var selectedCity = ""
    private var selectedReason = ""
    private var selectdeAccountStatus = ""
    private var selectedRangeId = ""
    private var selectedEmploymentGiven = ""
    private var selectedFamilyMemberPartTime = ""
    private var selectedSettlementPhoto = ""
    private var selectedPassbookCopy = ""
    private var selectedAppointmentLetter = ""

    lateinit var userPreferences: UserPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.veryfication_settlement_bottomsheet_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())

        commonViewModel.getSalaryRange(
            AppUtil.getSavedTokenPreference(requireContext()),
            SalaryRangeReq(
                BuildConfig.VERSION_NAME,
                AppUtil.getAndroidId(requireContext()),
                userPreferences.getUseID()
            )
        )

        // log("token", AppUtil.getSavedTokenPreference(requireContext())+ BuildConfig.VERSION_NAME, userPreferences.getUseID(),AppUtil.getAndroidId(requireContext())))

        //  log("token", AppUtil.getSavedTokenPreference(requireContext())

        commonViewModel.getSettleStatusApi(
            AppUtil.getSavedTokenPreference(requireContext()),
            SettleStatusRequest(
                BuildConfig.VERSION_NAME,
                userPreferences.getUseID(),
                AppUtil.getAndroidId(requireContext())
            )
        )

        collectStatusResponse()

        collectSalaryRangeResponse()

        // ✅ Load saved data and prefill the form
        //loadsavefromdata()


        val ifscEt = view.findViewById<EditText>(R.id.etIfscCode)
        val ifscBtn = view.findViewById<TextView>(R.id.ifscBtn)
        bankName = view.findViewById<TextView>(R.id.etBankName)
        branchName = view.findViewById<TextView>(R.id.etBranchName)
        total = view.findViewById<TextView>(R.id.total)
        progressBar = view.findViewById(R.id.progressBarr)
        accountNo = view.findViewById(R.id.etBankAcNo)
        spinnerSettleStatus = view.findViewById<AutoCompleteTextView>(R.id.spinnerStatusBottom)
        spinnerAccount = view.findViewById<AutoCompleteTextView>(R.id.spinnerAccountStatus)
        spinnerFamimyMemberJob =
            view.findViewById<AutoCompleteTextView>(R.id.spinnerFamilyMemberPartTime)
        llselfInvestment = view.findViewById(R.id.llselfinvestment)
        llBankinvestment = view.findViewById(R.id.llBankinvestment)
        llTotal = view.findViewById(R.id.llTotal)
        etCity = view.findViewById(R.id.etCity)


        etSelfInvestment = view.findViewById(R.id.etselfInvestment)
        etReason = view.findViewById(R.id.etReason)
        etCreditFromBank = view.findViewById(R.id.etCredit)
        etEmploymentGiven = view.findViewById(R.id.etEmploymentGiven)
        ivSettlementPhoto = view.findViewById(R.id.settlmentPhoto)
        ivPassbookCopy = view.findViewById(R.id.passbookPhoto)
        ivAppointmentLetter = view.findViewById(R.id.appointmentLetter)
        spinnerEarning = view.findViewById<AutoCompleteTextView>(R.id.spinnerEarningsIncome)
        btnSettledSubmit = view.findViewById(R.id.btnSettled)
        settleText = view.findViewById(R.id.settleText)


        //account adapter
        accountStatusAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            accountStatusList
        )
        spinnerAccount.setAdapter(accountStatusAdapter)



        spinnerAccount.setOnItemClickListener { parent, view, position, id ->
            selectdeAccountStatus = parent.getItemAtPosition(position).toString()
        }
        //family Member adapter
        familyMemberPartTimeJobAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            familyMemberPartTimeJobList
        )
        spinnerFamimyMemberJob.setAdapter(familyMemberPartTimeJobAdapter)

        spinnerFamimyMemberJob.setOnItemClickListener { parent, view, position, id ->
            selectedFamilyMemberPartTime = parent.getItemAtPosition(position).toString()
        }

        ivSettlementPhoto.setOnClickListener {
            openGallery(ivSettlementPhoto)
        }
        // salary adapter
        salaryRangeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            SalaryRangeNameList
        )
        spinnerEarning.setAdapter(salaryRangeAdapter)
        etCreditFromBank.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Get values safely, ensuring no empty strings
                val selfInvestment = etSelfInvestment.text.toString().toIntOrNull() ?: 0
                val creditFromBank = etCreditFromBank.text.toString().toIntOrNull() ?: 0

                selectedTotal = selfInvestment + creditFromBank
                total.text = selectedTotal.toString()
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
        // save form data

        // submit button
        btnSettledSubmit.setOnClickListener {
            selectedSelfInvestmentItem = etSelfInvestment.text.toString()
            SelectedCreditFromBankItem = etCreditFromBank.text.toString()
            selectedUpperCaseIfscText = ifscEt.text.toString()
            selectedLoanAcc = accountNo.text.toString()
            selectedCity = etCity.text.toString()
            selectedReason = etReason.text.toString()
            selectedEmploymentGiven = etEmploymentGiven.text.toString()

            if (selectedStatusItem.isEmpty()) {
                Toast.makeText(requireContext(), "Please select status.", Toast.LENGTH_SHORT)
                    .show()
            }
            else if (selectedStatusItem == "Self Settled") {
                if (selectedSelfInvestmentItem.isNotEmpty() &&
                    SelectedCreditFromBankItem.isNotEmpty() &&
                    selectedUpperCaseIfscText.isNotEmpty() &&
                    selectedLoanAcc.isNotEmpty() &&
                    selectedCity.isNotEmpty() &&
                    selectedReason.isNotEmpty() &&
                    selectdeAccountStatus.isNotEmpty() &&
                    selectedRangeId.isNotEmpty() &&
                    selectedEmploymentGiven.isNotEmpty() &&
                    selectedFamilyMemberPartTime.isNotEmpty() &&
                    selectedSettlementPhoto.isNotEmpty() &&
                    selectedPassbookCopy.isNotEmpty() &&
                    selectedAppointmentLetter.isNotEmpty()
                ) {

                    val result = Bundle().apply {
                        putString("selectedStatusItem", selectedStatusId)
                        putString("selectedSelfInvestmentItem", selectedSelfInvestmentItem)
                        putString("SelectedCreditFromBankItem", SelectedCreditFromBankItem)
                        putInt("selectedTotal", selectedTotal)
                        putString("selectedUpperCaseIfscText", selectedUpperCaseIfscText)
                        putString("selectedBankCode", selectedBankCode.toString())
                        putString("selectedBranchCode", SelectedBranchCode.toString())
                        putString("selectedLoanAcc", selectedLoanAcc)
                        putString("selectedCity", selectedCity)
                        putString("selectedReason", selectedReason)
                        putString("selectdeAccountStatus", selectdeAccountStatus)
                        putString("selectedRangeId", selectedRangeId)
                        putString("selectedEmploymentGiven", selectedEmploymentGiven)
                        putString("selectedFamilyMemberPartTime", selectedFamilyMemberPartTime)
                        putString("selectedSettlementPhoto", selectedSettlementPhoto)
                        putString("selectedPassbookCopy", selectedPassbookCopy)
                        putString("selectedAppointmentLetter", selectedAppointmentLetter)
                    }
                    commonViewModel.settlementData.value=result

                    // ✅ Dismiss the bottom sheet
                    dismiss()
                } else {
                    // Show success message when all fields are filled
                    Toast.makeText(
                        requireContext(),
                        "Kindly fill all details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else if (selectedStatusItem == "Settled in service") {
                if (selectedUpperCaseIfscText.isNotEmpty() &&
                    selectedLoanAcc.isNotEmpty() &&
                    selectedCity.isNotEmpty() &&
                    selectedReason.isNotEmpty() &&
                    selectdeAccountStatus.isNotEmpty() &&
                    selectedRangeId.isNotEmpty() &&
                    selectedEmploymentGiven.isNotEmpty() &&
                    selectedFamilyMemberPartTime.isNotEmpty() &&
                    selectedPassbookCopy.isNotEmpty() &&
                    selectedAppointmentLetter.isNotEmpty()
                ) {

                    val result = Bundle().apply {
                        putString("selectedStatusItem", selectedStatusId)
                        putString("selectedSelfInvestmentItem", selectedSelfInvestmentItem)
                        putString("SelectedCreditFromBankItem", SelectedCreditFromBankItem)
                        putInt("selectedTotal", selectedTotal)
                        putString("selectedUpperCaseIfscText", selectedUpperCaseIfscText)
                        putString("selectedBankCode", selectedBankCode.toString())
                        putString("selectedBranchCode", SelectedBranchCode.toString())
                        putString("selectedLoanAcc", selectedLoanAcc)
                        putString("selectedCity", selectedCity)
                        putString("selectedReason", selectedReason)
                        putString("selectdeAccountStatus", selectdeAccountStatus)
                        putString("selectedRangeId", selectedRangeId)
                        putString("selectedEmploymentGiven", selectedEmploymentGiven)
                        putString("selectedFamilyMemberPartTime", selectedFamilyMemberPartTime)
                        putString("selectedSettlementPhoto", selectedSettlementPhoto)
                        putString("selectedPassbookCopy", selectedPassbookCopy)
                        putString("selectedAppointmentLetter", selectedAppointmentLetter)
                    }
                    commonViewModel.settlementData.value=result

                    dismiss()

                } else
                    Toast.makeText(
                        requireContext(),
                        "Kindly fill all details first",
                        Toast.LENGTH_SHORT
                    ).show()

            }
            //validateform()


            else if (selectedStatusItem == "SHG") {
                if (selectedUpperCaseIfscText.isNotEmpty() &&
                    selectedLoanAcc.isNotEmpty() &&
                    selectedCity.isNotEmpty() &&
                    selectedReason.isNotEmpty() &&
                    selectdeAccountStatus.isNotEmpty() &&
                    selectedRangeId.isNotEmpty() &&
                    selectedEmploymentGiven.isNotEmpty() &&
                    selectedFamilyMemberPartTime.isNotEmpty() &&
                    selectedPassbookCopy.isNotEmpty() &&
                    selectedAppointmentLetter.isNotEmpty()
                ) {

                    val result = Bundle().apply {
                        putString("selectedStatusItem", selectedStatusId)
                        putString("selectedSelfInvestmentItem", selectedSelfInvestmentItem)
                        putString("SelectedCreditFromBankItem", SelectedCreditFromBankItem)
                        putInt("selectedTotal", selectedTotal)
                        putString("selectedUpperCaseIfscText", selectedUpperCaseIfscText)
                        putString("selectedBankCode", selectedBankCode.toString())
                        putString("selectedBranchCode", SelectedBranchCode.toString())
                        putString("selectedLoanAcc", selectedLoanAcc)
                        putString("selectedCity", selectedCity)
                        putString("selectedReason", selectedReason)
                        putString("selectdeAccountStatus", selectdeAccountStatus)
                        putString("selectedRangeId", selectedRangeId)
                        putString("selectedEmploymentGiven", selectedEmploymentGiven)
                        putString("selectedFamilyMemberPartTime", selectedFamilyMemberPartTime)
                        putString("selectedSettlementPhoto", selectedSettlementPhoto)
                        putString("selectedPassbookCopy", selectedPassbookCopy)
                        putString("selectedAppointmentLetter", selectedAppointmentLetter)
                    }
                    commonViewModel.settlementData.value=result

                    // ✅ Dismiss the bottom sheet
                    dismiss()

                } else
                    Toast.makeText(
                        requireContext(),
                        "Kindly fill all details first",
                        Toast.LENGTH_SHORT
                    ).show()

            }
            else if (selectedStatusItem == "Bank") {
                if (selectedUpperCaseIfscText.isNotEmpty() &&
                    selectedLoanAcc.isNotEmpty() &&
                    selectedCity.isNotEmpty() &&
                    selectedReason.isNotEmpty() &&
                    selectdeAccountStatus.isNotEmpty() &&
                    selectedRangeId.isNotEmpty() &&
                    selectedEmploymentGiven.isNotEmpty() &&
                    selectedFamilyMemberPartTime.isNotEmpty() &&
                    selectedPassbookCopy.isNotEmpty() &&
                    selectedAppointmentLetter.isNotEmpty()
                ) {

                    val result = Bundle().apply {
                        putString("selectedStatusItem", selectedStatusId)
                        putString("selectedSelfInvestmentItem", selectedSelfInvestmentItem)
                        putString("SelectedCreditFromBankItem", SelectedCreditFromBankItem)
                        putInt("selectedTotal", selectedTotal)
                        putString("selectedUpperCaseIfscText", selectedUpperCaseIfscText)
                        putString("selectedBankCode", selectedBankCode.toString())
                        putString("selectedBranchCode", SelectedBranchCode.toString())
                        putString("selectedLoanAcc", selectedLoanAcc)
                        putString("selectedCity", selectedCity)
                        putString("selectedReason", selectedReason)
                        putString("selectdeAccountStatus", selectdeAccountStatus)
                        putString("selectedRangeId", selectedRangeId)
                        putString("selectedEmploymentGiven", selectedEmploymentGiven)
                        putString("selectedFamilyMemberPartTime", selectedFamilyMemberPartTime)
                        putString("selectedSettlementPhoto", selectedSettlementPhoto)
                        putString("selectedPassbookCopy", selectedPassbookCopy)
                        putString("selectedAppointmentLetter", selectedAppointmentLetter)
                    }
                    commonViewModel.settlementData.value=result

                    // ✅ Dismiss the bottom sheet
                    dismiss()

                } else
                    Toast.makeText(
                        requireContext(),
                        "Kindly fill all details first",
                        Toast.LENGTH_SHORT
                    ).show()

            }
        }





        spinnerEarning.setOnItemClickListener { parent, view, position, id ->

            if (position in SalaryRangeList.indices) {
                selectedRangeId = SalaryRangeIdList[position]

            }
        }

        // Rohit
        spinnerSettleStatus.setOnItemClickListener { parent, view, position, id ->
            selectedStatusItem = parent.getItemAtPosition(position) as String
            selectedStatusId = statusId[position]
            Log.d("FollowUpFra", "Status: $selectedStatusItem, SelectedReason: $selectedStatusId")


            // Check if the selected item is "Settled In service"
            if (selectedStatusItem.equals("Settled In service", ignoreCase = true)) {
                // Hide the self-investment section
                llselfInvestment.visibility = View.GONE
                llBankinvestment.visibility = View.GONE
                llTotal.visibility = View.GONE
                selectedSelfInvestmentItem = ""
                SelectedCreditFromBankItem = ""
                selectedTotal = 0
//                etSelfInvestment.text.clear()
//                etCreditFromBank.text.clear()
                ivSettlementPhoto.setImageDrawable(null)
                settleText.visibility = View.GONE
                ivSettlementPhoto.visibility = View.GONE
            } else if (selectedStatusItem.equals("Self Settled", ignoreCase = true)) {
                llselfInvestment.visibility = View.VISIBLE
                llBankinvestment.visibility = View.VISIBLE
                llTotal.visibility = View.VISIBLE
                settleText.visibility = View.VISIBLE
                ivSettlementPhoto.visibility = View.VISIBLE
                //selectedStatusItem=""
                selectedSelfInvestmentItem = ""
                SelectedCreditFromBankItem = ""
                selectedTotal = 0
//                etSelfInvestment.text.clear()
//                etCreditFromBank.text.clear()

            }
        }
        ivSettlementPhoto.setOnClickListener {
            openGallery(ivSettlementPhoto)
        }


        ivPassbookCopy.setOnClickListener {
            openGallery(ivPassbookCopy)
        }

        ivAppointmentLetter.setOnClickListener {
            openGallery(ivAppointmentLetter)
        }

//        ifscBtn.setOnClickListener {
//
//            if (ifscEt.text.toString().isNotEmpty()) {
//                val inputText = ifscEt.text.toString()
//                selectedUpperCaseIfscText = inputText.uppercase()
//                commonViewModel.getbankIFSCAPI(
//                    AppUtil.getSavedTokenPreference(requireContext()),
//                    BankIFSCSearchReq(
//                        BuildConfig.VERSION_NAME,
//                        selectedUpperCaseIfscText,
//                        AppUtil.getAndroidId(requireContext()),
//                        userPreferences.getUseID()
//                    )
//                )
//                collectBankDetailResponse()
//            } else
//                toastShort("Please Enter Ifsc Code")
//        }
    }


    // Bank Details
    private fun collectBankDetailResponse() {
        lifecycleScope.launch {
            commonViewModel.getbankIFSCAPI.collectLatest { it ->
                when (it) {
                    is Resource.Loading -> Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT)
                        .show()

                    is Resource.Error -> {
                        //   hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Internal Server Error",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    is Resource.Success -> {
                        // hideProgressBar()
                        it.data?.let { getBankDetails ->
                            if (getBankDetails.responseCode == 200) {
                                ifscSearchList = getBankDetails.bankDetailsList


                                for (x in ifscSearchList) {
                                    bankName.text = x.bankName
                                    branchName.text = x.branchName
                                    selectedBankCode = x.bankCode
                                    SelectedBranchCode = x.branchCode
                                    accLenghth = x.branchCode
                                }
                                accountNo.filters = arrayOf(InputFilter.LengthFilter(accLenghth))

                            } else if (getBankDetails.responseCode == 301) {
                                Toast.makeText(
                                    requireContext(),
                                    getBankDetails.responseMsg,
                                    Toast.LENGTH_SHORT
                                ).show()


                            } else if (getBankDetails.responseCode == 302) {

                                Toast.makeText(
                                    requireContext(),
                                    getBankDetails.responseDesc,
                                    Toast.LENGTH_SHORT
                                ).show()
                                bankName.text = ""
                                branchName.text = ""
                                selectedBankCode = 0
                                SelectedBranchCode = 0
                                accLenghth = 0

                            } else if (getBankDetails.responseCode == 401) {
                                AppUtil.showSessionExpiredDialog(
                                    findNavController(),
                                    requireContext()
                                )


                            } else toastLong(getBankDetails.responseDesc)

                        } ?: Toast.makeText(
                            requireContext(),
                            "Internal Server Error",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }
    }


    //status response
    private fun collectStatusResponse() {
        lifecycleScope.launch {
            commonViewModel.getSettleStatusApi.collectLatest { it ->
                when (it) {
                    is Resource.Loading -> Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT)
                        .show()

                    is Resource.Error -> {
                        //   hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Internal Server Error",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    is Resource.Success -> {
                        // hideProgressBar()
                        it.data?.let { getStatus ->
                            if (getStatus.responseCode == 200) {
                                settleStatusList = getStatus.wrappedList


                                for (x in settleStatusList) {

                                    statusId.add(x.statusId.toString())
                                    statusNameList.add(x.status)

                                }

                                // Update spinner adapter with string list
                                settleStatusAdapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_spinner_dropdown_item,
                                    statusNameList
                                )
                                spinnerSettleStatus.setAdapter(settleStatusAdapter)

                            } else if (getStatus.responseCode == 301) {
                                Toast.makeText(
                                    requireContext(),
                                    getStatus.responseMsg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (getStatus.responseCode == 302) {
                                Toast.makeText(
                                    requireContext(),
                                    getStatus.responseMsg,
                                    Toast.LENGTH_SHORT
                                ).show()


                            } else if (getStatus.responseCode == 401) {
                                AppUtil.showSessionExpiredDialog(
                                    findNavController(),
                                    requireContext()
                                )


                            } else toastLong(getStatus.responseDesc)

                        } ?: Toast.makeText(
                            requireContext(),
                            "Internal Server Error",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }
    }


    //salary
    private fun collectSalaryRangeResponse() {
        lifecycleScope.launch {
            commonViewModel.salaryDetailsState.collectLatest { it ->
                when (it) {
                    is Resource.Loading -> Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT)
                        .show()

                    //showProgressBar()
                    is Resource.Error -> {
                        //  hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "Internal Server Error",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    is Resource.Success -> {
                        //  hideProgressBar()
                        it.data?.let { getSalaryRangeDetails ->
                            if (getSalaryRangeDetails.responseCode == 200) {
                                SalaryRangeList = getSalaryRangeDetails.wrappedList



                                for (x in SalaryRangeList) {
                                    SalaryRangeNameList.add(x.salaryRange)
                                    SalaryRangeIdList.add(x.salaryRangeId)
                                }
                                // ✅ Retrieve saved salary range from the database
                                val savedSalaryRangeId = getSavedSalaryRangeIdFromDB()

                                if (savedSalaryRangeId.isNotEmpty()) {
                                    val savedIndex = SalaryRangeIdList.indexOf(savedSalaryRangeId)
                                    if (savedIndex != -1) {
                                        // ✅ Set the saved salary range in the spinner
                                        spinnerEarning.setText(
                                            SalaryRangeNameList[savedIndex],
                                            false
                                        )
                                        selectedRangeId = savedSalaryRangeId
                                    }

                                    // ✅ Ask the user if they want to update their salary range
                                    showSalaryUpdateDialog()
                                }
                            } else if (getSalaryRangeDetails.responseCode == 301) {
                                Toast.makeText(
                                    requireContext(),
                                    getSalaryRangeDetails.responseMsg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (getSalaryRangeDetails.responseCode == 401) {
                                AppUtil.showSessionExpiredDialog(
                                    findNavController(),
                                    requireContext()
                                )
                            } else toastLong(getSalaryRangeDetails.responseDesc)

                        } ?: Toast.makeText(
                            requireContext(),
                            "Internal Server Error",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        }
    }
    //status

    private fun getSavedSalaryRangeIdFromDB(): String {
        val sharedPreferences =
            requireContext().getSharedPreferences("UserPreferences", Activity.MODE_PRIVATE)
        return sharedPreferences.getString("saved_salary_range_id", "") ?: ""
    }

    private fun saveSalaryRangeIdToDB(salaryRangeId: String) {
        val sharedPreferences =
            requireContext().getSharedPreferences("UserPreferences", Activity.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("saved_salary_range_id", salaryRangeId)
            apply()
        }
    }

    private fun showSalaryUpdateDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Update Salary Range")
        alertDialog.setMessage("Do you want to update your salary range?")

        alertDialog.setPositiveButton("Yes") { dialog, _ ->
            spinnerEarning.setText("", false)  // Clear the selection
            selectedRangeId = ""
            dialog.dismiss()
        }

        alertDialog.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    /* private fun showProgressBar() {
     }         progressBar.visibility = View.VISIBLE


     private fun hideProgressBar() {
         progressBar.visibility = View.GONE
     }*/

    private fun openGallery(imageView: ImageView) {
        selectedImageView = imageView // Store the clicked ImageView
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            val inputStream = requireActivity().contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            // Set the selected image to the clicked ImageView
            selectedImageView.setImageBitmap(bitmap)

            // Convert image to Base64
            val base64String = encodeImageToBase64(bitmap)

            // Store Base64 in the correct variable
            when (selectedImageView.id) {
                R.id.settlmentPhoto -> {
                    selectedSettlementPhoto = base64String
                }

                R.id.passbookPhoto -> {
                    selectedPassbookCopy = base64String
                }

                R.id.appointmentLetter -> {
                    selectedAppointmentLetter = base64String
                }
            }
        }
    }


    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}







