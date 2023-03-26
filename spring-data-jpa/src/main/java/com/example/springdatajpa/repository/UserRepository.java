package com.example.springdatajpa.repository;

import com.example.springdatajpa.model.User;
import com.example.springdatajpa.model.UserProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //Basics
    Optional<User> findByUsername(String username);

    List<User> findAllByOrderByUsernameAsc();

    List<User> findByRegistrationDateBetween(LocalDate start, LocalDate end);

    List<User> findByUsernameAndEmail(String username, String email);

    List<User> findByUsernameOrEmail(String username, String email);

    List<User> findByUsernameIgnoreCase(String username);

    List<User> findByLevelOrderByUsernameDesc(int level);

    List<User> findByLevelGreaterThanEqual(int level);

    List<User> findByUsernameContaining(String text);

    List<User> findByUsernameLike(String text);

    List<User> findByUsernameStartingWith(String text);

    List<User> findByUsernameEndingWith(String text);

    List<User> findByActive(boolean active);

    List<User> findByRegistrationDateIn(Collection<LocalDate> dates);

    List<User> findByRegistrationDateNotIn(Collection<LocalDate> dates);

    Streamable<User> findByEmailContaining(String email);

    Streamable<User> findByLevel(int level);

    //Sorting and Paging
    Optional<User> findFirstByOrderByUsernameAsc();

    Optional<User> findTopByOrderByRegistrationDateDesc();

    @Override
    Page<User> findAll(Pageable pageable);

    List<User> findFirst2ByLevel(int level, Sort sort);

    List<User> findByLevel(int level, Sort sort);

    List<User> findByActive(boolean active, Pageable pageable);

    //Queries
    @Query("select count(*) from User u where u.active = ?1")
    int findNumberOfUsersByActivity(boolean active);

    @Query("select u from User u where u.level = :level and u.active = :active")
    List<User> findByLevelAndActive(@Param("level") int level, @Param("active") boolean active);

    @Query(value = "select count(*) from users where ACTIVE = :active", nativeQuery = true)
    int findNumberOfUsersByActivityNative(@Param("active") boolean active);

    @Query("select u.username, length(u.email) as email_length from #{#entityName} u where u.username like %:text%")
    List<Object[]> findByAsArrayAndSort(@Param("text") String text, Sort sort);

    //Projection
    List<UserProjection.UserSummary> findByRegistrationDateAfter(LocalDate date);

    List<UserProjection.UsernameOnly> findByEmail(String username);

    <T> List<T> findByEmail(String username, Class<T> type);

    //Modifying
    @Transactional
    @Modifying
    @Query("update User u set u.level = :new_level where u.level = :old_level")
    int updateLevel(@Param("old_level") int oldLevel, @Param("new_level") int newLevel);

    //Delete

    @Transactional
    int deleteByLevel(int level);

    @Transactional
    @Modifying
    @Query("delete from User u where u.level = :level")
    int deleteBulkByLevel(@Param("level") int level);

}
