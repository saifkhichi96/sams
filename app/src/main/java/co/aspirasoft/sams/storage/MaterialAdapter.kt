package co.aspirasoft.sams.storage

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import co.aspirasoft.adapter.ModelViewAdapter
import co.aspirasoft.sams.R
import co.aspirasoft.sams.model.CourseFile
import co.aspirasoft.sams.storage.FileUtils.openInExternalApp
import co.aspirasoft.sams.view.CourseFileView
import co.aspirasoft.util.ViewUtils.showError
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.io.IOException

class MaterialAdapter(val context: Activity, val material: ArrayList<CourseFile>, private val fileManager: FileManager)
    : ModelViewAdapter<CourseFile>(context, material, CourseFileView::class) {

    override fun notifyDataSetChanged() {
        material.sortBy { it.metadata.creationTimeMillis }
        super.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = super.getView(position, convertView, parent) as CourseFileView
        val item = material[position]
        if (fileManager.hasInCache(item.name)) {
            v.setStatus(CourseFileView.FileStatus.Local)
        } else {
            v.setStatus(CourseFileView.FileStatus.Cloud)
        }

        v.setOnClickListener {
            v.setStatus(CourseFileView.FileStatus.Downloading)
            fileManager.download(
                    item.name,
                    OnSuccessListener { file ->
                        v.setStatus(CourseFileView.FileStatus.Local)
                        try {
                            file.openInExternalApp(context)
                        } catch (ex: IOException) {
                            showError(v, ex.message ?: context.getString(R.string.error_open))
                        }
                    },
                    OnFailureListener {
                        v.setStatus(CourseFileView.FileStatus.Cloud)
                        showError(v, it.message ?: context.getString(R.string.error_download))
                    }
            )
        }

        v.setOnLongClickListener {
            // TODO: Delete file in editable mode
            false
        }
        return v
    }

}