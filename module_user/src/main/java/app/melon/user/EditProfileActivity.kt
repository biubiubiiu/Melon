package app.melon.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import app.melon.user.databinding.ActivityEditProfileBinding
import app.melon.permission.helper.EditHelper
import app.melon.user.ui.edit.EditProfileController
import app.melon.user.ui.edit.EditProfileViewModel
import app.melon.util.extensions.setTitleColor
import app.melon.util.extensions.showToast
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class EditProfileActivity : DaggerAppCompatActivity(), EditProfileController.Action {

    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding!!

    private val controller = EditProfileController(this)
    private val editHelper = EditHelper(this)

    @Inject internal lateinit var viewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        binding.recyclerView.setController(controller)

        viewModel.user.observe(this, Observer {
            controller.setData(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_save)?.setTitleColor(R.color.colorPrimary)
        menuInflater.inflate(R.menu.menu_edit_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_save -> {
                viewModel.save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }


    override fun afterHometownChanged(content: String) = viewModel.hometownChanged(content)
    override fun afterCollegeChanged(content: String) = viewModel.collegeChanged(content)
    override fun afterMajorChanged(content: String) = viewModel.majorChanged(content)
    override fun afterDegreeChanged(content: String) = viewModel.degreeChanged(content)
    override fun afterBioChanged(content: String) = viewModel.descriptionChanged(content)

    override fun onClickDeleteImage(url: String, position: Int) {
        showToast("Call delete image: $url; $position")
    }

    override fun onClickEditAvatar() = editHelper.showEditOptions()
    override fun onClickEditBackground() = editHelper.showEditOptions()
    override fun onClickUploadImage() = editHelper.showEditOptions()

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, EditProfileActivity::class.java))
        }
    }
}