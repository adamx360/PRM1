package com.example.prm1.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.prm1.R
import com.example.prm1.databinding.FragmentSettingsBinding
import com.example.prm1.model.Settings

class SettingsFragment : Fragment(), SeekBar.OnSeekBarChangeListener {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSettingsBinding.inflate(
        inflater, container, false
    ).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.seekBar.setOnSeekBarChangeListener(this)
        binding.color.setOnCheckedChangeListener { _, _ -> changeSettings() }
    }

    private fun idToColor(id: Int): Int = when (id){
        R.id.black -> Color.BLACK
        R.id.white -> Color.WHITE
        R.id.green -> Color.GREEN
        else -> throw NotImplementedError()
    }

    override fun onProgressChanged(seekBar: SeekBar, value: Int, isUserSet: Boolean) {
        if (isUserSet) changeSettings()
    }

    private fun changeSettings() {
        val settings = Settings(
            idToColor(binding.color.checkedRadioButtonId),
            binding.seekBar.progress.toFloat()
        )
        (parentFragmentManager.findFragmentByTag(PaintFragment::class.java.name) as? PaintFragment)?.setSettings(settings)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {}

    override fun onStopTrackingTouch(p0: SeekBar?) {}

}