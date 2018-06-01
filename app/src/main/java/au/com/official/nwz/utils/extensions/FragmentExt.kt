package au.com.official.nwz.utils.extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import au.com.official.nwz.R

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun Fragment.replaceChildFragmentInFragment(fragment: Fragment, frameId: Int, tag: String) {
    childFragmentManager.transact {
        replace(frameId, fragment, tag)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun Fragment.addChildFragmentToFragment(fragment: Fragment, tag: String) {
    childFragmentManager.transact {
        add(fragment, tag)
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        setCustomAnimations(0, R.anim.fade_out_short)
        action()
    }.commit()
}
