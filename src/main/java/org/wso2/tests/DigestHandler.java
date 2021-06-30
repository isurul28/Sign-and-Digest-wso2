package org.wso2.tests;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class DigestHandler extends AbstractMediator {
	private static final Log log = LogFactory.getLog(DigestHandler.class);
	private String Messagebody;
	String assertion;
	PrivateKey privateKey;
	String signatureAlgorithm;
	byte[] Signature;

	public boolean mediate(MessageContext context) {
		try {

			// digest

			log.info("Digest started");
			//getting Json message
			String jsonPayloadToString = JsonUtil
					.jsonPayloadToString(((Axis2MessageContext) context).getAxis2MessageContext());
			Messagebody = context.getMessageString();
			// System.out.println("Old
			// BOdy::"+context.getEnvelope().getBody().getFirstOMChild());
			System.out.println("Meesage::" + jsonPayloadToString);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(jsonPayloadToString.getBytes());
			byte[] digest = md.digest();
			System.out.println("Digest value:::" + digest);

			@SuppressWarnings("unchecked")
			Map<String, Object> transportHeaders = (Map<String, Object>) ((Axis2MessageContext) context)
					.getAxis2MessageContext().getProperty("TRANSPORT_HEADERS");

			System.out.println("Digest value:::" + digest);
			transportHeaders.put("Digest", digest);
			
			//Signature Part	
			
			
			// ******Explanation of each parameter*******************
			// @param assertion valid JWT assertion
			// @param privateKey private key which use to sign the JWT assertion
			// @param signatureAlgorithm signature algorithm which use to sign the JWT assertion
			//***************************************************************
			//Un comment this part to use the signature header
			
			//signJwt SignJwt = new signJwt();
			//Signature = SignJwt.signJwt(assertion, privateKey, signatureAlgorithm);
			//transportHeaders.put("Signature", Signature);
			
			
			//************Testing Purpose***************
			System.out.println("------Transport Headers-------");
			@SuppressWarnings("unchecked")
			Map<String, Object> mapTransportProperties = (Map<String, Object>) ((Axis2MessageContext) context)
					.getAxis2MessageContext().getProperty("TRANSPORT_HEADERS");
			for (Map.Entry entry : mapTransportProperties.entrySet()) {
				System.out.println("TRANS:" + entry.getKey() + ", " + entry.getValue());

			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
}
