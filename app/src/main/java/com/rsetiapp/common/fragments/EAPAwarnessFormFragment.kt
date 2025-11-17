package com.rsetiapp.common.fragments
import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.rsetiapp.common.model.response.WrappedList
import com.rsetiapp.common.model.response.BlockList
import com.rsetiapp.common.model.response.DistrictList
import com.rsetiapp.common.model.response.GrampanchayatList
import com.rsetiapp.common.model.response.VillageList
import com.rsetiapp.common.CommonViewModel
import com.rsetiapp.core.basecomponent.BaseFragment
import com.rsetiapp.core.util.Resource
import com.rsetiapp.core.util.toastShort
import com.rsetiapp.databinding.FragmentEapAwarnessBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rsetiapp.BuildConfig
import com.rsetiapp.R
import com.rsetiapp.common.CandidateBottomSheetFragment
import com.rsetiapp.common.CandidateUpdateListener
import com.rsetiapp.common.adapter.CandidateAdapter
import com.rsetiapp.common.model.request.Candidate
import com.rsetiapp.common.model.request.EAPInsertRequest
import com.rsetiapp.common.model.response.AutoFetch
import com.rsetiapp.common.model.response.EapList
import com.rsetiapp.common.model.response.Institute
import com.rsetiapp.common.model.response.Program
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.AppUtil.getCurrentDate
import com.rsetiapp.core.util.AppUtil.hasStoragePermission
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.toastLong
import com.rsetiapp.core.util.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class EAPAwarnessFormFragment  : BaseFragment<FragmentEapAwarnessBinding>(FragmentEapAwarnessBinding::inflate),
    CandidateUpdateListener {

    private var formName=""
    private var eapId=""

    private var stateNme=""
    private var stateCode=""
    private var districtCode=""
    private var districtName=""
    private var blockName=""
    private var blockCode=""
    private var gpName=""
    private var gpCode=""
    private var villageName=""
    private var villageCode=""
    private var eapName=""
    private var programCode=""


    private var selectedDate=""

    private var selectedTotalParticipants=""
    private var selectedNameOfNGO=""
    private var selectedNoOfAppExpectedNextMonth=""
    private var selectedBrief=""
    private var image1Base64=""
    private var image2Base64=""
    private var locationLatLang=""
    private var locationAddress=""
    private var imageUri: Uri? = null
    private var currentImageView: ImageView? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var adapter: CandidateAdapter
    private val candidateList = mutableListOf<Candidate>()

    private var institute: MutableList<Institute> = mutableListOf()
    private var eapData: MutableList<AutoFetch> = mutableListOf()



    private val commonViewModel: CommonViewModel by activityViewModels()
    private lateinit var locationSettingLauncher: ActivityResultLauncher<IntentSenderRequest>



    private var counts = ""
    private var latitude : Double? = null
    private var longitude : Double? = null

    private var orgCode = ""
    private var orgName = ""
    private var instituteName = ""
    private var instituteCode = ""
    private var officialName = ""
    private var designationName = ""



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())

        formName = arguments?.getString("formName").toString()
        eapId = arguments?.getString("eapId").toString()
        stateNme=  arguments?.getString("stateNme").toString()
         stateCode= arguments?.getString("stateCode").toString()
        districtCode= arguments?.getString("districtCode").toString()
       districtName= arguments?.getString("districtName").toString()
        blockName= arguments?.getString("blockName").toString()
         blockCode= arguments?.getString("blockCode").toString()
         gpName= arguments?.getString("gpName").toString()
        gpCode= arguments?.getString("gpCode").toString()
         villageName= arguments?.getString("villageName").toString()
        villageCode= arguments?.getString("villageCode").toString()
        eapName= arguments?.getString("eapName").toString()
         programCode= arguments?.getString("programCode").toString()


        binding.spinnerAutoState.text= stateNme
        binding.districValue.text= districtName
        binding.blockValue.text= blockName
        binding.gpValue.text= gpName
        binding.villageValue.text= villageName
        binding.eapName.text= eapName
        userPreferences = UserPreferences(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        init()

    }

    private fun init(){


        locationSettingLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // GPS was enabled by the user
            }
            else {
                // GPS was not enabled
            }
        }
        checkAndRequestStoragePermissions()

        checkAndPromptGPS()

        // checkAndRequestPermissions()
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        val candidateCountTextView = view?.findViewById<TextView>(R.id.candidteCount)

        @SuppressLint("SetTextI18n")
        fun updateCandidateCount(count: Int) {
            candidateCountTextView?.text = "Candidates: "+count.toString()
            counts=count.toString()
        }

        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        adapter = CandidateAdapter(
            candidateList,
            onDelete = { position ->
                if (position in candidateList.indices) {
                    candidateList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position, candidateList.size)
                    updateCandidateCount(candidateList.size)
                }
            },
            onUpdateCount = { count ->
                updateCandidateCount(count)
            }
        )

// Set adapter after initializing RecyclerView
        recyclerView?.adapter = adapter

// Set initial count after adapter is set
        updateCandidateCount(candidateList.size)

        commonViewModel.getStateListApi(AppUtil.getSavedTokenPreference(requireContext()),userPreferences.getUseID(),AppUtil.getAndroidId(requireContext()))
        commonViewModel.getEapAutoFetchListAPI(AppUtil.getSavedTokenPreference(requireContext()),userPreferences.getUseID(), BuildConfig.VERSION_NAME,AppUtil.getAndroidId(requireContext()))

        collectEapAutoFetchResponse()

        listener()
    }
    @SuppressLint("SetTextI18n")
    private fun listener(){

        val currentDate= getCurrentDate()
        binding.tvDate.text = currentDate
        selectedDate= currentDate

        binding.eapIdName.text= eapId
        //Submit Button

        binding.btnSubmit.setOnClickListener {

            selectedTotalParticipants =binding.etTotalParticipant.text.toString()
            selectedNameOfNGO =binding.etNameOfOrgt.text.toString()

            selectedNoOfAppExpectedNextMonth =binding.etNoOfAppExpec.text.toString()
            selectedBrief =binding.etBrief.text.toString()


            if (selectedDate.isNotEmpty()&& selectedTotalParticipants.isNotEmpty()&& selectedNameOfNGO.isNotEmpty()
                && selectedNoOfAppExpectedNextMonth.isNotEmpty() && selectedBrief.isNotEmpty()&& image1Base64.isNotEmpty()&&
                image2Base64.isNotEmpty()){



                val totalParticipant = binding.etTotalParticipant.text.toString()

                if (counts != totalParticipant) {

                    AppUtil.showAlertDialog(requireContext(),"Alert","Candidates should be equal to total participants")

                }


                else{

                    commonViewModel.insertEAPAPI(AppUtil.getSavedTokenPreference(requireContext()),
                        EAPInsertRequest(AppUtil.getAndroidId(requireContext()),userPreferences.getUseID(),
                            BuildConfig.VERSION_NAME,orgCode,eapId,instituteCode,selectedDate,selectedTotalParticipants,selectedNameOfNGO,officialName,designationName,
                        programCode,stateCode,districtCode,blockCode,gpCode,villageCode,

                        selectedNoOfAppExpectedNextMonth,selectedBrief,image1Base64,image2Base64,

                        latitude.toString(),

                        longitude.toString(),AppUtil.getSavedEntityPreference(requireContext()),AppUtil.getSavedHRIdPreference(requireContext()),candidateList))

                    collectInsertResponse()
                }

            }

            else
                toastShort("Kindly fill all the fields first")


        }





        binding.btnAddCandidate.setOnClickListener {
            val bottomSheet = CandidateBottomSheetFragment(candidateList, adapter) { count ->

                 counts = count.toString()
                view?.findViewById<TextView>(R.id.candidteCount)?.text = "Candidates: $count"
            }
            val totalParticipant = binding.etTotalParticipant.text.toString()

            if (counts != totalParticipant) {
                bottomSheet.show(parentFragmentManager, "CandidateBottomSheet")
            }
            else


           AppUtil.showAlertDialog(requireContext(),"Limit Reached","You cannot add more candidates as the maximum number of participants has been reached.")

        }


        binding.tvFormName.text= formName
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }










        binding.image1.setOnClickListener {

            openCamera(binding.image1)
        }
        binding.image2.setOnClickListener {
            openCamera(binding.image2)
        }
    }


    private fun collectEapAutoFetchResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.getEapAutoFetchListAPI) {
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
                        it.data?.let { getEapAutoFetchListAPI ->
                            if (getEapAutoFetchListAPI.responseCode == 200) {


                                for (x in getEapAutoFetchListAPI.wrappedList){

                                   institute= x.instituteData.toMutableList()
                                    eapData= x.autoFetchData.toMutableList()


                                }

                                for (x in institute){



                                    instituteName= x.instituteName
                                    instituteCode= x.instituteCode.toString()



                                    binding.tvInstituteName.text=instituteName


                                }

                                for (x in eapData){
                                    orgCode= x.orgCode
                                    orgName= x.orgName
                                    officialName= x.officialName
                                    designationName= x.designation
                                    AppUtil.saveEapCanAgeLimitPreference(requireContext(),x.ageLimit)
                                    binding.tvOrganizationName.text=orgName
                                    binding.tvparticipatingOfficialName.text=officialName
                                    binding.tvdesiginationName.text=designationName


                                }


                            } else if (getEapAutoFetchListAPI.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            }   else if (getEapAutoFetchListAPI.responseCode==401){
                                AppUtil.showSessionExpiredDialog(findNavController(),requireContext())
                            }
                            else {
                               // toastLong(getEapAutoFetchListAPI.responseDesc)
                            }
                        } ?: showSnackBar("Internal Server Error")
                    }

                }
            }
        }
    }


    private fun collectInsertResponse() {
        lifecycleScope.launch {
            collectLatestLifecycleFlow(commonViewModel.insertEAPAPI) {
                when (it) {
                    is Resource.Loading -> showProgressBar()
                    is Resource.Error -> {
                        hideProgressBar()
                        it.error?.let { baseErrorResponse ->
                            showSnackBar("Internal Server Error111")

                        }
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        it.data?.let { insertApiResp ->
                            if (insertApiResp.responseCode == 200) {

                                showSnackBar("Success")

                                findNavController().navigate(
                                    R.id.eapListFragment,
                                    null,
                                    NavOptions.Builder()
                                        .setPopUpTo(R.id.EAPAwarnessFormFragment, true)
                                        .build()
                                )



                            }


                            else if (insertApiResp.responseCode == 301) {
                                showSnackBar("Please Update from PlayStore")
                            }
                            else if (insertApiResp.responseCode==401){

                                AppUtil.showSessionExpiredDialog(findNavController(),requireContext())
                            }
                            else {
                                toastLong(insertApiResp.responseDesc)
                            }

                        } ?: showSnackBar("Internal Server Error")
                    }

                    else -> { showSnackBar("Internal Server Error")}
                }
            }
        }
    }



    private fun showDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()

        // Get today's date (milliseconds)
        val today = calendar.timeInMillis

        // Get the last day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val endOfMonth = calendar.timeInMillis

        // Set constraints: From today to the last day of the month
        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(today)     // Restrict to today
            .setEnd(endOfMonth)  // Restrict to the last day of the current month
            .setValidator(DateValidatorPointForward.now()) // Only future dates including today

        // Create Material Date Picker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a Date")
            .setSelection(today) // Default today
            .setCalendarConstraints(constraintsBuilder.build()) // Apply constraints
            .build()

        // Show Date Picker
        datePicker.show(parentFragmentManager, "DATE_PICKER")

        // Handle date selection
        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = sdf.format(Date(selection))
            textView.text = formattedDate
            selectedDate = formattedDate
        }
    }    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permissions.any {
                ContextCompat.checkSelfPermission(requireContext(), it) != android.content.pm.PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, 100)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults.all { it == android.content.pm.PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(requireContext(), "Permissions granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permissions denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        return try {
            val maxSize = 1024 // Resize to max 1024px width/height
            val width = bitmap.width
            val height = bitmap.height
            val scale = if (width > height) maxSize.toFloat() / width else maxSize.toFloat() / height

            val newWidth = (width * scale).toInt()
            val newHeight = (height * scale).toInt()

            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } catch (e: Exception) {
            e.printStackTrace()
            bitmap // Return the original bitmap if compression fails
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        return try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream) // Increase quality to 90
            val byteArray = outputStream.toByteArray()
            outputStream.close()
            Base64.encodeToString(byteArray, Base64.NO_WRAP) // Use NO_WRAP to avoid line breaks
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun openCamera(imageView: ImageView) {
        checkAndRequestPermissions()
        currentImageView = imageView

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
            != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Camera permission required!", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            if (imageBitmap == null || currentImageView == null) {
                Toast.makeText(requireContext(), "Image capture failed!", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }

            val compressedBitmap = compressBitmap(imageBitmap)
            currentImageView?.setImageBitmap(compressedBitmap)

            val base64Image = bitmapToBase64(compressedBitmap)

            when (currentImageView?.id) {
                R.id.image1 -> {
                    image1Base64 = base64Image
                }
                R.id.image2 -> {
                    image2Base64 = base64Image
                }
            }

            getCurrentLocation()
            binding.lllatLang.visible()
            binding.llAdress.visible()
        }
    }



    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude

                locationLatLang = "Lat: $latitude, Lng: $longitude"
                binding.location.text = locationLatLang

                // Fetch and update address
                getAddressFromLocation(latitude!!, longitude!!)
            } else {
                // If last known location is null, request a fresh location update
                requestNewLocation()
            }
        }.addOnFailureListener {
            binding.location.text = getString(R.string.location_not_found)
            binding.address.text = getString(R.string.address_not_found)
            Log.e("LocationError", "Failed to get location: ${it.message}")
        }
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocation() {
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, 5000 // Update every 5 sec
        ).apply {
            setWaitForAccurateLocation(true) // Ensures accuracy
            setMinUpdateIntervalMillis(2000) // Minimum update interval
            setMaxUpdateDelayMillis(10000) // Max delay
            setMaxUpdates(1) // Get only one update and stop
        }.build()

        val locationCallback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                locationResult.lastLocation?.let { location ->
                    latitude = location.latitude
                    longitude = location.longitude

                    locationLatLang = "Lat: $latitude, Lng: $longitude"
                    binding.location.text = locationLatLang

                    // Fetch and update address
                    getAddressFromLocation(latitude!!, longitude!!)

                    fusedLocationClient.removeLocationUpdates(this) // Stop updates after getting location
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }


    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        try {
            val geocoder = Geocoder(requireContext(), Locale("en", "IN"))
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val fullAddress = address.getAddressLine(0) ?: "Address not available"
                val city = address.locality ?: "Unknown City"
                val state = address.adminArea ?: "Unknown State"
                val pincode = address.postalCode ?: "No Pincode"
                val country = address.countryName ?: "Unknown Country"

                locationAddress = "$fullAddress, $city, $state, $pincode, $country"
                binding.address.text = locationAddress
            } else {
                binding.address.text = getString(R.string.address_not_found)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.address.text = getString(R.string.error_address)
        }
    }


    override fun onCandidateAdded(count: Int) {
        updateCandidateCount(count)    }

    @SuppressLint("SetTextI18n")
    fun updateCandidateCount(count: Int) {
        binding.candidteCount.text = "Candidates: $count"
        counts=count.toString()

    }
    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            // proceed with file/media access
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkAndRequestStoragePermissions() {
        if (!hasStoragePermission(requireContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                storagePermissionLauncher.launch(AppUtil.storagePermissions)
            } else {
                storagePermissionLauncher.launch(arrayOf(AppUtil.legacyStoragePermission))
            }
        } else {
            // Permissions already granted, continue your logic
        }
    }


    private fun checkAndPromptGPS() {
        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val task = settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // GPS is already enabled
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    locationSettingLauncher.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

}