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
	private final String uniqueId;

	public Author(AuthorId id, String name, byte[] publicKey) {
		int length;
		try {
			length = name.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		if (length == 0 || length > AuthorConstants.MAX_AUTHOR_NAME_LENGTH)
			throw new IllegalArgumentException();
		this.id = id;

		//pattern to separate the uniqueId from the tag
		final Pattern pattern = Pattern.compile("(.+?)<UniqueIdTag>(.+?)</UniqueIdTag>");
		final Matcher matcher = pattern.matcher(name);
		if(matcher.find()){
		    //name is the first group match and uniqueId is the second;
            this.name = matcher.group(1);
            this.uniqueId = matcher.group(2);
        }
        else{
		    //mostly for test purposes
            this.name = name;
            this.uniqueId = "1233345";
        }

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
