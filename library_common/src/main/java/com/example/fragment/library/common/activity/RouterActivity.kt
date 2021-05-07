package com.example.fragment.library.common.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fragment.library.base.component.activity.BaseActivity
import com.example.fragment.library.base.utils.FragmentUtils
import com.example.fragment.library.common.constant.Router


abstract class RouterActivity : BaseActivity() {

    enum class NavMode { SWITCH, POP_BACK_STACK }

    private var curFragment: Class<out Fragment>? = null

    abstract fun frameLayoutId(): Int

    abstract fun navigation(
        name: Router,
        bundle: Bundle? = null,
        onBack: Boolean = true,
        navMode: NavMode = NavMode.SWITCH,
    )

    fun switcher(
        clazz: Class<out Fragment>,
        bundle: Bundle? = null,
        addToBackStack: Boolean,
        navMode: NavMode
    ) {
        if (navMode == NavMode.SWITCH) {
            curFragment =
                FragmentUtils.switcher(
                    supportFragmentManager,
                    frameLayoutId(),
                    curFragment,
                    clazz,
                    bundle,
                    addToBackStack
                )
        } else if (navMode == NavMode.POP_BACK_STACK) {
            curFragment = FragmentUtils.pop(supportFragmentManager, clazz)
        }
    }

}