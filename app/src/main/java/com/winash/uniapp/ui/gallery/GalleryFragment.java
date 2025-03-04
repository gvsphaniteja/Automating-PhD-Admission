package com.winash.uniapp.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.winash.uniapp.R;
import com.winash.uniapp.databinding.FragmentHomeBinding;
import com.winash.uniapp.ui.AddCourse.Course;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private RecyclerView recycleview;
    private SearchView search;
    private ArrayList<Course> list;
    private DatabaseReference ref;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view=null;
        view = inflater.inflate(R.layout.fragment_gallery,container, false);
        View xyz=view;
        recycleview=(RecyclerView) view.findViewById(R.id.Search_Recycler_View_1);
        search=(SearchView) view.findViewById(R.id.Search_course_field_1);
        list=new ArrayList<>();
        ref= FirebaseDatabase.getInstance().getReference();
        UserSearchCourseAdapter adapter= new UserSearchCourseAdapter(list,view.getContext());
        recycleview.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycleview.setAdapter(adapter);
        recycleview.setHasFixedSize(true);
        ref.child("Course").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    for(DataSnapshot db:snapshot.getChildren()){
                        Course c=db.getValue(Course.class);
                        list.add(c);
                    }
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e){
                    Toast.makeText(xyz.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //adapter.getFilter().filter(null);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Course> filteredlist = new ArrayList<>();
                for(Course item: list){
                    if(item.getCoursename().toLowerCase().contains(s.toLowerCase()))
                        filteredlist.add(item);
                }
                adapter.filterList(filteredlist);
                return false;
            }
        });
        return view;
    }
}