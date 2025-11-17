package com.rsetiapp.common.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.gone
import com.rsetiapp.core.util.visible
import com.rsetiapp.databinding.FragmentLanguageChangeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint

class
LanguageChangeFragment : BaseFragment<FragmentLanguageChangeBinding>(FragmentLanguageChangeBinding :: inflate)  {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())


        binding.progressBackButton.setOnClickListener {

            findNavController().navigateUp()
        }


        if (AppUtil.getSavedLanguagePreference(requireContext()).contains("en")){
            binding.checkEnglishIcon.visible()
            binding.checkIconHindi.gone()
            binding.checkTamilIcon.gone()
            binding.checkAssameseIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkGujaratiIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()
        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("hi")){
            binding.checkIconHindi.visible()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkAssameseIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkGujaratiIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()

        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("ta")){
            binding.checkTamilIcon.visible()
            binding.checkIconHindi.gone()
            binding.checkEnglishIcon.gone()
            binding.checkAssameseIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkGujaratiIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()



        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("bn")){
            binding.checkBengaliIcon.visible()
            binding.checkTamilIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkEnglishIcon.gone()
            binding.checkAssameseIcon.gone()
            binding.checkGujaratiIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()
        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("gu")){
            binding.checkGujaratiIcon.visible()
            binding.checkBengaliIcon.gone()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkAssameseIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()
        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("kn")){
            binding.checkGujaratiIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkAssameseIcon.gone()
            binding.checkKannadaIcon.visible()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()
        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("ml")){
            binding.checkGujaratiIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkAssameseIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.visible()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()
        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("or")){
            binding.checkGujaratiIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkAssameseIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.visible()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()
        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("mr")){
            binding.checkGujaratiIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkAssameseIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.visible()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()
        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("pa")){
            binding.checkGujaratiIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkAssameseIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.visible()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()
        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("te")){
            binding.checkGujaratiIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkAssameseIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.visible()
            binding.checkUrduIcon.gone()



        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("ur")){
            binding.checkGujaratiIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkAssameseIcon.gone()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.visible()



        }
        else if (AppUtil.getSavedLanguagePreference(requireContext()).contains("as")){
            binding.checkGujaratiIcon.gone()
            binding.checkBengaliIcon.gone()
            binding.checkTamilIcon.gone()
            binding.checkEnglishIcon.gone()
            binding.checkIconHindi.gone()
            binding.checkAssameseIcon.visible()
            binding.checkKannadaIcon.gone()
            binding.checkMalayalamIcon.gone()
            binding.checkOdiaIcon.gone()
            binding.checkMarathiIcon.gone()
            binding.checkPunjabiIcon.gone()
            binding.checkTeluguIcon.gone()
            binding.checkUrduIcon.gone()



        }
        else
            binding.checkEnglishIcon.visible()

        binding.languageEng.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button

                    AppUtil.changeAppLanguage(requireContext(),"en")
                    lifecycleScope.launch {
                        AppUtil.saveLanguagePreference(requireContext(),"en")
                        binding.checkEnglishIcon.visible()
                        binding.checkIconHindi.gone()
                        binding.checkTamilIcon.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkGujaratiIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        findNavController().navigateUp()

                    }



                },
                onNoClicked = {

                }
            )
        }

        binding.languageHindi.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"hi")
                        binding.checkIconHindi.visible()
                        binding.checkEnglishIcon.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkGujaratiIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"hi")

                        findNavController().navigateUp()


                    }



                },
                onNoClicked = {

                }
            )

        }

        binding.languageTamil.setOnClickListener {
            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"ta")
                        binding.checkTamilIcon.visible()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkGujaratiIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"ta")

                        findNavController().navigateUp()


                    }



                },
                onNoClicked = {

                }
            )

        }

        binding.languageAssamese.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"as")
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.visible()
                        binding.checkBengaliIcon.gone()
                        binding.checkGujaratiIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"as")
                        findNavController().navigateUp()



                    }



                },
                onNoClicked = {

                }
            )

        }
        binding.languageBengali.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"bn")
                        binding.checkBengaliIcon.visible()
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkGujaratiIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"bn")

                        findNavController().navigateUp()


                    }



                },
                onNoClicked = {

                }
            )

        }
        binding.languageGujarati.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"gu")
                        binding.checkGujaratiIcon.visible()
                        binding.checkBengaliIcon.gone()
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"gu")

                        findNavController().navigateUp()


                    }



                },
                onNoClicked = {

                }
            )

        }
        binding.languageKannada.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"kn")
                        binding.checkGujaratiIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkKannadaIcon.visible()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"kn")
                        findNavController().navigateUp()



                    }



                },
                onNoClicked = {

                }
            )

        }
        binding.languageMalayalam.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"ml")
                        binding.checkGujaratiIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.visible()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"ml")

                        findNavController().navigateUp()


                    }



                },
                onNoClicked = {

                }
            )

        }
        binding.languageOdia.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"or")
                        binding.checkGujaratiIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.visible()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"or")
                        findNavController().navigateUp()



                    }



                },
                onNoClicked = {

                }
            )

        }
        binding.languageMarathi.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"mr")
                        binding.checkGujaratiIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.visible()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"mr")

                        findNavController().navigateUp()


                    }



                },
                onNoClicked = {

                }
            )

        }
        binding.languagePunjabi.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"pa")
                        binding.checkGujaratiIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.visible()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"pa")

                        findNavController().navigateUp()


                    }



                },
                onNoClicked = {

                }
            )

        }
        binding.languageTelugu.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"te")
                        binding.checkGujaratiIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.visible()
                        binding.checkUrduIcon.gone()
                        AppUtil.saveLanguagePreference(requireContext(),"te")

                        findNavController().navigateUp()


                    }



                },
                onNoClicked = {

                }
            )

        }
        binding.languageUrdu.setOnClickListener {

            showYesNoDialog(
                context = requireContext(),  // Use your context here (e.g., `requireContext()` in fragments)
                title = "Confirmation",
                message = "Do you want to change language?",
                onYesClicked = {
                    // Action for Yes button
                    lifecycleScope.launch{

                        AppUtil.changeAppLanguage(requireContext(),"ur")
                        binding.checkGujaratiIcon.gone()
                        binding.checkBengaliIcon.gone()
                        binding.checkTamilIcon.gone()
                        binding.checkEnglishIcon.gone()
                        binding.checkIconHindi.gone()
                        binding.checkAssameseIcon.gone()
                        binding.checkKannadaIcon.gone()
                        binding.checkMalayalamIcon.gone()
                        binding.checkOdiaIcon.gone()
                        binding.checkMarathiIcon.gone()
                        binding.checkPunjabiIcon.gone()
                        binding.checkTeluguIcon.gone()
                        binding.checkUrduIcon.visible()
                        AppUtil.saveLanguagePreference(requireContext(),"ur")

                        findNavController().navigateUp()


                    }



                },
                onNoClicked = {

                }
            )

        }


    }

    fun showYesNoDialog(context: Context, title: String, message: String, onYesClicked: () -> Unit, onNoClicked: () -> Unit) {
        // Create the AlertDialog.Builder
        val builder = AlertDialog.Builder(context)

        // Set the title and message
        builder.setTitle(title)
        builder.setMessage(message)

        // Set the positive button (Yes)
        builder.setPositiveButton("Yes") { dialog, _ ->
            onYesClicked() // Execute the Yes action
            dialog.dismiss()
        }

        // Set the negative button (No)
        builder.setNegativeButton("No") { dialog, _ ->
            onNoClicked() // Execute the No action
            dialog.dismiss()
        }

        // Optionally set the dialog to be cancelable (can be canceled by clicking outside)
        builder.setCancelable(true)

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }



}





