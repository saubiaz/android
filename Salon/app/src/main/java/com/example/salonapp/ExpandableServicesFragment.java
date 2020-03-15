package com.example.salonapp;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.salonapp.dtos.BeautyService;
import com.example.salonapp.dtos.Cart;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExpandableServicesFragment extends Fragment {


    private OnListFragmentInteractionListener mListener;
    Context context;
    private static ArrayList<BeautyService> beautyServiceArrayList = new ArrayList<>();
    private int mCartItemCount =0;
    public List<Cart> cartList;
    private String service;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExpandableServicesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExpandableServicesFragment newInstance(Context applicationContext, List<Cart> cartList) {
        ExpandableServicesFragment fragment = new ExpandableServicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            if (getArguments() != null) {
            if(getArguments().containsKey("cartCount")){
                mCartItemCount = getArguments().getInt("cartCount");
            }
            if( getArguments().containsKey("cartList") && getArguments().getSerializable("cartList")!=null) {
                cartList = (List<Cart>) getArguments().getSerializable("cartList");
            }
            if(getArguments().containsKey("service")){
               service =  getArguments().getString("service");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedicureservice_list, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(service).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    List<BeautyService> beautyServiceList = task.getResult().toObjects(BeautyService.class);
                    beautyServiceArrayList = new ArrayList<>();
                    beautyServiceArrayList.addAll(beautyServiceList);
                }
            } else {
                Log.w("Error getting document", task.getException());
            }
        }).addOnCompleteListener(task -> {
            //CREATE AND BIND TO ADAPTER
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
                recyclerView.addItemDecoration(divider);
                recyclerView.setAdapter(new ExpandableServicesFgmntRecyclerViewAdapter(beautyServiceArrayList, mListener,context,cartList,mCartItemCount));
            }
        });;
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(int cartCount,List<Cart> cartItems);
    }
}
