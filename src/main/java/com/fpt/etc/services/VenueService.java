package com.fpt.etc.services;

import com.fpt.etc.dto.room.RoomResponse;
import com.fpt.etc.dto.venue.CreateVenueDto;
import com.fpt.etc.dto.venue.UpdateVenueDto;
import com.fpt.etc.dto.venue.VenueResponse;
import com.fpt.etc.entity.Venue;
import com.fpt.etc.repository.RoomRepository;
import com.fpt.etc.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;
    private final CloudinaryService cloudinaryService;
    private final SlugService slugService;
    private final RoomRepository roomRepository;

    public List<VenueResponse> getAll() {
        return venueRepository.findAllByDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private VenueResponse mapToResponse(Venue venue) {
        // Lấy rooms theo list id
        List<RoomResponse> rooms = roomRepository.findByIdIn(venue.getRoomIds())
                .stream()
                .map(r -> new RoomResponse(r.getId(), r.getName(), r.getPrice()))
                .toList();

        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .area(venue.getArea())
                .people(venue.getPeople())
                .description(venue.getDescription())
                .address(venue.getAddress())
                .openTime(venue.getOpenTime())
                .closeTime(venue.getCloseTime())
                .image(venue.getImage())
                .rooms(rooms)
                .heroBanners(venue.getHeroBanners())
                .galleryImages(venue.getGalleryImages())
                .thumbnailImages(venue.getThumbnailImages())
                .days(venue.getDays())
                .slug(venue.getSlug())
                .build();
    }

    public VenueResponse getBySlug(String slug) {
        Venue results =  venueRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        return mapToResponse(results);
    }

    public Venue create(CreateVenueDto dto) {
        String slug = slugService.generate(dto.getName());
        int counter = 1;
        while (venueRepository.existsBySlug(slug)) {
            slug = slug + "-" + counter++;
        }

        List<String> heroBanners = uploadImages(dto.getHeroBanners());
        List<String> thumbnails = uploadImages(dto.getThumbnailImages());
        List<String> galleries = uploadImages(dto.getGalleryImages());
        String imageUrl = dto.getImage() != null ? cloudinaryService.upload(dto.getImage()) : "";

        Venue venue = Venue.builder()
                .name(dto.getName())
                .slug(slug)
                .description(dto.getDescription())
                .area(dto.getArea())
                .people(dto.getPeople())
                .roomIds(dto.getRoomIds() != null ? dto.getRoomIds() : new ArrayList<>())
                .heroBanners(heroBanners)
                .thumbnailImages(thumbnails)
                .galleryImages(galleries)
                .address(dto.getAddress())
                .openTime(dto.getOpen())
                .closeTime(dto.getClose())
                .days(dto.getDays())
                .image(imageUrl)
                .deleted(false)
                .build();

        return venueRepository.save(venue);
    }

    public Venue update(Long id, UpdateVenueDto dto) {
        Venue venue = venueRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        if (dto.getName() != null) {
            venue.setName(dto.getName());
            venue.setSlug(slugService.generate(dto.getName()));
        }
        if (dto.getDescription() != null) venue.setDescription(dto.getDescription());
        if (dto.getArea() != null) venue.setArea(dto.getArea());
        if (dto.getPeople() != null) venue.setPeople(dto.getPeople());
        if (dto.getAddress() != null) venue.setAddress(dto.getAddress());
        if (dto.getOpen() != null) venue.setOpenTime(dto.getOpen());
        if (dto.getClose() != null) venue.setCloseTime(dto.getClose());
        if (dto.getDays() != null) venue.setDays(dto.getDays());

        if (dto.getAddRoomIds() != null) {
            venue.getRoomIds().addAll(dto.getAddRoomIds());
        }
        if (dto.getRemoveRoomIds() != null) {
            venue.getRoomIds().removeAll(dto.getRemoveRoomIds());
        }

        if (dto.getRemoveHeroBanners() != null) {
            dto.getRemoveHeroBanners().forEach(cloudinaryService::delete);
            venue.getHeroBanners().removeAll(dto.getRemoveHeroBanners());
        }
        if (dto.getAddHeroBanners() != null) {
            venue.getHeroBanners().addAll(uploadImages(dto.getAddHeroBanners()));
        }

        if (dto.getRemoveThumbnailImages() != null) {
            dto.getRemoveThumbnailImages().forEach(cloudinaryService::delete);
            venue.getThumbnailImages().removeAll(dto.getRemoveThumbnailImages());
        }
        if (dto.getAddThumbnailImages() != null) {
            venue.getThumbnailImages().addAll(uploadImages(dto.getAddThumbnailImages()));
        }

        if (dto.getRemoveGalleryImages() != null) {
            dto.getRemoveGalleryImages().forEach(cloudinaryService::delete);
            venue.getGalleryImages().removeAll(dto.getRemoveGalleryImages());
        }
        if (dto.getAddGalleryImages() != null) {
            venue.getGalleryImages().addAll(uploadImages(dto.getAddGalleryImages()));
        }

        if (dto.getImage() != null) {
            cloudinaryService.delete(venue.getImage());
            venue.setImage(cloudinaryService.upload(dto.getImage()));
        }

        return venueRepository.save(venue);
    }

    public void delete(Long id) {
        Venue venue = venueRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        List<String> allImages = new ArrayList<>();
        if (venue.getImage() != null) allImages.add(venue.getImage());
        allImages.addAll(venue.getHeroBanners());
        allImages.addAll(venue.getThumbnailImages());
        allImages.addAll(venue.getGalleryImages());

        allImages.forEach(cloudinaryService::delete);

        venue.setDeleted(true);
        venueRepository.save(venue);
    }

    private List<String> uploadImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return new ArrayList<>();
        return files.stream().map(cloudinaryService::upload).collect(Collectors.toList());
    }
}

