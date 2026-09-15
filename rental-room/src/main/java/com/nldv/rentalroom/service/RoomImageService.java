/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.service;

/**
 *
 * @author ASUS
 */
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.RoomImage;
import com.nldv.rentalroom.repository.RoomImageRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RoomImageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final RoomImageRepository roomImageRepository;
    private final Cloudinary cloudinary;

    public RoomImageService(
            RoomImageRepository roomImageRepository,
            Cloudinary cloudinary) {

        this.roomImageRepository = roomImageRepository;
        this.cloudinary = cloudinary;
    }

    public List<RoomImage> findByRoomId(Integer roomId) {

        return roomImageRepository.findByRoomId(roomId);
    }

    public RoomImage findById(Integer id) {

        return roomImageRepository
                .findById(id)
                .orElse(null);
    }

    public RoomImage uploadImage(
            Room room,
            MultipartFile file,
            boolean thumbnail) throws IOException {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "File ảnh không được để trống");
        }

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "Kích thước ảnh không được vượt quá 5MB");
        }

        String contentType = file.getContentType();

        if (contentType == null) {

            throw new IllegalArgumentException(
                    "Không xác định được loại file");
        }

        if (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png")
                && !contentType.equals("image/webp")) {

            throw new IllegalArgumentException(
                    "Chỉ hỗ trợ JPG, PNG và WEBP");
        }

        if (thumbnail) {

            List<RoomImage> images
                    = roomImageRepository
                            .findByRoomId(room.getId());

            for (RoomImage image : images) {
                image.setIsThumbnail(false);
            }

            roomImageRepository.saveAll(images);
        }

        boolean hasThumbnail
                = roomImageRepository
                        .existsByRoomIdAndIsThumbnailTrue(
                                room.getId());

        boolean finalThumbnail
                = thumbnail || !hasThumbnail;

        Map<?, ?> uploadResult
                = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "folder",
                                "rental-room/rooms/"
                                + room.getId()
                        )
                );

        String imageUrl
                = (String) uploadResult.get("secure_url");

        String publicId
                = (String) uploadResult.get("public_id");

        RoomImage roomImage
                = new RoomImage();

        roomImage.setRoom(room);
        roomImage.setImageUrl(imageUrl);
        roomImage.setPublicId(publicId);
        roomImage.setIsThumbnail(finalThumbnail);

        return roomImageRepository.save(roomImage);
    }

    public void deleteImage(
            RoomImage roomImage) throws IOException {

        boolean wasThumbnail
                = Boolean.TRUE.equals(
                        roomImage.getIsThumbnail());

        Integer roomId
                = roomImage.getRoom().getId();

        if (roomImage.getPublicId() != null
                && !roomImage.getPublicId().isBlank()) {

            cloudinary.uploader().destroy(
                    roomImage.getPublicId(),
                    ObjectUtils.emptyMap()
            );
        }

        roomImageRepository.delete(roomImage);

        if (wasThumbnail) {

            List<RoomImage> remainingImages
                    = roomImageRepository
                            .findByRoomId(roomId);

            if (!remainingImages.isEmpty()) {

                RoomImage newThumbnail
                        = remainingImages.get(0);

                newThumbnail.setIsThumbnail(true);

                roomImageRepository.save(
                        newThumbnail);
            }
        }
    }

    public RoomImage setThumbnail(
            RoomImage roomImage) {

        Integer roomId
                = roomImage.getRoom().getId();

        List<RoomImage> images
                = roomImageRepository
                        .findByRoomId(roomId);

        for (RoomImage image : images) {

            image.setIsThumbnail(
                    image.getId()
                            .equals(roomImage.getId()));
        }

        roomImageRepository.saveAll(images);

        return roomImageRepository
                .findById(roomImage.getId())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy hình ảnh"));
    }
}
