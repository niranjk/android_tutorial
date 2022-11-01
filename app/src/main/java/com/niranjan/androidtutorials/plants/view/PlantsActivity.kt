package com.niranjan.androidtutorials.plants.view

import android.os.Bundle
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivityPlantsBinding
import com.niranjan.androidtutorials.drawer.DrawerBaseActivity

class PlantsActivity : DrawerBaseActivity(){
    lateinit var binding: ActivityPlantsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        allocateActivityTitle(getString(R.string.label_plants_app))
    }
}