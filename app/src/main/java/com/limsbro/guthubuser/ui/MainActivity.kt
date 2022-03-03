package com.limsbro.guthubuser.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.limsbro.guthubuser.R
import com.limsbro.guthubuser.databinding.ActivityMainBinding
import com.limsbro.guthubuser.helper.PrefsHelper
import com.limsbro.guthubuser.helper.UserItemClickListener
import com.limsbro.guthubuser.model.UserModel
import com.limsbro.guthubuser.ui.adapter.UserAdapter
import com.limsbro.guthubuser.ui.viewmodel.MainViewModel
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.snackbars.fire.NoInternetSnackbarFire

class MainActivity : AppCompatActivity(), UserItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var userAdapter: UserAdapter
    var searchMenu: MenuItem? = null

    //Variable for checking progressbar loading or not
    private var isLoading: Boolean = false
    private var isSearch: Boolean = false
    private var isOnline: Boolean = false

    // No Internet Snackbar: Fire
    private var noInternetSnackbarFire: NoInternetSnackbarFire? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
        loadData()
    }

    private fun initView() {

        noInternetSnackbarFire = NoInternetSnackbarFire.Builder(
            binding.mainLayout,
            lifecycle
        ).apply {
            snackbarProperties.apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {
                        isOnline = hasActiveConnection
                    }
                }
                duration = Snackbar.LENGTH_INDEFINITE // Optional
                noInternetConnectionMessage = "No active Internet connection!" // Optional
                onAirplaneModeMessage = "You have turned on the airplane mode!" // Optional
                showActionToDismiss = true // Optional
                snackbarDismissActionText = "OK" // Optional
            }
        }.build()

        try {
            val txtSearch: EditText =
                binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text)
            txtSearch.setTextColor(Color.WHITE)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        userAdapter = UserAdapter(this)
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mainRecycleView.layoutManager = layoutManager
        binding.mainRecycleView.adapter = userAdapter

        addScrollerListener(layoutManager)
    }

    private fun addScrollerListener(layoutManager: LinearLayoutManager) {

        binding.mainRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !isSearch) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == userAdapter.itemCount - 1) {
                        binding.mainProgressBar.visibility = View.VISIBLE
                        getDataFromAPI(userAdapter.getLastItemId().toString())
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadData() {

        viewModel.allSavedUserData.observe(this, { userData: List<UserModel> ->
            if (userData.isEmpty() && !isSearch) {
                getDataFromAPI("0")
            }
            userAdapter.setUserList(userData)
        })

        viewModel.loading.observe(this, {
            if (it) {
                binding.mainProgressBar.visibility = View.VISIBLE
            } else {
                isLoading = false
                binding.mainProgressBar.visibility = View.GONE
            }
        })
        viewModel.errorMessage.observe(this, {
            Snackbar.make(binding.mainLayout, it, Snackbar.LENGTH_SHORT)
                .show()
        })
        searchViewHandle()
        viewModel.setSearchFilterData("")
    }

    // here call api to load github user list, pass last loaded userID pass 0 first time load
    private fun getDataFromAPI(lastID: String) {

        if (isOnline)
            viewModel.getUserDataFromAPI(lastID)
    }

    //handle search view command here
    private fun searchViewHandle() {

        binding.searchView.setOnCloseListener {
            searchMenu?.isVisible = true
            isSearch = false
            binding.searchView.visibility = View.GONE
            false
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // search while more then two char,
//                if (newText.length > 2)
//                    viewModel.setSearchFilterData("")
//                else
                isSearch = true
                viewModel.setSearchFilterData(newText)
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        searchMenu = menu.findItem(R.id.action_search)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_search -> {
                binding.searchView.visibility = View.VISIBLE
                binding.searchView.setIconifiedByDefault(true)
                binding.searchView.isFocusable = true
                binding.searchView.isIconified = false
                item.isVisible = false
                isSearch = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClickListener(userModel: UserModel) {
        PrefsHelper(this).saveUsername(userModel.login)
        PrefsHelper(this).saveUserId(userModel.id)
        startActivity(Intent(this@MainActivity, UserProfileActivity::class.java))
    }

}