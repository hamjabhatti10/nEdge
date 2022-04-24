package com.app.nEdge.ui.dialogFragments.chooserDialogFragment

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import com.app.nEdge.BuildConfig
import com.app.nEdge.application.nEdgeApplication
import com.app.nEdge.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File

class ChooserDialogFragmentViewModel : BaseViewModel() {
    lateinit var getContentGallery: ActivityResultLauncher<String>
    lateinit var getContentCameraVideo: ActivityResultLauncher<Intent>
    lateinit var getContentCameraImage: ActivityResultLauncher<Uri>
    var imageLatestTmpUri: Uri? = null
    var mImageUri: MutableList<Uri> = ArrayList()
    var mRequestCode: Int = -1
    var viewType = 0

    fun openGalleryForVideo() {
        getContentGallery.launch("video/*")
    }

    fun openCameraForVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        getContentCameraVideo.launch(intent)
    }

    fun openGalleryIntent() {
        getContentGallery.launch("image/*")
    }

    fun openCameraIntent() {
        viewModelScope.launch {
            getTmpFileUri().let { uri ->
                imageLatestTmpUri = uri
                getContentCameraImage.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            nEdgeApplication.getContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }
}