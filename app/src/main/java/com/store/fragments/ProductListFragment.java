package com.store.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.store.R;
import com.store.activities.ProductInfo;
import com.store.controller.DatabaseAccess;
import com.store.controller.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends ListFragment {
    private ArrayList<Product> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_list, container,
                false);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();

        Bundle bundle = this.getArguments();

        if(bundle != null) {


            products = databaseAccess.getProducts(bundle.getInt("categoryId"));
            System.out.println(bundle.getInt("categoryId"));
        }
        else
            products = databaseAccess.getProducts(null);



        databaseAccess.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, buildStringProductRepresentation(products));
        setListAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        openProductPage(products.get(position));
    }

    private List<String> buildStringProductRepresentation(List<Product> productList) {
        List<String> productStrings = new ArrayList<>();
        for (Product product : productList) {
            productStrings.add(product.productName + "   price:   " + product.price + " " + product.priceCurrency);
        }
        return productStrings;
    }

    private void openProductPage(Product product) {
        Intent intent = new Intent(getActivity(), ProductInfo.class);
        intent.putExtra("name", product.productName);
        intent.putExtra("price", String.valueOf(product.price));
        intent.putExtra("description", product.description);
        intent.putExtra("currency", product.priceCurrency);
        startActivity(intent);
    }
}
