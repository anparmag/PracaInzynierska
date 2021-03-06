package aviationModelling.service;

import aviationModelling.dto.EventDTO;
import aviationModelling.dto.FlightDTO;
import aviationModelling.dto.RoundDTO;
import aviationModelling.dto.VaultResponseDTO;
import aviationModelling.entity.EventRound;
import aviationModelling.entity.Flight;
import aviationModelling.entity.Round;
import aviationModelling.exception.CustomNotFoundException;
import aviationModelling.exception.CustomResponse;
import aviationModelling.mapper.EventMapper;
import aviationModelling.mapper.FlightMapper;
import aviationModelling.mapper.RoundMapper;
import aviationModelling.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoundServiceImpl implements RoundService {

    private RoundRepository roundRepository;
    private EventRoundRepository eventRoundRepository;
    private VaultService vaultService;
    private EventRepository eventRepository;
    private PilotRepository pilotRepository;
    private FlightRepository flightRepository;
    private FlightService flightService;

//    public RoundServiceImpl(RoundRepository roundRepository, EventRoundRepository eventRoundRepository, VaultService vaultService) {
//        this.roundRepository = roundRepository;
//        this.eventRoundRepository = eventRoundRepository;
//        this.vaultService = vaultService;
//    }


    public RoundServiceImpl(RoundRepository roundRepository, EventRoundRepository eventRoundRepository, VaultService vaultService, EventRepository eventRepository, PilotRepository pilotRepository, FlightRepository flightRepository, FlightService flightService) {
        this.roundRepository = roundRepository;
        this.eventRoundRepository = eventRoundRepository;
        this.vaultService = vaultService;
        this.eventRepository = eventRepository;
        this.pilotRepository = pilotRepository;
        this.flightRepository = flightRepository;
        this.flightService = flightService;
    }

    @Override
    public List<RoundDTO> findAll(Integer eventId) {
        List<EventRound> roundList = roundRepository.findAll(eventId);
        if (roundList.size() == 0) {
            return new ArrayList<>();
        }
        return RoundMapper.MAPPER.toRoundDTOList(roundList);
    }


    @Override
    public ResponseEntity<RoundDTO> createRound(Integer roundNum, Integer eventId, Integer numberOfGroups) {
        if (roundRepository.findByRoundNum(roundNum) == null) {
            Round round = new Round();
            round.setRoundNum(roundNum);
            roundRepository.save(round);
        }
        EventRound eventRound = eventRoundRepository.findByRoundNumAndEventId(roundNum, eventId);
        if (eventRound == null) {
            eventRound = new EventRound();
            eventRound.setRoundNum(roundNum);
            eventRound.setEventId(eventId);
            eventRound.setNumberOfGroups(numberOfGroups);
            eventRound.setSynchronized(false);
            eventRoundRepository.save(eventRound);
        }
        return new ResponseEntity<>(RoundMapper.MAPPER.toRoundDTO(eventRound), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RoundDTO> createRound(Integer roundNum, Integer eventId, Integer numberOfGroups, boolean isCancelled) {
        if (roundRepository.findByRoundNum(roundNum) == null) {
            Round round = new Round();
            round.setRoundNum(roundNum);
            roundRepository.save(round);
        }
        EventRound eventRound = eventRoundRepository.findByRoundNumAndEventId(roundNum, eventId);
        if (eventRound == null) {
            eventRound = new EventRound();
            eventRound.setRoundNum(roundNum);
            eventRound.setEventId(eventId);
            eventRound.setNumberOfGroups(numberOfGroups);
            eventRound.setCancelled(isCancelled);
            eventRound.setSynchronized(false);
            eventRoundRepository.save(eventRound);
        }
        return new ResponseEntity<>(RoundMapper.MAPPER.toRoundDTO(eventRound), HttpStatus.CREATED);
    }
//
//    @Override
//    public ResponseEntity<RoundDTO> createRound(RoundDTO roundDTO) {
//        if (roundRepository.findByRoundNum(roundDTO.getRoundNum()) == null) {
//            Round round = new Round();
//            round.setRoundNum(roundDTO.getRoundNum());
//            roundRepository.save(round);
//        }
//        if (roundDTO.getNumberOfGroups() == null) roundDTO.setNumberOfGroups(1);
//        eventRoundRepository.save(RoundMapper.MAPPER.toEventRound(roundDTO));
//        return new ResponseEntity<>(roundDTO, HttpStatus.CREATED);
//    }

    @Override
    public ResponseEntity<CustomResponse> cancelRound(Integer roundNum, Integer eventId) {
        EventRound eventRound = roundRepository.findEventRound(roundNum, eventId);
        eventRound.setCancelled(true);
        eventRound.setSynchronized(false);
        eventRoundRepository.save(eventRound);
        return new ResponseEntity<>(new CustomResponse(HttpStatus.OK.value(),
                "Round " + roundNum + " cancelled."), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponse> uncancelRound(Integer roundNum, Integer eventId) {
        EventRound eventRound = roundRepository.findEventRound(roundNum, eventId);
        eventRound.setCancelled(false);
        eventRound.setSynchronized(false);
        eventRoundRepository.save(eventRound);
        return new ResponseEntity<>(new CustomResponse(HttpStatus.OK.value(),
                "Round " + roundNum + " uncancelled."), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomResponse> finishRound(Integer roundNum, Integer eventId) {
        EventRound eventRound = roundRepository.findEventRound(roundNum, eventId);
        eventRound.setFinished(true);
        eventRoundRepository.save(eventRound);
        return new ResponseEntity<>(new CustomResponse(HttpStatus.OK.value(),
                "Round " + roundNum + " finished."), HttpStatus.OK);
    }

    //    @Override
//    public ResponseEntity<RoundDTO> save(RoundDTO roundDTO) {
//        roundRepository.save(RoundMapper.MAPPER.toRound(roundDTO));
//        return new ResponseEntity<>(roundDTO, HttpStatus.CREATED);
//    }
//
    @Override
    public EventRound findEventRound(Integer roundNum, Integer eventId) {
        EventRound eventRound = roundRepository.findEventRound(roundNum, eventId);
        if (eventRound == null) {
            throw new CustomNotFoundException("Round " + roundNum + " not found");
        }
        return eventRound;
    }

    public void synchronizeEventRound(Integer roundNum, Integer eventId) {
        final EventRound eventRound = findEventRound(roundNum, eventId);
        eventRound.setSynchronized(true);
        eventRoundRepository.save(eventRound);
    }

    public void desynchronizeEventRound(Integer roundNum, Integer eventId) {
        final EventRound eventRound = findEventRound(roundNum, eventId);
        eventRound.setSynchronized(false);
        eventRoundRepository.save(eventRound);
    }


    @Override
    public List<FlightDTO> getRoundFlights(Integer roundNum, Integer eventId) {
//        check if event exists
        final EventDTO event = EventMapper.MAPPER.toEventDTO(eventRepository.findByEventId(eventId));

        if (event == null) {
            throw new CustomNotFoundException("Event " + eventId + " doesn't exist");

        }
//        check if round exists
        final Optional<RoundDTO> foundRound = event.getRounds()
                .stream()
                .filter(round -> round.getRoundNum().equals(roundNum))
                .findFirst();

        final RoundDTO roundDTO = foundRound.orElseThrow(() -> new CustomNotFoundException("Round " + roundNum + " not found"));

        return roundDTO.getFlights();

//        List<Flight> flightList = roundRepository.findRoundFlights(roundNum, eventId);
//        if (flightList.size() == 0) {
//            throw new CustomNotFoundException("Flights from round " + roundNum + " not found");
//        }
//        return FlightMapper.MAPPER.toFlightDTOList(flightList);
    }
//


    @Override
    public ResponseEntity<CustomResponse> updateLocalScore(Integer roundNum, Integer eventId) {
        EventRound eventRound = roundRepository.findEventRound(roundNum, eventId);
        if (eventRound.getFlights() == null) {
            return new ResponseEntity<>(new CustomResponse(200, "ok"), HttpStatus.OK);
        }

        List<Flight> validFlights = getValidFlights(eventRound);

        if (validFlights.size() != 0) {
//            wyodrebnij ilosc grup, w mapowaniu domyslnie grupa="", wiec przy braku podzialu groups.size() = 1
            Set<String> groups = getGroupNames(validFlights);

            if (eventRound.getNumberOfGroups() == 1 && groups.size() != 1) {
                eventRound.setNumberOfGroups(groups.size());
            }

            groups.forEach(group -> {
                Float bestTime = findBestTime(validFlights, group);

                countFlightScore(eventRound, group, bestTime);

                eventRoundRepository.save(eventRound);
            });


        } else {
            return new ResponseEntity<>(new CustomResponse(200, "ok"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new CustomResponse(HttpStatus.OK.value(),
                "Scores in round " + roundNum + " updated."), HttpStatus.OK);
    }

    private void countFlightScore(EventRound eventRound, String group, Float bestTime) {
        eventRound.getFlights().stream().filter(flight -> flight.getSeconds() != null && flight.getGroup().equals(group))
                .forEach(flight -> {
                    if (flight.getSeconds().equals(0F)) flight.setScore(0F);
                    else flight.setScore(bestTime / flight.getSeconds() * 1000);
                });
    }

    private Float findBestTime(List<Flight> validFlights, String group) {
        final List<Flight> flights = validFlights.stream()
                .filter(flight -> flight.getGroup().equals(group) && flight.getSeconds() > 0).collect(Collectors.toList());
        if (flights.size() > 0) {
            return flights.stream()
                    .min(Comparator.comparingDouble(Flight::getSeconds))
                    .get().getSeconds();
        } else {
            return 0F;
        }
    }

    private Set<String> getGroupNames(List<Flight> validFlights) {
        Set<String> groups = new HashSet<>();
        validFlights.forEach(flight -> groups.add(flight.getGroup()));
        return groups;
    }

    //        zwroc liste 'punktowych' lotow (odrzuc wszystkie z czasem 0 lub null)
    private List<Flight> getValidFlights(EventRound eventRound) {
        return eventRound.getFlights().stream()
                .filter(flight -> flight.getSeconds() != null)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<CustomResponse> updateAllRounds(Integer eventId) {
        List<Integer> roundNumbers = getRoundNumbers(eventId);
        List<Integer> cancelled = new ArrayList<>();
        for (Integer number : roundNumbers) {
            updateLocalScore(number, eventId);
        }
        return new ResponseEntity<>(new CustomResponse(HttpStatus.OK.value(),
                roundNumbers.size() - cancelled.size() +
                        " rounds score updated, " + cancelled.size() +
                        " rounds cancelled because of invalid data"), HttpStatus.OK);
    }

    @Override
    public List<Integer> getRoundNumbers(Integer eventId) {
        return roundRepository.getValidRoundNumbers(eventId);
    }

    @Override
    public FlightDTO findBestRoundFlight(Integer roundNum, Integer eventId) {
        final List<Flight> bestRoundFlight = roundRepository.findBestRoundFlight(roundNum, eventId);
        return FlightMapper.MAPPER.toFlightDTO(bestRoundFlight.stream().findFirst().orElse(null));
    }

    @Override
    public ResponseEntity<VaultResponseDTO> updateEventRoundStatus(Integer roundNum, Integer eventId) {
        final EventRound eventRound = findEventRound(roundNum, eventId);
        if (eventRound.getFlights().size() == 0) {
            return new ResponseEntity<>(new VaultResponseDTO(400, "1000", "This round has no flights"), HttpStatus.BAD_REQUEST);
        }
        final Integer highestValidRoundNumber = flightService.findHighestValidRoundNumber(eventId);
        if (roundNum > (highestValidRoundNumber + 1)) {
            return new ResponseEntity<>(new VaultResponseDTO(400, "1000", "Cannot update a round that's more than one ahead of the last."), HttpStatus.BAD_REQUEST);
        }
        VaultResponseDTO response = vaultService.updateEventRoundStatus(roundNum, eventId, eventRound.isCancelled());
        if (response.getResponse_code().equals(0)) {
            throw new RuntimeException(response.getError_string());
        } else {
            synchronizeEventRound(roundNum, eventId);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> synchronizeAfterOffline(List<RoundDTO> dtos) {

        final List<EventRound> eventRounds = RoundMapper.MAPPER.toEventRoundList(dtos);
        createEventRoundsIfNotExist(eventRounds);
        saveFlightsToDb(dtos);
//        setFinishedFlag(eventRounds);
//        setCancelledFlag(eventRounds);
//        setSynchronizedFlag(eventRounds);
        updateRoundsFlags(eventRounds);

        return new ResponseEntity<>(new CustomResponse(HttpStatus.OK.value(),
                "Event synchronized in local db."), HttpStatus.OK);
    }

    private void updateRoundsFlags(List<EventRound> eventRounds) {
        eventRounds.forEach(round -> updateRound(round));
    }

    private void updateRound(EventRound round) {
        final EventRound entity = roundRepository.findEventRound(round.getRoundNum(), round.getEventId());
        entity.setCancelled(round.isCancelled());
        entity.setFinished(round.isFinished());
        entity.setSynchronized(round.isSynchronized());
        eventRoundRepository.save(entity);
    }

//
//    private void setCancelledFlag(List<EventRound> eventRounds) {
//        eventRounds.stream()
//                .forEach(eventRound -> setCancelledFlag(eventRound.getRoundNum(), eventRound.getEventId(), eventRound.isCancelled()));
//    }


//    private void setFinishedFlag(List<EventRound> eventRounds) {
//        eventRounds.stream().filter(eventRound -> eventRound.isFinished())
//                .forEach(eventRound -> finishRound(eventRound.getRoundNum(), eventRound.getEventId()));
//    }

    @Override
    public ResponseEntity<?> sendFlightsToVaultAfterOffline(Integer eventId) {
        final List<EventRound> eventRounds = roundRepository.findAll(eventId);
        final List<RoundDTO> dtos = RoundMapper.MAPPER.toRoundDTOList(eventRounds);


        final Stream<RoundDTO> unsynchronizedRounds = dtos.stream().filter(roundDTO -> !roundDTO.isSynchronized());


        final List<List<FlightDTO>> listOfListOfUnsynchronizedFlights = unsynchronizedRounds.map(roundDTO -> changeRoundDTOIntoListOfUnsynchronizedFlights(roundDTO))
                .collect(Collectors.toList());

        final List<FlightDTO> listOfUnsynchronizedFlights = listOfListOfUnsynchronizedFlights.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        listOfUnsynchronizedFlights.forEach(dto -> flightService.postScore(dto.getRoundNum(), dto.getPilotId(), dto.getEventId()));

        updateNotUpdatedRounds(dtos);

        return new ResponseEntity<>(new CustomResponse(HttpStatus.OK.value(),
                "Event synchronized on Vault"), HttpStatus.OK);
    }

    ;


    private List<FlightDTO> changeRoundDTOIntoListOfUnsynchronizedFlights(RoundDTO roundDTO) {
        final List<FlightDTO> flights = roundDTO.getFlights();
        return flights.stream().filter(flightDTO -> !flightDTO.isSynchronized()).collect(Collectors.toList());

    }

    private void updateNotUpdatedRounds(List<RoundDTO> dtos) {
        dtos.stream().filter(eventRound -> !eventRound.isSynchronized())
                .forEach(eventRound -> updateEventRoundStatus(eventRound.getRoundNum(), eventRound.getEventId()));
    }

    private void saveFlightsToDb(List<RoundDTO> roundDTOS) {

        final Stream<List<FlightDTO>> listStream = roundDTOS.stream().map(roundDTO -> roundDTO.getFlights());
        final Stream<FlightDTO> flightDTOStream = listStream.flatMap(Collection::stream);
        final List<FlightDTO> unsynchronizedFlights = flightDTOStream.filter(flightDTO -> !flightDTO.isSynchronized()).collect(Collectors.toList());
        unsynchronizedFlights.forEach(flightDTO -> saveFlight(flightDTO));
    }

    private void saveFlight(FlightDTO flightDTO) {
        final Flight flight = createFlightEntity(flightDTO);
        flightRepository.save(flight);
    }

    private Flight createFlightEntity(FlightDTO flightDTO) {
        final Integer eventPilotId = pilotRepository.getEventPilotId(flightDTO.getPilotId(), flightDTO.getEventId());
        final Integer eventRoundId = roundRepository.getEventRoundId(flightDTO.getRoundNum(), flightDTO.getEventId());
        final Flight flight = FlightMapper.MAPPER.toFlight(flightDTO);
        flight.setFlightId(new Flight.FlightId(eventPilotId, eventRoundId));
        return flight;
    }

    private void createEventRoundsIfNotExist(List<EventRound> eventRounds) {
        eventRounds.forEach(eventRound -> createEventRoundIfNotExists(eventRound));
    }

    private void createEventRoundIfNotExists(EventRound eventRound) {
        final EventRound foundRound = roundRepository.findEventRound(eventRound.getRoundNum(), eventRound.getEventId());
        if (foundRound == null) {
            createRound(eventRound.getRoundNum(), eventRound.getEventId(), eventRound.getNumberOfGroups());
        }
    }

    @Override
    public List<RoundDTO> getDtos() {
        final EventRound eventRound1 = roundRepository.findEventRound(10, 1834);
        final EventRound eventRound2 = roundRepository.findEventRound(11, 1834);
        final List<EventRound> eventRoundsMock = new ArrayList<>(Arrays.asList(eventRound1, eventRound2));
        return RoundMapper.MAPPER.toRoundDTOList(eventRoundsMock);
    }


}
