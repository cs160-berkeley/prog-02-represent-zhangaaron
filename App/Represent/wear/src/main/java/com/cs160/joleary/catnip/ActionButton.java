package com.cs160.joleary.catnip;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.wearable.view.CircledImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActionButton.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActionButton#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActionButton extends Fragment implements View.OnClickListener {

    private static View.OnClickListener mListener;
    private CircledImageView vIcon;
    private TextView vLabel;

    public static ActionButton create(int iconResId, int labelResId, View.OnClickListener listener) {
        mListener = listener;
        ActionButton fragment = new ActionButton();
        Bundle args = new Bundle();
        args.putInt("ICON", iconResId);
        args.putInt("LABEL", labelResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action_button, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vIcon = (CircledImageView) view.findViewById(R.id.icon);
        vLabel = (TextView) view.findViewById(R.id.label);
        vIcon.setImageResource(getArguments().getInt("ICON"));
        vLabel.setText(getArguments().getInt("LABEL"));
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick(v);
    }
}