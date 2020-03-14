package watertank.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import watertank.enums.Distance;
import watertank.repositories.MeasurementRepository;
import watertank.models.Measurement;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/api")
public class MeasurementController {
    @Autowired
    private MeasurementRepository repository;

    @PostMapping(path="/measurements", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Measurement addMeasurement(@Valid @RequestBody Measurement measurement) {
        // There is needed to calculate real water level distance based on spillway distance from ultrasonic sensor
        int measuredWaterLevelDistance = measurement.getWaterLevelDistance();
        int maxWaterLevel = Distance.ULTRASONIC_SENSOR.getDistance() - Distance.SPILLWAY.getDistance();

        int realWaterLevelDistance = measuredWaterLevelDistance - maxWaterLevel;



        measurement.setWaterLevelDistance(realWaterLevelDistance);

        return repository.save(measurement);
    }

    @GetMapping(path="/measurements")
    public @ResponseBody Iterable<Measurement> getAllMeasurements(@RequestParam(required = false) final Integer numberOfLatestRecords) {
        return numberOfLatestRecords == null
                ? repository.findAll()
                : repository.findLatestXRecords(PageRequest.of(0, numberOfLatestRecords));
    }
}