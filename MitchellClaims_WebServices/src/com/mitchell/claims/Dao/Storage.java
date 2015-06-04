package com.mitchell.claims.Dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.mitchell.claims.Dao.ClaimDao;
import com.mitchell.claims.exceptions.DataNotFoundException;
import com.mitchell.claims.model.CauseOfLossCode;
import com.mitchell.claims.model.LossInfoType;
import com.mitchell.claims.model.MitchellClaimType;
import com.mitchell.claims.model.VehicleInfoType;
import com.mitchell.claims.model.VehicleListType;
import com.sun.jersey.api.NotFoundException;

public class Storage implements ClaimDao {

	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	static Map<String, MitchellClaimType> store;

	public Storage() {
		try {
			// Dummy Data
			store = new HashMap<String, MitchellClaimType>();
			MitchellClaimType claim = new MitchellClaimType();
			LossInfoType loss = new LossInfoType();
			VehicleInfoType info = new VehicleInfoType();
			info.setDamageDescription("HeadLight Damaged.");
			info.setEngineDescription("VX2");
			info.setExteriorColor("Blue");
			info.setLicPlate("JHGDF");
			info.setLicPlateExpDate(ft.parse("2015-12-30"));
			info.setLicPlateState("CA");
			info.setMakeDescription("Ferari");
			info.setMileage(20145);
			info.setModelDescription("GX2");
			info.setModelYear(2012);
			info.setVin("1FGHJ");
			List<VehicleInfoType> vehicles = new ArrayList<VehicleInfoType>();
			vehicles.add(info);
			claim.setVehicles(new VehicleListType());
			claim.getVehicles().setVehicleDetails(vehicles);

			loss.setCauseOfLoss(CauseOfLossCode.FIRE);
			loss.setLossDescription("Hit the tree");
			loss.setReportedDate(ft.parse("2014-3-25"));

			claim.setAssignedAdjusterID((long) 15487);
			claim.setClaimantFirstName("Paras");
			claim.setClaimantLastName("Patel");
			claim.setClaimNumber("abcd");
			claim.setLossDate(ft.parse("2014-6-18"));
			claim.setLossInfo(loss);

			store.put(claim.getClaimNumber(), claim);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean insertClaim(MitchellClaimType claim) {
		if (claim != null) {
			store.put(claim.getClaimNumber(), claim);
			return true;
		}
		return false;
	}

	@Override
	public MitchellClaimType getClaim(String claimNumber) {

		if (claimNumber != null && !claimNumber.equals("")) {
			MitchellClaimType claim = store.get(claimNumber);
			if (claim == null) {
				throw new DataNotFoundException("ClaimNumber " + claimNumber
						+ " Not Found");
			}
			return claim;
		}
		return null;
	}

	@Override
	public boolean updateClaim(MitchellClaimType claim) {

		if (claim != null) {

			MitchellClaimType cl = store.get(claim.getClaimNumber());
			if (cl != null) {

				cl.setAssignedAdjusterID(claim.getAssignedAdjusterID());
				VehicleInfoType type = cl.getVehicles().getVehicleDetails()
						.get(0);
				VehicleInfoType clv = claim.getVehicles().getVehicleDetails()
						.get(0);
				type.setExteriorColor(clv.getExteriorColor());
				type.setVin(clv.getVin());
				type.setLicPlateExpDate(clv.getLicPlateExpDate());

				return true;

			}
			throw new DataNotFoundException("Claim is not available");

		}
		return false;
	}

	@Override
	public void deleteClaim(String claimNumber) {
		if (claimNumber != null && !claimNumber.equals("")) {
			MitchellClaimType claim = store.get(claimNumber);
			if (claim == null) {
				throw new DataNotFoundException("claim is not available");
			}
			store.remove(claimNumber);

		}

	}

	@Override
	public List<MitchellClaimType> getList(String startDate, String endDate) {
		List<MitchellClaimType> list = new ArrayList<MitchellClaimType>();

		try {
			Date start = ft.parse(startDate);
			Date end = ft.parse(endDate);

			for (Entry<String, MitchellClaimType> m : store.entrySet()) {
				Date curr = m.getValue().getLossDate();
				if (curr.after(start) && curr.before(end)) {
					list.add(m.getValue());

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public MitchellClaimType getVehicleInfo(String claimNumber, String licPlate) {
		if (claimNumber != null && !claimNumber.equals("")) {

			for (Entry<String, MitchellClaimType> m : store.entrySet()) {
				String c_number = m.getValue().getClaimNumber();
				if (claimNumber.equals(c_number)) {
					for (VehicleInfoType vtype : m.getValue().getVehicles()
							.getVehicleDetails()) {
						if (vtype.getLicPlate().equals(licPlate)) {
							return m.getValue();
						}
					}

				}
			}
		}
		return null;
	}

}
