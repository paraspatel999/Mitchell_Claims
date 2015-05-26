package com.mitchell.claims.Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.mitchell.claims.DbConnection.DbConnection;
import com.mitchell.claims.model.CauseOfLossCode;
import com.mitchell.claims.model.LossInfoType;
import com.mitchell.claims.model.MitchellClaimType;
import com.mitchell.claims.model.StatusCode;
import com.mitchell.claims.model.VehicleInfoType;
import com.mitchell.claims.model.VehicleListType;

public class ClaimDaoImp implements ClaimDao {

	@Override
	public boolean insertClaim(MitchellClaimType claim) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DbConnection.getConnection();
			ps = con.prepareStatement("insert into Claims(ClaimNumber,ClaimFirstName,ClaimLastName,Status,LossDate,AssignedAdjusterId) values(?,?,?,?,?,?)");
			ps.setString(1, claim.getClaimNumber());
			ps.setString(2, claim.getClaimantFirstName());
			ps.setString(3, claim.getClaimantLastName());
			ps.setString(4, claim.getStatus().toString());

			ps.setDate(5, new Date(claim.getLossDate().getTime()));
			ps.setLong(6, claim.getAssignedAdjusterID());
			ps.execute();
			ps.close();
			int claim_id = 0;
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select Claim_id from Claims where ClaimNumber='"
							+ claim.getClaimNumber() + "'");
			while (rs.next()) {
				claim_id = rs.getInt(1);
			}
			LossInfoType loss = claim.getLossInfo();
			ps = con.prepareStatement("insert into LossInfoType(Claim_id,CauseOfLossCode,ReportedDate,LossDescription) values(?,?,?,?)");
			ps.setInt(1, claim_id);
			ps.setString(2, loss.getCauseOfLoss().toString());
			ps.setDate(3, new Date(loss.getReportedDate().getTime()));
			ps.setString(4, loss.getLossDescription());
			ps.execute();
			ps.close();
			ps = con.prepareStatement("insert into VehicleInfoType(Claim_id,ModelYear,MakeDescription,ModelDescription,EngineDescription,ExteriorColor,Vin,LicPlate,LicPlateState,DamageDescription,LicPlateExpDate,Mileage) values(?,?,?,?,?,?,?,?,?,?,?,?)");

			for (VehicleInfoType info : claim.getVehicles().getVehicleDetails()) {

				ps.setInt(1, claim_id);
				ps.setInt(2, info.getModelYear());
				ps.setString(3, info.getMakeDescription());
				ps.setString(4, info.getModelDescription());
				ps.setString(5, info.getEngineDescription());
				ps.setString(6, info.getExteriorColor());
				ps.setString(7, info.getVin());
				ps.setString(8, info.getLicPlate());
				ps.setString(9, info.getLicPlateState());
				ps.setString(10, info.getDamageDescription());
				ps.setDate(11, new Date(info.getLicPlateExpDate().getTime()));
				ps.setInt(12, info.getMileage());
				ps.execute();
				
				
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	@Override
	public MitchellClaimType getClaim(String claimNumber) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement ps = null;
		MitchellClaimType claim = new MitchellClaimType();
		List<VehicleInfoType> listOfVehicles = new ArrayList<VehicleInfoType>();
		VehicleListType vehicleType = new VehicleListType();
		try {

			con = DbConnection.getConnection();
			ps = con.prepareStatement("select c.*,l.* from Claims c,LossInfoType l where ClaimNumber='"
					+ claimNumber + "' and  c.claim_id = l.claim_id");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				LossInfoType lossInfo = new LossInfoType();
				lossInfo.setCauseOfLoss(CauseOfLossCode.fromValue(rs.getString(
						"CauseOfLossCode").trim()));
				lossInfo.setLossDescription(rs.getString("LossDescription"));
				lossInfo.setReportedDate(null);

				claim.setAssignedAdjusterID(rs.getLong("AssignedAdjusterId"));
				claim.setClaimantFirstName(rs.getString("ClaimFirstName"));
				claim.setClaimantLastName(rs.getString("ClaimLastName"));
				claim.setClaimNumber(rs.getString("ClaimNumber"));
				claim.setLossDate(null);
				claim.setLossInfo(lossInfo);
				claim.setStatus(StatusCode.fromValue(rs.getString("Status")));

				ps = con.prepareStatement("select * from VehicleInfoType where Claim_id="
						+ rs.getInt(1));
				ResultSet rs1 = ps.executeQuery();
				while (rs1.next()) {
					VehicleInfoType vehicleInfo = new VehicleInfoType();
					vehicleInfo.setDamageDescription(rs1
							.getString("DamageDescription"));
					vehicleInfo.setEngineDescription(rs1
							.getString("EngineDescription"));
					vehicleInfo
							.setExteriorColor(rs1.getString("ExteriorColor"));
					vehicleInfo.setLicPlate(rs1.getString("LicPlate"));

					vehicleInfo.setLicPlateExpDate(null);
					vehicleInfo
							.setLicPlateState(rs1.getString("LicPlateState"));
					vehicleInfo.setMakeDescription(rs1
							.getString("MakeDescription"));
					vehicleInfo.setMileage(rs1.getInt("Mileage"));
					vehicleInfo.setModelDescription(rs1
							.getString("ModelDescription"));
					vehicleInfo.setModelYear(rs1.getInt("ModelYear"));
					vehicleInfo.setVin(rs1.getString("Vin"));
					listOfVehicles.add(vehicleInfo);

				}
				claim.setVehicles(vehicleType);
				vehicleType.setVehicleDetails(listOfVehicles);
			}

			return claim;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

	@Override
	public boolean updateClaim(MitchellClaimType claim) {
		
		Connection con = null;
		PreparedStatement ps = null;
		try {

			con = DbConnection.getConnection();
			
			ps = con.prepareStatement("update Claims,VehicleInfoType set AssignedAdjusterID=?,Vin=?,ExteriorColor=?,LicPlateExpDate=? where Claims.ClaimNumber=? and Claims.Claim_id = VehicleInfoType.Claim_id");
			ps.setLong(1, claim.getAssignedAdjusterID());
			VehicleInfoType type = claim.getVehicles().getVehicleDetails()
					.get(0);
			ps.setString(2, type.getVin());
			ps.setString(3, type.getExteriorColor());
			ps.setDate(4, new Date(type.getLicPlateExpDate().getTime()));
			ps.setString(5, claim.getClaimNumber());

			ps.executeUpdate();
			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}

	@Override
	public boolean deleteClaim(String claimNumber) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DbConnection.getConnection();
			ps = con.prepareStatement("delete Claims,LossInfoType,VehicleInfoType from claims,LossInfoType,VehicleInfoType   where claims.ClaimNumber=? and claims.Claim_id = VehicleInfoType.Claim_id and claims.Claim_id = LossInfoType.Claim_id;");
			ps.setString(1, claimNumber);
			int a = ps.executeUpdate();
			if (a != 0) {
				return true;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public List<MitchellClaimType> getList(String startDate, String endDate) {


		List<MitchellClaimType> list = new ArrayList<MitchellClaimType>();
		Connection con = null;
		PreparedStatement ps = null;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = new Date(ft.parse(startDate).getTime());
			Date end = new Date(ft.parse(endDate).getTime());
			con = DbConnection.getConnection();
			ps = con.prepareStatement("select c.*,l.* from Claims c,LossInfoType l where c.Claim_id = l.Claim_id and LossDate between '"
					+ start + "' and '" + end + "'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				MitchellClaimType claim = new MitchellClaimType();
				VehicleListType vehicleType = new VehicleListType();
				LossInfoType lossInfo = new LossInfoType();
				lossInfo.setCauseOfLoss(CauseOfLossCode.fromValue(rs.getString(
						"CauseOfLossCode").trim()));
				lossInfo.setLossDescription(rs.getString("LossDescription"));
				lossInfo.setReportedDate(null);

				claim.setAssignedAdjusterID(rs.getLong("AssignedAdjusterId"));
				claim.setClaimantFirstName(rs.getString("ClaimFirstName"));
				claim.setClaimantLastName(rs.getString("ClaimLastName"));
				claim.setClaimNumber(rs.getString("ClaimNumber"));
				claim.setLossDate(null);
				claim.setLossInfo(lossInfo);
				claim.setStatus(StatusCode.fromValue(rs.getString("Status")));

				ps = con.prepareStatement("select * from VehicleInfoType where Claim_id="
						+ rs.getInt(1));
				ResultSet rs1 = ps.executeQuery();
				List<VehicleInfoType> listOfVehicles = new ArrayList<VehicleInfoType>();
				while (rs1.next()) {
					VehicleInfoType vehicleInfo = new VehicleInfoType();
					vehicleInfo.setDamageDescription(rs1
							.getString("DamageDescription"));
					vehicleInfo.setEngineDescription(rs1
							.getString("EngineDescription"));
					vehicleInfo
							.setExteriorColor(rs1.getString("ExteriorColor"));
					vehicleInfo.setLicPlate(rs1.getString("LicPlate"));

					vehicleInfo.setLicPlateExpDate(null);
					vehicleInfo
							.setLicPlateState(rs1.getString("LicPlateState"));
					vehicleInfo.setMakeDescription(rs1
							.getString("MakeDescription"));
					vehicleInfo.setMileage(rs1.getInt("Mileage"));
					vehicleInfo.setModelDescription(rs1
							.getString("ModelDescription"));
					vehicleInfo.setModelYear(rs1.getInt("ModelYear"));
					vehicleInfo.setVin(rs1.getString("Vin"));
					listOfVehicles.add(vehicleInfo);

				}
				claim.setVehicles(vehicleType);
				vehicleType.setVehicleDetails(listOfVehicles);
				list.add(claim);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	
	
	@Override
	public MitchellClaimType getVehicleInfo(String claimNumber, String licPlate) {
		Connection con = null;
		PreparedStatement ps =null;
		try{
			con = DbConnection.getConnection();
			ps = con.prepareStatement("select VehicleInfoType.* from Claims,VehicleInfoType where Claims.ClaimNumber=? and VehicleInfoType.LicPlate=? and Claims.Claim_id=VehicleInfoType.Claim_id");
			ps.setString(1,claimNumber);
			ps.setString(2, licPlate);
			ResultSet rs = ps.executeQuery();
			MitchellClaimType claim = new MitchellClaimType();
			VehicleInfoType info = new VehicleInfoType();
			while(rs.next()){
				info.setDamageDescription(rs.getString("DamageDescription"));
				info.setEngineDescription(rs.getString("EngineDescription"));
				info.setExteriorColor(rs.getString("ExteriorColor"));
				info.setLicPlate(rs.getString("LicPlate"));

				info.setLicPlateExpDate(null);
				info.setLicPlateState(rs.getString("LicPlateState"));
				info.setMakeDescription(rs.getString("MakeDescription"));
				info.setMileage(rs.getInt("Mileage"));
				info.setModelDescription(rs.getString("ModelDescription"));
				info.setModelYear(rs.getInt("ModelYear"));
				info.setVin(rs.getString("Vin"));
				
			}
			claim.setClaimNumber(claimNumber);
			claim.setVehicles(new VehicleListType());
			claim.getVehicles().getVehicleDetails().add(info);
			
			return claim;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				ps.close();
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
				
		return null;
	}

	

}
