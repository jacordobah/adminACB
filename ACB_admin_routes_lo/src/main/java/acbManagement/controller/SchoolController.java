package acbManagement.controller;

import acbManagement.model.school.DTO.SchoolRequest;
import acbManagement.model.school.DTO.SchoolResponse;
import acbManagement.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<List<SchoolResponse>> list(
            @RequestParam(required = false) String zone) {
        return ResponseEntity.ok(schoolService.findAll(zone));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SchoolResponse> create(@RequestBody @Valid SchoolRequest request) {
        return new ResponseEntity<>(schoolService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid SchoolRequest request) {
        return ResponseEntity.ok(schoolService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        schoolService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
