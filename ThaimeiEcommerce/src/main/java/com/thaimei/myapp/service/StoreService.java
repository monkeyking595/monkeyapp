package com.thaimei.myapp.service;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.dto.sellersDto.RegisterStoreDto;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.repository.StoreRepo;
import com.thaimei.myapp.model.StoreModel;
import com.thaimei.myapp.enums.StoreStatus;


@Service
public class StoreService {
    private final StoreRepo storeRepo;

    public StoreService(StoreRepo storeRepo) {
        this.storeRepo = storeRepo;
    }

    public void saveStore(RegisterStoreDto storeDto, User user) {
        StoreModel store = new StoreModel();
        store.setStoreName(storeDto.getStoreName());
        store.setBusinessType(storeDto.getBusinessType());
        store.setStoreStatus(StoreStatus.PENDING);
        store.setLatitude(storeDto.getLatitude());
        store.setLongitude(storeDto.getLongitude());
        store.setUser(user);
        storeRepo.save(store);
    }
}
