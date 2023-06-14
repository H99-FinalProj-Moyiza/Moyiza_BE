package com.example.moyiza_be.review.repository;


import com.example.moyiza_be.review.entity.ReviewImage;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
