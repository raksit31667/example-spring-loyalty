package com.raksit.example.loyalty.job.writer;

import com.raksit.example.loyalty.user.User;
import com.raksit.example.loyalty.user.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActiveUserRepositoryItemWriter implements ItemWriter<User> {

  private final UserRepository userRepository;

  public ActiveUserRepositoryItemWriter(
      UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void write(List<? extends User> users) throws Exception {
    final RepositoryItemWriter<User> userRepositoryItemWriter = new RepositoryItemWriterBuilder<User>()
        .repository(userRepository)
        .build();
    userRepositoryItemWriter.write(users.stream()
        .filter(User::getIsActive)
        .collect(Collectors.toList()));
  }
}
