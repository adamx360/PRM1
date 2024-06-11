package com.example.prm1.fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.prm1.R
import com.example.prm1.databinding.FragmentPaintBinding
import com.example.prm1.model.Settings
import com.google.android.material.floatingactionbutton.FloatingActionButton

var picPath: String = ""

class PaintFragment : Fragment() {
    private lateinit var binding: FragmentPaintBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null

    private val onTakePhoto: (Boolean) -> Unit = { photography: Boolean ->
        if (photography) {
            loadBitmap()
        } else {
            imageUri?.let {
                requireActivity().contentResolver.delete(it, null, null)
            }
        }
    }

    private fun loadBitmap() {
        val imageUri = imageUri ?: return
        requireContext().contentResolver.openInputStream(imageUri)
            ?.use {
                BitmapFactory.decodeStream(it)
            }?.let {
                binding.paintView.background = it
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture(),
            onTakePhoto
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPaintBinding.inflate(
        inflater, container, false
    ).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createPicture()
        val fabSettings: FloatingActionButton = view.findViewById(R.id.settingsButton)
        fabSettings.setOnClickListener {
            findNavController().navigate(R.id.action_paintFragment_to_settingsFragment)
        }
        val fabSave: FloatingActionButton = view.findViewById(R.id.saveButton)
        fabSave.setOnClickListener {
            save()
            findNavController().popBackStack()
        }

    }

    private fun createPicture() {
        val imagesUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        imageUri = requireContext().contentResolver.insert(imagesUri, ct)
        imageUri?.let { cameraLauncher.launch(it) }
    }

    private fun createPicture2() {
        val imagesUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        imageUri = requireContext().contentResolver.insert(imagesUri, ct)
    }

    fun setSettings(settings: Settings) {
        binding.paintView.apply {
            color = settings.color
            size = settings.size
        }
    }

    private fun save() {
        val imageUri = imageUri ?: return createPicture2()
        val bmp = binding.paintView.generateBitmap()
        requireContext().contentResolver.openOutputStream(imageUri)?.use {
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, it)
        }
        picPath = imageUri.path ?: ""
        println(picPath)
    }
}