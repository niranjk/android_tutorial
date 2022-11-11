package com.niranjan.androidtutorials

import com.niranjan.androidtutorials.uimodel.MainItemUiModel
import com.niranjan.androidtutorials.uimodel.TopicItemUiModel

object DummyData {

    fun dummyList() = listOf(
        MainItemUiModel(
            title = "Slot Machine App"
        ),
        TopicItemUiModel(
            topics = "- RecyclerView \n" +
                    "- RecyclerView.Adapter\n" +
                    "- ListAdapter\n" +
                    "- RecyclerView.ViewHolder \n" +
                    "- ConstraintLayout \n" +
                    "- RandomNumber Generation \n" +
                    "- DataBinding \n" +
                    "- Custom Adapter using @BindingAdapter \n" +
                    "- DiffUtil.ItemCallback"
        ),
        MainItemUiModel(
            title = "Price Calculator App"
        ),
        TopicItemUiModel(
            "- Handling Keyboard Input KeyEvent \n" +
                    "- Inputs From EditText Field"
        ),
        MainItemUiModel(
            title = "Quiz App"
        ),
        TopicItemUiModel("- Multiscreen Activity \n" +
                "- Intent : Explicit and Implicit \n" +
                "- NavigationUI"),
        TopicItemUiModel("- Toolbar\n" +
                "- CollapsingToolbarLayout\n" +
                "- ActionBar \n" +
                "- AppBarConfiguration"),
        TopicItemUiModel("- DrawerLayout\n" +
                "- FragmentContainerView\n" +
                "- NavigationView "),
        TopicItemUiModel("BottomNavigationView"),
        MainItemUiModel(
            title = "Runner App"
        ),
        TopicItemUiModel("- Activity Life Cycle \n" +
                "- Save Activity State "),
        TopicItemUiModel("- Fragment Life Cycle \n" +
                "- Saving Fragment State "),
        TopicItemUiModel("- androidx.lifecycle Library \n" +
                "- Lifecycle class \n" +
                "- LifecycleOwner class \n" +
                "- LifecycleEventObserver class \n" +
                "- App Permission Handling \n" +
                "- LocationListener"),
        MainItemUiModel(
            title = "Notes App"
        ),
        TopicItemUiModel("- Room DB \n" +
                "- Coroutines & Flow \n" +
                "- ViewModel & ViewModelFactory\n" +
                "- LiveData\n" +
                "- Repository\n" +
                "- StartActivityForResult"),
        MainItemUiModel(
            title = "Quotes App"
        ),
        TopicItemUiModel("- Callbacks \n" +
                "- Coroutines \n" +
                "- Retrofit \n" +
                "- Gson Converter\n" +
                "- Gson Serialization \n" +
                "- WorkManager"),
        MainItemUiModel(
            title = "Plants App"
        ),
        TopicItemUiModel("- Advanced LiveData\n" +
                "- MutableLiveData\n" +
                "- Transformations with map and switchMap\n" +
                "- set MutableLiveData and observe LiveData \n" +
                "- Declarative API of Flow  \n" +
                "- Declarative Operators: combine, flowOn, conflate \n" +
                "- MenuProvider \n" +
                "- Switching between two flows using flatMapLatest")
    )
}