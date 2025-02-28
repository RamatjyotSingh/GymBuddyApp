package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import comp3350.gymbuddy.R;

public abstract class SearchableListActivity extends AppCompatActivity {
    protected SearchView searchView;
    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedBundleInstance){
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_searchable_list);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerViewList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
