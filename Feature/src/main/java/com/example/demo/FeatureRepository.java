package com.example.demo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.enums.FeatureStatus;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {

    // Find features by status
    List<Feature> findByStatus(FeatureStatus status);

    // Find features by isActive flag
    List<Feature> findByIsActive(boolean isActive);

    // Find features by name (exact match)
    List<Feature> findByName(String name);

    // Find features where name contains a keyword (case insensitive)
    List<Feature> findByNameContainingIgnoreCase(String keyword);

    // Find features by status and isActive
    List<Feature> findByStatusAndIsActive(FeatureStatus status, boolean isActive);

    // Find features with description not null
    List<Feature> findByDescriptionIsNotNull();
}
