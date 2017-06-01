package kr.or.dgit.bigdata.jollygo.jollygo_v01.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.customviews.GridRecyclerView;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgWords;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter.ListRvAdapter;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter.RvAdapter;

public class SearchMainFragment extends Fragment {

    //
    private ImgWords imgWords;
    private GridRecyclerView grv;
    private RvAdapter rvAdapter;
    private ListRvAdapter listRvAdapter;
    private static ProgressBar bar;

    public SearchMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {//
        View root = inflater.inflate(R.layout.fragment_search_main,container,false);
        grv = (GridRecyclerView) root.findViewById(R.id.recyclerView);// 확인요망 -> 작동완료
        bar = (ProgressBar) root.findViewById(R.id.cardPb);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e("카드 액티비티 완성<<<<<<<<<<<","ㅇㅇㅇㅇ");
        super.onActivityCreated(savedInstanceState);

        //-->
        grv.setHasFixedSize(true);

        grv.setLayoutManager(new GridLayoutManager(getContext(),3));
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        imgWords = new ImgWords();//초기화
        rvAdapter = new RvAdapter(getContext(),fab,imgWords,bar);
        grv.setAdapter(rvAdapter);
        final TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        grv.gridChangeListener(tvTitle);
        //<--


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
                bar.setVisibility(View.VISIBLE);
                Log.e("카드 추가 시작<<<<<<<<<<<","ㅇㅇㅇ");
                sv.setQuery("",true);

                Message msg = new Message();
                msg.what =1;
                msg.obj = query;

                Handler mh = new mHandler();
                mh.sendMessageDelayed(msg,200);

               /* getRvAdapter().addItem(query); //수정*/
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //검색결과 가지고 리스트 프래그먼트로 이동
                if(getRvAdapter().getImgWords().getmDataset().size()>0){
                    LinearLayoutManager llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false); //reverseLayout은  sort 순서 반대로하기 기능
                    grv.setLayoutManager(llm);
                   // Log.e("초기 단어>>>",getRvAdapter().getImgWords().getmDataset().get(0)); 확인
                    listRvAdapter = new ListRvAdapter(getContext(),getRvAdapter().getImgWords());

                    grv.setAdapter(listRvAdapter);


                    //이후 뷰처리 및 초기화
                    tvTitle.setText("JOLLYGO-Recipe List");
                    sv.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    List<String> newList= new ArrayList<String>();
                    getRvAdapter().getImgWords().setmDataset(newList);
                }else{
                    Toast.makeText(getContext(),"재료들을 먼저 입력해 주세요.",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private class mHandler extends Handler{ // 리사이클링 뷰 갱신하기 전에 프로그래스바 보이기 위한 핸들러
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1){
                String query = (String) msg.obj;
                getRvAdapter().addItem(query); //수정
            }

        }
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
