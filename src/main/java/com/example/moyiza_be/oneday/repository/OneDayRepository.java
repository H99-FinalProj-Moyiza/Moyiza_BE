package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneDayRepository extends JpaRepository<OneDay, User> {

    Optional<OneDay> findById(Long oneDayId);

    void deleteById(Long oneDayId);
}
