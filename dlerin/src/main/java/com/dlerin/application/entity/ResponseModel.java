package com.dlerin.application.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel {

	private String error;
	private String msg;
	public ResponseModel() {

	}
	public ResponseModel(String msg) {
		this.msg = msg;
	}
	public ResponseModel(String message, String error) {
		this.error = error;
		this.msg = message;
	}

}

