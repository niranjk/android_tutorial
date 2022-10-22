package com.niranjan.androidtutorials

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class LifecycleDemoFragment : Fragment() {

    /**
     * Called when fragment is attached to the context
     * During onAttach we won't have access to fragment layout
     * onAttach() will immediately call onCreate() of fragment, so
     * we really don't need to override this method
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    /**
     * Fragment is being created.
     * It is not recommended to go inflate the layout and initialise
     * the UI here.
     * * When the new instance of fragment is created we can retrieve
     * the saved data by receiving the Bundle..
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * It is usually recommended to inflate the layout in onCreateView() method
     * and return the root view
     * This method will create the View Hierarchy.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Here the View is Available, so we implement the logic that modifies
     * the returned view.
     * example: setting up UI, Restoring state from Bundle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * We can also save fragment state across config changes using
     * onSaveInstanceState()
     * Note that we can only store minimal data here. For larger data storage we
     * should use the database.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    /**
     * Fragment is started and visible
     */
    override fun onStart() {
        super.onStart()
    }

    /**
     * Fragment has input focus
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * Fragment has no input focus
     */
    override fun onPause() {
        super.onPause()
    }

    /**
     * Fragment is not visible
     */
    override fun onStop() {
        super.onStop()
    }

    /**
     * Here the ViewHierarchy created from onCreateView is being removed
     * from the fragment
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     * Here the fragment is no longer attached to host Activity.
     */
    override fun onDetach() {
        super.onDetach()
    }
}