package com.oreilly.rdf.tenuki;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.restlet.data.Tag;

public class Utils {
	public static Tag calculateTag(byte[] content) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			byte[] digest = sha1.digest(content);
			return new Tag( new BigInteger(1, digest).toString(16), false);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
