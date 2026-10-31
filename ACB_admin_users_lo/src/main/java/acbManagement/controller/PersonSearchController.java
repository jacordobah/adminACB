package acbManagement.controller;
import acbManagement.service.PersonSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class PersonSearchController {

    private final PersonSearchService personSearchService;

    // Ruta de búsqueda universal por documento: /api/v1/search?type=CC&number=12345
    @GetMapping
    public ResponseEntity<Object> searchByDocument(
            @RequestParam String type,
            @RequestParam String number) {
        return ResponseEntity.ok(personSearchService.searchByDocument(type, number));
    }
}
