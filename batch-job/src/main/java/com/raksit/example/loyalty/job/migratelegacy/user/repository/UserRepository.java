package com.raksit.example.loyalty.job.migratelegacy.user.repository;

import com.raksit.example.loyalty.job.migratelegacy.user.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  List<User> findAllByEmailIn(List<String> emails);
}
