package com.niranjan.androidtutorials.quotes.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.niranjan.androidtutorials.R
import com.niranjan.androidtutorials.databinding.ActivityQuotesBinding
import com.niranjan.androidtutorials.drawer.DrawerBaseActivity
import com.niranjan.androidtutorials.quotes.db.QuotesDatabase
import com.niranjan.androidtutorials.quotes.db.getQuotesDatabase
import com.niranjan.androidtutorials.quotes.model.QuotesRepository
import com.niranjan.androidtutorials.quotes.network.getService
import com.niranjan.androidtutorials.quotes.viewmodel.QuotesViewModel

class QuotesActivity : DrawerBaseActivity(){

    lateinit var quotesBinding: ActivityQuotesBinding
    private lateinit var database : QuotesDatabase
    private lateinit var repository: QuotesRepository
    private lateinit var viewModel: QuotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quotesBinding = ActivityQuotesBinding.inflate(layoutInflater)
        setContentView(quotesBinding.root)
        allocateActivityTitle(getString(R.string.label_quotes_app))
        setView()
        setListeners()
        setObservers()
    }

    private fun setView(){
        database = getQuotesDatabase(this)
        repository = QuotesRepository(
            getService(),
            database.quotesDao
        )
        viewModel = ViewModelProvider(
            this,
            QuotesViewModel.FACTORY(repository)
        )[QuotesViewModel::class.java]

    }

    private fun setListeners(){
        quotesBinding.rootLayout.setOnClickListener {
            viewModel.onMainViewClicked()
        }
    }

    private fun setObservers(){
        viewModel.quote.observe(this){ quote ->
            quote?.let {
                quotesBinding.title.text = it
            }
        }

        viewModel.taps.observe(this){
            quotesBinding.taps.text = it
        }

        viewModel.spinner.observe(this){
            quotesBinding.spinner.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.snackbar.observe(this){
            it?.let {
                Snackbar.make(
                    quotesBinding.rootLayout,
                    it,
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.onSnackbarShown()
            }
        }
    }
}