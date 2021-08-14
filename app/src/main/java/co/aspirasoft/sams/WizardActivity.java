package co.aspirasoft.sams;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.button.MaterialButton;
import params.com.stepview.StatusViewScroller;

/**
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 05/04/2019 5:44 PM
 */
public abstract class WizardActivity extends AppCompatActivity {

    private final StepFragment[] mStepFragments;
    private final String[] mStepLabels;

    private TextView mTitleView;
    private ViewPager mContentView;
    private StatusViewScroller mStepsView;
    private MaterialButton mNextButton;

    protected WizardActivity(StepFragment[] stepFragments, String[] stepLabels) {
        this.mStepFragments = stepFragments;
        this.mStepLabels = stepLabels;

        if (BuildConfig.DEBUG && stepLabels.length != stepFragments.length) {
            throw new AssertionError();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);

        mTitleView = findViewById(R.id.title_view);

        mStepsView = findViewById(R.id.stepsView);
        mStepsView.getStatusView().setStepCount(mStepLabels.length);

        WizardStepAdapter adapter = new WizardStepAdapter(getSupportFragmentManager(), mStepLabels, mStepFragments);
        mContentView = findViewById(R.id.contentView);
        mContentView.setAdapter(adapter);
        mContentView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int newPosition) {
                int currentPosition = mStepsView.getStatusView().getCurrentCount() - 1;
                if (newPosition > currentPosition) {
                    if (isCurrentInputValid()) {
                        mStepsView.getStatusView().setCurrentCount(newPosition + 1);
                        mStepsView.scrollToStep(newPosition + 1);

                        mTitleView.setText(mStepLabels[newPosition]);
                    } else {
                        mContentView.setCurrentItem(currentPosition);
                    }
                } else {
                    mStepsView.getStatusView().setCurrentCount(newPosition + 1);
                    mStepsView.scrollToStep(newPosition + 1);

                    mTitleView.setText(mStepLabels[newPosition]);
                }

                if (isLastStep()) {
                    mNextButton.setText("Submit");
                } else {
                    mNextButton.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mNextButton = findViewById(R.id.button_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCurrentInputValid()) {
                    nextPage();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mContentView.setCurrentItem(0);
        mTitleView.setText(mStepLabels[0]);
    }

    private boolean isCurrentInputValid() {
        return mStepFragments[mStepsView.getStatusView().getCurrentCount() - 1].isDataValid();
    }

    private boolean isFirstStep() {
        return mStepsView.getStatusView().getCurrentCount() == 1;
    }

    private boolean isLastStep() {
        return mStepsView.getStatusView().getCurrentCount() == mStepLabels.length;
    }

    private void nextPage() {
        if (isLastStep()) {
            SparseArray<String> wizardData = new SparseArray<>();
            for (StepFragment fragment : mStepFragments) {
                for (int i = 0; i < fragment.getData().size(); i++) {
                    int key = fragment.getData().keyAt(i);
                    String value = fragment.getData().valueAt(i);

                    wizardData.put(key, value);
                }
            }

            submitForm(wizardData);
        } else {
            mContentView.setCurrentItem(mContentView.getCurrentItem() + 1);
        }
    }

    @Override
    public void onBackPressed() {
        if (isFirstStep()) {
            super.onBackPressed();
        } else {
            mContentView.setCurrentItem(mContentView.getCurrentItem() - 1);
        }
    }

    protected abstract void submitForm(SparseArray<String> wizardData);

    public static class WizardStepAdapter extends FragmentPagerAdapter {

        private final StepFragment[] mStepFragments;
        private final String[] mStepLabels;

        public WizardStepAdapter(FragmentManager fragmentManager, String[] stepLabels, StepFragment[] stepFragments) {
            super(fragmentManager);
            this.mStepLabels = stepLabels;
            this.mStepFragments = stepFragments;
        }

        @Override
        public int getCount() {
            return mStepLabels.length;
        }

        @Override
        public StepFragment getItem(int position) {
            return position < getCount() ? mStepFragments[position] : null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStepLabels[position];
        }
    }

}