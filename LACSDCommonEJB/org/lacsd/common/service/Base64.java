package org.lacsd.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/******************************************************************************
//* Copyright (c) 2003 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:		Base64.java
//* Revision: 		1.0
//* Author:			DYIP@LACSD.ORG
//* Created On: 	01-28-2002
//* Modified by:	MFEINBERG@LACSD.ORG
//* Modified On:	10-15-2003
//*					08-20-2008 by Hoa Ho Adding method to decode user's LANID
//* Description:	-> Made into Singleton Service Object
//* 
//* This library is free software; you can redistribute it and/or
//* modify it under the terms of the GNU Library General Public
//* License as published by the Free Software Foundation; either
//* version 2 of the License, or (at your option) any later version.
//*
//* This library is distributed in the hope that it will be useful,
//* but WITHOUT ANY WARRANTY; without even the implied warranty of
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//* Library General Public License for more details.
//*
//* You should have received a copy of the GNU Library General Public
//* License along with this library; if not, write to the
//* Free Software Foundation, Inc., 59 Temple Place - Suite 330,
//* Boston, MA  02111-1307, USA.
//*
//* Base64 encoder/decoder.  Does not stream, so be careful with
//* using large amounts of data.
//*/
/******************************************************************************/

public class Base64 {


private static Base64 _INSTANCE = new Base64();

/**
 * Private Constructor - Singleton Pattern
*/
private Base64() {
	super();
}

/**
 * Return Singleton Instance
 * @return DateCovnert
*/
public static Base64 getInstance() {
	return _INSTANCE;
}





	/**
	 *  Encode String and return String.
	 */
	public String encode(String sS) {
		return encode(sS.getBytes());
	}

	/**
	 *  Encode some data and return a String.
	 */
	public String encode(byte[] d) {
		if (d == null)
			return null;
		byte data[] = new byte[d.length + 2];
		System.arraycopy(d, 0, data, 0, d.length);
		byte dest[] = new byte[(data.length / 3) * 4];

		// 3-byte to 4-byte conversion
		for (int sidx = 0, didx = 0; sidx < d.length; sidx += 3, didx += 4) {
			dest[didx] = (byte) ((data[sidx] >>> 2) & 077);
			dest[didx + 1] =
				(byte) ((data[sidx + 1] >>> 4) & 017 | (data[sidx] << 4) & 077);
			dest[didx + 2] =
				(byte) ((data[sidx + 2] >>> 6) & 003 | (data[sidx + 1] << 2) & 077);
			dest[didx + 3] = (byte) (data[sidx + 2] & 077);
		}

		// 0-63 to ascii printable conversion
		for (int idx = 0; idx < dest.length; idx++) {
			if (dest[idx] < 26)
				dest[idx] = (byte) (dest[idx] + 'A');
			else
				if (dest[idx] < 52)
					dest[idx] = (byte) (dest[idx] + 'a' - 26);
				else
					if (dest[idx] < 62)
						dest[idx] = (byte) (dest[idx] + '0' - 52);
					else
						if (dest[idx] < 63)
							dest[idx] = (byte) '+';
						else
							dest[idx] = (byte) '/';
		}

		// add padding
		for (int idx = dest.length - 1; idx > (d.length * 4) / 3; idx--) {
			dest[idx] = (byte) '=';
		}
		return new String(dest);
	}

	/**
	 *  Decode data and return a String.
	 */
	public String decode2Str(String str) {
		if (str == null)
			return null;
		return new String(decode(str));
	}

	/**
	 *  Decode data and return bytes.
	 */
	public byte[] decode(String str) {
		if (str == null)
			return null;
		str = str + "=";
		byte data[] = str.getBytes();
		return decode(data);
	}

	/**
	 *  Decode data and return bytes.  Assumes that the data passed
	 *  in is ASCII text.
	 */
	public byte[] decode(byte[] data) {
		int tail = data.length;
		while (data[tail - 1] == '=')
			tail--;
		byte dest[] = new byte[tail - data.length / 4];

		// ascii printable to 0-63 conversion
		for (int idx = 0; idx < data.length; idx++) {
			if (data[idx] == '=')
				data[idx] = 0;
			else
				if (data[idx] == '/')
					data[idx] = 63;
				else
					if (data[idx] == '+')
						data[idx] = 62;
					else
						if (data[idx] >= '0' && data[idx] <= '9')
							data[idx] = (byte) (data[idx] - ('0' - 52));
						else
							if (data[idx] >= 'a' && data[idx] <= 'z')
								data[idx] = (byte) (data[idx] - ('a' - 26));
							else
								if (data[idx] >= 'A' && data[idx] <= 'Z')
									data[idx] = (byte) (data[idx] - 'A');
		}

		// 4-byte to 3-byte conversion
		int sidx, didx;
		for (sidx = 0, didx = 0; didx < dest.length - 2; sidx += 4, didx += 3) {
			dest[didx] = (byte) (((data[sidx] << 2) & 255) | ((data[sidx + 1] >>> 4) & 3));
			dest[didx + 1] =
				(byte) (((data[sidx + 1] << 4) & 255) | ((data[sidx + 2] >>> 2) & 017));
			dest[didx + 2] =
				(byte) (((data[sidx + 2] << 6) & 255) | (data[sidx + 3] & 077));
		}
		if (didx < dest.length) {
			dest[didx] = (byte) (((data[sidx] << 2) & 255) | ((data[sidx + 1] >>> 4) & 3));
		}
		if (++didx < dest.length) {
			dest[didx] =
				(byte) (((data[sidx + 1] << 4) & 255) | ((data[sidx + 2] >>> 2) & 017));
		}
		return dest;
	}
	
	/**
	 * Get User LANID
	 * @param request
	 * @param response
	 * @return
	 */
	public String getNTLogin(HttpServletRequest request, HttpServletResponse response) {
	    String auth = request.getHeader("Authorization");
	    String userID = "";
	    try {
	    	if (auth == null){
	    	      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    	      response.setHeader("WWW-Authenticate", "NTLM");
	    	      response.flushBuffer();
	    	      return "";
	        }
	        if (auth.startsWith("NTLM ")){
	          byte[] msg = new sun.misc.BASE64Decoder().decodeBuffer(auth.substring(5));
	          int off = 0, length, offset;
	          if (msg[8] == 1)
	          {
	    	        byte z = 0;
	    	        byte[] msg1 = {(byte)'N', (byte)'T', (byte)'L', (byte)'M', (byte)'S', (byte)'S', (byte)'P', 
	    	         z,(byte)2, z, z, z, z, z, z, z,(byte)40, z, z, z, 
	    	         (byte)1, (byte)130, z, z,z, (byte)2, (byte)2,
	    	         (byte)2, z, z, z, z, z, z, z, z, z, z, z, z};
	    	       response.setHeader("WWW-Authenticate", "NTLM " + new sun.misc.BASE64Encoder().encodeBuffer(msg1).trim());
	    	       response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	               return "";
	    	   }
	    	      else if (msg[8] == 3){
	    	        off = 30;

	    	        length = msg[off+17]*256 + msg[off+16];
	    	        offset = msg[off+19]*256 + msg[off+18];
	    	        //String remoteHost = new String(msg, offset, length);

	    	        length = msg[off+1]*256 + msg[off];
	    	        offset = msg[off+3]*256 + msg[off+2];
	    	        //String domain = new String(msg, offset, length);

	    	        length = msg[off+9]*256 + msg[off+8];
	    	        offset = msg[off+11]*256 + msg[off+10];
	    	        String tempID = new String(msg, offset, length);
	    	        for (int i = 0; i < tempID.length();i++) {
	    	        	if (new Character(tempID.charAt(i)).hashCode() > 0) {
	    	        		userID += tempID.charAt(i);
	    	        	}
	    	        }
	    	        
	    	       	/*System.out.println("<br>Decode get User ID == " + userID );
	    	        System.out.println("<br>Username:"+userID);
	    	        System.out.println("<br>RemoteHost:"+remoteHost);
	    	        System.out.println("<br>Domain:"+domain);*/
	    	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    	       
	    		}
	    	}
	    }
	    catch(Exception e) {
	    	userID = "";
	    }
	    return userID;
	}	
	
}