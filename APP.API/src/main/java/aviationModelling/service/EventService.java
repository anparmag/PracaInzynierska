package aviationModelling.service;

import aviationModelling.dto.EventDTO;
import aviationModelling.entity.Event;
import aviationModelling.exception.CustomResponse;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface EventService {
    EventDTO getEvent(int id);
////    ResponseEntity<String> save(Event event);
    ResponseEntity<CustomResponse> updateAllTotalScores(int eventId);
    ResponseEntity<CustomResponse> delete(int eventId);
    ResponseEntity<CustomResponse> initializeDbWithDataFromVault(int eventId);
    

}
