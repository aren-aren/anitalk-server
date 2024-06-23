package com.anitalk.app.domain.attach;

import com.anitalk.app.domain.attach.dto.AttachRecord;
import com.anitalk.app.utils.DateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component
@RequiredArgsConstructor
public class AttachManager {
    private final AttachUploader attachUploader;
    private final AttachRepository attachRepository;

    public AttachRecord uploadAttach(String category, MultipartFile file) throws Exception {
        return uploadAttach(category, null, file);
    }

    public AttachRecord uploadAttach(String category, Long parentId, MultipartFile file) throws Exception {
        if(file == null || file.isEmpty()){
            throw new Exception("파일이 비어있습니다.");
        }

        if(category.equals("animations")){
            AttachEntity existEntity = attachRepository.findByParentId(parentId);
            if(existEntity != null){
                throw new Exception("이미 존재하는 파일");
            }
        }

        String filename = UUID.randomUUID().toString();
        String originName = file.getOriginalFilename();
        String url = attachUploader.uploadAttach(filename, file);

        AttachEntity entity = AttachEntity.builder()
                .category(category)
                .name(filename)
                .parentId(parentId)
                .originName(originName)
                .uploadDate(DateManager.today())
                .build();

        entity = attachRepository.save(entity);
        return new AttachRecord(
                entity.getId(),
                entity.getCategory(),
                entity.getName(),
                entity.getParentId(),
                entity.getOriginName(),
                url
        );
    }

    public void deleteAttach(Long id) throws Exception {
        AttachEntity entity = attachRepository.findById(id).orElseThrow();
        deleteAttach(entity);
    }

    public void deleteAttach(AttachEntity entity) throws Exception {
        attachUploader.deleteAttach(entity.getName());
        attachRepository.delete(entity);
    }

    public void connectAttaches(String category, Long parentId, Set<String> attaches) {
        List<AttachEntity> entities = attachRepository.findAllByNameIn(attaches);

        if(entities.size() != attaches.size()) throw new NoSuchElementException("첨부파일 등록 실패");

        for (AttachEntity entity : entities) {
            entity.setCategory(category);
            entity.setParentId(parentId);
        }
        attachRepository.saveAll(entities);
    }

    public void PutConnectionAttaches(String category, Long parentId, Set<String> attaches) {
        List<AttachEntity> connectedEntities = attachRepository.findAllByCategoryAndParentIdOrNameIn(category, parentId, attaches);

        for (AttachEntity entity : connectedEntities) {
            if(!attaches.contains(entity.getName())){
                entity.setCategory(null);
                entity.setParentId(null);
                continue;
            }

            entity.setCategory(category);
            entity.setParentId(parentId);
        }

        attachRepository.saveAll(connectedEntities);
    }

    public void deleteNoParentAttach() throws Exception {
        List<AttachEntity> noParentAttaches = attachRepository.findAllToDeleteScheduling("boards", DateManager.getDate(-7));
        if (noParentAttaches.isEmpty()) return;

        for (AttachEntity attach : noParentAttaches) {
            deleteAttach(attach);
        }
    }
}
