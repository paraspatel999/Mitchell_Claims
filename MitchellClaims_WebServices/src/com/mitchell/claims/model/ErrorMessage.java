package com.mitchell.claims.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {

	private String errormessage;
   public ErrorMessage(){
	   
   }
	public ErrorMessage(String message) {
          errormessage=message;
	}

	public String getErrormessage() {
		return errormessage;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

}
