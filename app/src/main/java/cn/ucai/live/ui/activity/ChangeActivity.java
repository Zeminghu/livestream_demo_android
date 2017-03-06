package cn.ucai.live.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.live.R;

/**
 * Created by clawpo on 2017/3/6.
 */
public class ChangeActivity extends BaseActivity {
    @BindView(R.id.tv_change_balance)
    TextView mTvChangeBalance;
    @BindView(R.id.target_layout)
    LinearLayout mTargetLayout;
    View loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);
        ButterKnife.bind(this);
        loadingView = LayoutInflater.from(ChangeActivity.this)
                .inflate(R.layout.rp_loading, mTargetLayout,false);
        mTargetLayout.addView(loadingView);
    }
        }