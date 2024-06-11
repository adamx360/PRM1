package com.example.prm1.model.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.prm1.R
import com.example.prm1.model.FormType

class AddPicture : Destination() {
    override fun navigate(controller: NavController) {
        controller.navigate(
            R.id.action_formFragment_to_paintFragment,
            bundleOf("type" to FormType.New)
        )
    }
}