package com.niranjan.androidtutorials.price

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.niranjan.androidtutorials.DrawerBaseActivity
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivityPriceLayoutBinding

class PriceCalculatorActivity : DrawerBaseActivity() {

    private lateinit var priceLayoutBinding: ActivityPriceLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        priceLayoutBinding = ActivityPriceLayoutBinding.inflate(layoutInflater)
        setContentView(priceLayoutBinding.root)
        allocateActivityTitle(getString(R.string.label_price_calculator))
        // Set up Key Listener on the EditText field : to listen to "Enter" button pressed
        with(priceLayoutBinding){
            // Listen to Keyboard "Enter" button pressed
            costOfProductEditText.setOnKeyListener { view, i, keyEvent ->
                handleKeyEvent(
                    view,
                    i
                )
            }
            // Calculate the discounted price
            calculateButton.setOnClickListener {
                calculateDiscountedPrice()
            }
        }
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean{
        if (keyCode == KeyEvent.KEYCODE_ENTER){
            // Hide the Keyboard
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                0
            )
            return true
        }
        return false
    }

    private fun calculateDiscountedPrice(){
        // Get value from the edittext field
        val valueEditTextField = priceLayoutBinding.costOfProductEditText.text.toString()
        val cost = valueEditTextField.toDoubleOrNull()

        // If the cost is null or 0, then display 0 and exit the function
        if (cost == null || cost == 0.0){
            displayPrice(0.0)
        } else {
            // Get the discount percentage based on which radio button is selected
            val discountPercentage = when (priceLayoutBinding.discountOptions.checkedRadioButtonId) {
                R.id.option_twenty_five_percent -> 0.25
                R.id.option_twenty_percent -> 0.20
                else -> 0.15
            }

            // Discount Value
            var discount = discountPercentage * cost

            // If the round up switch is check round the discount value otherwise not
            val roundUp = priceLayoutBinding.roundUpSwitch.isChecked
            if (roundUp){
                discount = kotlin.math.ceil(discount)
            }
            displayPrice(cost-discount)
        }
    }

    private fun displayPrice(price: Double){
        priceLayoutBinding.price = price
    }
}