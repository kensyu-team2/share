// src/main/java/app/repository/TypeRepository.java
package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Type;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
}