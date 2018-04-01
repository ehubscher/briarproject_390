package org.briarproject.briar.android.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.briarproject.bramble.restClient.BServerServicesImpl;
import org.briarproject.bramble.util.StringUtils;
import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;

import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NONE;
import static org.briarproject.bramble.api.identity.AuthorConstants.MAX_AUTHOR_NAME_LENGTH;
import static org.briarproject.briar.android.util.UiUtils.setError;

public class AuthorNameFragment extends SetupFragment {

	private final static String TAG = AuthorNameFragment.class.getName();
    private BServerServicesImpl services = new BServerServicesImpl();;
	private TextInputLayout authorNameWrapper;
	private TextInputEditText authorNameInput;
	private Button nextButton;
	private Button confirmationButton;
	private volatile boolean  NickNameTaken = false;

	public static AuthorNameFragment newInstance() {
		return new AuthorNameFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setTitle(getString(R.string.setup_title));
		View v = inflater.inflate(R.layout.fragment_setup_author_name,
				container, false);
		authorNameWrapper = v.findViewById(R.id.nickname_entry_wrapper);
		authorNameInput = v.findViewById(R.id.nickname_entry);
		nextButton = v.findViewById(R.id.next);
        confirmationButton = v.findViewById(R.id.confirmation);
		authorNameInput.addTextChangedListener(this);
		nextButton.setOnClickListener(this);
		confirmationButton.setOnClickListener(this);
		return v;
	}

	@Override
	public String getUniqueTag() {
		return TAG;
	}

	@Override
	public void injectFragment(ActivityComponent component) {
		component.inject(this);
	}

	@Override
	protected String getHelpText() {
		return getString(R.string.setup_name_explanation);
	}

	@Override
	public void onTextChanged(CharSequence authorName, int i, int i1, int i2) {
		int authorNameLength = StringUtils.toUtf8(authorName.toString()).length;
		boolean error = authorNameLength > MAX_AUTHOR_NAME_LENGTH;
		setError(authorNameWrapper, getString(R.string.name_too_long), error);
		boolean enabled = authorNameLength > 0 && !error;
		authorNameInput
				.setImeOptions(enabled ? IME_ACTION_NEXT : IME_ACTION_NONE);
		authorNameInput.setOnEditorActionListener(enabled ? this : null);
		NickNameTaken = false;
		nextButton.setEnabled(false);

	}

	@Override
	public void onClick(View view) {
            setError(authorNameWrapper, getString(R.string.name_already_taken), NickNameTaken);
            authorNameInput
                    .setImeOptions(NickNameTaken? IME_ACTION_NEXT : IME_ACTION_NONE);
            authorNameInput.setOnEditorActionListener(NickNameTaken ? this : null);
        switch (view.getId()){
            case R.id.next:
                if(!NickNameTaken)setupController.setAuthorName(authorNameInput.getText().toString());
            break;
            case R.id.confirmation:
                new CallServerAsync().execute();

            break;
            default:
                break;

        }
	}

    /**
     * This class is implementing an Async task as recommended for Android
     * It is made to make sure to separate server call from main UI Thread
     */
	class CallServerAsync extends AsyncTask<Void,Integer,String>{

	    boolean resultFromDoesItExists;
        @Override
        protected String doInBackground(Void... voids) {
                boolean obj = services.doesUsernameExistsInDB(authorNameInput.getText().toString());
                resultFromDoesItExists = obj;
                return null;
        }

        protected void onPostExecute(String result) {
            NickNameTaken = resultFromDoesItExists;
            if(!NickNameTaken){
                nextButton.setEnabled(true);
            }else{
                nextButton.setEnabled(false);
            }
        }
    }
}