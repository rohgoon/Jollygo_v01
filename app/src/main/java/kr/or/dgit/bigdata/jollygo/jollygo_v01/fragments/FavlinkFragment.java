package kr.or.dgit.bigdata.jollygo.jollygo_v01.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.R;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.customviews.GridRecyclerView;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Favlink;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter.FavRvAdapter;

public class FavlinkFragment extends Fragment {
    private GridRecyclerView grv;
    private static ProgressBar bar;
    private FavRvAdapter favRvAdapter;
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private List<Favlink> favlinkList;


    public FavlinkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favlink,container,false);
        grv = (GridRecyclerView) root.findViewById(R.id.flRecyclerView);// 확인요망 -> 작동완료
        bar = (ProgressBar) root.findViewById(R.id.flCardPb);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 즐겨찾기 리스트 받아오기
        //->차후 프래그먼트로 이동
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();//유저정보

        favlinkList = new ArrayList<>();
        getFavlinkOnce(currentUser);//한번 불러오기
        //뷰 구현
        //-->
        grv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true); //reverseLayout은  sort 순서 반대로하기 기능
        grv.setLayoutManager(llm);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);//처음엔 플로팅버튼이 안보이게함.


    }
    private void getFavlinkOnce(FirebaseUser currentUser) {//동작은 잘됨, 마지막 fno를 가져오는 걸로 변경 할것
        DatabaseReference flRef = databaseReference.child("favlink").equalTo(currentUser.getUid(),"uid").getRef();

        flRef.addListenerForSingleValueEvent(new ValueEventListener() {//처음 한번 리스트 불러오기 체크 요망
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //flcount= (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    favlinkList.add(d.getValue(Favlink.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
