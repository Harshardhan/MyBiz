package com.example.demo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.enums.BillingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="billing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Billing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "account_id", nullable = false, length = 20)
	private String accountId;
	
	@NotNull
	@Size(min = 3, max = 50)
	@Column(nullable = false)
	private String description;
	
	@Column(name = "billing_date")
	private LocalDate billingDate;
	
	@Column(name = "billing_cycle", length = 20)
	private String billingCycle;
	@Enumerated(EnumType.STRING)
	private BillingStatus status;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {
	    createdAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
	    updatedAt = LocalDateTime.now();
	}

}
