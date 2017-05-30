package kr.or.dgit.bigdata.jollygo.jollygo_v01.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.customviews.GridRecyclerView;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgWords;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter.RvAdapter;

public class SearchMainFragment extends Fragment {

    //
    private ImgWords imgWords;
    private GridRecyclerView grv;
    private RvAdapter rvAdapter;
    public SearchMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {//
        View root = inflater.inflate(R.layout.fragment_search_main,container,false);
        grv = (GridRecyclerView) root.findViewById(R.id.recyclerView);// 확인요망
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        grv.setHasFixedSize(true);
        grv.setLayoutManager(new GridLayoutManager(getContext(),3));
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        imgWords = new ImgWords();
        rvAdapter = new RvAdapter(getContext(),fab,imgWords);
        grv.setAdapter(rvAdapter);
        final TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        grv.gridChangeListener(tvTitle);

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