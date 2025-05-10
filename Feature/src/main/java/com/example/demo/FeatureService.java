package com.example.demo;

import java.util.List;

import com.example.demo.exceptions.FeatureAlreadyExistsException;
import com.example.demo.exceptions.FeatureNotFoundException;
import com.example.demo.exceptions.InValidFeatureException;

public interface FeatureService {

	Feature addFeature(Feature feature)throws InValidFeatureException, FeatureAlreadyExistsException;
	
	Feature updateFeature(Long id, Feature featureDetails)throws FeatureAlreadyExistsException, InValidFeatureException;
	
	void deleteFeature(Long id)throws FeatureNotFoundException;
	
	Feature getFeatureById(Long id) throws FeatureNotFoundException;

	List<Feature> getAllFeatures();

}
