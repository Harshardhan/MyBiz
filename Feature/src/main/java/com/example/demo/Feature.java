package com.example.demo;

import java.time.LocalDateTime;

import com.example.demo.enums.FeatureStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feature {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FeatureStatus status;
	
	@Column(nullable = false)
	@NotNull
	@Size(min = 3 , max =50)
	private String name;
	
	private String description;
	
	private String remarks;
	
	private boolean isActive;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

    @Version
    private Long version;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();  // Set createdAt when a new feature is created
        updatedAt = createdAt;  // Set updatedAt the same as createdAt initially
    }



}
