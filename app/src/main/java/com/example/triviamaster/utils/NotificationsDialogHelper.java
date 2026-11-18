package com.example.triviamaster.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.R;
import com.example.triviamaster.adapters.SocialAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.FriendshipRepository;
import com.example.triviamaster.models.Friendship;
import com.example.triviamaster.models.User;
import java.util.ArrayList;
import java.util.List;

public final class NotificationsDialogHelper {

    private NotificationsDialogHelper() {
    }

    public static void show(Context context, PreferencesManager prefsManager,
                            FriendshipRepository friendshipRepository, Runnable onUpdated) {
        long myId = prefsManager.getCurrentUserId();
        if (myId <= 0) {
            return;
        }
        friendshipRepository.getFriendsAndRequests(myId, new DbCallback<List<Friendship>>() {
            @Override
            public void onSuccess(List<Friendship> items) {
                List<Friendship> incoming = new ArrayList<>();
                if (items != null) {
                    for (Friendship f : items) {
                        if (f.isIncoming()) {
                            incoming.add(f);
                        }
                    }
                }
                if (incoming.isEmpty()) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.notif_center_title)
                            .setMessage(R.string.notif_empty)
                            .setPositiveButton(R.string.btn_back, null)
                            .show();
                    return;
                }

                int pad = (int) (12 * context.getResources().getDisplayMetrics().density);
                LinearLayout container = new LinearLayout(context);
                container.setOrientation(LinearLayout.VERTICAL);
                container.setPadding(pad, pad, pad, pad);

                RecyclerView rv = new RecyclerView(context);
                rv.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                rv.setLayoutManager(new LinearLayoutManager(context));

                final AlertDialog[] dialogHolder = new AlertDialog[1];
                final SocialAdapter[] adapterHolder = new SocialAdapter[1];
                adapterHolder[0] = new SocialAdapter(SocialAdapter.MODE_INCOMING_REQUEST, prefsManager,
                        new SocialAdapter.OnSocialActionListener() {
                            @Override
                            public void onAcceptRequest(Friendship friendship) {
                                friendshipRepository.acceptFriendRequest(friendship.getId(), new DbCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean ok) {
                                        Toast.makeText(context,
                                                context.getString(R.string.social_request_accepted, friendship.getOtherUsername()),
                                                Toast.LENGTH_SHORT).show();
                                        incoming.remove(friendship);
                                        adapterHolder[0].setFriendships(incoming);
                                        if (onUpdated != null) onUpdated.run();
                                        if (incoming.isEmpty() && dialogHolder[0] != null) {
                                            dialogHolder[0].dismiss();
                                        }
                                    }

                                    @Override
                                    public void onError(Exception error) {
                                    }
                                });
                            }

                            @Override
                            public void onRejectRequest(Friendship friendship) {
                                friendshipRepository.removeFriendship(friendship.getId(), new DbCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean ok) {
                                        incoming.remove(friendship);
                                        adapterHolder[0].setFriendships(incoming);
                                        if (onUpdated != null) onUpdated.run();
                                        if (incoming.isEmpty() && dialogHolder[0] != null) {
                                            dialogHolder[0].dismiss();
                                        }
                                    }

                                    @Override
                                    public void onError(Exception error) {
                                    }
                                });
                            }

                            @Override
                            public void onChallengeFriend(Friendship friendship) {
                            }

                            @Override
                            public void onAddUser(User user) {
                            }
                        });
                adapterHolder[0].setFriendships(incoming);
                rv.setAdapter(adapterHolder[0]);
                container.addView(rv);

                dialogHolder[0] = new AlertDialog.Builder(context)
                        .setTitle(R.string.notif_center_title)
                        .setView(container)
                        .setPositiveButton(R.string.btn_back, null)
                        .show();
            }

            @Override
            public void onError(Exception error) {
            }
        });
    }
}
