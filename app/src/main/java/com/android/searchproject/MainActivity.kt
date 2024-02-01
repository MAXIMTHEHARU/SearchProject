package com.android.searchproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.searchproject.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import android.content.Context


import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private var items: ArrayList<Document> = arrayListOf()
    private var favoriteItems: ArrayList<Document> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewPager()


        val pref = getSharedPreferences("pref", 0)
        binding.etMainSearch.setText(pref.getString("searchWord", ""))

        binding.btnMainSearch.setOnClickListener {
            communicationNetwork(setUpSearchParameter(binding.etMainSearch.text.toString()))

            binding.etMainSearch.clearFocus()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        }

        binding.tapLayout[0].setOnClickListener {
            setFragment(SearchFragment.newInstance(items, favoriteItems))
        }

//        binding.tapLayout[1].setOnClickListener {
//            setFragment(StorageFragment.newInstance(favoriteItems))
//        }
    }

    private fun communicationNetwork(param: HashMap<String, String>) = lifecycleScope.launch {
        val responseData = NetworkClient.searchNetwork.searchImage(param)
        items.clear()
        responseData.documents?.forEach {
            items.add(it)
        }
        items.sortByDescending { it.dateTime }

        setFragment(SearchFragment.newInstance(items, favoriteItems))
    }

    private fun setUpSearchParameter(word: String): HashMap<String, String> {
        return hashMapOf(
            "query" to word,
            "sort" to "accuracy",
            "page" to "1",
            "size" to "80"
        )
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()


        transaction.replace(R.id.frameLayout, fragment)


        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (currentFragment != null) {
            transaction.remove(currentFragment)
        }
        if (!transaction.isAddToBackStackAllowed) {
            transaction.addToBackStack("uniqueTransactionName")
        }

        transaction.commit()
    }


    override fun onBackPressed() {
        if (supportFragmentManager.fragments[0] is SearchFragment) {
            val pref = getSharedPreferences("pref", MODE_PRIVATE)
            val edit = pref.edit()
            edit.putString("searchWord", binding.etMainSearch.text.toString())
            edit.apply()
            finish()
        }

        super.onBackPressed()
    }

    //
    private fun initViewPager() {
        var viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.addFragment(SearchFragment.newInstance(items, favoriteItems))
        viewPagerAdapter.addFragment(StorageFragment.newInstance(favoriteItems))

        binding.viewPager.apply {
            adapter = viewPagerAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }
        TabLayoutMediator(binding.tapLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "이미지 검색"
                }
                1 -> {
                    tab.text = "좋아요 보관함"
                }
            }
        }.attach()

    }
}