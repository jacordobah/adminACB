package acbManagement.controller;

import acbManagement.model.guide.DTO.GuideDetailResponse;
import acbManagement.model.guide.Guide;
import acbManagement.model.guide.GuideRegistrationDto;
import acbManagement.model.user.DTO.GuideRequest;
import acbManagement.service.GuideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guides")
@RequiredArgsConstructor
public class GuideController {
    private final GuideService guideService;

    @PostMapping
    public ResponseEntity<Guide> createGuide(@RequestBody @Valid GuideRequest request) {
        Guide newGuide = guideService.registerCompleteGuide(request.user(), request.guide());
        return new ResponseEntity<>(newGuide, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<GuideDetailResponse>> getAllGuides(Pageable pageable) {
        return ResponseEntity.ok(guideService.getAllGuidesPaged(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideDetailResponse> getGuideById(@PathVariable Long id) {
        return ResponseEntity.ok(guideService.getGuideById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuideDetailResponse> updateGuide(@PathVariable Long id, @RequestBody GuideRegistrationDto dto) {
        return ResponseEntity.ok(guideService.updateGuide(id, dto));
    }
}
