package com.dlerin.application.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dler_invoice_details")
public class DlerInvoiceDetails {
	
	@Id
	@Column(name="generated_invoice_id")
	private String generatedInvoiceId;
	@Column(name="order_id")
	private String orderId;
	@Column(name="total_amount")
	private int totalAmount;
	
	@CreationTimestamp
	@Column(name="invoice_date")
	private String invoiceDate;
	@Column(name="invoice_to")
	private String inviceTo;
	@Column(name="updated_by")
	private String updateBy;
	
	@UpdateTimestamp
	@Column(name="updated_date")
	private String updatedDate;
	
	@PrePersist
	private void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		String year = String.valueOf(now.getYear());
		String month = String.format("%02d", now.getMonthValue());
		String day = String.format("%02d", now.getDayOfMonth());
		String hour = String.format("%02d", now.getHour());
		String minute = String.format("%02d", now.getMinute());
		String second = String.format("%02d", now.getSecond());
		String millis = String.format("%03d", now.getNano() / 1000000).substring(0, 2);
		this.generatedInvoiceId= "INV" + year + month + day + hour + minute + second + millis;
	}

}
