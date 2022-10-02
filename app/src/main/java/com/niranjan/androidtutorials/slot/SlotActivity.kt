package com.niranjan.androidtutorials.slot

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.niranjan.androidtutorials.databinding.ActivitySlotBinding

class SlotActivity : AppCompatActivity() {

    lateinit var slotBinding: ActivitySlotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slotBinding = ActivitySlotBinding.inflate(LayoutInflater.from(this))
        setContentView(slotBinding.root)
        slotBinding.button.setOnClickListener {
            generateSlotNumbers()
        }
    }

    private fun generateSlotNumbers(){
        with(slotBinding){
            slot1Tv.text = (1..100).random().toString()
            slot2Tv.text= (200..500).random().toString()
            slot3Tv.text = (500..1000).random().toString()
        }
    }
}