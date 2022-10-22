package com.niranjan.androidtutorials.slot

import android.os.Bundle
import com.niranjan.androidtutorials.drawer.DrawerBaseActivity
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivitySlotBinding

class SlotActivity : DrawerBaseActivity() {

    lateinit var slotBinding: ActivitySlotBinding
    lateinit var mSlotData: SlotData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slotBinding = ActivitySlotBinding.inflate(layoutInflater)
        setContentView(slotBinding.root)
        allocateActivityTitle(getString(R.string.label_slot_machine))
        setDefaultNumbers()
        slotBinding.button.setOnClickListener {
            generateSlotNumbers()
        }
    }
    private fun setDefaultNumbers(){
        mSlotData =  SlotData(
            firstSlot = "1",
            secondSlot = "2",
            thirdSlot = "3"
        )
        with(slotBinding){
            slotData = mSlotData
        }
    }

    private fun generateSlotNumbers(){
        val nSlotData = SlotData(
            (1..100).random().toString(),
                   (200..500).random().toString(),
                    (500..1000).random().toString()
        )
        with(slotBinding){
            slotData = nSlotData
        }
    }
}