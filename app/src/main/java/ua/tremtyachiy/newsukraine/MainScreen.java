package ua.tremtyachiy.newsukraine;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import ua.tremtyachiy.newsukraine.adapter.NewsAdapter;
import ua.tremtyachiy.newsukraine.service.ServiceLoad;
import ua.tremtyachiy.newsukraine.utils.AddDataToList;
import ua.tremtyachiy.newsukraine.utils.InternetConnection;


public class MainScreen extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipe;
    private NewsAdapter adapter;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        if(InternetConnection.hasConnection(this)) {
            initItems();
            startServiceLoad();
        } else {
            Toast.makeText(MainScreen.this, R.string.not_connection, Toast.LENGTH_LONG).show();
            initItems();
            loadList();
        }
    }

    /*Start service for load dara from JSON and input in SQL database*/
    private void startServiceLoad(){
        PendingIntent pendingIntent;
        pendingIntent = createPendingResult(1, new Intent(), 0);
        startService(new Intent(this, ServiceLoad.class).putExtra("finish", pendingIntent));
    }

    /*Init View Elements*/
    private void initItems() {
        /*Toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_title));
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        /*RecyclerView and Adapter*/
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new NewsAdapter(this);
        mRecyclerView.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /*Swipe for refresh data*/
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipe.setColorScheme(R.color.blue, R.color.green, R.color.yellow, R.color.red);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(InternetConnection.hasConnection(MainScreen.this)) {
                    startServiceLoad();
                } else {
                    swipe.setRefreshing(false);
                    Toast.makeText(MainScreen.this, R.string.not_connection, Toast.LENGTH_LONG).show();
                }
            }
        });
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, NewsScreen.class));
            }
        });
        /*Pagination*/
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    /*something wrong :( */
                    loading = true;
                }
            }
        });
    }

    /*Refresh data in adapter and adapter when service of load data from JSON is end*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadList();
            swipe.setRefreshing(false);
        }
    }

    private void loadList(){
        NewsAdapter.setList(AddDataToList.getListOfNews(this));
        adapter.notifyDataSetChanged();
    }
}


