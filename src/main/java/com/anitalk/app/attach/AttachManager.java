package com.anitalk.app.attach;

import com.anitalk.app.attach.dto.AttachRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AttachManager {
    private final AttachUploader attachUploader;
    private final AttachRepository attachRepository;

    public AttachRecord uploadAttach(String category, MultipartFile file) throws Exception {
        if(file == null || file.isEmpty()){
            throw new Exception("파일이 비어있습니다.");
        }
        String filename = UUID.randomUUID().toString();
        String originName = file.getOriginalFilename();
        String url = attachUploader.uploadAttach(filename, file);

        AttachEntity entity = AttachEntity.builder()
                .category(category)
                .name(filename)
                .originName(originName)
                .build();

        entity = attachRepository.save(entity);
        return new AttachRecord(
                entity.getId(),
                entity.getCategory(),
                entity.getName(),
                entity.getOriginName(),
                url
        );
    }

    public void deleteAttach(Long id) throws Exception {
        AttachEntity entity = attachRepository.findById(id).orElseThrow();
        attachUploader.deleteAttach(entity.getName());
    }

    public void connectAttaches(String category, Long parentId, List<String> attaches) {
        List<AttachEntity> entities = attachRepository.findAllByNameIn(attaches);

        if(entities.size() != attaches.size()) throw new NoSuchElementException("첨부파일 등록 실패");

        for (AttachEntity entity : entities) {
            entity.setCategory(category);
            entity.setParentId(parentId);
        }
        attachRepository.saveAll(entities);
    }

    public void PutConnectionAttaches(String category, Long parentId, List<String> attaches) {
        List<AttachEntity> entities = attachRepository.findAllByCategoryAndParentId(category, parentId);

        List<AttachEntity> deleteEntities = new ArrayList<>();

    }
}
