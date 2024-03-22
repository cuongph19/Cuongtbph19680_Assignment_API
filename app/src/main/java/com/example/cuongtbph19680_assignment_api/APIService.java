package com.example.cuongtbph19680_assignment_api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("/api/list")
    Call<List<Product>> getProducts();

    @POST("/add/")
    Call<Product> addProduct(@Body Product product);

    @PUT("/update/{id}")
    Call<Void> updateProduct(@Path("id") String id, @Body Product product);

    @GET("/delete/{id}")
    Call<Void> deleteProduct(@Path("id") String id);
    @GET("/api/search")
    Call<List<Product>> searchProductsByName(@Query("keyword") String keyword);
}