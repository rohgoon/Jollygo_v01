package kr.or.dgit.bigdata.jollygo.jollygo_v01.fragments;

import android.app.Activity;
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
    private Activity activityThis;
    private FloatingActionButton fab;

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
        activityThis = getActivity();
        Log.e("카드 액티비티 완성<<<<<<<<<<<","ㅇㅇㅇㅇ");
        super.onActivityCreated(savedInstanceState);

        //-->
        grv.setHasFixedSize(true);

        grv.setLayoutManager(new GridLayoutManager(getContext(),3));
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        imgWords = new ImgWords();//초기화
        rvAdapter = new RvAdapter(getContext(),fab,imgWords);
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

                Log.e("카드 추가 시작<<<<<<<<<<<","ㅇㅇㅇ");
                sv.setQuery("",true);
                //프로그래스바 등장
                bar.setVisibility(View.VISIBLE);

                Message msg = new Message();
                msg.what =1;
                msg.obj = query;

                Handler mh = new mHandler();
                mh.sendMessageDelayed(msg,100);
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
                    tvTitle.setText(searchCount + " items were ready");
                }else if (searchCount == 1){
                    tvTitle.setText(searchCount + " item was ready");
                }else if (searchCount == 0 ){
                    tvTitle.setText("What a lot of chefs in the world");
                }
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {   //검색결과 가지고 리스트 프래그먼트로 이동
            @Override
            public void onClick(View view) {

                Log.e("플로팅버튼클릭>>>>","확인"+grv.getAdapter().getClass().getName().toString());
                if(grv.getAdapter().getClass().getName().equals("kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter.RvAdapter")) {//확인 요망
                    if (getRvAdapter().getImgWords().getmDataset().size() > 0) {
                        fab.setVisibility(View.INVISIBLE);
                        //프로그래스바 등장
                        bar.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "쉐프님들이 모이고 있어요", Toast.LENGTH_LONG).show();
                        Message msg = new Message();
                        msg.what = 2;
                        Handler mh = new mHandler();
                        mh.sendMessageDelayed(msg, 100);
                        fab.setImageResource(R.drawable.fabback);
                    } else {
                        Toast.makeText(getContext(), "재료들을 먼저 입력해 주세요.", Toast.LENGTH_SHORT).show();

                    }
                }else if(grv.getAdapter().getClass().getName().equals("kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter.ListRvAdapter")) {
                    //-->
                    grv.setHasFixedSize(true);

                    grv.setLayoutManager(new GridLayoutManager(getContext(),3));
                   // FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

                    imgWords = new ImgWords();//초기화
                    rvAdapter = new RvAdapter(getContext(),fab,imgWords);
                    grv.setAdapter(rvAdapter);
                    final TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
                    grv.gridChangeListener(tvTitle);
                    //<--
                    fab.setImageResource(R.drawable.fabhome);

                    sv.setVisibility(View.VISIBLE);
                    sv.setIconified(true);
                    tvTitle.setText("What a lot of chefs in the world");

                }
            }
        });

    }//onActivityCreated
    private class mHandler extends Handler{ // 리사이클링 뷰 갱신하기 전에 프로그래스바 보이기 위한 핸들러
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1){
                String query = (String) msg.obj;
                getRvAdapter().addItem(query); //수정
                bar.setVisibility(View.GONE);

                removeMessages(msg.what);//
            }else if (msg.what ==2){

                LinearLayoutManager llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false); //reverseLayout은  sort 순서 반대로하기 기능
                grv.setLayoutManager(llm);
                listRvAdapter = new ListRvAdapter(getContext(),getRvAdapter().getImgWords());
                grv.setAdapter(listRvAdapter);

                //이후 뷰처리 및 초기화
                ((TextView)activityThis.findViewById(R.id.tvTitle)).setText("Recipe List");
                ((SearchView)activityThis.findViewById(R.id.search_view)).setVisibility(View.INVISIBLE);
                //((FloatingActionButton)activityThis.findViewById(R.id.fab)).setVisibility(View.INVISIBLE);
                //((FloatingActionButton)activityThis.findViewById(R.id.fab)).setImageResource(R.drawable.);
                List<String> newList= new ArrayList<String>();
                getRvAdapter().getImgWords().setmDataset(newList);
                bar.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                removeMessages(msg.what);//
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
