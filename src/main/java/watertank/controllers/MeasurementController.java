package watertank.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import watertank.dtos.MeasurementDTO;
import watertank.services.MeasurementService;

import java.util.List;

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
    public MeasurementDTO addMeasurement(@Validated @RequestBody MeasurementDTO measurementDTO) {
        return measurementService.saveMeasurement(measurementDTO);
    }


    @GetMapping
    public List<MeasurementDTO> getAllMeasurements(@RequestParam(value = "last", required = false) final Long numberOfLatestRecords,
                                                   @RequestParam(value = "dailyMedians", required = false) final Boolean daysMedian) {
        if (daysMedian == null || !daysMedian) {
            return numberOfLatestRecords != null
                    ? measurementService.findLatestXRecords(numberOfLatestRecords)
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