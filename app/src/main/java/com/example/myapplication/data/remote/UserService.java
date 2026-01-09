package com.example.myapplication.data.remote;

import com.example.myapplication.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @POST("create_student/{student_id}")
    Call<Void> createStudent(@Path("student_id") String studentId);

    @POST("create_user/{student_id}")
    Call<User> createUser(@Path("student_id") String studentId, @Body User user);

    @GET("read_user/{student_id}/{username}")
    Call<User> readUser(@Path("student_id") String studentId, @Path("username") String username);

    @GET("read_all_users/{student_id}")
    Call<List<User>> readAllUsers(@Path("student_id") String studentId);

    @PUT("update_user/{student_id}/{username}")
    Call<User> updateUser(@Path("student_id") String studentId, @Path("username") String username, @Body User user);

    @DELETE("delete_user/{student_id}/{username}")
    Call<Void> deleteUser(@Path("student_id") String studentId, @Path("username") String username);
}
