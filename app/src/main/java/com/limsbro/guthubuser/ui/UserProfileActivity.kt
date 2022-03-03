package com.limsbro.guthubuser.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.limsbro.guthubuser.R
import com.limsbro.guthubuser.databinding.ActivityUserProfileBinding
import com.limsbro.guthubuser.helper.PrefsHelper
import com.limsbro.guthubuser.model.UserProfileModel
import com.limsbro.guthubuser.ui.viewmodel.ProfileViewModel
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.snackbars.fire.NoInternetSnackbarFire

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private var noInternetSnackbarFire: NoInternetSnackbarFire? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initViews()
        loadData()
    }

    private fun initViews() {

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noInternetSnackbarFire = NoInternetSnackbarFire.Builder(
            binding.mainLayout,
            lifecycle
        ).apply {
            snackbarProperties.apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {
                        Log.e("tags", hasActiveConnection.toString())
                        if (hasActiveConnection) {
                            viewModel.getUserProfileFromAPI(PrefsHelper(this@UserProfileActivity).getUserName())
                        }
                    }
                }
                duration = Snackbar.LENGTH_INDEFINITE // Optional
                noInternetConnectionMessage = "No active Internet connection!" // Optional
                onAirplaneModeMessage = "You have turned on the airplane mode!" // Optional
                showActionToDismiss = true // Optional
                snackbarDismissActionText = "OK" // Optional
            }
        }.build()

        binding.userProBtnSave.setOnClickListener {
            val notes: String = binding.userProTextILNotes.editText?.text.toString().trim()
            if (notes.isEmpty()) {
                showAlertMessage("Please enter note")
                return@setOnClickListener
            }
            viewModel.saveNote(PrefsHelper(this).getUserId(), notes)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else
            super.onOptionsItemSelected(item)
    }

    private fun showAlertMessage(msg: String) {

        MaterialAlertDialogBuilder(this)
            .setTitle("Alert")
            .setMessage(msg)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun loadData() {

        viewModel.userProfileModel.observe(this, { userData: UserProfileModel ->
            setUserProfileData(userData)
        })

        viewModel.loading.observe(this, {
            if (it) {
                binding.mainLayout.visibility = View.GONE
                binding.userProShimmer.visibility = View.VISIBLE
            } else {
                binding.mainLayout.visibility = View.VISIBLE
                binding.userProShimmer.visibility = View.GONE
            }
        })

        viewModel.errorMessage.observe(this, {
            if (it == "success")
                showAlertMessage("Note Saved")
            else
                showAlertMessage(it)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setUserProfileData(userData: UserProfileModel) {

        binding.userProTxtName.text = "Name :" + userData.name
        binding.userProTxtCompany.text =
            "Company :" + if (!userData.company.isNullOrBlank()) userData.company else binding.userProTxtCompany.visibility =
                View.GONE
        binding.userProTxtEmail.text =
            "Email :" + if (!userData.email.isNullOrBlank()) userData.email else binding.userProTxtEmail.visibility =
                View.GONE
        binding.userProTxtBlog.text =
            "Blog :" + if (!userData.blog.isNullOrBlank()) userData.blog else binding.userProTxtBlog.visibility =
                View.GONE

        binding.userProTxtFollowers.text = "Followers : ${userData.followers}"
        binding.userProTxtFollowing.text = "Following : ${userData.following}"

        Glide
            .with(this@UserProfileActivity)
            .load(userData.avatar_url)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.userProImgAvatar)

        viewModel.getNotes(PrefsHelper(this).getUserId()).observe(this, {
            binding.userProTextILNotes.editText?.setText(it.notes)
        })

        supportActionBar?.title = userData.name
    }
}