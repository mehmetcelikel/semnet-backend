package com.boun.swe.semnet.commons.type;

import java.text.MessageFormat;

public enum ErrorCode {

	SUCCESS("Success", "0"),
	
	VALIDATION_ERROR("Validation has failed", "1000"),
	
	USER_NOT_FOUND("User not found", "100"),
	DUPLICATE_USER("There is already a user with given username", "101"),
	OPERATION_NOT_ALLOWED("Operation is not allowed for not authenticated users", "102"),
	DUPLICATE_FRIEND("Duplicate friend request", "103"),
	CANNOT_ADD_SAME_USER_AS_FRIEND("You cannot add same user as friend", "104"),
	FRIEND_NOT_FOUND("Friend not found", "105"),
	
	INVALID_INPUT("Invalid input-> {0}", "106"),

	CONTENT_NOT_FOUND("Content not found", "107"),
	CONTENT_DOES_NOT_BELONG_TO_YOU("Content does not belong to you", "108"),
	COMMENT_DOES_NOT_BELONG_TO_YOU("Comment does not belong to you", "109"),
	
	CORRUPTED_IMAGE("Image is corrupted", "130"),
	
	INTERNAL_SERVER_ERROR("Internal Server Error", "900")
	;

	private String message;
	private String code;
	
	private ErrorCode(String message, String errorCode) {
		this.message = message;
		this.code = errorCode;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getFormattedCode() {
		return "SNET_"+code;
	}
	
	public String format(Object... args){
		return MessageFormat.format(message, args);
	}
}
