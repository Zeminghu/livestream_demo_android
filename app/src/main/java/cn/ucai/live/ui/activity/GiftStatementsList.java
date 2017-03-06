package cn.ucai.live.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.live.I;
import cn.ucai.live.R;
import cn.ucai.live.data.NetDao;
import cn.ucai.live.data.model.GiftStatements;
import cn.ucai.live.data.model.Result;
import cn.ucai.live.utils.OnCompleteListener;
import cn.ucai.live.utils.ResultUtils;

/**
 * Created by clawpo on 2017/3/6.
 */
public class GiftStatementsList extends BaseActivity {
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.recycleview)
    RecyclerView mRecycleview;
    @BindView(R.id.loading_bar)
    ProgressBar mLoadingBar;
    @BindView(R.id.loading_text)
    TextView mLoadingText;
    @BindView(R.id.loading_layout)
    LinearLayout mLoadingLayout;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    LinearLayoutManager gm;

    int giftStatementType;
    String username;
    int pageId=1;
    int pageSize = 10;
    GiftStatementAdapter adapter;
    List<GiftStatements> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_statements);
        ButterKnife.bind(this);
        username = EMClient.getInstance().getCurrentUser();
        giftStatementType = getIntent().getIntExtra(I.GIFT_STATEMENT_TYPE, I.GIFT_STATEMENT_TYPE_GIBVING);
        initView();
        initData();
    }

    private void initData() {
        if (giftStatementType == I.GIFT_STATEMENT_TYPE_GIBVING) {
            mTvTitle.setText(R.string.my_bank_card);
            NetDao.loadGiftStatementsGiving(GiftStatementsList.this, username, pageId, pageSize, listener);
        } else {
            mTvTitle.setText(R.string.my_money_records);
            NetDao.loadGiftStatementsReceiving(GiftStatementsList.this, username, pageId, pageSize, listener);
        }
    }

    OnCompleteListener<String> listener = new OnCompleteListener<String>() {
        @Override
        public void onSuccess(String s) {
            boolean success = false;
            if (s != null) {
                Result result = ResultUtils.getListResultFromJson(s, GiftStatements.class);
                if (result != null && result.isRetMsg()) {
                    List<GiftStatements> l = (List<GiftStatements>) result.getRetData();
                    if (l != null && l.size() > 0) {
                        list.addAll(l);
                        adapter.notifyDataSetChanged();
                        success = true;
                    }
                }
                if (!success){

                }
            }
        }

        @Override
        public void onError(String error) {

        }
    };

    private void setView(boolean isMore){
        if (isMore){

        }else{

        }
    }

    private void initView() {
        gm = new LinearLayoutManager(GiftStatementsList.this);
        gm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(gm);
        adapter = new GiftStatementAdapter(GiftStatementsList.this,list);
        mRecycleview.setAdapter(adapter);
    }

    class GiftStatementAdapter extends RecyclerView.Adapter<GiftStatementViewHolder> {
        Context mContext;
        List<GiftStatements> mGiftStatementsList;

        public GiftStatementAdapter(Context context, List<GiftStatements> giftStatementsList) {
            mContext = context;
            mGiftStatementsList = giftStatementsList;
        }

        @Override
        public GiftStatementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            GiftStatementViewHolder vh =
                    new GiftStatementViewHolder(LayoutInflater.from(mContext)
                            .inflate(R.layout.item_statement,parent,false));
            return vh;
        }

        @Override
        public void onBindViewHolder(GiftStatementViewHolder holder, int position) {
            GiftStatements statements = mGiftStatementsList.get(position);
            if (giftStatementType==I.GIFT_STATEMENT_TYPE_GIBVING){
                EaseUserUtils.setAppUserAvatar(mContext,statements.getAnchor(),holder.mIvItemAvatarIcon);
                holder.mTvMoneyToUser.setText(statements.getAnchor());
            } else {
                EaseUserUtils.setAppUserAvatar(mContext,statements.getUname(),holder.mIvItemAvatarIcon);
                holder.mTvMoneyToUser.setText(statements.getUname());
            }
            holder.mTvTime.setText(statements.getGdate());
//            holder.mTvItemMoneyAmount.setText("￥" + Float.valueOf(
//                    String.valueOf(LiveHelper.getInstance().getAppGiftList().get(statements.getId()))));
        }

        @Override
        public int getItemCount() {
            return mGiftStatementsList!=null?mGiftStatementsList.size():0;
        }
    }

    class GiftStatementViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_avatar_icon)
        ImageView mIvItemAvatarIcon;
        @BindView(R.id.tv_money_to_user)
        TextView mTvMoneyToUser;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_item_money_amount)
        TextView mTvItemMoneyAmount;
        @BindView(R.id.tv_best_icon)
        TextView mTvBestIcon;

        GiftStatementViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}