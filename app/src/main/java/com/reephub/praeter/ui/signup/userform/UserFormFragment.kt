package com.reephub.praeter.ui.signup.userform

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.reephub.praeter.ui.signup.SignUpViewModel

class UserFormFragment : Fragment() {

    private val mViewModel: SignUpViewModel by activityViewModels()

    companion object {
        val TAG = UserFormFragment::class.java.simpleName

        fun newInstance(): UserFormFragment {
            val args = Bundle()
            val fragment = UserFormFragment()
            fragment.arguments = args
            return fragment
        }
    }
}