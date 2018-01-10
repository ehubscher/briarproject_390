package org.briarproject.bramble.keyagreement;

import org.briarproject.bramble.api.crypto.CryptoComponent;
import org.briarproject.bramble.api.crypto.KeyAgreementCrypto;
import org.briarproject.bramble.api.crypto.KeyPair;
import org.briarproject.bramble.api.crypto.KeyParser;
import org.briarproject.bramble.api.crypto.PublicKey;
import org.briarproject.bramble.api.crypto.SecretKey;
import org.briarproject.bramble.api.keyagreement.Payload;
import org.briarproject.bramble.api.keyagreement.PayloadEncoder;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import static org.briarproject.bramble.api.keyagreement.KeyAgreementConstants.MASTER_SECRET_LABEL;
import static org.briarproject.bramble.api.keyagreement.KeyAgreementConstants.PROTOCOL_VERSION;
import static org.briarproject.bramble.api.keyagreement.KeyAgreementConstants.SHARED_SECRET_LABEL;

/**
 * Implementation of the BQP protocol.
 * <p/>
 * Alice:
 * <ul>
 * <li>Send A_KEY</li>
 * <li>Receive B_KEY
 * <ul>
 * <li>Check B_KEY matches B_COMMIT</li>
 * </ul></li>
 * <li>Calculate s</li>
 * <li>Send A_CONFIRM</li>
 * <li>Receive B_CONFIRM
 * <ul>
 * <li>Check B_CONFIRM matches expected</li>
 * </ul></li>
 * <li>Derive master</li>
 * </ul><p/>
 * Bob:
 * <ul>
 * <li>Receive A_KEY
 * <ul>
 * <li>Check A_KEY matches A_COMMIT</li>
 * </ul></li>
 * <li>Send B_KEY</li>
 * <li>Calculate s</li>
 * <li>Receive A_CONFIRM
 * <ul>
 * <li>Check A_CONFIRM matches expected</li>
 * </ul></li>
 * <li>Send B_CONFIRM</li>
 * <li>Derive master</li>
 * </ul>
 */
@NotNullByDefault
class KeyAgreementProtocol {

	interface Callbacks {

		void connectionWaiting();

		void initialRecordReceived();
	}

	private final Callbacks callbacks;
	private final CryptoComponent crypto;
	private final KeyAgreementCrypto keyAgreementCrypto;
	private final PayloadEncoder payloadEncoder;
	private final KeyAgreementTransport transport;
	private final Payload theirPayload, ourPayload;
	private final KeyPair ourKeyPair;
	private final boolean alice;

	KeyAgreementProtocol(Callbacks callbacks, CryptoComponent crypto,
			KeyAgreementCrypto keyAgreementCrypto,
			PayloadEncoder payloadEncoder, KeyAgreementTransport transport,
			Payload theirPayload, Payload ourPayload, KeyPair ourKeyPair,
			boolean alice) {
		this.callbacks = callbacks;
		this.crypto = crypto;
		this.keyAgreementCrypto = keyAgreementCrypto;
		this.payloadEncoder = payloadEncoder;
		this.transport = transport;
		this.theirPayload = theirPayload;
		this.ourPayload = ourPayload;
		this.ourKeyPair = ourKeyPair;
		this.alice = alice;
	}

	/**
	 * Perform the BQP protocol.
	 *
	 * @return the negotiated master secret.
	 * @throws AbortException when the protocol may have been tampered with.
	 * @throws IOException for all other other connection errors.
	 */
	SecretKey perform() throws AbortException, IOException {
		try {
			PublicKey theirPublicKey;
			if (alice) {
				sendKey();
				// Alice waits here until Bob obtains her payload.
				callbacks.connectionWaiting();
				theirPublicKey = receiveKey();
			} else {
				theirPublicKey = receiveKey();
				sendKey();
			}
			SecretKey s = deriveSharedSecret(theirPublicKey);
			if (alice) {
				sendConfirm(s, theirPublicKey);
				receiveConfirm(s, theirPublicKey);
			} else {
				receiveConfirm(s, theirPublicKey);
				sendConfirm(s, theirPublicKey);
			}
			return crypto.deriveKey(MASTER_SECRET_LABEL, s);
		} catch (AbortException e) {
			sendAbort(e.getCause() != null);
			throw e;
		}
	}

	private void sendKey() throws IOException {
		transport.sendKey(ourKeyPair.getPublic().getEncoded());
	}

	private PublicKey receiveKey() throws AbortException {
		byte[] publicKeyBytes = transport.receiveKey();
		callbacks.initialRecordReceived();
		KeyParser keyParser = crypto.getAgreementKeyParser();
		try {
			PublicKey publicKey = keyParser.parsePublicKey(publicKeyBytes);
			byte[] expected = keyAgreementCrypto.deriveKeyCommitment(publicKey);
			if (!Arrays.equals(expected, theirPayload.getCommitment()))
				throw new AbortException();
			return publicKey;
		} catch (GeneralSecurityException e) {
			throw new AbortException();
		}
	}

	private SecretKey deriveSharedSecret(PublicKey theirPublicKey)
			throws AbortException {
		try {
			byte[] ourPublicKeyBytes = ourKeyPair.getPublic().getEncoded();
			byte[] theirPublicKeyBytes = theirPublicKey.getEncoded();
			byte[][] inputs = {
					new byte[] {PROTOCOL_VERSION},
					alice ? ourPublicKeyBytes : theirPublicKeyBytes,
					alice ? theirPublicKeyBytes : ourPublicKeyBytes
			};
			return crypto.deriveSharedSecret(SHARED_SECRET_LABEL,
					theirPublicKey, ourKeyPair, inputs);
		} catch (GeneralSecurityException e) {
			throw new AbortException(e);
		}
	}

	private void sendConfirm(SecretKey s, PublicKey theirPublicKey)
			throws IOException {
		byte[] confirm = keyAgreementCrypto.deriveConfirmationRecord(s,
				payloadEncoder.encode(theirPayload),
				payloadEncoder.encode(ourPayload),
				theirPublicKey, ourKeyPair,
				alice, alice);
		transport.sendConfirm(confirm);
	}

	private void receiveConfirm(SecretKey s, PublicKey theirPublicKey)
			throws AbortException {
		byte[] confirm = transport.receiveConfirm();
		byte[] expected = keyAgreementCrypto.deriveConfirmationRecord(s,
				payloadEncoder.encode(theirPayload),
				payloadEncoder.encode(ourPayload),
				theirPublicKey, ourKeyPair,
				alice, !alice);
		if (!Arrays.equals(expected, confirm))
			throw new AbortException();
	}

	private void sendAbort(boolean exception) {
		transport.sendAbort(exception);
	}
}
