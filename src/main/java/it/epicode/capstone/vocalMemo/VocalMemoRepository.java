package it.epicode.capstone.vocalMemo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocalMemoRepository extends JpaRepository<VocalMemo, Long> {
    List<VocalMemo> findByUserId(Long userId);
}
