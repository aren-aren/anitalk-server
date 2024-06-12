package com.anitalk.app.attach;

import com.anitalk.app.attach.dto.AttachRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AttachManager {
    private static AttachUploader attachUploader;
    private static AttachRepository attachRepository;

    public AttachRecord uploadAttach(Long boardId, MultipartFile file) throws Exception {
        if(file == null || file.isEmpty()){
            throw new Exception("파일이 비어있습니다.");
        }
        String filename = UUID.randomUUID().toString();
        String originName = file.getOriginalFilename();
        String url = attachUploader.uploadAttach(filename, file);

        AttachEntity entity = AttachEntity.builder()
                .boardId(boardId)
                .name(filename)
                .originName(originName)
                .build();

        entity = attachRepository.save(entity);
        return new AttachRecord(
                entity.getId(),
                entity.getBoardId(),
                entity.getName(),
                entity.getOriginName(),
                url
        );
    }

    public void deleteAttach(Long id) throws Exception {
        AttachEntity entity = attachRepository.findById(id).orElseThrow();
        attachUploader.deleteAttach(entity.getName());
    }
}
