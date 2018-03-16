package org.briarproject.bramble.api.contact.event;

import org.briarproject.bramble.api.contact.ContactId;
import org.briarproject.bramble.api.event.Event;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a contact changes their avatar.
 */
@Immutable
@NotNullByDefault
public class ContactAvatarChangedEvent extends Event {

    private final ContactId contactId;
    private final int avatarId;

    public ContactAvatarChangedEvent(ContactId contactId, int avatarId) {
        this.contactId = contactId;
        this.avatarId = avatarId;
    }

    public ContactId getContactId() {
        return contactId;
    }

    public int getAvatarId() {
        return avatarId;
    }
}
