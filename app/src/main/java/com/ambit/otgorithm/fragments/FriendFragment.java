package com.ambit.otgorithm.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.FriendListAdapter;
import com.ambit.otgorithm.dto.Chat;
import com.ambit.otgorithm.dto.ExitMessage;
import com.ambit.otgorithm.dto.Message;
import com.ambit.otgorithm.dto.UserDTO;
import com.ambit.otgorithm.modules.RecyclerViewItemClickListener;
import com.ambit.otgorithm.views.ChatActivity;
import com.ambit.otgorithm.views.ChatMain;
import com.ambit.otgorithm.views.MainActivity;
import com.ambit.otgorithm.views.ProfileActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {

    @BindView(R.id.search_area)
    LinearLayout mSearchArea;

    @BindView(R.id.edtContent)
    EditText edtEmail;

    @BindView(R.id.friendRecyclerView)
    RecyclerView mRecyclerView;

    private FirebaseUser mFirebaseUser;

    private FirebaseAuth mFirebaseAuth;

    private FirebaseDatabase mFirebaseDb;

    private DatabaseReference mFriendsDBRef;
    private DatabaseReference mUserDBRef;
    private DatabaseReference mChatRef;
    private DatabaseReference mChatMemberRef;

    private FriendListAdapter friendListAdapter;

    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View friendView = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, friendView);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFriendsDBRef = mFirebaseDb.getReference("users").child(mFirebaseUser.getUid()).child("theSameBoat");
        mUserDBRef = mFirebaseDb.getReference("users");
        mChatRef = mFirebaseDb.getReference("users").child(mFirebaseUser.getUid()).child("chats");
        mChatMemberRef = mFirebaseDb.getReference("chat_members");
        // 1. 리얼타임데이터베이스에서 나의 친구목록을 리스터를 통하여 데이터를 가져옵니다.
        addFriendListener();
        // 2. 가져온 데이터를 통해서 recyclerview의 아답터에 아이템을 추가 시켜줍니다. (UI)갱신
        friendListAdapter = new FriendListAdapter();
        friendListAdapter.setFragment(this);
        mRecyclerView.setAdapter(friendListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 3. 아이템별로 (친구) 클릭이벤트를 주어서 선택한 친구와 대화를 할 수 있도록 한다.
        mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(), new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (friendListAdapter.getItem(position) != null) {
                    final UserDTO friend = friendListAdapter.getItem(position);

                    if (friendListAdapter.getSelectionMode() == FriendListAdapter.UNSELECTION_MODE) {

                        Snackbar.make(view, friend.getName() + "님과 대화를 하시겠습니까?", Snackbar.LENGTH_LONG).setAction("예", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                mUserDBRef.child(mFirebaseUser.getUid()).child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("chat2", "들어옴");
                                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                                            Chat chat = children.getValue(Chat.class);
                                            Log.d("chat22", "드루어옴");
                                            if (chat.getTitle().equals(friend.getName())) {
                                                //기존방이 있을때
                                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                chatIntent.putExtra("chat_id", chat.getChatId());
                                                startActivityForResult(chatIntent, ChatFragment.JOIN_ROOM_REQUEST_CODE);
                                                Log.d("chat222", "드루루어옴");
                                                return;
                                            }
                                        }
                                        //기존방이 없을때
                                        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                        chatIntent.putExtra("uid", friend.getUid());
                                        startActivityForResult(chatIntent, ChatFragment.JOIN_ROOM_REQUEST_CODE);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }).show();
                    } else {
                        friend.setSelection(friend.isSelection() ? false : true);
                        int selectedUserCount = friendListAdapter.getSelectionUsersCount();
                        Snackbar.make(view, selectedUserCount + "명과 대화를 하시겠습니까?", Snackbar.LENGTH_LONG).setAction("예", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                                chatIntent.putExtra("uids", friendListAdapter.getSelectedUids());
                                startActivityForResult(chatIntent, ChatFragment.JOIN_ROOM_REQUEST_CODE);
                            }
                        }).show();
                    }

                }
            }
        }));

        return friendView;
    }

    public void toggleSearchBar(){
        if (mSearchArea == null) return;
        mSearchArea.setVisibility( mSearchArea.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE );
    }

    public void toggleSelectionMode(){
        friendListAdapter
                .setSelectionMode(friendListAdapter.getSelectionMode() == FriendListAdapter.SELECTION_MODE ? FriendListAdapter.UNSELECTION_MODE :
                        FriendListAdapter.SELECTION_MODE);
    }

    @OnClick(R.id.findBtn)
    public void addFriend(){

        // 1.입력된 이메일을 가져옵니다.
        final String inputEmail = edtEmail.getText().toString();
        // 2. 이메일이 입력되지 않았다면 이메일을 입력해주시라는 메세지를 띄워줍니다.
        if ( inputEmail.isEmpty()) {
            Snackbar.make(mSearchArea, "이메일을 입력해주세요. ", Snackbar.LENGTH_LONG).show();
            return;
        }
        // 3. 자기 자신을 친구로 등록할 수 없기때문에 FirebaseUser의 email이 입력한 이메일과 같다면, 자기자신은 등록 불가 메세지를 띄워줍니다.
        if ( inputEmail.equals(mFirebaseUser.getEmail())) {
            Snackbar.make(mSearchArea, "자기자신은 친구로 등록할 수 없습니다. ", Snackbar.LENGTH_LONG).show();
            return;
        }
        // 3. 이메일이 정상이라면 나의 정보를 조회하여 이미등록된 친구인지를 판단하고
        mFriendsDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> friendsIteratble = dataSnapshot.getChildren();
                Iterator<DataSnapshot> friendsIterator = friendsIteratble.iterator();


                while ( friendsIterator.hasNext()) {
                    UserDTO user = friendsIterator.next().getValue(UserDTO.class);

                    if ( user.getEmail().equals(inputEmail)) {
                        Snackbar.make(mSearchArea, "이미 등록된 친구입니다.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }

                // 4. users db에 존재 하지 않는 이메일이라면, 가입하지 않는 친구라는 메세지를 띄워주고
                mUserDBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> userIterator = dataSnapshot.getChildren().iterator();
                        int userCount = (int)dataSnapshot.getChildrenCount();
                        int loopCount = 1;


                        while (userIterator.hasNext()) {
                            final UserDTO currentUser = userIterator.next().getValue(UserDTO.class);
                            if ( inputEmail.equals(currentUser.getEmail())) {
                                // 친구 등록 로직
                                // 5. users/{myuid}/friends/{someone_uid}/firebasePush/상대 정보를 등록하고     //  저의 친구에 상대방을 등록
                                mFriendsDBRef.push().setValue(currentUser, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(final DatabaseError databaseError, DatabaseReference databaseReference) {
                                        // 6. users/{someone_uid}/friends/{myuid}/상대 정보를 등록하고

                                        // 나의 정보를 가져온다
                                        mUserDBRef.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                UserDTO user = dataSnapshot.getValue(UserDTO.class);
                                                mUserDBRef.child(currentUser.getUid()).child("friends").push().setValue(user);
                                                Snackbar.make(mSearchArea, "친구등록이 완료되었습니다. ", Snackbar.LENGTH_LONG).show();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("me", mFirebaseUser.getEmail());
                                                bundle.putString("friend",currentUser.getEmail());
                                                mFirebaseAnalytics.logEvent("addFriend", bundle);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });
                            } else {
                                if ( loopCount++ >= userCount ) {
                                    Snackbar.make(mSearchArea, "가입을 하지 않은 친구입니다.", Snackbar.LENGTH_LONG).show();
                                    return;
                                }
                            }


                            // 총 사용자의 명수 == loopCount
                            // 등록된 사용자가 없다는 메세지를 출력 합니다.
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // 5. users/{myuid}/friends/{someone_uid}/상대 정보를 등록하고
    }

    private void addFriendListener(){
        mFriendsDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    clearList();
                    Log.d("dataSnapshot사이즈",Long.toString(dataSnapshot.getChildrenCount()));
                    if((int)dataSnapshot.getChildrenCount() != 0){
                        for(DataSnapshot children : dataSnapshot.getChildren()){
                            UserDTO friend = children.getValue(UserDTO.class);
                            drawUI(friend);
                        }
                    }else {
                        friendListAdapter.notifyDataSetChanged();
                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      /*  mFriendsDBRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserDTO friend = dataSnapshot.getValue(UserDTO.class);
                // 2. 가져온 데이터를 통해서 recyclerview의 아답터에 아이템을 추가 시켜줍니다. (UI)갱신
                drawUI(friend);
                Log.d("테스트1","1");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserDTO friend = dataSnapshot.getValue(UserDTO.class);
                // 2. 가져온 데이터를 통해서 recyclerview의 아답터에 아이템을 추가 시켜줍니다. (UI)갱신
                eraseUI(friend);
                Log.d("테스트3","3");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    private void clearList(){
        friendListAdapter.clearList();
    }

    private void drawUI(UserDTO friend){
        friendListAdapter.addItem(friend);
    }

    private void eraseUI(UserDTO friend){
        friendListAdapter.removeItem(friend);
    }

    public void blackList(final UserDTO friend){
        Snackbar.make(getView(),friend.getName()+"을 차단하시겠습니까?",Snackbar.LENGTH_LONG).setAction("예", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserDBRef.child(mFirebaseUser.getUid()).child("theSameBoat").child(friend.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mUserDBRef.child(friend.getUid()).child("theSameBoat").child(mFirebaseUser.getUid()).removeValue();
                        mUserDBRef.child(mFirebaseUser.getUid()).child("blacklist").child(friend.getUid()).setValue(friend);
                    }
                });
                mUserDBRef.child(mFirebaseUser.getUid()).child("favorites").child(friend.getUid()).removeValue();
                mUserDBRef.child(friend.getUid()).child("fans").child(mFirebaseUser.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mUserDBRef.child(mFirebaseUser.getUid()).child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot children : dataSnapshot.getChildren()){
                                    Chat chat = children.getValue(Chat.class);
                                    if(chat != null && chat.getTitle().equals(friend.getName())){
                                        leaveChat(chat);
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        }).show();
    }

    public void leaveChat(final Chat chat) {
        final DatabaseReference messageRef = mFirebaseDb.getReference("chat_messages").child(chat.getChatId());

                // 나의 대화방 목록에서 제거
                // users/{uid}/chats
                mChatRef.child(chat.getChatId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference chatRef) {

                        //exit 메세지 발송
                        // chat_messages > {chat_id} > {message_id} >  { - 내용...~}

                        final ExitMessage exitMessage = new ExitMessage();
                        String messageId = messageRef.push().getKey();
                        if(MainActivity.userUri!=null) {
                            exitMessage.setMessageUser(new UserDTO(mFirebaseUser.getUid(), mFirebaseUser.getEmail(), MainActivity.nickName, MainActivity.userUri.toString()));
                        }else {
                            exitMessage.setMessageUser(new UserDTO(mFirebaseUser.getUid(), mFirebaseUser.getEmail(), MainActivity.nickName, null));
                        }
                        exitMessage.setMessageDate(new Date());
                        exitMessage.setMessageId(messageId);
                        exitMessage.setChatId(chat.getChatId());
                        messageRef.child(messageId).setValue(exitMessage);

                        // 채팅 멤버 목록에서 제거
                        // chat_members/{chat_id}/{user_id} 제거
                        mChatMemberRef
                                .child(chat.getChatId())
                                .child(mFirebaseUser.getUid())
                                .removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        // 메세지 unreadCount에서도 제거
                                        // getReadUserList 내가 있다면 읽어진거니깐 pass
                                        // 없다면 unreadCount - 1
                                        // messages/{chat_id}
                                        // 모든 메세지를 가져온다.
                                        // 가져와서 루프를 통해서 내가 읽었는지 여부 판단.

                                        Bundle bundle = new Bundle();
                                        bundle.putString("me", mFirebaseUser.getEmail());
                                        bundle.putString("chatId", chat.getChatId());

                                        mFirebaseAnalytics.logEvent("leaveChat", bundle);

                                        // 채팅방의 멤버정보를 받아와서
                                        // 채팅방의 정보를 가져오고 (각각)
                                        // 그정보의 라스트 메세지를 수정해줍니다.

                                        mChatMemberRef
                                                .child(chat.getChatId())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Iterator<DataSnapshot> chatMemberIterator = dataSnapshot.getChildren().iterator();

                                                        while ( chatMemberIterator.hasNext()) {
                                                            UserDTO chatMember = chatMemberIterator.next().getValue(UserDTO.class);

                                                            // chats -> {uid} -> {chat_id} - { ... }
                                                            mFirebaseDb
                                                                    .getReference("users")
                                                                    .child(chatMember.getUid())
                                                                    .child("chats")
                                                                    .child(chat.getChatId())
                                                                    .child("lastMessage")
                                                                    .setValue(exitMessage);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });




                                        mFirebaseDb.getReference("messages").child(chat.getChatId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Iterator<DataSnapshot> messageIterator = dataSnapshot.getChildren().iterator();

                                                while ( messageIterator.hasNext()) {
                                                    DataSnapshot messageSnapshot = messageIterator.next();
                                                    Message currentMessage = messageSnapshot.getValue(Message.class);
                                                    if ( !currentMessage.getReadUserList().contains(mFirebaseUser.getUid())) {
                                                        // message
                                                        messageSnapshot.child("unreadCount").getRef().setValue(currentMessage.getUnreadCount() - 1);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });

                        /**
                         * <추가 반영분>
                         * 대화방의 타이틀이 변경됨을 알려주어 채팅방의 리스너가 감지하게 하여 방 이름을 업데이트 하게 해야합니다
                         * 방의 제목은 방의 업데이트, 추가되었을때의 리스너가 처리하기때문에 방 제목만 변경 시켜서 변경이 되었음만 알리면 됩니다
                         */
                        mChatMemberRef.child(chat.getChatId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterator<DataSnapshot> memberIterator = dataSnapshot.getChildren().iterator();

                                while( memberIterator.hasNext()) {
                                    // 방 참여자의 UID를 가져오기 위하여 user 정보 조회
                                    UserDTO chatMember = memberIterator.next().getValue(UserDTO.class);
                                    // 해당 참여자의 방 정보의 업데이트를 위하여 방이름을 임의로 업데이트 진행
                                    mFirebaseDb.getReference("users")
                                            .child(chatMember.getUid())
                                            .child("chats")
                                            .child(chat.getChatId())
                                            .child("title")
                                            .setValue("");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });


    }

}
