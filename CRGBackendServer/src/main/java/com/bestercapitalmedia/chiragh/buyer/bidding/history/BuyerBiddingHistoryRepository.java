package com.bestercapitalmedia.chiragh.buyer.bidding.history;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bestercapitalmedia.chiragh.buyer.details.PropertyBuyerDetails;
import com.bestercapitalmedia.chiragh.property.Chiraghproperty;

public interface BuyerBiddingHistoryRepository extends CrudRepository<BuyerBiddingHistory, Integer>{
	
	
	@Query(value = "select * from buyerbiddinghistory where property_Id=?1  ", nativeQuery = true)
	public BuyerBiddingHistory findBuyerByPropertyId(int propertyId);
	
	@Query(value = "Select * from buyerbiddinghistory;", nativeQuery = true)
	public List<BuyerBiddingHistory> getAll();
	
	//@Query(value = "Select * from buyerbiddinghistory;", nativeQuery = true)
	//public BuyerBiddingHistory findByBidReferenceNo(String bidreferenceno);
	
	@Query(value = "select * from buyerbiddinghistory where  user_Id=?1 And property_Id=?2", nativeQuery = true)
	public BuyerBiddingHistory findBuyerExisting(int userId,int propertyId);
	
}
