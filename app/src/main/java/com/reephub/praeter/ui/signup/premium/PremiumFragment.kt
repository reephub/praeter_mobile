package com.reephub.praeter.ui.signup.premium

import android.os.Bundle
import com.reephub.praeter.ui.base.BaseFragment
import com.reephub.praeter.ui.signup.plan.PlanFragment

class PremiumFragment: BaseFragment() {
    override fun onConnected(isConnected: Boolean) {
        TODO("Not yet implemented")
    }



    companion object {
        val TAG = PremiumFragment::class.java.simpleName

        fun newInstance(): PremiumFragment {
            val args = Bundle()
            val fragment = PremiumFragment()
            fragment.arguments = args
            return fragment
        }
    }
}