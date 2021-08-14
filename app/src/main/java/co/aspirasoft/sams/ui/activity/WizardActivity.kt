package co.aspirasoft.sams.ui.activity

import android.os.Bundle
import android.util.SparseArray
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import co.aspirasoft.sams.BuildConfig
import co.aspirasoft.sams.R
import co.aspirasoft.sams.ui.fragment.StepFragment
import com.google.android.material.button.MaterialButton
import params.com.stepview.StatusViewScroller

/**
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 05/04/2019 5:44 PM
 */
abstract class WizardActivity protected constructor(
    private val mStepFragments: Array<StepFragment>,
    private val mStepLabels: Array<String>
) : AppCompatActivity() {

    private lateinit var mTitleView: TextView
    private lateinit var mContentView: ViewPager
    private lateinit var mStepsView: StatusViewScroller
    private lateinit var mNextButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wizard)

        mTitleView = findViewById(R.id.title_view)
        mStepsView = findViewById(R.id.stepsView)
        mStepsView.statusView.stepCount = mStepLabels.size

        val adapter = WizardStepAdapter(supportFragmentManager, mStepLabels, mStepFragments)
        mContentView = findViewById(R.id.contentView)
        mContentView.adapter = adapter
        mContentView.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(newPosition: Int) {
                val currentPosition = mStepsView.statusView.currentCount - 1
                if (newPosition > currentPosition) {
                    if (isCurrentInputValid) {
                        mStepsView.statusView.currentCount = newPosition + 1
                        mStepsView.scrollToStep(newPosition + 1)
                        mTitleView.text = mStepLabels[newPosition]
                    } else {
                        mContentView.currentItem = currentPosition
                    }
                } else {
                    mStepsView.statusView.currentCount = newPosition + 1
                    mStepsView.scrollToStep(newPosition + 1)
                    mTitleView.text = mStepLabels[newPosition]
                }
                if (isLastStep) {
                    mNextButton.text = "Submit"
                } else {
                    mNextButton.text = "Next"
                }
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
        mNextButton = findViewById(R.id.button_next)
        mNextButton.setOnClickListener {
            if (isCurrentInputValid) {
                nextPage()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mContentView.currentItem = 0
        mTitleView.text = mStepLabels[0]
    }

    private val isCurrentInputValid: Boolean
        get() = mStepFragments.get(mStepsView.statusView.currentCount - 1).isDataValid

    private val isFirstStep: Boolean
        get() = mStepsView.statusView.currentCount == 1

    private val isLastStep: Boolean
        get() = mStepsView.statusView.currentCount == mStepLabels.size

    private fun nextPage() {
        if (isLastStep) {
            val wizardData = SparseArray<String?>()
            for (fragment in mStepFragments) {
                for (i in 0 until fragment.data.size()) {
                    val key = fragment.data.keyAt(i)
                    val value = fragment.data.valueAt(i)
                    wizardData.put(key, value)
                }
            }
            submitForm(wizardData)
        } else {
            mContentView.currentItem = mContentView.currentItem + 1
        }
    }

    override fun onBackPressed() {
        if (isFirstStep) {
            super.onBackPressed()
        } else {
            mContentView.currentItem = mContentView.currentItem - 1
        }
    }

    protected abstract fun submitForm(wizardData: SparseArray<String?>)
    class WizardStepAdapter(
        fragmentManager: FragmentManager?,
        private val mStepLabels: Array<String>,
        private val mStepFragments: Array<StepFragment>
    ) : FragmentPagerAdapter(
        fragmentManager!!
    ) {
        override fun getCount(): Int {
            return mStepLabels.size
        }

        override fun getItem(position: Int): StepFragment {
            return if (position < count) mStepFragments[position] else mStepFragments[0]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mStepLabels[position]
        }
    }

    init {
        if (BuildConfig.DEBUG && mStepLabels.size != mStepFragments.size) {
            throw AssertionError()
        }
    }
}