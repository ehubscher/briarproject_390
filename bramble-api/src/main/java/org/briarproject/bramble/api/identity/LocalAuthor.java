package org.briarproject.bramble.api.identity;

import org.briarproject.bramble.api.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * A pseudonym for the local user.
 */
@Immutable
@NotNullByDefault
public class LocalAuthor extends Author {

	private final byte[] privateKey;
	private final long created;
	// Try to insert our field here
    private String password;

	public LocalAuthor(AuthorId id, String name, byte[] publicKey,
			byte[] privateKey, long created) {
		super(id, name, publicKey);
		this.privateKey = privateKey;
		this.created = created;
	}

	/**
	 * Returns the private key used to generate the pseudonym's signatures.
	 */
	public byte[] getPrivateKey() {
		return privateKey;
	}

	/**
	 * Returns the time the pseudonym was created, in milliseconds since the
	 * Unix epoch.
	 */
	public long getTimeCreated() {
		return created;
	}


	public void setPassword(String pwd){
	    password = pwd;
    }

    public String getPassword(){
	    return password;
    }
}
