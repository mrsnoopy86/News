package ua.tremtyachiy.newsukraine.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ua.tremtyachiy.newsukraine.NewsScreen;
import ua.tremtyachiy.newsukraine.R;
import ua.tremtyachiy.newsukraine.downloadimage.ImageManager;
import ua.tremtyachiy.newsukraine.utils.Item;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private static List<Item> mList = Collections.emptyList();
    Context context;

    public NewsAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = mList.get(position);
        holder.textViewCompany.setText(item.company);
        holder.textViewText.setText(item.text);
        holder.textViewTimeAgo.setText(item.timeAgo);
        holder.textViewTitle.setText(item.title);
        ImageManager.fetchImage(item.imageUrl, holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView textViewTitle;
        private TextView textViewText;
        private TextView textViewCompany;
        private TextView textViewTimeAgo;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.ivImageNews);
            textViewTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            textViewText = (TextView) itemView.findViewById(R.id.tvText);
            textViewCompany = (TextView) itemView.findViewById(R.id.tvCompany);
            textViewTimeAgo = (TextView) itemView.findViewById(R.id.tvTimeAgo);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, NewsScreen.class);
            intent.putExtra("image", mList.get(getPosition()).imageUrlFull);
            intent.putExtra("title", mList.get(getPosition()).title);
            intent.putExtra("text", mList.get(getPosition()).text);
            intent.putExtra("urlNews", mList.get(getPosition()).urlNews);
            context.startActivity(intent);
        }
    }

    /*Set list for adapter*/
    public static void setList(List<Item> list){
        mList = list;
    }

}
