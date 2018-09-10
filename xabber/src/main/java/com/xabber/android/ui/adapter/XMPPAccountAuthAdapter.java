package com.xabber.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xabber.android.R;
import com.xabber.android.data.account.AccountItem;
import com.xabber.android.data.account.AccountManager;
import com.xabber.android.data.entity.AccountJid;
import com.xabber.android.data.extension.avatar.AvatarManager;
import com.xabber.android.data.extension.privatestorage.PrivateStorageManager;
import com.xabber.android.ui.color.ColorManager;

import java.util.List;

/**
 * Created by valery.miller on 20.07.17.
 */

public class XMPPAccountAuthAdapter extends RecyclerView.Adapter {

    private List<AccountJid> items;
    private Listener listener;

    public XMPPAccountAuthAdapter(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onAccountClick(String accountJid);
    }

    public void setItems(List<AccountJid> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new XMPPAccountVH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_xmpp_account_choose, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AccountItem account = AccountManager.getInstance().getAccount(items.get(position));
        XMPPAccountVH viewHolder = (XMPPAccountVH) holder;

        // set color
        viewHolder.jid.setTextColor(ColorManager.getInstance().getAccountPainter().
                getAccountMainColor(account.getAccount()));

        // set avatar
        viewHolder.avatar.setImageDrawable(
                AvatarManager.getInstance().getAccountAvatar(account.getAccount()));

        // set jid
        final String accountJid = account.getAccount().getFullJid().asBareJid().toString();
        final String fullJid = account.getAccount().getFullJid().toString();
        viewHolder.jid.setText(accountJid);

        // set action
        boolean haveBind = PrivateStorageManager.getInstance().haveXabberAccountBinding(account.getAccount());
        viewHolder.action.setText(haveBind ? R.string.xaccount_exist : R.string.xaccount_not_exist);

        // set listener
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAccountClick(fullJid);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class XMPPAccountVH extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView jid;
        TextView action;

        XMPPAccountVH(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            jid = (TextView) itemView.findViewById(R.id.tvAccountJid);
            action = (TextView) itemView.findViewById(R.id.tvAction);
        }
    }
}