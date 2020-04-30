package co.aspirasoft.sams

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import co.aspirasoft.adapter.ModelViewAdapter
import co.aspirasoft.sams.core.DashboardChildActivity
import co.aspirasoft.sams.dao.NoticeBoardDao
import co.aspirasoft.sams.model.NoticeBoardPost
import co.aspirasoft.sams.model.Subject
import co.aspirasoft.sams.model.Teacher
import co.aspirasoft.sams.model.User
import co.aspirasoft.sams.view.MessageInputDialog
import co.aspirasoft.sams.view.NoticeBoardPostView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_list.*

class NoticeActivity : DashboardChildActivity() {

    private lateinit var posts: ArrayList<NoticeBoardPost>
    private lateinit var adapter: PostAdapter

    private var subject: Subject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val subject = intent.getSerializableExtra(MyApplication.EXTRA_SCHOOL_SUBJECT) as Subject?
        val posts = intent.getParcelableArrayListExtra<NoticeBoardPost>(MyApplication.EXTRA_NOTICE_POSTS)
        if (subject == null && posts == null) {
            finish()
            return
        }

        this.posts = posts ?: ArrayList()
        this.adapter = PostAdapter(this, this.posts)

        if (subject != null) {
            this.posts.clear()
            this.subject = subject
            NoticeBoardDao.getPostsBySubject(schoolId, subject, OnSuccessListener {
                this.posts.addAll(it)
                this.adapter.notifyDataSetChanged()
            })
        }

        addButton.visibility = when (currentUser) {
            is Teacher -> {
                addButton.setOnClickListener { onAddNoticeClicked() }
                View.VISIBLE
            }
            else -> View.GONE
        }
    }

    override fun updateUI(currentUser: User) {
        contentList.adapter = this.adapter
    }

    private fun onAddNoticeClicked() {
        MessageInputDialog(this)
                .setOnMessageReceivedListener { message ->
                    try {
                        val status = Snackbar.make(contentList, getString(R.string.status_posting), Snackbar.LENGTH_INDEFINITE)
                        status.show()

                        val post = NoticeBoardPost(postContent = message, postAuthor = currentUser.name)
                        val onCompleteListener = OnCompleteListener<Void?> {
                            if (it.isSuccessful) {
                                status.setText(getString(R.string.status_posted))
                                posts.add(post)
                                adapter.notifyDataSetChanged()
                            } else {
                                status.setText(it.exception?.message ?: getString(R.string.status_post_failed))
                            }

                            Handler().postDelayed({
                                status.dismiss()
                            }, 1500L)
                        }

                        if (subject != null) {
                            NoticeBoardDao.add(schoolId, subject!!, post, onCompleteListener)
                        } else {
                            NoticeBoardDao.add(schoolId, (currentUser as Teacher).classId!!, post, onCompleteListener)
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
                .show()
    }

    private inner class PostAdapter(context: Context, val posts: ArrayList<NoticeBoardPost>)
        : ModelViewAdapter<NoticeBoardPost>(context, posts, NoticeBoardPostView::class) {

        override fun notifyDataSetChanged() {
            posts.sortByDescending { it.postDate }
            super.notifyDataSetChanged()
        }

    }

}