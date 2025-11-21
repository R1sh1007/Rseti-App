package com.rsetiapp.common.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rsetiapp.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.common.model.request.FogotPaasReq
import com.rsetiapp.common.model.request.OtpGenerateRequest
import com.rsetiapp.common.model.request.ValidateOtpReq
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.util.*
import com.rsetiapp.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {

    private val commonViewModel: CommonViewModel by activityViewModels()
    private var countDownTimer: CountDownTimer? = null
    private var mobileNo = ""
    private var userId = ""

    private var otpJob: Job? = null
    private var forgotPassJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())
        init()
    }

    private fun init() {
        listener()
        resendOTPTimer()
        addTextWatchers()
        otpUI()
    }

    private fun listener() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvVerify.setOnClickListener {

            //changes
            val enteredOtp = "${binding.et1.text}${binding.et2.text}${binding.et3.text}${binding.et4.text}"

            commonViewModel.getOtpValidateApi(ValidateOtpReq(BuildConfig.VERSION_NAME,mobileNo,AppUtil.getAndroidId(requireContext()),enteredOtp,userId))
            collectValidateOtpResponse()
            clearOtpFields()
        }

        binding.tvSendOtpAgain.setOnClickListener {
            binding.tvSendOtpAgain.isEnabled = false
            binding.tvSendOtpAgain.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.color_grey)
            )

            mobileNo = binding.etPhone.text.toString()
            userId = binding.etId.text.toString()

            if (mobileNo.isNotEmpty() && userId.isNotEmpty()) {
                commonViewModel.generateOtpAPI(OtpGenerateRequest(BuildConfig.VERSION_NAME, userId, mobileNo,AppUtil.getAndroidId(requireContext())))
                collectOtpAndMobileVerifyResponse()
            } else {
                toastShort("Please fill all fields")
            }

            resendOTPTimer()
        }
        binding.progressButton.centerButton.setOnClickListener {
            mobileNo = binding.etPhone.text.toString()
            userId = binding.etId.text.toString()

            if (mobileNo.isNotEmpty() && userId.isNotEmpty()) {
                commonViewModel.generateOtpAPI(OtpGenerateRequest(BuildConfig.VERSION_NAME, userId, mobileNo,AppUtil.getAndroidId(requireContext())))
                collectOtpAndMobileVerifyResponse()
            } else {
                toastShort("Please fill all fields")
            }
        }
    }

    private fun resendOTPTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - (minutes * 60)
                binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvSendOtpAgain.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.color_dark_green)
                )
                binding.tvSendOtpAgain.isEnabled = true
            }
        }.start()
    }


    private fun collectOtpAndMobileVerifyResponse() {
        otpJob?.cancel() // ✅ Cancel the previous job before starting a new one
        otpJob = viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                commonViewModel.generateOtpAPI.collectLatest { response ->
                    when (response) {
                        is Resource.Loading -> showProgressBar()
                        is Resource.Error -> {
                            hideProgressBar()
                            response.error?.message?.let { toastShort(it) }
                        }
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { mobileVerifyRes ->
                                when (mobileVerifyRes.responseCode) {
                                    200 -> {
                                        toastShort(mobileVerifyRes.responseDesc)
                                        binding.clForgotOTP.visible()
                                        binding.tvVerify.visible()
                                        binding.llFields.gone()
                                        binding.etPhone.gone()
                                        binding.progressButton.root.gone()
                                    }
                                    301 -> showSnackBar("Please Update from PlayStore")

                                    201 -> toastShort(mobileVerifyRes.responseDesc)
                                    203 -> toastShort(mobileVerifyRes.responseDesc)
                                    else -> showSnackBar(mobileVerifyRes.responseDesc)
                                }
                            } ?: showSnackBar("Internal Server Error")
                        }
                    }
                }
            }
        }
    }
    private fun addTextWatchers() {
        binding.et1.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) binding.et2.requestFocus()
            showVerifyButtonUI()
        }

        binding.et2.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) binding.et3.requestFocus() else binding.et1.requestFocus()
            showVerifyButtonUI()
        }

        binding.et3.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) binding.et4.requestFocus() else binding.et2.requestFocus()
            showVerifyButtonUI()
        }

        binding.et4.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 1) binding.et4.clearFocus() else binding.et3.requestFocus()
            showVerifyButtonUI()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun otpUI() {
        binding.et1.setOnClickListener {
            showKeyboard(binding.et1)
        }
        binding.et2.setOnClickListener {
            showKeyboard(binding.et2)
        }
        binding.et3.setOnClickListener {
            showKeyboard(binding.et3)
        }
        binding.et4.setOnClickListener {
            showKeyboard(binding.et4)
        }

        binding.et4.onDone {

            //changes
            val enteredOtp = "${binding.et1.text}${binding.et2.text}${binding.et3.text}${binding.et4.text}"
                binding.clForgotOTP.gone()

            commonViewModel.getOtpValidateApi(ValidateOtpReq(BuildConfig.VERSION_NAME,mobileNo,AppUtil.getAndroidId(requireContext()),enteredOtp,userId))
            collectValidateOtpResponse()
               /* commonViewModel.forgetPasswordAPI(FogotPaasReq(BuildConfig.VERSION_NAME, userId))
                collectForgotPassSendResponse()*/

        }
    }
    private fun collectForgotPassSendResponse() {
        forgotPassJob?.cancel() // ✅ Cancel the previous job before starting a new one
        forgotPassJob = viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                commonViewModel.forgetPasswordAPI.collectLatest { response ->
                    when (response) {
                        is Resource.Loading -> showProgressBar()
                        is Resource.Error -> {
                            hideProgressBar()
                            response.error?.message?.let { toastShort(it) }
                        }
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { mobileVerifyRes ->
                                when (mobileVerifyRes.responseCode) {
                                    200 -> {
                                        toastShort(mobileVerifyRes.responseDesc)
                                        findNavController().navigateUp()
                                    }
                                    301 -> showSnackBar("Please Update from PlayStore")
                                    304 -> mobileVerifyRes.responseMsg?.let { showSnackBar(it) }
                                    303 -> toastLong(mobileVerifyRes.responseDesc)
                                    else -> showSnackBar(mobileVerifyRes.responseDesc)
                                }
                            } ?: showSnackBar("Internal Server Error")
                        }
                    }
                }
            }
        }
    }

    private fun clearOtpFields() {
        binding.et1.text.clear()
        binding.et2.text.clear()
        binding.et3.text.clear()
        binding.et4.text.clear()
    }

    private fun setOtpFieldError() {
        val errorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_otp_invalid_rectangle)
        binding.et1.background = errorDrawable
        binding.et2.background = errorDrawable
        binding.et3.background = errorDrawable
        binding.et4.background = errorDrawable
    }
    private fun showVerifyButtonUI() {
        val enteredOtp = "${binding.et1.text}${binding.et2.text}${binding.et3.text}${binding.et4.text}"

        if (enteredOtp.length == 4) {
            binding.tvVerify.visible()  // ✅ Show Verify Button
        } else {
            binding.tvVerify.gone()  // ✅ Hide Verify Button
        }
    }

    private fun collectValidateOtpResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getOtpValidateApi) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            toastShort(baseErrorResponse.message)
                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { getOtpValidateApi ->
                            if (getOtpValidateApi.responseCode == 200) {
                                toastShort(getOtpValidateApi.responseDesc)
                                binding.clForgotOTP.gone()
                                commonViewModel.forgetPasswordAPI(FogotPaasReq(BuildConfig.VERSION_NAME, userId))
                                collectForgotPassSendResponse()

                            } else if (getOtpValidateApi.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            }

                            else if (getOtpValidateApi.responseCode == 207) {
                                toastShort(getOtpValidateApi.responseDesc)
                            }
                            else if (getOtpValidateApi.responseCode == 210) {
                                toastShort(getOtpValidateApi.responseDesc)
                            }



                            else {
                                showSnackBar(getOtpValidateApi.responseDesc)
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }
                }
            }
        }
    }



}
