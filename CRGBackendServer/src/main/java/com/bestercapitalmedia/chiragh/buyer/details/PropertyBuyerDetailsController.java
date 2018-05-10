package com.bestercapitalmedia.chiragh.buyer.details;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bestercapitalmedia.chiragh.mail.MailService;
import com.bestercapitalmedia.chiragh.property.Chiraghproperty;
import com.bestercapitalmedia.chiragh.property.PropertyRepository;
import com.bestercapitalmedia.chiragh.seller.details.PropertySellerDetailDTO;
import com.bestercapitalmedia.chiragh.seller.details.Propertysellerdetails;
import com.bestercapitalmedia.chiragh.user.Chiraghuser;
import com.bestercapitalmedia.chiragh.user.UserRegisterationDTO;
import com.bestercapitalmedia.chiragh.user.UserRepository;
import com.bestercapitalmedia.chiragh.utill.ChiragUtill;
import com.bestercapitalmedia.chiragh.utill.LogUtill;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bestercapitalmedia.chiragh.buyer.bidding.history.BuyerBiddingHistory;
import com.bestercapitalmedia.chiragh.buyer.bidding.history.BuyerBiddingHistoryRepository;
import com.bestercapitalmedia.chiragh.buyer.details.PropertyBuyerDetailsRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/buyerdetails")
public class PropertyBuyerDetailsController {

	private static final Logger log = LoggerFactory.getLogger(PropertyBuyerDetailsController.class);
	@Autowired
	private PropertyBuyerDetailsRepository propertybuyerdetailRepository;
	@Autowired
	private ChiragUtill chiraghUtill;
	@Autowired
	private MailService mailService;
	@Autowired
	private PropertyRepository propertyRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PropertyBuyerDetailsService propertybuyerdetailsservice;
	@Autowired
	private LogUtill logUtill;
	@Autowired
	private BuyerBiddingHistoryRepository buyerbiddinghistoryRepository;
	List<String> files = new ArrayList<String>();

	
	
	@RequestMapping(value = "/getOwnerBuyerDetails/{propertyId}", method = RequestMethod.GET)
	public ResponseEntity getOwnerDetailsById(@PathVariable(value = "propertyId") int propertyId,
			HttpServletRequest httpServletRequest) {
		try {

			if (chiraghUtill.isValidSession(httpServletRequest) == false)
				return new ResponseEntity(chiraghUtill.getMessageObject("Invalid Session!"), HttpStatus.OK);

			ModelMapper mapper = new ModelMapper();
			ObjectMapper objectMapper = new ObjectMapper();
			
			BuyerBiddingHistory buyerbiddinghistory = buyerbiddinghistoryRepository
					.findBuyerByPropertyId(propertyId);
			int buyerhistory=buyerbiddinghistory.getBuyerBiddingHistoryId();
			List<PropertyBuyerDetails> propertybuyerownerdetailsdto =  
					propertybuyerdetailRepository.findBuyerOwnerByBiddingHistoryId(buyerhistory);
	
			
			
			List<PropertyBuyerDetailsDTO> propertybuyerownerdetailsDTO = propertybuyerownerdetailsdto.stream()
					.map(object -> mapper.map(object, PropertyBuyerDetailsDTO.class)).collect(Collectors.toList());

			try {
				logUtill.inputLog(httpServletRequest, chiraghUtill.getSessionUser(httpServletRequest),
						"/api/buyerdetails/getOwnerBuyerDetails/{property_Id}",
						objectMapper.writeValueAsString(propertyId),
						objectMapper.writeValueAsString(propertybuyerownerdetailsDTO));
			} catch (Exception e) {
				return new ResponseEntity(chiraghUtill.getMessageObject("Log Generation Fail!"), HttpStatus.OK);
			}

			return new ResponseEntity(propertybuyerownerdetailsDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(chiraghUtill.getMessageObject("Internal Server Error!" + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	@RequestMapping(value = "/getPOABuyerDetails/{propertyId}", method = RequestMethod.GET)
	public ResponseEntity getPOADetailsById(@PathVariable(value = "propertyId") int propertyId,
			HttpServletRequest httpServletRequest) {
		try {

			if (chiraghUtill.isValidSession(httpServletRequest) == false)
				return new ResponseEntity(chiraghUtill.getMessageObject("Invalid Session!"), HttpStatus.OK);

			ModelMapper mapper = new ModelMapper();
			ObjectMapper objectMapper = new ObjectMapper();
			
			BuyerBiddingHistory buyerbiddinghistory = buyerbiddinghistoryRepository
					.findBuyerByPropertyId(propertyId);
			int buyerhistory=buyerbiddinghistory.getBuyerBiddingHistoryId();
			List<PropertyBuyerDetails> propertybuyerpoadetailsdto =  
					propertybuyerdetailRepository.findBuyerPOAByBiddingHistoryId(buyerhistory);
	
			
			
			List<PropertyBuyerDetailsDTO> propertybuyerpoadetailsDTO = propertybuyerpoadetailsdto.stream()
					.map(object -> mapper.map(object, PropertyBuyerDetailsDTO.class)).collect(Collectors.toList());

			try {
				logUtill.inputLog(httpServletRequest, chiraghUtill.getSessionUser(httpServletRequest),
						"/api/buyerdetails/getPOABuyerDetails/{property_Id}",
						objectMapper.writeValueAsString(propertyId),
						objectMapper.writeValueAsString( propertybuyerpoadetailsDTO));
			} catch (Exception e) {
				return new ResponseEntity(chiraghUtill.getMessageObject("Log Generation Fail!"), HttpStatus.OK);
			}

			return new ResponseEntity( propertybuyerpoadetailsDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(chiraghUtill.getMessageObject("Internal Server Error!" + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity ownerlist(HttpServletRequest httpServletRequest) {
		
		try {
			if (chiraghUtill.isValidSession(httpServletRequest) == false)
				return new ResponseEntity(chiraghUtill.getMessageObject("Invalid Session!"), HttpStatus.OK);
	
			ObjectMapper mapper = new ObjectMapper();
			
			List<PropertyBuyerDetailsDTO> propertyownerdetailsdto = (List<PropertyBuyerDetailsDTO>) propertybuyerdetailsservice
					.getPropertyBuyerDetailsList();
			try {
				logUtill.inputLog(httpServletRequest, chiraghUtill.getSessionUser(httpServletRequest),
						"/api/buyerdetails/ownerdetails/getAll", mapper.writeValueAsString(""),
						mapper.writeValueAsString(propertyownerdetailsdto));
			} catch (Exception e) {
				return new ResponseEntity(chiraghUtill.getMessageObject("Log not Generated"), HttpStatus.OK);
			}
			
			return new ResponseEntity(propertyownerdetailsdto, HttpStatus.OK);
             
		} catch (Exception e) {
			return new ResponseEntity(chiraghUtill.getMessageObject("Internal Server Error!" + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}// end of list method*/

	
		

	@RequestMapping(value = "/hello")
	public String getMsg() {
		return DigestUtils.md5DigestAsHex("123".getBytes());
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity createOWNER(@Valid @RequestBody PropertyBuyerDetailsDTO propertyownerdetailsdto,
			HttpServletRequest httpServletRequest) {
		try {

			if (chiraghUtill.isValidSession(httpServletRequest) == false)
				return new ResponseEntity(chiraghUtill.getMessageObject("Invalid Session!"), HttpStatus.OK);

			ObjectMapper mapper = new ObjectMapper();
			PropertyBuyerDetailsDTO propertyownerdetailsDTO =  propertybuyerdetailsservice
					.saveBuyerOwner(propertyownerdetailsdto, httpServletRequest);
             
			if (propertyownerdetailsDTO == null)
				return new ResponseEntity(chiraghUtill.getMessageObject("Buyer Not Saved!"), HttpStatus.OK);
			try {
				logUtill.inputLog(httpServletRequest, chiraghUtill.getSessionUser(httpServletRequest),
						"/api/buyerdetails/post", mapper.writeValueAsString(propertyownerdetailsdto),
						mapper.writeValueAsString(propertyownerdetailsDTO));
			} catch (Exception e) {
				return new ResponseEntity(chiraghUtill.getMessageObject("Log not Generated"), HttpStatus.OK);
			}

			return new ResponseEntity("Buyer Saved Successfully", HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity(chiraghUtill.getMessageObject("Internal Server Error!" + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	


	/*@RequestMapping(value = "/getAllFiles", method = RequestMethod.GET)
	public String getListFiles(PropertyBuyerDetails model) {
		String rtnObject = "";

		try {
			// List<String> files = new ArrayList<String>();
			ObjectMapper objectMapper = new ObjectMapper();
			String msg = "";

			List<String> fileNames = files.stream()
					.map(fileName -> MvcUriComponentsBuilder
							.fromMethodName(PropertyBuyerDetailsController.class, "getFile", fileName).build()
							.toString())
					.collect(Collectors.toList());

			rtnObject = objectMapper.writeValueAsString(fileNames);

			msg = "success";
		} catch (Exception ee) {
			throw new RuntimeException("FAIL!");
		}

		return rtnObject;
	}*/

	// @RequestMapping(value = "/files/{filename:.+}", method = RequestMethod.GET)
	// public Resource getFile(@PathVariable String filename) {
	//
	// Resource file = chiraghUtil.loadFile(filename);
	// return file;
	// }*/

}
