package com.fpt.etc.services;

import com.fpt.etc.dto.service.CreateServiceDto;
import com.fpt.etc.dto.service.UpdateServiceDto;
import com.fpt.etc.entity.ServiceEntity;
import com.fpt.etc.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final CloudinaryService cloudinaryService;

    public List<ServiceEntity> getAll() {
        return serviceRepository.findByDeletedFalse();
    }

    public ServiceEntity getById(Long id) {
        return serviceRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public ServiceEntity create(CreateServiceDto dto) {
        List<String> uploaded = uploadImages(dto.getImages());

        ServiceEntity service = ServiceEntity.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .icon(dto.getIcon())
                .images(uploaded)
                .deleted(false)
                .build();

        return serviceRepository.save(service);
    }

    public ServiceEntity update(Long id, UpdateServiceDto dto) {
        ServiceEntity service = getById(id);

        if (dto.getName() != null) service.setName(dto.getName());
        if (dto.getPrice() != null) service.setPrice(dto.getPrice());
        if (dto.getDescription() != null) service.setDescription(dto.getDescription());
        if (dto.getIcon() != null) service.setIcon(dto.getIcon());

        if (dto.getRemoveImages() != null && !dto.getRemoveImages().isEmpty()) {
            for (String url : dto.getRemoveImages()) {
                cloudinaryService.delete(url);
            }
            service.getImages().removeAll(dto.getRemoveImages());
        }

        if (dto.getAddImages() != null && !dto.getAddImages().isEmpty()) {
            List<String> added = uploadImages(dto.getAddImages());
            service.getImages().addAll(added);
        }

        return serviceRepository.save(service);
    }

    public void delete(Long id) {
        ServiceEntity service = getById(id);
        service.setDeleted(true);
        serviceRepository.save(service);
    }

    private List<String> uploadImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return new ArrayList<>();

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = cloudinaryService.upload(file);
            urls.add(url);
        }
        return urls;
    }
}

