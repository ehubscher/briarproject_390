package org.briarproject.bramble.api.contact;

import org.briarproject.bramble.api.identity.Author;
import org.briarproject.bramble.api.identity.AuthorId;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class Contact {

	private final ContactId id;
	private final Author author;
	private final AuthorId localAuthorId;
	private final boolean verified, active, favourite;
	private final int avatarId;

	public Contact(ContactId id, Author author, AuthorId localAuthorId,
				   boolean verified, boolean active, boolean favourite, int avatarId) {
		this.id = id;
		this.author = author;
		this.localAuthorId = localAuthorId;
		this.verified = verified;
		this.active = active;
		this.favourite = favourite;
		this.avatarId = avatarId;
	}

	public ContactId getId() {
		return id;
	}

	public Author getAuthor() {
		return author;
	}

	public AuthorId getLocalAuthorId() {
		return localAuthorId;
	}

	public int getAvatarId() { return this.avatarId; }

	public boolean isVerified() {
		return verified;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isFavourite() {
		return favourite;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Contact && id.equals(((Contact) o).id);
	}
}
