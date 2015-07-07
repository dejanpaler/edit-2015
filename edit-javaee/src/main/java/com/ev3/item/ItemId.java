package com.ev3.item;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class ItemId {
	private static final int UUID_BYTE_LENGTH = 16;
	
	public static String generate() {
		UUID uuid = UUID.randomUUID();
		
		ByteBuffer uuidByteBuffer = ByteBuffer.wrap(new byte[UUID_BYTE_LENGTH]);
		uuidByteBuffer.putLong(uuid.getMostSignificantBits());
		uuidByteBuffer.putLong(uuid.getLeastSignificantBits());
		
		return Base64.getUrlEncoder().encodeToString(uuidByteBuffer.array());
	}
}
