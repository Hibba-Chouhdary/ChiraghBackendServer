package com.bestercapitalmedia.chiragh.buyer.details;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bestercapitalmedia.chiragh.buyer.bidding.history.BuyerBiddingHistory;
import com.bestercapitalmedia.chiragh.buyer.bidding.history.BuyerBiddingHistoryDTO;
import com.bestercapitalmedia.chiragh.buyer.bidding.history.BuyerBiddingHistoryRepository;
import com.bestercapitalmedia.chiragh.imageutill.StorageService;
import com.bestercapitalmedia.chiragh.property.Chiraghproperty;
import com.bestercapitalmedia.chiragh.property.PropertyRepository;
import com.bestercapitalmedia.chiragh.seller.details.PropertySellerDetailDTO;
import com.bestercapitalmedia.chiragh.seller.details.Propertysellerdetails;
import com.bestercapitalmedia.chiragh.user.Chiraghuser;
import com.bestercapitalmedia.chiragh.user.UserRepository;
import com.bestercapitalmedia.chiragh.utill.ChiragUtill;
import com.bestercapitalmedia.chiragh.utill.ValidatedInput;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PropertyBuyerDetailsService {

	@Autowired
	private PropertyBuyerDetailsRepository propertybuyerdetailsRepository;
	@Autowired
	private ValidatedInput validatedInput;
	@Autowired
	private ChiragUtill chiraghUtill;
	@Autowired
	private BuyerBiddingHistoryRepository buyerbiddinghistoryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PropertyRepository propertyRepository;
	@Autowired
	private StorageService storageService;

	//List<String> files = new ArrayList<String>();
	//private static String UPLOADED_FOLDER = "C:\\Users\\hp\\";

	public boolean validateMultipartFiles(MultipartFile passportCopyUpload, MultipartFile idCopyUpload,
			MultipartFile scannedNotorizedCopy, String ownerType) {
		if (ownerType.equals("owner") && passportCopyUpload != null && idCopyUpload != null
				&& chiraghUtill.checkMineType(passportCopyUpload) && chiraghUtill.checkMineType(idCopyUpload)) {
			return true;
		} else if (ownerType.equals("poa") && scannedNotorizedCopy != null && passportCopyUpload != null
				&& idCopyUpload != null && chiraghUtill.checkMineType(scannedNotorizedCopy)
				&& chiraghUtill.checkMineType(passportCopyUpload) && chiraghUtill.checkMineType(idCopyUpload)) {
			return true;
		} else
			return false;
	}

	public List<PropertyBuyerDetailsDTO> getPropertyBuyerDetailsList() {
		ModelMapper modelMapper = new ModelMapper();
		return propertybuyerdetailsRepository.getAll().stream()
				.map(temp -> modelMapper.map(temp, PropertyBuyerDetailsDTO.class)).collect(Collectors.toList());

	}

	
	public int getPropertyIdFromSession(HttpServletRequest httpServletRequest) {
		try {
			return Integer.parseInt(httpServletRequest.getSession(false).getAttribute("propertyId").toString());
		} catch (Exception e) {
			return 0;
		}
	}
	
	public PropertyBuyerDetailsDTO saveBuyerOwner(PropertyBuyerDetailsDTO propertyownerdetailsDTO,
			HttpServletRequest httpServletRequest) {

		try {

			ModelMapper modelmapper = new ModelMapper();
			PropertyBuyerDetails propertyBuyerDetails = modelmapper.map(propertyownerdetailsDTO,
					PropertyBuyerDetails.class);

		    int propertyId = getPropertyIdFromSession(httpServletRequest);
			//int propertyId = propertyownerdetailsDTO.getPropertyId();
			int userId = chiraghUtill.getSessionUser(httpServletRequest).getUserId();
		   //int  buyerBiddingHistoryId =chiraghUtill.getSessionBuyer(httpServletRequest).getBuyerBiddingHistoryId();
			
			BuyerBiddingHistory buyerbiddinghistory = buyerbiddinghistoryRepository.findBuyerExisting(userId,propertyId);
			
			
			if (buyerbiddinghistory == null) {
				// Creating buyer first time

				BuyerBiddingHistory newbuyerbiddinghistory = new BuyerBiddingHistory();
				Chiraghuser chiraghuser = userRepository.findByUserId(userId);
				newbuyerbiddinghistory.setChiraghuser(chiraghuser);
				Chiraghproperty chiraghproperty = propertyRepository.findByPropertyId(propertyId);
				newbuyerbiddinghistory.setChiraghproperty(chiraghproperty);
				newbuyerbiddinghistory.setBidReferenceNo(chiraghUtill.genearteRandomNo("BidReferenceNo"));

				BuyerBiddingHistory new2buyerbiddinghistory = buyerbiddinghistoryRepository
						.save(newbuyerbiddinghistory);
				propertyBuyerDetails.setBuyerbiddinghistory(new2buyerbiddinghistory);
				PropertyBuyerDetails newpropertybuyerdetails = propertybuyerdetailsRepository
						.save(propertyBuyerDetails);
				/*
				 * // saving images of buyer String path = "/" +
				 * new2buyerbiddinghistory.getBidReferenceNo() + "/Buyer"; String fileName =
				 * chiraghproperty.getPropertyReferenceNo() + "," +
				 * newpropertybuyerdetails.getPropertyBuyerId()();
				 * storageService.store(idCopyUpload, path, fileName + ",idCopyUpload");
				 * storageService.store(passportCopyUpload, path, fileName +
				 * ",passportCopyUpload"); storageService.store( scannedNotorizedCopy, path,
				 * fileName + ",scannedNotorizedCopy");
				 * 
				 * // updating buyer file info newpropertybuyerdetails.setIdCopyUpload(fileName
				 * + ",idCopyUpload"); newpropertybuyerdetails.setPassportCopyUpload(fileName +
				 * ",passportCopyUpload");
				 * newpropertybuyerdetails.setScannedNotorizedCopy(fileName +
				 * ",scannedNotorizedCopy");
				 * propertybuyerdetailsRepository.save(newpropertybuyerdetails);
				 */
				
				return modelmapper.map(newpropertybuyerdetails, PropertyBuyerDetailsDTO.class);
				//PropertyBuyerDetailsDTO obj = modelmapper.map(newpropertybuyerdetails, PropertyBuyerDetailsDTO.class);
				//List<PropertyBuyerDetailsDTO> list = new ArrayList<PropertyBuyerDetailsDTO>();
				//list.add(obj);
				//return list;

			}

			else {
				
				//int  buyerBiddingHistoryId =chiraghUtill.getSessionBuyer(httpServletRequest).getBuyerBiddingHistoryId();
				int buyerBiddingHistoryId = buyerbiddinghistory.getBuyerBiddingHistoryId();
				propertyBuyerDetails.setBuyerbiddinghistory(buyerbiddinghistory);
				PropertyBuyerDetails newpropertybuyerdetails = propertybuyerdetailsRepository
						.save(propertyBuyerDetails);
				
				//PropertyBuyerDetail list1 = propertybuyerdetailsRepository
						//.findBuyerByBiddingHistoryId(buyerBiddingHistoryId);
				//return list1.stream().map(temp -> modelmapper.map(temp, PropertyBuyerDetailsDTO.class))
						//.collect(Collectors.toList());
				return modelmapper.map(newpropertybuyerdetails, PropertyBuyerDetailsDTO.class);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/*public String FileUploading(MultipartFile file) {

		String msg = "";
		if (file.isEmpty()) {
			return "File is Empty";
		}

		try {
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			msg = "success";
		} catch (Exception e) {
			msg = e.getMessage();
		}
		if (msg.equals("success"))
			return "success";
		else
			return msg;

	}*/

}
