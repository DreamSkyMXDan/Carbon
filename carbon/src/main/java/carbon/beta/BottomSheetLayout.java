package carbon.beta;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.component.BottomSheetCell;
import carbon.component.BottomSheetRow;
import carbon.component.MenuItem;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.internal.Menu;
import carbon.recycler.RowListAdapter;
import carbon.widget.LinearLayout;
import carbon.widget.RecyclerView;
import carbon.widget.TextView;

public class BottomSheetLayout extends LinearLayout {

    public enum Style {
        List, Grid
    }

    private Menu menu;
    private android.view.MenuItem.OnMenuItemClickListener listener;
    private TextView titleTv;
    private RecyclerView recycler;
    private Style style = Style.List;

    public BottomSheetLayout(Context context) {
        super(context);
        initBottomSheet();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBottomSheet();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBottomSheet();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBottomSheet();
    }

    private void initBottomSheet() {
        View.inflate(getContext(), R.layout.carbon_bottomsheet, this);
        setOrientation(VERTICAL);
        titleTv = (TextView) findViewById(R.id.carbon_bottomSheetTitle);
        recycler = (RecyclerView) findViewById(R.id.carbon_bottomSheetRecycler);
    }

    public void setOnMenuItemClickListener(android.view.MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public void setMenu(int resId) {
        menu = Carbon.getMenu(getContext(), resId);
        updateRecycler();
    }

    public void setMenu(final android.view.Menu baseMenu) {
        menu = Carbon.getMenu(getContext(), baseMenu);
        updateRecycler();
    }

    public android.view.Menu getMenu() {
        return menu;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
        updateRecycler();
    }

    public void setTitle(String title) {
        titleTv.setText(title);
        titleTv.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
        updateRecycler();
    }

    private void updateRecycler() {
        if (menu == null)
            return;

        recycler.setLayoutManager(style == Style.List ? new LinearLayoutManager(getContext()) : new GridLayoutManager(getContext(), 3));

        List<Serializable> items = new ArrayList<>();
        if (!titleTv.isVisible())
            items.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        items.addAll(menu.getVisibleItems());
        items.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));

        RowListAdapter<Serializable> adapter = new RowListAdapter<>(MenuItem.class, style == Style.List ? BottomSheetRow.FACTORY : BottomSheetCell.FACTORY);
        adapter.addFactory(PaddingItem.class, PaddingRow::new);
        adapter.setItems(items);

        recycler.setAdapter(adapter);
    }
}