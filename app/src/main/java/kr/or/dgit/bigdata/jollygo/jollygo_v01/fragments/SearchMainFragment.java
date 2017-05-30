package kr.or.dgit.bigdata.jollygo.jollygo_v01.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.customviews.GridRecyclerView;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgWords;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter.RvAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImgWords imgWords;
    private OnFragmentInteractionListener mListener;
    private GridRecyclerView grv;
    private RvAdapter rvAdapter;
    public SearchMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchMainFragment newInstance(String param1,String param2) {
        SearchMainFragment fragment = new SearchMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {//
        View root = inflater.inflate(R.layout.fragment_search_main,container,false);
        grv = (GridRecyclerView) root.findViewById(R.id.recyclerView);// 확인요망
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        grv.setHasFixedSize(true);
        grv.setLayoutManager(new GridLayoutManager(getContext(),3));
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        rvAdapter = new RvAdapter(getContext(),fab,imgWords);
        grv.setAdapter(rvAdapter);
        final TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        grv.gridChangeListener(tvTitle);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //검색결과 리스트 액티비티로 이동
            if(getRvAdapter().getImgWords().getmDataset().size()>0){

            }else{
                Toast.makeText(getContext(),"재료들을 먼저 입력해 주세요.",Toast.LENGTH_SHORT).show();
            }
            }
        });
        final SearchView sv = (SearchView) getActivity().findViewById(R.id.search_view);
        sv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("");
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                sv.setQuery("",true);
                getRvAdapter().addItem(query); //수정
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                int searchCount = getRvAdapter().getItemCount(); // 수정
                if (searchCount>1) {
                    tvTitle.setText("JOLLYGO- " + searchCount + " items were ready");
                }else if (searchCount == 1){
                    tvTitle.setText("JOLLYGO- " + searchCount + " item was ready");
                }else if (searchCount == 0 ){
                    tvTitle.setText("JOLLYGO-Search your own recipe");
                }
                return false;
            }
        });
    }

    public GridRecyclerView getGrv() {
        return grv;
    }

    public void setGrv(GridRecyclerView grv) {
        this.grv = grv;
    }

    public RvAdapter getRvAdapter() {
        return rvAdapter;
    }

    public void setRvAdapter(RvAdapter rvAdapter) {
        this.rvAdapter = rvAdapter;
    }

}
