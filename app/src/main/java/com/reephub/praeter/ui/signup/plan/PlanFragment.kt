package com.reephub.praeter.ui.signup.plan

import android.os.Bundle
import com.reephub.praeter.ui.base.BaseFragment
import com.reephub.praeter.ui.signup.terms.TermsOfServiceFragment

class PlanFragment: BaseFragment() {
    override fun onConnected(isConnected: Boolean) {
        TODO("Not yet implemented")
    }


    companion object {
        val TAG = PlanFragment::class.java.simpleName

        fun newInstance(): PlanFragment {
            val args = Bundle()
            val fragment = PlanFragment()
            fragment.arguments = args
            return fragment
        }
    }
}