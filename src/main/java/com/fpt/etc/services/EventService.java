package com.fpt.etc.services;

import com.fpt.etc.dto.event.CreateEventDto;
import com.fpt.etc.dto.event.EventResponse;
import com.fpt.etc.dto.event.UpdateEventDto;
import com.fpt.etc.dto.menu.MenuResponse;
import com.fpt.etc.dto.service.ServiceResponse;
import com.fpt.etc.entity.Event;
import com.fpt.etc.repository.EventRepository;
import com.fpt.etc.repository.MenuRepository;
import com.fpt.etc.repository.ServiceRepository;
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
    private final MenuRepository menuRepository; // service upload ảnh
    private final ServiceRepository serviceRepository; // service upload ảnh

    public List<EventResponse> getAll() {
        return eventRepository.findAll().stream()
                .filter(ev -> !ev.isDeleted())
                .map(this::mapToResponse)
                .toList();
    }

    private EventResponse mapToResponse(Event event) {
        // Lấy menu theo id
        List<MenuResponse> menus = event.getMenuIds().isEmpty() ? List.of() :
                menuRepository.findAllById(event.getMenuIds()).stream()
                        .map(m -> MenuResponse.builder()
                                .id(m.getId())
                                .name(m.getName())
                                .price(m.getPrice())
                                .build())
                        .toList();

        // Lấy service theo id
        List<ServiceResponse> services = event.getServiceIds().isEmpty() ? List.of() :
                serviceRepository.findAllById(event.getServiceIds()).stream()
                        .map(s -> ServiceResponse.builder()
                                .id(s.getId())
                                .name(s.getName())
                                .price(s.getPrice())
                                .build())
                        .toList();

        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .subName(event.getSubName())
                .icon(event.getIcon())
                .slug(event.getSlug())
                .description(event.getDescription())
                .images(event.getImages())
                .hot(event.isHot())
                .menus(menus)
                .services(services)
                .build();
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

