package co.aspirasoft.sams.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.aspirasoft.sams.model.Lecture
import co.aspirasoft.sams.model.Subject
import co.aspirasoft.view.NestedListView

class TimetableFragment(val data: List<Pair<Subject, Lecture>>?, private val showClass: Boolean) : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = NestedListView(inflater.context).apply {
        this.isHorizontalScrollBarEnabled = false
        this.isVerticalScrollBarEnabled = false
        this.adapter = data?.let {
            LectureAdapter(inflater.context, it, showClass).apply {
                sort { o1, o2 ->
                    o1.second.startTime.compareTo(o2.second.startTime)
                }
            }
        }
    }
}
