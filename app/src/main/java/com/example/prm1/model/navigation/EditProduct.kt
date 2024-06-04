package com.example.prm1.model.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.prm1.R
import com.example.prm1.model.FormType

class EditProduct(val id: Int) : Destination() {
    override fun navigate(controller: NavController) {
        controller.navigate(
            R.id.action_listFragment_to_formFragment,
            bundleOf("type" to FormType.Edit(id))
        )

    }
}