package com.niranjan.androidtutorials

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.niranjan.androidtutorials.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    lateinit var mainBinding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        return mainBinding.root
    }

}