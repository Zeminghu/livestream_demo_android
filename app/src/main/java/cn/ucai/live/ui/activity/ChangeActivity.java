package cn.ucai.live.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.live.I;
import cn.ucai.live.R;
import cn.ucai.live.data.NetDao;
import cn.ucai.live.data.model.Result;
import cn.ucai.live.data.model.Wallet;
import cn.ucai.live.utils.CommonUtils;
import cn.ucai.live.utils.MFGT;
import cn.ucai.live.utils.OnCompleteListener;
import cn.ucai.live.utils.PreferenceManager;
import cn.ucai.live.utils.ResultUtils;

/**
 * Created by clawpo on 2017/3/6.
 */
public class ChangeActivity extends BaseActivity {
    @BindView(R.id.tv_change_balance)
    TextView mTvChangeBalance;
    @BindView(R.id.target_layout)
    LinearLayout mTargetLayout;
    View loadingView;

    int change;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changes);
        ButterKnife.bind(this);
        loadingView = LayoutInflater.from(ChangeActivity.this)
                .inflate(R.layout.rp_loading, mTargetLayout, false);
        mTargetLayout.addView(loadingView);
        setChange();
        initData();
    }

    private void initData() {
        NetDao.loadChange(ChangeActivity.this, EMClient.getInstance().getCurrentUser(),
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        boolean success = false;
                        if (s != null) {
                            Result result = ResultUtils.getResultFromJson(s, Wallet.class);
                            if (result != null && result.isRetMsg()) {
                                success = true;
                                Wallet wallet = (Wallet) result.getRetData();
                                PreferenceManager.getInstance().setCurrentuserChange(wallet.getBalance());
                                change = wallet.getBalance();
                                setChange();
                            }
                        }
                        if (!success) {
                            PreferenceManager.getInstance().setCurrentuserChange(0);
                        }
                        loadingView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(String error) {
                        loadingView.setVisibility(View.GONE);
                        CommonUtils.showShortToast(error);
                    }
                });
    }

    private void setChange() {
        change = PreferenceManager.getInstance().getCurrentuserChange();
        mTvChangeBalance.setText("￥" + Float.valueOf(String.valueOf(change)));
    }

    @OnClick(R.id.img_back)
    public void imgBack() {
        MFGT.finish(ChangeActivity.this);
    }

    @OnClick({R.id.tv_my_giving_gift_list, R.id.tv_my_gift_records})
    public void myGiftList(View view) {
        int giftListType = I.GIFT_STATEMENT_TYPE_GIBVING;
        switch (view.getId()) {
            case R.id.tv_my_giving_gift_list:
                giftListType = I.GIFT_STATEMENT_TYPE_GIBVING;
                break;
            case R.id.tv_my_gift_records:
                giftListType = I.GIFT_STATEMENT_TYPE_RECEIVING;
                break;
        }
        MFGT.gotoGiftStatementsList(ChangeActivity.this,giftListType);
    }
}