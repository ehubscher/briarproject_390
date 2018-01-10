package org.briarproject.briar.android.contactselection;

import org.briarproject.bramble.api.contact.ContactId;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.api.sync.GroupId;
import org.briarproject.briar.android.controller.DbController;
import org.briarproject.briar.android.controller.handler.ResultExceptionHandler;

import java.util.Collection;

@NotNullByDefault
public interface ContactSelectorController<I extends SelectableContactItem>
		extends DbController {

	void loadContacts(GroupId g, Collection<ContactId> selection,
			ResultExceptionHandler<Collection<I>, DbException> handler);

}
