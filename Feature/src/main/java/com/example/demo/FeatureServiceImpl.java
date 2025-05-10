package com.example.demo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.FeatureAlreadyExistsException;
import com.example.demo.exceptions.FeatureNotFoundException;
import com.example.demo.exceptions.InValidFeatureException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FeatureServiceImpl implements FeatureService{

	private static final Logger logger = LoggerFactory.getLogger(FeatureServiceImpl.class);

	private final FeatureRepository featureRepository;
	
	public FeatureServiceImpl(FeatureRepository featureRepository) {
		this.featureRepository = featureRepository;
	}

    @Override
    @Transactional
    @CircuitBreaker(name = "FeatureService", fallbackMethod = "addFeatureCallBack")
    @RateLimiter(name = "FeatureService")
    @Retry(name = "FeatureService")
    public Feature addFeature(Feature feature) throws InValidFeatureException, FeatureAlreadyExistsException {
        logger.info("Attempting to add a new feature: {}", feature);

        if (feature == null) {
            logger.error("Feature object is null.");
            throw new InValidFeatureException("Feature is null.");
        }

        if (feature.getName() == null || feature.getName().trim().isEmpty()) {
            logger.error("Feature name is empty.");
            throw new InValidFeatureException("Feature name is empty.");
        }

        List<Feature> existingFeatures = featureRepository.findByName(feature.getName().trim());
        if (!existingFeatures.isEmpty()) {
            logger.warn("Feature with name '{}' already exists.", feature.getName());
            throw new FeatureAlreadyExistsException("Feature with name already exists: " + feature.getName());
        }

        Feature savedFeature = featureRepository.save(feature);
        logger.info("Feature saved successfully: {}", savedFeature);
        return savedFeature;
    }

    // Fallback method
    public Feature addFeatureCallBack(Feature feature, Throwable t) {
        logger.error("Fallback triggered for addFeature. Reason: {}", t.getMessage(), t);
        return null; // Or return a default Feature object if appropriate
    }
    @Override
    public Feature updateFeature(Long id, Feature featureDetails) throws FeatureAlreadyExistsException, InValidFeatureException {

        Feature updatedFeature = featureRepository.findById(id)
            .map(existingFeature -> {
                existingFeature.setDescription(featureDetails.getDescription());
                existingFeature.setRemarks(featureDetails.getRemarks());
                existingFeature.setStatus(featureDetails.getStatus());
                existingFeature.setActive(featureDetails.isActive());
                return featureRepository.save(existingFeature);
            })
            .orElseThrow(() -> new InValidFeatureException("Feature not found with id: " + id));

        logger.info("Feature with id '{}' updated successfully", id);
        return updatedFeature;
    }

    @Override
    public void deleteFeature(Long id) throws FeatureNotFoundException {
        Feature feature = featureRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Attempted to delete non-existent feature with id {}", id);
                return new FeatureNotFoundException("Feature with id " + id + " not found.");
            });

        featureRepository.deleteById(id);
        logger.info("Successfully deleted feature: {}", feature);
    }

    @Override
    @CircuitBreaker(name = "FeatureService", fallbackMethod = "getFeatureByIdCallBack")
    @RateLimiter(name = "FeatureService")
    @Retry(name = "FeatureService")
    public Feature getFeatureById(Long id) throws FeatureNotFoundException {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Attempted to get feature with id not found {}", id);
                    return new FeatureNotFoundException("Feature with id " + id + " not found.");
                });
        logger.info("Successfully retrieved feature: {}", id);
        return feature;
    }

    // Fallback method
    public Feature getFeatureByIdCallBack(Long id, Throwable t) {
        logger.warn("Fallback triggered for getFeatureById due to: {}", t.getMessage());
        return new Feature(); // Customize as needed
    }
    @Override
    @CircuitBreaker(name = "FeatureService", fallbackMethod = "getAllFeaturesFallback")
    @RateLimiter(name = "FeatureService")
    @Retry(name = "FeatureService")
    public List<Feature> getAllFeatures() {
        List<Feature> getAllFeatures = featureRepository.findAll();
        logger.info("Successfully retrieved all features.");
        return getAllFeatures;
    }

    // Fallback method
    public List<Feature> getAllFeaturesFallback(Throwable t) {
        logger.warn("Fallback triggered for getAllFeatures due to: {}", t.getMessage());
        return Collections.emptyList(); // or provide default data if needed
    }
}
