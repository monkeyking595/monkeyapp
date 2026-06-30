package com.thaimei.myapp.service;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.dto.sellersDto.RegisterStoreDto;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.repository.StoreRepo;
import com.thaimei.myapp.model.StoreModel;
import com.thaimei.myapp.enums.StoreStatus;
import java.util.List;
import com.thaimei.myapp.dto.sellersDto.StoresDto;
import org.modelmapper.ModelMapper;
import com.thaimei.myapp.dto.adminDto.AdminStoreApprovalDto;
import com.thaimei.myapp.error.ResourceNotFoundException;
import com.thaimei.myapp.dto.adminDto.AdminStoresDto;
import com.thaimei.myapp.repository.UserRepository;

@Service
public class StoreService {
    private final StoreRepo storeRepo;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public StoreService(StoreRepo storeRepo, ModelMapper modelMapper, UserRepository userRepository) {
        this.storeRepo = storeRepo;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
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

    public void updateStoreStatus(long storeId, long userId, AdminStoreApprovalDto dto) {
        StoreModel store = storeRepo.findById(storeId)
        .orElseThrow(()-> new ResourceNotFoundException("Store not found"));
        
        store.setStoreStatus(dto.getStatus());
        storeRepo.save(store);

        //audit log for admins will be done here...
    }

    //outer List is for returning a list of StoresDto to the endpoint
    public List<StoresDto> getStoresByUser(User user) {
        //inner list is for storing the list of StoreModel which will come from the repo they should always match the type 
        List<StoreModel> stores = storeRepo.findAllByUser(user);
        return stores.stream()
        //take each store model object and convert it to storeDto
        .map(store -> modelMapper.map(store, StoresDto.class))
        //converts back to list 
        //stop confusing over silly stuff you dumbass
        .toList();
    }

    public List<AdminStoresDto> getAllStoresForAdmin(long sellerId) {
        User seller = userRepository.findById(sellerId)
        .orElseThrow(()-> new ResourceNotFoundException ("seller  not found"));
        List<StoreModel> stores = storeRepo.findAllByUser(seller);
        return stores.stream()
        .map(store -> modelMapper.map(store, AdminStoresDto.class))
        .toList();
    }
}
