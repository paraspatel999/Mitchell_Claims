package com.mitchell.claims.Dao;


import java.util.List;




import com.mitchell.claims.model.MitchellClaimType;

public interface ClaimDao {

	public boolean insertClaim(MitchellClaimType claim);
	public MitchellClaimType getClaim(String claimNumber);
	public boolean updateClaim(MitchellClaimType claim);
	public boolean deleteClaim(String claimNumber);
	public List<MitchellClaimType> getList(String startDate,String endDate);
	public MitchellClaimType getVehicleInfo(String claimNumber, String licPlate);
}
