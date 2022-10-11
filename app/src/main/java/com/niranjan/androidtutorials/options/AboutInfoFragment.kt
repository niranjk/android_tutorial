package com.niranjan.androidtutorials.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.FragmentAboutinfoBinding

class AboutInfoFragment : Fragment(){

    lateinit var fragmentAboutinfoBinding: FragmentAboutinfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentAboutinfoBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_aboutinfo,
            container,
            false
        )
        return fragmentAboutinfoBinding.root
    }
}