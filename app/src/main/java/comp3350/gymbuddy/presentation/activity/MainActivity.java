package comp3350.gymbuddy.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import comp3350.gymbuddy.R;
import comp3350.gymbuddy.persistence.interfaces.IWorkoutProfilePersistence;
import comp3350.gymbuddy.persistence.stubs.WorkoutProfileStub;
import comp3350.gymbuddy.objects.WorkoutProfile;
import comp3350.gymbuddy.presentation.adapters.WorkoutProfileAdapter;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView recyclerViewWorkouts;
    private WorkoutProfileAdapter workoutProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // get the id of the clicked item
            int id = item.getItemId();

            if (id == R.id.home) {
                // already on the home screen, do nothing
                return true;
            } else if (id == R.id.build_workouts) {
                // Nnvigate to WorkoutBuilderActivity
                Intent workoutIntent = new Intent(MainActivity.this, WorkoutBuilderActivity.class);
                startActivity(workoutIntent);
                return true;
            }

            return false;
        });
        // highlight the active menu item
        bottomNavigationView.setSelectedItemId(R.id.home);

        // initialize stub
        IWorkoutProfilePersistence workoutProfilePersistence = new WorkoutProfileStub();

        // initialize RecyclerView
        recyclerViewWorkouts = findViewById(R.id.recyclerViewWorkouts);
        recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(this));

        // fetch data from stub
        List<WorkoutProfile> workoutProfiles = workoutProfilePersistence.getAll();

        // set up adapter
        workoutProfileAdapter = new WorkoutProfileAdapter(workoutProfiles);
        recyclerViewWorkouts.setAdapter(workoutProfileAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
