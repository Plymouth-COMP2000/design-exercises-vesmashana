package com.example.myapplication.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import com.example.myapplication.data.model.MenuItem;
import com.example.myapplication.data.repository.MenuRepository;

// ViewModel for menu screens; UI delegates data retrieval to the repository in MVVM.
public class MenuViewModel extends ViewModel {
    private final MenuRepository menuRepository;

    public MenuViewModel(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuItem> getMenuByCategory(String category) {
        return menuRepository.getMenuByCategory(category);
    }

    public MenuItem getMenuById(int id) {
        return menuRepository.getMenuById(id);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final MenuRepository menuRepository;

        public Factory(MenuRepository menuRepository) {
            this.menuRepository = menuRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MenuViewModel.class)) {
                return (T) new MenuViewModel(menuRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
