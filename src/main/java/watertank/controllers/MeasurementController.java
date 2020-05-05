package watertank.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import watertank.dtos.MeasurementDTO;
import watertank.services.MeasurementService;

import java.util.Set;

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
    public MeasurementDTO addMeasurement(@RequestBody MeasurementDTO measurementDTO) {
        return measurementService.saveMeasurement(measurementDTO);
    }


    @GetMapping
    public Set<MeasurementDTO> getAllMeasurements(@RequestParam(required = false) final Long numberOfLatestRecords) {
        return numberOfLatestRecords == null
                ? measurementService.findAllMeasurements()
                : measurementService.findLatestXRecords(numberOfLatestRecords);
    }


    @GetMapping("/{id}")
    public MeasurementDTO getMeasurementById(@PathVariable String id) {
        return measurementService.findById(Long.valueOf(id));
    }
}