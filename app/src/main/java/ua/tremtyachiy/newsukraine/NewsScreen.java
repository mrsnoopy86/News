package ua.tremtyachiy.newsukraine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ua.tremtyachiy.newsukraine.downloadimage.ImageManager;


public class NewsScreen extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewText;
    private ImageView imageViewImage;
    private Button button;
    private Toolbar toolbar;
    private String urlNews = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsscreen);
        textViewTitle = (TextView) findViewById(R.id.tvNewsTitle);
        textViewText = (TextView) findViewById(R.id.tvNewsText);
        imageViewImage = (ImageView) findViewById(R.id.ivNewsImage);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlNews));
                startActivity(intent);
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_title));
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        generateNews();
    }

    /*Create a news from intent, when click on item of RecyclerView*/
    private void generateNews(){
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String text = intent.getStringExtra("text");
        String title = intent.getStringExtra("title");
        urlNews = intent.getStringExtra("urlNews");
        textViewTitle.setText(title);
        textViewText.setText(text);
        ImageManager.fetchImage(image, imageViewImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shared) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            if(!urlNews.isEmpty()) {
                sharingIntent.putExtra(Intent.EXTRA_TEXT, urlNews);
            }
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
