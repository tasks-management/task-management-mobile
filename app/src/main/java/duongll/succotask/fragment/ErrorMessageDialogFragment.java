package duongll.succotask.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import duongll.succotask.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorMessageDialogFragment extends Fragment {


    public ErrorMessageDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error_message_dialog, container, false);
    }

}
