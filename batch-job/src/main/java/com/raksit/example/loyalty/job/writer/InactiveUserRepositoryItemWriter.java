package com.raksit.example.loyalty.job.writer;

import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InactiveUserRepositoryItemWriter implements ItemWriter<User> {

  private final UserRepository userRepository;

  public InactiveUserRepositoryItemWriter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void write(List<? extends User> users) throws Exception {
    final RepositoryItemWriter<User> writer = new RepositoryItemWriter<>() {
      @Override
      protected void doWrite(List<? extends User> users) {
        userRepository.deleteAll(users);
      }
    };
    final List<User> activeUsers = userRepository.findAllByEmailIn(users.stream()
        .filter(user -> !user.getIsActive())
        .map(User::getEmail)
        .collect(Collectors.toList()));
    writer.write(activeUsers);
  }
}
