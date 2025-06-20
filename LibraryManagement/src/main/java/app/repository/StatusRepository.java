package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    // 基本的なCRUD操作（findById, findAll, saveなど）はJpaRepositoryが提供するため、
    // 特殊な検索が不要な場合は、この中身は空のままで問題ありません。
}