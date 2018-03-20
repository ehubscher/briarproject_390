package org.briarproject.bramble.api.identity;

import org.briarproject.bramble.api.nullsafety.NotNullByDefault;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.concurrent.Immutable;

/**
 * A pseudonym for a user.
 */
@Immutable
@NotNullByDefault
public class Author {

	public enum Status {
		NONE, ANONYMOUS, UNKNOWN, UNVERIFIED, VERIFIED, OURSELVES
	}

	private final AuthorId id;
	private final String name;
	private final byte[] publicKey;
	//TODO make the proper utilization of unique Id when we can transfer it properly.
	private final String uniqueId = "1233345";

	public Author(AuthorId id, String name, byte[] publicKey) {//pattern to separate the uniqueId from the tag
		int length;
		try {
			length = name.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		if (length == 0 || length > AuthorConstants.MAX_AUTHOR_NAME_LENGTH)
			throw new IllegalArgumentException();
		this.id = id;
		this.name = name;
		this.publicKey = publicKey;
	}

	/**
	 * Returns the author's unique identifier.
	 */
	public AuthorId getId() {
		return id;
	}

	/**
	 * Returns the author's name.
	 */
	public String getName() {
	    return name;
	}

	/**
	 * Returns the author's unique id.
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * Returns the public key used to verify the pseudonym's signatures.
	 */
	public byte[] getPublicKey() {
		return publicKey;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Author && id.equals(((Author) o).id);
	}
}
