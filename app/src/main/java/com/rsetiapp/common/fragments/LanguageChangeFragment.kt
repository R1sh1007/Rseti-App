package com.rsetiapp.common.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rsetiapp.R

import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.gone
import com.rsetiapp.core.util.visible
import com.rsetiapp.databinding.FragmentLanguageChangeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguageChangeFragment :
    BaseFragment<FragmentLanguageChangeBinding>(FragmentLanguageChangeBinding::inflate) {

    private lateinit var languageIconMap: Map<String, View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())

        setupLanguageIconMap()
        showSelectedLanguageIcon(AppUtil.getSavedLanguagePreference(requireContext()))

        binding.progressBackButton.setOnClickListener { findNavController().navigateUp() }

        setupClickListeners()
    }


    private fun setupLanguageIconMap() {
        languageIconMap = mapOf(
            "en" to binding.checkEnglishIcon,
            "hi" to binding.checkIconHindi,
            "ta" to binding.checkTamilIcon,
            "as" to binding.checkAssameseIcon,
            "bn" to binding.checkBengaliIcon,
            "gu" to binding.checkGujaratiIcon,
            "kn" to binding.checkKannadaIcon,
            "ml" to binding.checkMalayalamIcon,
            "or" to binding.checkOdiaIcon,
            "mr" to binding.checkMarathiIcon,
            "pa" to binding.checkPunjabiIcon,
            "te" to binding.checkTeluguIcon,
            "ur" to binding.checkUrduIcon
        )
    }


    private fun showSelectedLanguageIcon(code: String) {
        languageIconMap.values.forEach { it.gone() }
        languageIconMap[code]?.visible()
        val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.zoom_in_out)
        languageIconMap[code]?.startAnimation(shake)
    }


    private fun setupClickListeners() {
        mapOf(
            binding.languageEng to "en",
            binding.languageHindi to "hi",
            binding.languageTamil to "ta",
            binding.languageAssamese to "as",
            binding.languageBengali to "bn",
            binding.languageGujarati to "gu",
            binding.languageKannada to "kn",
            binding.languageMalayalam to "ml",
            binding.languageOdia to "or",
            binding.languageMarathi to "mr",
            binding.languagePunjabi to "pa",
            binding.languageTelugu to "te",
            binding.languageUrdu to "ur"
        ).forEach { (view, langCode) ->
            view.setOnClickListener { confirmLanguageChange(langCode) }
        }
    }

    private fun confirmLanguageChange(langCode: String) {
        showYesNoDialog(
            context = requireContext(),
            title = "Confirmation",
            message = "Do you want to change language?",
            onYesClicked = {
                lifecycleScope.launch {
                    AppUtil.changeAppLanguage(requireContext(), langCode)
                    AppUtil.saveLanguagePreference(requireContext(), langCode)
                    showSelectedLanguageIcon(langCode)
                    findNavController().navigateUp()
                }
            },
            onNoClicked = {}
        )
    }

    // (Existing method – untouched)
    fun showYesNoDialog(
        context: Context,
        title: String,
        message: String,
        onYesClicked: () -> Unit,
        onNoClicked: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton("Yes") { dialog, _ ->
            onYesClicked()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            onNoClicked()
            dialog.dismiss()
        }

        builder.setCancelable(true)
        builder.create().show()
    }
}