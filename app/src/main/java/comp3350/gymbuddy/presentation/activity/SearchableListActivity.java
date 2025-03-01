package comp3350.gymbuddy.presentation.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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

        recyclerView = findViewById(R.id.listRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void setSearchView(int searchViewLayout){
        // used to inject searchView into activity
        LayoutInflater layoutInflater = getLayoutInflater();

        View sv = layoutInflater.inflate(searchViewLayout, null); // this is the container of the searchView

        ViewGroup insertPoint = findViewById(R.id.listContainer);
        insertPoint.addView(sv, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setConstraints(sv.getId());
    }

    private void setConstraints(int searchViewContainerId){
        ConstraintLayout layout = findViewById(R.id.listContainer);
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(recyclerView.getId(), ConstraintSet.TOP, searchViewContainerId, ConstraintSet.BOTTOM);

        set.applyTo(layout);
    }
}
