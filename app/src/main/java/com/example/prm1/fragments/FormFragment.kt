package com.example.prm1.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.prm1.databinding.FragmentFormBinding
import com.example.prm1.model.FormType
import com.example.prm1.viewmodel.FormViewModel
import java.io.IOException

private const val TYPE_KEY = "type"
var LOC_CITY = "kraków"

class FormFragment : Fragment() {

    private lateinit var type: FormType
    private lateinit var binding: FragmentFormBinding
    private val viewModel by viewModels<FormViewModel>()
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFilePath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(TYPE_KEY, FormType::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getSerializable(TYPE_KEY) as? FormType
            } ?: FormType.New
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFormBinding.inflate(layoutInflater, container, false).also {
            binding = it
            binding.viewModel = viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewModel) {
            init((type as? FormType.Edit)?.id)
            navigation.observe(viewLifecycleOwner) {
                it.resolve(findNavController())
            }
        }

        setupButtons()
    }

    private fun startRecording() {
        audioFilePath = "${requireContext().externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFilePath)

            try {
                prepare()
                start()
                Toast.makeText(requireContext(), "Recording started", Toast.LENGTH_SHORT).show()
                binding.recordButton.isEnabled = false
                binding.stopButton.isEnabled = true
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Recording failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        Toast.makeText(requireContext(), "Recording stopped", Toast.LENGTH_SHORT).show()
        binding.stopButton.isEnabled = false
        binding.playButton.isEnabled = true
        binding.recordButton.isEnabled = true
    }

    private fun setupButtons() {
        binding.recordButton.setOnClickListener {
            startRecording()
        }

        binding.stopButton.setOnClickListener {
            stopRecording()
        }

        binding.playButton.setOnClickListener {
            playRecording()
        }

        binding.button.setOnClickListener {
            saveFormData()
        }

        binding.stopButton.isEnabled = false
        binding.playButton.isEnabled = false
    }

    private fun playRecording() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioFilePath)
                prepare()
                start()
                Toast.makeText(requireContext(), "Playback started", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Playback failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (requestCode == PERMISSION_REQUEST_LOCATION) {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            5000,
                            5f,
                            locationListener
                        )
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permission denied to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        5f,
                        locationListener
                    )
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied to access location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (requestCode == PERMISSION_REQUEST_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.recordButton.isEnabled = true
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied to record audio",
                    Toast.LENGTH_SHORT
                ).show()
                binding.recordButton.isEnabled = false
            }
        }
    }

    private fun saveFormData() {
        val viewModel = ViewModelProvider(this)[FormViewModel::class.java]
        viewModel.onSave()
    }

    override fun onStop() {
        super.onStop()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }

    companion object {
        private const val PERMISSION_REQUEST_LOCATION = 1
        private const val PERMISSION_REQUEST_RECORD_AUDIO = 2
    }
}