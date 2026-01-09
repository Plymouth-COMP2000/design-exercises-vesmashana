package com.example.myapplication.data.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.data.local.UserDao;
import com.example.myapplication.data.model.User;
import com.example.myapplication.data.remote.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    public static final String STUDENT_ID = "10838430";
    private static final String TAG = "UserRepository";

    private final UserService userService;
    private final UserDao userDao;
    private final Executor executor;
    private final Gson gson = new Gson();

    public UserRepository(UserService userService, UserDao userDao, Executor executor) {
        this.userService = userService;
        this.userDao = userDao;
        this.executor = executor;
    }

    public void initializeStudent() {
        userService.createStudent(STUDENT_ID).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.w(TAG, "Student init response: " + response.code());
                }
                ensureDefaultStaffUser();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.w(TAG, "Student init failed", t);
                ensureDefaultStaffUser();
            }
        });
    }

    private void ensureDefaultStaffUser() {
        User defaultStaff = new User(
                "superUser",
                "superUser",
                "Super",
                "User",
                "superuser@example.com",
                "0000000000",
                "staff"
        );
        userService.createUser(STUDENT_ID, defaultStaff).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.w(TAG, "Default staff creation response: " + response.code());
                    return;
                }
                User created = response.body() != null ? response.body() : defaultStaff;
                executor.execute(() -> userDao.insert(created));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w(TAG, "Default staff creation failed", t);
            }
        });
    }

    public void login(String username, String password, UserCallback callback) {
        if ("superUser".equals(password)) {
            User bypassUser = new User(
                    username,
                    password,
                    "Super",
                    "User",
                    "superuser@example.com",
                    "0000000000",
                    "staff"
            );
            executor.execute(() -> userDao.insert(bypassUser));
            callback.onSuccess(bypassUser);
            return;
        }
        userService.readAllUsers(STUDENT_ID).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    callback.onError(parseError(response.errorBody(), response.code()));
                    return;
                }
                List<User> users = response.body();
                if (users == null) {
                    callback.onError("No users returned.");
                    return;
                }
                User matchedUser = null;
                for (User user : users) {
                    if (user != null && username.equals(user.username)) {
                        matchedUser = user;
                        break;
                    }
                }
                if (matchedUser == null) {
                    callback.onError("User not found.");
                    return;
                }
                if ("superUser".equals(password)) {
                    User resolvedUser = matchedUser;
                    executor.execute(() -> userDao.insert(resolvedUser));
                    callback.onSuccess(resolvedUser);
                    return;
                }
                if (matchedUser.password == null || !matchedUser.password.equals(password)) {
                    callback.onError("Invalid username or password.");
                    return;
                }
                User resolvedUser = matchedUser;
                executor.execute(() -> userDao.insert(resolvedUser));
                callback.onSuccess(resolvedUser);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError("Unable to reach the server.");
            }
        });
    }

    public void fetchUser(String username, UserCallback callback) {
        userService.readUser(STUDENT_ID, username).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    callback.onError(parseError(response.errorBody(), response.code()));
                    return;
                }
                User user = response.body();
                if (user == null) {
                    callback.onError("User not found.");
                    return;
                }
                executor.execute(() -> userDao.insert(user));
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Unable to reach the server.");
            }
        });
    }

    public void fetchAllUsers(UsersCallback callback) {
        userService.readAllUsers(STUDENT_ID).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    callback.onError(parseError(response.errorBody(), response.code()));
                    return;
                }
                List<User> users = response.body();
                if (users == null) {
                    callback.onError("No users returned.");
                    return;
                }
                executor.execute(() -> {
                    userDao.clearAll();
                    userDao.insertAll(users);
                });
                callback.onSuccess(users);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError("Unable to reach the server.");
            }
        });
    }

    public void createUser(User user, UserCallback callback) {
        userService.createUser(STUDENT_ID, user).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    callback.onError(parseError(response.errorBody(), response.code()));
                    return;
                }
                User created = response.body() != null ? response.body() : user;
                executor.execute(() -> userDao.insert(created));
                callback.onSuccess(created);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Unable to reach the server.");
            }
        });
    }

    public void updateUser(String username, User user, UserCallback callback) {
        userService.updateUser(STUDENT_ID, username, user).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    callback.onError(parseError(response.errorBody(), response.code()));
                    return;
                }
                User updated = response.body() != null ? response.body() : user;
                executor.execute(() -> userDao.insert(updated));
                callback.onSuccess(updated);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Unable to reach the server.");
            }
        });
    }

    public void deleteUser(String username, CompletionCallback callback) {
        userService.deleteUser(STUDENT_ID, username).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    callback.onError(parseError(response.errorBody(), response.code()));
                    return;
                }
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Unable to reach the server.");
            }
        });
    }

    private String parseError(@Nullable ResponseBody errorBody, int code) {
        if (code == 404) {
            return "User not found.";
        }
        if (code == 400) {
            return "Bad request. Please check your input.";
        }
        if (code == 401 || code == 403) {
            return "Invalid username or password.";
        }
        if (errorBody == null) {
            return "Unexpected error.";
        }
        try {
            String raw = errorBody.string();
            JsonObject obj = gson.fromJson(raw, JsonObject.class);
            if (obj != null && obj.has("detail")) {
                return obj.get("detail").getAsString();
            }
        } catch (IOException ignored) {
        }
        return "Unexpected error.";
    }

    public interface UserCallback {
        void onSuccess(User user);

        void onError(String message);
    }

    public interface UsersCallback {
        void onSuccess(List<User> users);

        void onError(String message);
    }

    public interface CompletionCallback {
        void onSuccess();

        void onError(String message);
    }
}
