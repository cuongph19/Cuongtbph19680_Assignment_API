package com.example.cuongtbph19680_assignment_api;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvName, tvGia, tvSoluong, tvTonkho;
        private ImageView imgDelete, imgUpdate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvName = itemView.findViewById(R.id.tvName);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvSoluong = itemView.findViewById(R.id.tvSoluong);
            tvTonkho = itemView.findViewById(R.id.tvTonkho);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgUpdate = itemView.findViewById(R.id.imgUpdate);
        }

        public void bind(Product product) {
            tvId.setText(product.get_id());
            tvName.setText(product.getName());
            tvGia.setText(String.valueOf(product.getPrice()));
            tvSoluong.setText(String.valueOf(product.getQuantity()));
            tvTonkho.setText(product.isInventory() ? "Còn hàng" : "Hết hàng");

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog(getAdapterPosition());
                }
            });

            imgUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Product product = productList.get(getAdapterPosition());

                    // Tạo một AlertDialog để hiển thị các trường dữ liệu và cho phép người dùng chỉnh sửa
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.activity_add_product, null);
                    builder.setView(dialogView);

                    // Ánh xạ các trường EditText trong dialog
                    EditText edName = dialogView.findViewById(R.id.edName);
                    EditText edPrice = dialogView.findViewById(R.id.edPrice);
                    EditText edQuantity = dialogView.findViewById(R.id.edQuantity);
                    EditText edImage = dialogView.findViewById(R.id.edImage);
                    RadioGroup radioGroupInventory = dialogView.findViewById(R.id.radioGroupInventory);

                    // Đặt giá trị mặc định cho các trường EditText từ dữ liệu sản phẩm
                    edName.setText(product.getName());
                    edPrice.setText(String.valueOf(product.getPrice()));
                    edQuantity.setText(String.valueOf(product.getQuantity()));
                    edImage.setText(product.getImage());

                    // Đặt giá trị mặc định cho RadioGroup
                    if (product.isInventory()) {
                        radioGroupInventory.check(R.id.rbTrue);
                    } else {
                        radioGroupInventory.check(R.id.rbFalse);
                    }

                    // Xử lý sự kiện khi người dùng nhấn nút "Update"
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Lấy dữ liệu đã chỉnh sửa từ EditText
                            String updatedName = edName.getText().toString();
                            double updatedPrice = Double.parseDouble(edPrice.getText().toString());
                            int updatedQuantity = Integer.parseInt(edQuantity.getText().toString());
                            String updatedImage = edImage.getText().toString();
                            boolean updatedInventory = radioGroupInventory.getCheckedRadioButtonId() == R.id.rbTrue;

                            // Cập nhật dữ liệu sản phẩm trong danh sách
                            product.setName(updatedName);
                            product.setPrice(updatedPrice);
                            product.setQuantity(updatedQuantity);
                            product.setImage(updatedImage);
                            product.setInventory(updatedInventory);

                            // Gọi phương thức cập nhật dữ liệu sản phẩm lên server
                            updateProduct(product);
                        }
                    });

                    // Xử lý sự kiện khi người dùng nhấn nút "Cancel"
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // Hiển thị dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }
    private void updateProduct(Product product) {
        // Tạo đối tượng Retrofit
        APIService apiService = RetrofitClient.getClient().create(APIService.class);

        // Gọi phương thức updateProduct và truyền sản phẩm đã được cập nhật
        Call<Void> call = apiService.updateProduct(product.get_id(), product);

        // Gửi yêu cầu và xử lý kết quả
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cập nhật lại dữ liệu trong RecyclerView sau khi cập nhật thành công
                    loadDataFromServer();
                    Toast.makeText(context, "Product updated successfully!", Toast.LENGTH_SHORT).show();
                    Log.d("UpdateProduct", "Product updated successfully!");
                } else {
                    // Xử lý khi cập nhật sản phẩm thất bại
                    Toast.makeText(context, "Failed to update product. Error code: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("UpdateProduct", "Failed to update product. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra trong quá trình gửi yêu cầu cập nhật
                Toast.makeText(context, "Failed to update product. Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateProduct", "Failed to update product. Error: " + t.getMessage());
            }
        });
    }
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Product product = productList.get(position);
                String id = product.get_id();
                Log.d("DeleteProduct", "ID: " + id);
                deleteProduct(id);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteProduct(String id) {
        Log.d("DeleteProduct", "Đang cố gắng xóa sản phẩm có ID: " + id);

        // Tạo đối tượng Retrofit
        APIService apiService = RetrofitClient.getClient().create(APIService.class);

        // Gọi phương thức deleteProduct và truyền productId
        Call<Void> call = apiService.deleteProduct(id);

        // Gửi yêu cầu và xử lý kết quả
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadDataFromServer();
                    Log.d("DeleteProduct", "Đã xóa sản phẩm có ID: " + id);
                } else {
                    // Xử lý khi xóa sản phẩm thất bại
                    Log.e("DeleteProduct", "Xóa sản phẩm không thành công. Mã phản hồi: " + response.code());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra
                Log.e("DeleteProduct", "Xóa sản phẩm không thành công. Lỗi: " + t.getMessage());
            }
        });
    }
    private void loadDataFromServer() {
        // Tạo đối tượng Retrofit
        APIService apiService = RetrofitClient.getClient().create(APIService.class);

        // Gửi yêu cầu để tải dữ liệu mới từ server
        Call<List<Product>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    // Cập nhật dữ liệu mới cho RecyclerView
                    List<Product> productList = response.body();
                    // Gọi phương thức để cập nhật dữ liệu cho RecyclerView
                    upLoad(productList);
                } else {
                    // Xử lý khi không nhận được dữ liệu từ server
                    Log.e("LoadDataFromServer", "Không thể tải dữ liệu từ server. Mã phản hồi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra trong quá trình tải dữ liệu từ server
                Log.e("LoadDataFromServer", "Lỗi khi tải dữ liệu từ server: " + t.getMessage());
            }
        });
    }

    private void upLoad(List<Product> productList) {
        this.productList.clear(); // Xóa dữ liệu cũ
        this.productList.addAll(productList); // Thêm dữ liệu mới
        notifyDataSetChanged(); // Cập nhật giao diện RecyclerView
    }
}