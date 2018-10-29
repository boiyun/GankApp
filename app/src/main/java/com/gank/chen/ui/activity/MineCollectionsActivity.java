package com.gank.chen.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gank.chen.R;
import com.gank.chen.adapter.MainArticleAdapter;
import com.gank.chen.adapter.MineCollectionsAdapter;
import com.gank.chen.base.BaseActivity;
import com.gank.chen.base.CreatePresenter;
import com.gank.chen.common.RouterUrlManager;
import com.gank.chen.mvp.model.ArticleListModel;
import com.gank.chen.mvp.model.ArticleModel;
import com.gank.chen.mvp.presenter.CarsListPresenter;
import com.gank.chen.mvp.view.PullDownLoadMoreViewImp;
import com.gank.chen.util.RouterUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import butterknife.BindView;

/**
 * @author chenbo
 */
@CreatePresenter(CarsListPresenter.class)
@Route(path = RouterUrlManager.MINE_COLLECTION)
public class MineCollectionsActivity extends BaseActivity<PullDownLoadMoreViewImp<ArticleModel>, CarsListPresenter> implements PullDownLoadMoreViewImp {

    @BindView(R.id.rv_carslist)
    RecyclerView rvCarslist;
    private MineCollectionsAdapter adapter;
    private List<ArticleListModel> modelDatas;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cars_list;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getPresenter().getDetailData(this, stateView, page);
    }

    @Override
    public void initView() {
        initToolBar("我的收藏");
        rvCarslist.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new MineCollectionsAdapter(modelDatas);
        rvCarslist.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("weburl", ((ArticleListModel) (adapter.getData().get(position))).getLink());
                bundle.putString("title", ((ArticleListModel) (adapter.getData().get(position))).getTitle());
                RouterUtil.goToActivity(RouterUrlManager.DETAIL, bundle);
            }
        });
    }


    @Override
    public void onLoadSucess(Object obj) {
        ArticleModel model = (ArticleModel) obj;
        super.onLoadSucess(model.getDatas());
        modelDatas = model.getDatas();
        adapter.setNewData(modelDatas);
    }

    @Override
    protected void onLoadMoreData(RefreshLayout refreshLayout) {
        super.onLoadMoreData(refreshLayout);
        getPresenter().getMoreData(this, page);
    }

    @Override
    public void onLoadMoreSuccess(Object obj) {
        ArticleModel model = (ArticleModel) obj;
        super.onLoadMoreSuccess(model.getDatas());
        adapter.addData(model.getDatas());
    }
}