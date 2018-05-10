package com.bestercapitalmedia.chiragh.buyer.details;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bestercapitalmedia.chiragh.seller.details.Propertysellerdetails;

public interface PropertyBuyerDetailsRepository extends CrudRepository<PropertyBuyerDetails, Integer> {

	@Query(value = "Select * from propertybuyerdetails;", nativeQuery = true)
	public List<PropertyBuyerDetails> getAll();
	
	
	@Query(value = "select * from propertybuyerdetails where buyer_Bidding_History_Id=?1 And owner_Type='owner'", nativeQuery = true)
	public List<PropertyBuyerDetails> findBuyerOwnerByBiddingHistoryId(int buyerBiddingHistoryId);
	
	@Query(value = "select * from propertybuyerdetails where buyer_Bidding_History_Id=?1 And owner_Type='poa'", nativeQuery = true)
	public List<PropertyBuyerDetails> findBuyerPOAByBiddingHistoryId(int buyerBiddingHistoryId);
		
	//@Query(value = "Select * from propertybuyerdetails;", nativeQuery = true)
	//public List<PropertyBuyerDetails> findAll();
}
