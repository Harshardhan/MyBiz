package com.example.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exceptions.FeatureAlreadyExistsException;
import com.example.demo.exceptions.FeatureNotFoundException;
import com.example.demo.exceptions.InValidFeatureException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/features")
public class FeatureController {
	private static final Logger logger = LoggerFactory.getLogger(FeatureController.class);

	private final FeatureService featureService;

	public FeatureController(FeatureService featureService) {
		this.featureService = featureService;
	}

	@PostMapping
	public ResponseEntity<Feature> addFeature(@Valid @RequestBody Feature feature)
			throws InValidFeatureException, FeatureAlreadyExistsException {
		Feature addFeature = featureService.addFeature(feature);
		logger.info("Feature added successfully -featureId: {}", addFeature);
		return new ResponseEntity<>(addFeature, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Feature> updateFeature(@PathVariable("id") Long id,
			@Valid @RequestBody Feature featureDetails) throws FeatureAlreadyExistsException, InValidFeatureException {
		Feature updatedFeature = featureService.updateFeature(id, featureDetails);
		logger.info("Successfully updated feature with feature id {}", id);
		return ResponseEntity.ok(updatedFeature);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFeature(@PathVariable Long id) throws FeatureNotFoundException {
		featureService.deleteFeature(id);
		logger.info("Feature with ID{} deleted successfully", id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Feature> getFeatureById(@PathVariable Long id) throws FeatureNotFoundException {
		Feature getFeatureById = featureService.getFeatureById(id);
		logger.info("Successfully retreived feature with feature id{}", id);
		return new ResponseEntity<>(getFeatureById, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Feature>> getAllFeatures() {
		List<Feature> getAllFeatures = featureService.getAllFeatures();
		if (getAllFeatures.isEmpty()) {
			logger.warn("No features found in the system.");
			return ResponseEntity.noContent().build();
		}
		logger.info("Successfully retreived all features{}", getAllFeatures);
		return ResponseEntity.ok(getAllFeatures);

	}
}