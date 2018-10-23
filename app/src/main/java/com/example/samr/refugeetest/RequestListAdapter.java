package com.example.samr.refugeetest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static android.support.constraint.Constraints.TAG;

public class RequestListAdapter extends BaseAdapter {
    private Context context;
    private List<PojoRequest[]> requests;

    public RequestListAdapter(Context context, List<PojoRequest> requestList) {
        this.context = context;
        requests = new ArrayList<>();
        if (requestList != null && !requestList.isEmpty()) {
            for (int i = 0; i < requestList.size(); i += 2) {
                PojoRequest[] doubleRequest = new PojoRequest[2];
                doubleRequest[0] = requestList.get(i);
                try {
                    doubleRequest[1] = requestList.get(i + 1);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.d(TAG, "RequestListAdapter: out of bounds exception caught", e);
                } catch (Exception e) {
                    Log.w(TAG, "RequestListAdapter: unexpected exception caught", e);
                }
                requests.add(doubleRequest);
            }
        }
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public Object getItem(int i) {
        return requests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            //one by one list
//            PojoRequest currentRequest = requestList.get(i);
//
//            if (currentRequest == null) {
//                return null;
//            }
//            final View nView = inflateView(context, viewGroup, currentRequest);
            PojoRequest request1 = requests.get(i)[0];
            PojoRequest request2 = null;
            try {
                request2 = requests.get(i)[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                //out of bounds
                Log.d(TAG, "getView: out of bounds exception caught", e);
            } catch (Exception e) {
                Log.w(TAG, "getView: unhandled exception caught", e);
            }

            final View nView = inflateView(context, viewGroup, request1, request2);

            view = nView;
        }
        return view;
    }

    private View inflateView(Context context, ViewGroup parent, PojoRequest request) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View requestListItem = layoutInflater.inflate(R.layout.li_request, parent, false);

        // TODO: 10/18/18 check if strings are not null and handle

        ImageView iv_image = requestListItem.findViewById(R.id.li_req_iv_img);
        String imageUrl = request.getImageUrl();
        Picasso.with(context)
                .load(imageUrl)
                .resize(100, 100)
                .into(iv_image);

        TextView tvTitle = requestListItem.findViewById(R.id.li_req_tv_title);
        tvTitle.setText(request.getTitle());

        TextView tvDescription = requestListItem.findViewById(R.id.li_req_tv_description);
        tvDescription.setText(request.getDescription());

        TextView tvPrice = requestListItem.findViewById(R.id.li_req_tv_price);
        tvPrice.setText(request.getPrice().toString());

        TextView tvTime = requestListItem.findViewById(R.id.li_req_tv_time);
        tvTime.setText(request.getTime().toString());

        return requestListItem;
    }

    private View inflateView(Context context, ViewGroup parent, final PojoRequest request1,
                             PojoRequest request2) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View requestListItem = layoutInflater.inflate(R.layout.li_request_list_two_by_two, parent,
                false);
        View req1View = requestListItem.findViewById(R.id.li_req_layout_1);
        View req2View = requestListItem.findViewById(R.id.li_req_layout_2);
        if (request1 != null) {
            if (request1.getTitle() != null && !request1.getTitle().isEmpty()) {
                TextView tvTitle = req1View.findViewById(R.id.li_req_l1_tv_title);
                tvTitle.setText(request1.getTitle());
            }

            if (request1.getImageUrl() != null && !request1.getImageUrl().isEmpty()) {
                ImageView ivRequestImage = req1View.findViewById(R.id.li_req_l1_iv_img);
                Picasso.with(context)
                        .load(request1.getImageUrl())
                        .fit()
                        .into(ivRequestImage);
            }

            if (request1.getPrice() != null && !request1.getPrice().isEmpty()) {
                TextView tvPrice = req1View.findViewById(R.id.li_req_l1_tv_price);
                tvPrice.setText("JOD " + request1.getPrice());
            }

            if (request1.getUserId() != null && !request1.getUserId().isEmpty()) {
                TextView tvUserName = req1View.findViewById(R.id.li_req_l1_tv_username);
                ImageView ivUserAvatar = req1View.findViewById(R.id.li_req_l1_iv_usr_avatar);
                addUserListener(request1.getUserId(), tvUserName, ivUserAvatar);
            }
            req1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoRequestDetails(request1.getRequestId(), request1.getUserId());
                }
            });
        }

        if (request2 == null) {
            req2View.setVisibility(View.INVISIBLE);
        } else {
            if (request2.getTitle() != null && !request2.getTitle().isEmpty()) {
                TextView tvTitle = req2View.findViewById(R.id.li_req_l2_tv_title);
                tvTitle.setText(request2.getTitle());
            }

            if (request2.getImageUrl() != null && !request2.getImageUrl().isEmpty()) {
                ImageView ivRequestImage = req2View.findViewById(R.id.li_req_l2_iv_img);
                Picasso.with(context)
                        .load(request2.getImageUrl())
                        .fit()
                        .into(ivRequestImage);
            }

            if (request2.getPrice() != null && !request2.getPrice().isEmpty()) {
                TextView tvPrice = req2View.findViewById(R.id.li_req_l2_tv_price);
                tvPrice.setText("JOD " + request2.getPrice());
            }

            if (request2.getUserId() != null && !request2.getUserId().isEmpty()) {
                TextView tvUserName = req2View.findViewById(R.id.li_req_l2_tv_username);
                ImageView ivUserAvatar = req2View.findViewById(R.id.li_req_l2_iv_usr_avatar);
                addUserListener(request2.getUserId(), tvUserName, ivUserAvatar);
            }
        }
        return requestListItem;
    }

    private void addUserListener(final String userId, final TextView tvUserName, final ImageView ivUserAvatar) {
        DatabaseReference mDatabaseUserRef = FirebaseDatabase.getInstance().getReference("Users/" + userId);
        mDatabaseUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("first_name").getValue().toString();
                String lastName = dataSnapshot.child("last_name").getValue().toString();
                String name;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    StringJoiner nameBuilder = new StringJoiner(" ");
                    nameBuilder.add(firstName).add(lastName);
                    name = nameBuilder.toString();
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(firstName + " ").append(lastName);
                    name = stringBuilder.toString();
                }
                tvUserName.setText(name);

                String imageUrl = dataSnapshot.child("image_url").getValue().toString();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Picasso.with(context).load(imageUrl).fit().into(ivUserAvatar);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 10/21/18
            }
        });
    }

    private void gotoRequestDetails(String requestId, String userId) {
        Intent nIntent = new Intent(context, RequestDetailsActivity.class);
        nIntent.putExtra("requestId", requestId);
        nIntent.putExtra("userId", userId);
        context.startActivity(nIntent);
    }
}
