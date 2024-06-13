package com.anitalk.app.attach;

import com.anitalk.app.attach.dto.AttachRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attaches")
@RequiredArgsConstructor
public class AttachController {
    private final AttachManager attachManager;

    @PostMapping("/{category}")
    public ResponseEntity<AttachRecord> addAttach(@PathVariable String category, @RequestBody MultipartFile attach) throws Exception {
        AttachRecord attachRecord = attachManager.uploadAttach(category, attach);
        return ResponseEntity.ok(attachRecord);
    }

    @PostMapping("/{category}/parent/{parentId}")
    public ResponseEntity<AttachRecord> addAttachWithParent(
            @PathVariable String category,
            @PathVariable Long parentId,
            @RequestBody MultipartFile attach
    ) throws Exception {
        AttachRecord attachRecord = attachManager.uploadAttach(category, parentId, attach);
        return ResponseEntity.ok(attachRecord);
    }
}
