package com.mitchell.claims.services;


import java.util.ArrayList;
import java.util.List;







import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import com.mitchell.claims.Dao.ClaimDao;

import com.mitchell.claims.model.MitchellClaimType;
import com.mitchell.claims.model.ObjectFactory;
import com.mitchell.claims.Dao.Storage;


@Path("/mitchellclaims")
public class MichellClaimService {

	
	ObjectFactory ob= new ObjectFactory();
	ClaimDao dao = new Storage();
	
	@GET
	@Path("/read_claim/{claimNumber}")
	@Produces(MediaType.APPLICATION_XML)	
	public JAXBElement<MitchellClaimType> getClaim(@PathParam("claimNumber")String claimNumber){
		
		return ob.createMitchellClaim(dao.getClaim(claimNumber));
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public MitchellClaimType getVehicleInfo(@QueryParam("claimNumber")String claimNumber,@QueryParam("licPlate")String licPlate){
		return dao.getVehicleInfo(claimNumber, licPlate);
	}
	
	@PUT
	@Path("/update_claim")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateClaim(MitchellClaimType claim){
		
		boolean flag=dao.updateClaim(claim);
		if(flag == true){
			return "Successfully updated";
		}
		return "Internal error.Try again";
		 		
	}
	
	@GET
	@Path("/{startDate}/{endDate}")
	@Produces(MediaType.APPLICATION_XML)
	public List<JAXBElement<MitchellClaimType>> getListOfClaims(@PathParam("startDate")String startDate,@PathParam("endDate")String endDate){
	
		List<JAXBElement<MitchellClaimType>> list = new ArrayList<JAXBElement<MitchellClaimType>>();	 
		for(MitchellClaimType  claim: dao.getList(startDate, endDate) ){
		list.add(ob.createMitchellClaim(claim));
		}
		return list;
	}
	
	
	@POST
	@Path("/create_claim")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String createClaim(MitchellClaimType claim){	
		
		boolean flag=dao.insertClaim(claim);
		if(flag == true){
			return "Successfully inserted data";
		}
		return "Internal error. Try Again";
	}
	
	@DELETE
	@Path("/delete_claim/{ClaimNumber}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteClaim(@PathParam("ClaimNumber")String claimNumber){
		boolean flag = dao.deleteClaim(claimNumber);
		if(flag == true){
			return "successfully deleted";
		}
		return "Internal error";
	}
		


}


