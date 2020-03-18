package com.store.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.store.R;
import com.store.controller.Category;
import com.store.controller.DatabaseAccess;

import java.util.ArrayList;
import java.util.List;

public class CategoriesListFragment extends ListFragment {
    private ArrayList<Category> categories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categories_list, container,
                false);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        categories = databaseAccess.getCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, buildStringCategoryRepresentation(categories));
        setListAdapter(adapter);
        databaseAccess.close();
        return rootView;
    }

    private List<String> buildStringCategoryRepresentation(List<Category> categoryList) {
        List<String> categoryStrings = new ArrayList<>();
        for (Category category : categoryList) {
            categoryStrings.add(category.name);
        }
        return categoryStrings;
    }
    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        openCategoryPage(categories.get(position).id);
    }



    private void openCategoryPage(int categoryId) {
        Bundle bundle = new Bundle();
        bundle.putInt("categoryId", categoryId);
        ProductListFragment nextFragment = new ProductListFragment();
        nextFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, nextFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }
}
