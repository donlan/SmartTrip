package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import dong.lan.smarttrip.R;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.utils.TimeUtil;


/**
 * Created by 梁桂栋 on 17-2-12 ： 下午10:12.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class AddLineNodeListView extends RecyclerView {

    private final LabelTextView header;
    private List<Node> nodes;
    private DataAdapter adapter;
    private long time;


    public AddLineNodeListView(Context context, long time, List<Node> nodes, OnNodeClickListener listener) {
        super(context);
        this.time = time;
        header = new LabelTextView(context);
        header.setBgColor(getResources().getColor(R.color.colorPrimary));
        header.setGravity(Gravity.CENTER);
        header.setTextSize(20);
        header.setPadding(60, 14, 60, 14);
        header.setTextColor(getResources().getColor(R.color.white));
        adapter = new DataAdapter();
        setLayoutManager(new GridLayoutManager(context, 1));
        addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 1, 0xaaeeeeee));
        setAdapter(adapter);
        this.nodeClickListener = listener;
        this.nodes = nodes;
    }


    public void setHeaderText(String text) {
        header.setText(text);
    }

    public void addNode(Node node) {
        nodes.add(node);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
    }

    public void deleteNode(int position) {
        adapter.notifyItemRemoved(position + 1);
    }

    public long getNodeTime() {
        return time;
    }


    public static final int ACTION_DELETE = 1;
    public static final int ACTION_NODE = 0;
    public static final int ACTION_ADD = 2;
    private static final int TYPE_ADD = 0;
    private static final int TYPE_NODE = 1;
    private static final int TYPE_HEADER = 2;

    private OnNodeClickListener nodeClickListener;


    public interface OnNodeClickListener {
        void onNodeClick(AddLineNodeListView view, Node node, int position, int action);
    }

    private class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_HEADER) {

                return new HeaderHolder(header);
            }
            if (viewType == TYPE_ADD) {
                View addView = LayoutInflater.from(context).inflate(R.layout.item_add_line_pager, null);
                return new AddHolder(addView);
            }
            View nodeView = LayoutInflater.from(context).inflate(R.layout.item_node_list, null);
            return new NodeHolder(nodeView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_NODE) {
                Node node = nodes.get(position - 1);
                NodeHolder nodeHolder = (NodeHolder) holder;
                nodeHolder.timeTv.setText(TimeUtil.getTime(node.getArrivedTime(), "HH:mm"));
                nodeHolder.contentTv.setText(node.getAddress());
            }
        }

        @Override
        public int getItemCount() {
            return nodes == null ? 2 : nodes.size() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return TYPE_HEADER;
            if (position == getItemCount() - 1)
                return TYPE_ADD;
            return TYPE_NODE;
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    private class AddHolder extends RecyclerView.ViewHolder {
        public AddHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nodeClickListener != null)
                        nodeClickListener.onNodeClick(AddLineNodeListView.this, null, -1, ACTION_ADD);
                }
            });
        }
    }

    private class NodeHolder extends RecyclerView.ViewHolder {

        TextView timeTv;
        TextView contentTv;
        ImageButton deleteIb;

        public NodeHolder(View itemView) {
            super(itemView);
            timeTv = (TextView) itemView.findViewById(R.id.item_line_node_time);
            contentTv = (TextView) itemView.findViewById(R.id.item_line_node_location);
            deleteIb = (ImageButton) itemView.findViewById(R.id.item_line_node_delete);
            deleteIb.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nodeClickListener != null) {
                        int pos = getLayoutPosition() - 1;
                        nodeClickListener.onNodeClick(AddLineNodeListView.this, nodes.get(pos), pos, ACTION_DELETE);
                    }
                }
            });
            contentTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nodeClickListener != null) {
                        int pos = getLayoutPosition() - 1;
                        nodeClickListener.onNodeClick(AddLineNodeListView.this, nodes.get(pos), pos, ACTION_NODE);
                    }
                }
            });
        }
    }

}
