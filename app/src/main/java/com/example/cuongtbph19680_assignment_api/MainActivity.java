package com.example.cuongtbph19680_assignment_api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter adapter;
    private APIService apiService;
    private EditText edtTimKiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getClient().create(APIService.class);

        loadProducts();

        // EditText để tìm kiếm sản phẩm
        edtTimKiem = findViewById(R.id.edtTimKiem);
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Floating action button để thêm sản phẩm
        FloatingActionButton actionButton = findViewById(R.id.floatAddDanhSach);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });
    }

    private void loadProducts() {
        Call<List<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    productList = response.body();
                    adapter = new ProductAdapter(MainActivity.this, productList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddProductDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_add_product, null);
        dialogBuilder.setView(dialogView);

        final EditText edName = dialogView.findViewById(R.id.edName);
        final EditText edPrice = dialogView.findViewById(R.id.edPrice);
        final EditText edQuantity = dialogView.findViewById(R.id.edQuantity);
        final RadioGroup radioGroupInventory = dialogView.findViewById(R.id.radioGroupInventory);
        final EditText edImage = dialogView.findViewById(R.id.edImage);

        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edName.getText().toString();
                double price = Double.parseDouble(edPrice.getText().toString());
                int quantity = Integer.parseInt(edQuantity.getText().toString());
                String image = edImage.getText().toString();

                RadioButton selectedRadioButton = dialogView.findViewById(radioGroupInventory.getCheckedRadioButtonId());
                boolean inventory = selectedRadioButton.getText().equals("True");

                Product product = new Product();
                product.setName(name);
                product.setPrice(price);
                product.setQuantity(quantity);
                product.setInventory(inventory);
                product.setImage(image);

                addProduct(product);
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void searchProducts(String keyword) {
        Call<List<Product>> call = apiService.searchProductsByName(keyword);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    productList = response.body();
                    adapter = new ProductAdapter(MainActivity.this, productList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to search products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to search products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProduct(Product product) {
        Call<Product> call = apiService.addProduct(product);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    loadProducts(); // Reload the product list
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        });
    }
}