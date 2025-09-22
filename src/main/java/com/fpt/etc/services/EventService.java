package com.fpt.etc.services;

import com.fpt.etc.dto.request.CreateEventDto;
import com.fpt.etc.dto.request.UpdateEventDto;
import com.fpt.etc.entity.Event;
import com.fpt.etc.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SlugService slugService;     // service nhỏ để generate slug (tương tự Slugify trong C#)
    private final CloudinaryService cloudinaryService; // service upload ảnh

    public List<Event> getAll() {
        return eventRepository.findAll()
                .stream().filter(ev -> !ev.isDeleted()).toList();
    }

    public Event getBySlug(String slug) {
        return eventRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Transactional
    public Event create(CreateEventDto dto) {
        String slug = slugService.generate(dto.getName());
        int counter = 1;
        while (eventRepository.existsBySlug(slug)) {
            slug = slug + "-" + counter++;
        }

        List<String> uploadedImages = new ArrayList<>();
        if (dto.getImages() != null) {
            for (MultipartFile file : dto.getImages()) {
                uploadedImages.add(cloudinaryService.upload(file));
            }
        }

        Event event = Event.builder()
                .name(dto.getName())
                .subName(dto.getSubName())
                .icon(dto.getIcon())
                .slug(slug)
                .description(dto.getDescription())
                .menuIds(dto.getMenuIds() != null ? dto.getMenuIds() : new ArrayList<>())
                .serviceIds(dto.getServiceIds() != null ? dto.getServiceIds() : new ArrayList<>())
                .images(uploadedImages)
                .hot(dto.isHot())
                .deleted(false)
                .build();

        return eventRepository.save(event);
    }

    @Transactional
    public Event update(Long id, UpdateEventDto dto) {
        Event ev = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (dto.getName() != null) {
            ev.setName(dto.getName());
            ev.setSlug(slugService.generate(dto.getName()));
        }
        if (dto.getSubName() != null) ev.setSubName(dto.getSubName());
        if (dto.getDescription() != null) ev.setDescription(dto.getDescription());
        if (dto.getIcon() != null) ev.setIcon(dto.getIcon());

        List<String> currentImages = new ArrayList<>(ev.getImages());

        if (dto.getRemoveImages() != null) {
            for (String url : dto.getRemoveImages()) {
                cloudinaryService.delete(url);
            }
            currentImages.removeAll(dto.getRemoveImages());
        }

        if (dto.getAddImages() != null) {
            for (MultipartFile file : dto.getAddImages()) {
                currentImages.add(cloudinaryService.upload(file));
            }
        }

        ev.setImages(currentImages.stream().distinct().collect(Collectors.toList()));

        if (dto.getHot() != null) ev.setHot(dto.getHot());

        if (dto.getAddMenuIds() != null) {
            Set<Long> newMenus = new HashSet<>(ev.getMenuIds());
            newMenus.addAll(dto.getAddMenuIds());
            ev.setMenuIds(new ArrayList<>(newMenus));
        }
        if (dto.getRemoveMenuIds() != null) {
            ev.getMenuIds().removeAll(dto.getRemoveMenuIds());
        }

        if (dto.getAddServiceIds() != null) {
            Set<Long> newServices = new HashSet<>(ev.getServiceIds());
            newServices.addAll(dto.getAddServiceIds());
            ev.setServiceIds(new ArrayList<>(newServices));
        }
        if (dto.getRemoveServiceIds() != null) {
            ev.getServiceIds().removeAll(dto.getRemoveServiceIds());
        }

        return eventRepository.save(ev);
    }

    @Transactional
    public void delete(Long id) {
        Event ev = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        for (String img : ev.getImages()) {
            cloudinaryService.delete(img);
        }

        ev.setDeleted(true);
        eventRepository.save(ev);
    }
}

