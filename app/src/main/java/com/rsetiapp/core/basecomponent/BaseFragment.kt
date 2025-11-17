package com.rsetiapp.core.basecomponent

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.core.util.UserPreferences
import com.rsetiapp.core.util.log
import com.rsetiapp.R
import com.rsetiapp.security.SecurityUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding>(private val bindingInflater: (inflator: LayoutInflater) -> VB) :
    Fragment() {


    private var _binding: VB? = null
    val binding: VB get() = _binding as VB
    private val progress: AlertDialog? by lazy {
        AppUtil.getProgressDialog(context)
    }

    private var baseActivity: BaseActivity<VB>? = null
    fun getActivityContext(): BaseActivity<VB>? = baseActivity
    private var loadingCount = 0

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = bindingInflater.invoke(inflater)
        if (_binding == null)
            throw IllegalArgumentException("Binding cannot be null")

        // 🔹 Prevent screenshots and screen recording for all fragments
        /* requireActivity().window.setFlags(
             WindowManager.LayoutParams.FLAG_SECURE,
             WindowManager.LayoutParams.FLAG_SECURE
         )*/
      //  SecurityUtils.keyLogsTest()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseActivity = activity as BaseActivity<VB>
    }

    /*fun showProgressBar() {
        progress?.show()
    }

    fun hideProgressBar() {
        progress?.dismiss()
    }*/

    fun showProgressBar() {
        // Ensure context is not null and the fragment is attached
        if (context != null && isAdded && progress?.isShowing == false) {
            progress?.show()
        }
    }

    fun hideProgressBar() {
        // Hide the progress bar if it's currently showing
        if (progress?.isShowing == true) {
            progress?.dismiss()
        }
    }

    fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackBar.view.setPadding(0, 0, 0, 0)
        snackBar.view.elevation = 0f
        snackBar.view.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_rectangle_grey)
        snackBar.show()
    }


    fun hideSoftKeyboard() {
        if (requireActivity().currentFocus == null) {
            return
        }
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }


    fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            requireActivity().window.currentFocus?.windowToken,
            0
        )
    }

    suspend fun <T> collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
            flow.collectLatest(collect)
        }
    }

    fun compressImageFile(file: File): File? {

        var compressedFile: File? = null
        if (!file.exists()) file.mkdirs()
        val actualSize = file.length() / 1024
        log("ActualSizeofFile :", "$actualSize KB")

        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedBitmap: Bitmap = getResizedBitmap(bitmap)
        if (compressedBitmap != null) {
            compressedFile = bitmapToFile(requireActivity(), compressedBitmap)
            if (compressedFile != null) {
                val compressedSize = compressedFile.length() / 1024
                log("ActualSizeofFile :", "$compressedSize KB")
            }
        }
        return compressedFile

    }


    private fun bitmapToFile(activity: Activity, bitmap: Bitmap): File? {
        var file: File? = null
        try {
            file = File(
                activity.externalCacheDir.toString() + File.separator
                        + "/" + System.currentTimeMillis() + ".jpg"
            )
            file.createNewFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapData = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: java.lang.Exception) {
            log("BitmapToFileExp", e.toString())
        }
        return file
    }

    private fun getResizedBitmap(bitmap: Bitmap, maxSize: Int = 500): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        height = maxSize
        //width = (height * bitmapRatio).toInt()
        width = (height * bitmapRatio).toInt()
        log("ProfilePicWidthAndHeight", "height: $height, width: $width")

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }


}