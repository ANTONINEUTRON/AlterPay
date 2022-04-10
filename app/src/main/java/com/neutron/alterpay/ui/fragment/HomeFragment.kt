package com.neutron.alterpay.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.neutron.alterpay.databinding.FragmentHomeBinding
import com.neutron.alterpay.ui.activity.MainActivity
import com.neutron.alterpay.ui.viewModel.OTPViewModel
import com.neutron.alterpay.utils.Alert
import java.util.regex.Matcher
import java.util.regex.Pattern

class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding

    private var imageCapture: ImageCapture? = null
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        homeBinding.viewFinder
        val mainActivity: MainActivity = activity as MainActivity
        if(mainActivity.allPermissionsGranted()){
            startCamera()
        }else{
            mainActivity.requestPermission()
        }
        homeBinding.imageCaptureButton.setOnClickListener {
            takePhoto()
        }
        homeBinding.scannedDetails.setOnClickListener {
            openScannedDetails()
        }
    }

    private fun takePhoto() {
        processingState()
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    Alert.show(requireContext(), "Scanned.......")
                    recognizeDetails(image)
                    super.onCaptureSuccess(image)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                }
            }
        )

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(homeBinding.viewFinder.surfaceProvider)
                }

            //Initialize camera
            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e("AlterPay", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun recognizeDetails(imageProxy: ImageProxy) {
        val image:InputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        recognizer.process(image)
            .addOnSuccessListener { result ->
                defaultState()
                val stringBuilder = StringBuilder("")
                for (block in result.textBlocks) {
                    stringBuilder.append(block.text.toString() + "\n")
                }
                val detectedText: String = result.text
                //TODO filter text and constrain(VALIDATE)
                //identify card number and cvv from returned text
                //show to user through bottom dialog which and auto send to api
                if(areAllDetailsDetected(detectedText)){
                    homeBinding.scannedDetails.performClick()
                }
            }
            .addOnFailureListener { e ->
                Alert.show(requireContext(), e.message.toString())
            }
    }

    private fun areAllDetailsDetected(detectedText: String): Boolean {
        val splitedDetectedText: List<String> = detectedText.split("\n")
        if (cardName.isNullOrEmpty()){
            detectName(splitedDetectedText)
        }

        if(cardNumber.isNullOrEmpty()){
            detectCardNumber(splitedDetectedText)
        }

        if (cvv.isNullOrEmpty()){
            detectCVV(splitedDetectedText)
        }

        if(expiryDate.isNullOrEmpty()){
            detectCardExpiryDate(splitedDetectedText)
        }

        if(
            !cardName.isNullOrEmpty()  && !cardNumber.isNullOrEmpty()
            && !cvv.isNullOrEmpty() && !expiryDate.isNullOrEmpty()
        ){
            return true
        }

        displayErrorHint()
        return false
    }

    private fun displayErrorHint() {
        var msg = StringBuilder("Not all details were detected. \n")
        if(cardNumber.isNullOrEmpty()){
            msg.append("> Card number was not detected\n")
        }
        if (cardName.isNullOrEmpty()){
            msg.append("> Card name was not detected\n")
        }
        if (cvv.isNullOrEmpty()){
            msg.append("> CVV was not detected\n")
        }
        if (expiryDate.isNullOrEmpty()){
            msg.append("> Expiry Date  was not detected\n")
        }
        msg.append("Scan the side which they are located. \nor you can click on Icon on the bottom-right to manually set the card details.")

        homeBinding.errorMsg.setText(msg)
    }

    fun openScannedDetails(){
        val cardDetailsFragment = CardDetailsFragment.newInstance(
            cardNumber,
            cardName,
            cvv,
            expiryDate
        )
        cardDetailsFragment.show(childFragmentManager,"Show bottom")

        //Reset values
        cardNumber = null
        cardName = null
        cvv = null
        expiryDate = null
    }

    fun processingState(){
        homeBinding.imageCaptureButton.isEnabled = false
//        homeBinding.progressCircular.visibility = View.VISIBLE
    }

    fun defaultState(){
        homeBinding.imageCaptureButton.isEnabled = true
//        homeBinding.progressCircular.visibility = View.GONE
    }

    companion object{
        var cardNumber: String? = null
        var cardName: String? = null
        var cvv: String? = null
        var expiryDate: String? = null

        fun detectCardNumber(splitedDetectedText: List<String>) {
            val regx = "\\b(?:\\d[ -]*?){13,16}\\b"
            for (item in splitedDetectedText){
                val pattern: Pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
                val matcher: Matcher = pattern.matcher(item)
                if (matcher.find()){
//                    cardDetailsLayoutBinding.cardNumberTextInput.editText?.apply {
//                        setText(
//                            item.replace(" ", "").replace("-", "")
//                        )
//                        inputType = InputType.TYPE_NULL
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                            isFocusable = false
//                        }
//                    }
                    cardNumber = item.replace(" ", "").replace("-", "")

                    break
                }
            }
        }

        fun detectCardExpiryDate(splitedDetectedText: List<String>) {
            val regx = "^(0[1-9]|1[0-2])/?([0-9]{2})$"
            for (item in splitedDetectedText){
                val pattern: Pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
                val matcher: Matcher = pattern.matcher(item)
                if (matcher.find()){
//                    cardDetailsLayoutBinding.expiryDate.editText?.setText(item)
                    expiryDate = item
                    break
                }
            }
        }

        fun detectName(splitedDetectedText: List<String>) {
            val regx = "^[\\p{L} .'-]+$"
            val pattern: Pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
            for (item in splitedDetectedText){
                val numberOfSpaces = item.length - item.replace(" ","").length
                if(numberOfSpaces in 1..3) {
                    val matcher: Matcher = pattern.matcher(item)
                    if (matcher.find()) {
//                        cardDetailsLayoutBinding.nameTextInput.editText?.setText(item)
                        cardName = item
                        break
                    }
                }
            }

        }

        fun detectCVV(splitedDetectedText: List<String>) {
            for(item in splitedDetectedText){
                if(item.length == 3){
                    var isNum = true
                    try {
                        val num = Integer.parseInt(item)
                    }catch (e: Exception){
                        isNum = false
                    }
                    if (isNum){
                        cvv = item
//                        cardDetailsLayoutBinding.cvv.editText?.setText(item)
                    }
                    break
                }
            }
        }

    }

}