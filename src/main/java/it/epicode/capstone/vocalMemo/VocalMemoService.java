package it.epicode.capstone.vocalMemo;

import it.epicode.capstone.authentication.AppUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class VocalMemoService {
    private final VocalMemoRepository repository;
    private final VocalMemoMapper vocalMemoMapper;

public VocalMemoResponse save(@Valid VocalMemoRequest request, AppUser user) {
        VocalMemo vocalMemo = new VocalMemo();
        BeanUtils.copyProperties(request, vocalMemo);
        vocalMemo.setUser(user);
        repository.save(vocalMemo);
        return vocalMemoMapper.toVocalMemoResponse(vocalMemo, "http://localhost:8080/api/memo-vocali/" + vocalMemo.getId() + "/audio");
    }

public VocalMemoResponse update(Long id, @Valid VocalMemoRequest request) {
        VocalMemo vocalMemo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovata"));

        vocalMemo.setNomeRegistrazione(request.getNomeRegistrazione());
        vocalMemo.setRegistrazione(request.getRegistrazione());

        repository.save(vocalMemo);
        return vocalMemoMapper.toVocalMemoResponse(vocalMemo, "http://localhost:8080/api/memo-vocali/" + vocalMemo.getId() + "/audio");
    }

public void delete(Long id) {
        VocalMemo vocalMemo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Memo vocale non trovata"));
        repository.delete(vocalMemo);
    }

public VocalMemoResponse findById(Long id) {
        VocalMemo vocalMemo = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Memo Vocale non trovata"));

        return new VocalMemoResponse(
                vocalMemo.getId(),
                vocalMemo.getNomeRegistrazione(),
                vocalMemo.getDataInserimento(),
                vocalMemo.getUser().getId(),
                vocalMemo.getPlaylist() != null ? vocalMemo.getPlaylist().getId() : null,
                "http://localhost:8080/api/memo-vocali/" + vocalMemo.getId() + "/audio"
        );
    }

public List<VocalMemoResponse> findAll() {
        return repository.findAll().stream()
                .map(vocalMemo -> new VocalMemoResponse(
                        vocalMemo.getId(),
                        vocalMemo.getNomeRegistrazione(),
                        vocalMemo.getDataInserimento(),
                        vocalMemo.getUser().getId(),
                        vocalMemo.getPlaylist() != null ? vocalMemo.getPlaylist().getId() : null,
                        "http://localhost:8080/api/memo-vocali/" + vocalMemo.getId() + "/audio"
                ))
                .toList();
    }

public List<VocalMemoResponse> findAllByUser(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(vocalMemo -> new VocalMemoResponse(
                        vocalMemo.getId(),
                        vocalMemo.getNomeRegistrazione(),
                        vocalMemo.getDataInserimento(),
                        vocalMemo.getUser().getId(),
                        vocalMemo.getPlaylist() != null ? vocalMemo.getPlaylist().getId() : null,
                        "http://localhost:8080/api/memo-vocali/" + vocalMemo.getId() + "/audio"
                ))
                .toList();
    }
}
