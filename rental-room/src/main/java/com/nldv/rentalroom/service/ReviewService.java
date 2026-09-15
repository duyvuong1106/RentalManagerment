/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nldv.rentalroom.service;

/**
 *
 * @author ASUS
 */
import com.nldv.rentalroom.dto.ReviewCreateRequest;
import com.nldv.rentalroom.dto.ReviewResponse;
import com.nldv.rentalroom.enums.ContractStatus;
import com.nldv.rentalroom.pojo.Contract;
import com.nldv.rentalroom.pojo.Review;
import com.nldv.rentalroom.pojo.Room;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.repository.ContractRepository;
import com.nldv.rentalroom.repository.ReviewRepository;
import com.nldv.rentalroom.repository.RoomRepository;
import com.nldv.rentalroom.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            ContractRepository contractRepository) {

        this.reviewRepository = reviewRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.contractRepository = contractRepository;
    }

   
    @Transactional
    public ReviewResponse createReview(
            Integer customerId,
            ReviewCreateRequest request) {

        
        User customer = userRepository
                .findById(customerId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy khách hàng"));

       
        Room room = roomRepository
                .findById(request.getRoomId())
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy phòng"));

      
        if (reviewRepository.existsByUserIdAndRoomId(
                customerId,
                room.getId())) {

            throw new IllegalArgumentException(
                    "Bạn đã đánh giá phòng này");
        }

        
        List<Contract> contracts
                = contractRepository.findByUserId(customerId);

        boolean hasRentedRoom = contracts.stream()
                .anyMatch(contract -> {

                    if (contract.getRoom() == null) {
                        return false;
                    }

                    if (!contract.getRoom()
                            .getId()
                            .equals(room.getId())) {

                        return false;
                    }

                    ContractStatus status
                            = contract.getStatus();

                    return status == ContractStatus.ACTIVE
                            || status == ContractStatus.EXPIRED
                            || status == ContractStatus.TERMINATED;
                });

        if (!hasRentedRoom) {
            throw new IllegalArgumentException(
                    "Bạn chỉ có thể đánh giá phòng đã từng thuê");
        }

       
        Review review = new Review();

        review.setUser(customer);
        review.setRoom(room);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        
        review.setIsVisible(true);

        
        Review savedReview
                = reviewRepository.save(review);

        
        return toResponse(savedReview);
    }

    
    public List<ReviewResponse> getRoomReviews(
            Integer roomId) {

        // Kiểm tra phòng tồn tại
        roomRepository
                .findById(roomId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy phòng"));

        return reviewRepository
                .findByRoomIdAndIsVisibleTrue(roomId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

   
    public List<ReviewResponse> getCustomerReviews(
            Integer customerId) {

        return reviewRepository
                .findByUserId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    
    public ReviewResponse getCustomerReview(
            Integer customerId,
            Integer reviewId) {

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy đánh giá"));

        if (review.getUser() == null
                || review.getUser().getId() == null
                || !review.getUser()
                        .getId()
                        .equals(customerId)) {

            throw new IllegalArgumentException(
                    "Bạn không có quyền xem đánh giá này");
        }

        return toResponse(review);
    }

    
    public List<ReviewResponse> getAllReviews() {

        return reviewRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    
    @Transactional
    public ReviewResponse hideReview(
            Integer reviewId) {

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy đánh giá"));

        review.setIsVisible(false);

        Review savedReview
                = reviewRepository.save(review);

        return toResponse(savedReview);
    }

    
    @Transactional
    public ReviewResponse showReview(
            Integer reviewId) {

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Không tìm thấy đánh giá"));

        review.setIsVisible(true);

        Review savedReview
                = reviewRepository.save(review);

        return toResponse(savedReview);
    }

    
    private ReviewResponse toResponse(
            Review review) {

        ReviewResponse response
                = new ReviewResponse();

        response.setId(review.getId());

        if (review.getRoom() != null) {

            response.setRoomId(
                    review.getRoom().getId());

            response.setRoomNumber(
                    review.getRoom().getRoomNumber());
        }

        if (review.getUser() != null) {

            response.setCustomerId(
                    review.getUser().getId());

            response.setCustomerUsername(
                    review.getUser().getUsername());
        }

        response.setRating(
                review.getRating());

        response.setComment(
                review.getComment());

        response.setIsVisible(
                review.getIsVisible());

        response.setCreatedDate(
                review.getCreatedDate());

        response.setUpdatedDate(
                review.getUpdatedDate());

        return response;
    }
}
