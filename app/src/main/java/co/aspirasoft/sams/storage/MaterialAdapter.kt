package co.aspirasoft.sams.storage

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import co.aspirasoft.adapter.ModelViewAdapter
import co.aspirasoft.sams.model.CourseFile
import co.aspirasoft.sams.storage.FileUtils.openInExternalApp
import co.aspirasoft.sams.view.CourseFileView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class MaterialAdapter(val context: Activity, val material: ArrayList<CourseFile>, val fileManager: FileManager)
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
                            Snackbar.make(v, ex.message ?: "Could not open file.", Snackbar.LENGTH_SHORT).show()
                        }
                    },
                    OnFailureListener {
                        v.setStatus(CourseFileView.FileStatus.Cloud)
                        Snackbar.make(v, it.message ?: "Could not download file.", Snackbar.LENGTH_SHORT).show()
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