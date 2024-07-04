package watertank.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import watertank.dtos.MeasurementDTO;
import watertank.exceptions.MeasurementException;
import watertank.services.MeasurementService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(MeasurementController.BASE_URI)
public class MeasurementController {

    public static final String BASE_URI = "/api/measurements";

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MeasurementDTO addMeasurement(@Validated @RequestBody final MeasurementDTO measurementDTO, @RequestHeader("device-id") final String deviceId) {
        return Optional.ofNullable(deviceId)
                .map(id -> measurementService.saveMeasurement(measurementDTO, deviceId))
                .orElseThrow(() -> MeasurementException.MISSING_DEVICE_ID);
    }


    @GetMapping
    public List<MeasurementDTO> getAllMeasurements(@RequestParam(value = "last", required = false) final Long numberOfLatestRecords,
                                                   @RequestParam(value = "dailyMedians", required = false) final Boolean daysMedian) {
        if (daysMedian == null || !daysMedian) {
            return numberOfLatestRecords != null
                    ? measurementService.findLatestRecords(numberOfLatestRecords)
                    : measurementService.findAllMeasurements();
        } else {
            return measurementService.getDailyMedians();
        }
    }

    @GetMapping("/{id}")
    public MeasurementDTO getMeasurementById(@PathVariable Long id) {
        return measurementService.findById(id);
    }
}